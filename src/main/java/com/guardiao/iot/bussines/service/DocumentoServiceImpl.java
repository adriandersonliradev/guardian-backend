package com.guardiao.iot.bussines.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.guardiao.iot.bussines.iservice.DocumentoService;
import com.guardiao.iot.dto.DocumentoDTO;
import com.guardiao.iot.dto.TipoDocumentalDTO;
import com.guardiao.iot.entity.DocumentoEntity.Documento;
import com.guardiao.iot.entity.TipoDocumentoEntity.TipoDocumental;
import com.guardiao.iot.infrastructure.irepository.DocumentoRepository;
import com.guardiao.iot.infrastructure.irepository.TipoDocumentalRepository;
import com.guardiao.iot.mappers.DocumentoMapper;

import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ByteArrayResource;
import java.util.Map;


import javax.persistence.EntityNotFoundException;



@Service
public class DocumentoServiceImpl implements DocumentoService {

    @Autowired
    private DocumentoRepository documentoRepository;

    @Autowired
    private TipoDocumentalRepository tipoDocumentalRepository;

    @Override
    public List<DocumentoDTO> findAll() {
        return documentoRepository.findAll().stream()
                .map(DocumentoMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<DocumentoDTO> findById(Long id) {
        return documentoRepository.findById(id)
                .map(DocumentoMapper.INSTANCE::toDTO);
    }

    @Override
    @Transactional
    public DocumentoDTO save(DocumentoDTO documentoDTO, MultipartFile file) {
        Documento documento;

        if (documentoDTO.getId() != null) {
            documento = documentoRepository.findById(documentoDTO.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Documento não existe"));
            documento.setNomeDocumento(documentoDTO.getNomeDocumento());
        } else {
            documento = DocumentoMapper.INSTANCE.toEntity(documentoDTO);
            documento.setDataHora(LocalDate.now());
        }

        associarEntidades(documento, documentoDTO);
        addDocuementoToTipoDocumental(documento, documento.getTipoDocumental());

        try {
            documento.setArquivoPdf(file.getBytes());
            //String tipoDocumentalClassificado = classificarDocumentoNaAPI(file);
            //System.out.println("Tipo Documental Classificado pela API: " + tipoDocumentalClassificado);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar o arquivo PDF", e);
        }

        //documento.setDataExpiracao(LocalDate.now().plusDays(30));


        Documento savedDocumento = documentoRepository.save(documento);
        System.out.println("Arquivo PDF: " + savedDocumento.getArquivoPdf());
        DocumentoDTO savedDocumentoDTO = DocumentoMapper.INSTANCE.toDTO(savedDocumento);
        System.out.println("Arquivo PDF no DTO: " + savedDocumentoDTO.getArquivoPdf());
        return DocumentoMapper.INSTANCE.toDTO(savedDocumento);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Documento documento = documentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("documento não existe"));
        desassociarDocumentoDeTipoDocumental(documento);
        documentoRepository.deleteById(documento.getId());
    }

    @Override
    @Transactional
    public void excluirDocumentosPorIds(List<Long> ids) {
        for (Long id : ids) {
            if (documentoRepository.existsById(id)) {
                documentoRepository.deleteById(id);
            } else {
                throw new EntityNotFoundException("Documento com ID " + id + " não encontrado");
            }
        }
    }

    private void desassociarDocumentoDeTipoDocumental(Documento documento) {
        TipoDocumental tipoDocumental = documento.getTipoDocumental();
        if (tipoDocumental != null) {
            tipoDocumental.removeDocumento(documento);
            documentoRepository.save(documento);
        }
    }

    private void addDocuementoToTipoDocumental(Documento documento, TipoDocumental... tiposDocumentais) {
        for (TipoDocumental tipoDocumental : tiposDocumentais) {
            if (tipoDocumental != null && tipoDocumental.getId() != null) {
                tipoDocumental.addDocumento(documento);
                tipoDocumentalRepository.save(tipoDocumental);
            }
        }
    }

    private void associarEntidades(Documento documento, DocumentoDTO documentoDTO) {
        if (documentoDTO.getTipoDocumentalId() != null) {
            TipoDocumental tipoDocumental = tipoDocumentalRepository.findById(documentoDTO.getTipoDocumentalId())
                    .orElseThrow(() -> new EntityNotFoundException("Tipo Documental não existe"));

            if (isInativo(tipoDocumental)) {
                throw new IllegalStateException("Não é possível atribuir um Tipo Documental inativo.");
            }
            documento.setTipoDocumental(tipoDocumental);
        } else {
            documento.setTipoDocumental(null);
        }
    }

    @Override
    public byte[] getDocumentoArquivo(Long id) {
        Documento documento = documentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Documento não encontrado"));
        return documento.getArquivoPdf();
    }

    private boolean isInativo(TipoDocumental tipoDocumental) {
        return !tipoDocumental.isStatus();
    }

    @Override
    public List<DocumentoDTO> findDocumentosExpirados() {
        LocalDate hoje = LocalDate.now();
        List<Documento> documentosExpirados = documentoRepository.findDocumentosExpirados(hoje);
        return documentosExpirados.stream()
                .map(DocumentoMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }

}
