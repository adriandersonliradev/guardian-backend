package com.guardiao.iot.bussines.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.guardiao.iot.bussines.iservice.DocumentoService;
import com.guardiao.iot.dto.DocumentoDTO;
import com.guardiao.iot.entity.DocumentoEntity.Documento;
import com.guardiao.iot.entity.TipoDocumentoEntity.TipoDocumental;
import com.guardiao.iot.infrastructure.irepository.DocumentoRepository;
import com.guardiao.iot.infrastructure.irepository.TipoDocumentalRepository;
import com.guardiao.iot.mappers.DocumentoMapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

        if(documentoDTO.getId() != null){
            documento = documentoRepository.findById(documentoDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Documento não existe"));
        }else{
            documento = DocumentoMapper.INSTANCE.toEntity(documentoDTO);
            documento.setDataHora(LocalDateTime.now());
        }

        associarEntidades(documento, documentoDTO);
        addDocuementoToTipoDocumental(documento, documento.getTipoDocumental());

        try {
            documento.setArquivoPdf(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar o arquivo PDF", e);
        }

        Documento savedDocumento = documentoRepository.save(documento);
        System.out.println("Arquivo PDF: " + savedDocumento.getArquivoPdf());
        return DocumentoMapper.INSTANCE.toDTO(savedDocumento);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        documentoRepository.deleteById(id);
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
        // Associar Tipo Documental
        if (documentoDTO.getTipoDocumentalId() != null) {
            TipoDocumental tipoDocumental = tipoDocumentalRepository.findById(documentoDTO.getTipoDocumentalId())
                    .orElseThrow(() -> new EntityNotFoundException("Tipo Documental não existe"));
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


}