package com.guardiao.iot.bussines.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guardiao.iot.bussines.iservice.TipoDocumentalService;
import com.guardiao.iot.dto.TipoDocumentalDTO;
import com.guardiao.iot.entity.DocumentoEntity.Documento;
import com.guardiao.iot.entity.TipoDocumentoEntity.TipoDocumental;
import com.guardiao.iot.entity.UsuarioEntity.Usuario;
import com.guardiao.iot.infrastructure.irepository.DocumentoRepository;
import com.guardiao.iot.infrastructure.irepository.TipoDocumentalRepository;
import com.guardiao.iot.infrastructure.irepository.UsuarioRepository;
import com.guardiao.iot.mappers.TipoDocumentalMapper;

@Service
public class TipoDocumentalServiceImpl implements TipoDocumentalService {

    @Autowired
    private TipoDocumentalRepository tipoDocumentalRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DocumentoRepository documentoRepository;

    @Override
    @Transactional
    public List<TipoDocumentalDTO> findAll() {
        return tipoDocumentalRepository.findAll().stream()
                .map(TipoDocumentalMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TipoDocumentalDTO> findById(Long id) {
        return tipoDocumentalRepository.findById(id)
                .map(TipoDocumentalMapper.INSTANCE::toDTO);
    }

    @Override
    @Transactional
    public TipoDocumentalDTO save(TipoDocumentalDTO tipoDocumentalDTO, Long idUsuario) throws IllegalAccessException {
        String nomeDocumentoPadronizado = tipoDocumentalDTO.getNomeDocumento().trim();
        if (tipoDocumentalRepository.existsByNomeDocumento(nomeDocumentoPadronizado)) {
            throw new IllegalArgumentException("Já existe um tipo documental com o nome fornecido.");
        }

        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);
        if (usuarioOpt.isPresent() && usuarioOpt.get().getAdmin()) {
            TipoDocumental tipoDocumental;

            if (tipoDocumentalDTO.getId() != null) {
                tipoDocumental = tipoDocumentalRepository.findById(tipoDocumentalDTO.getId())
                        .orElseThrow(() -> new EntityNotFoundException("Tipo Documental não existe"));

               
                System.out.println(tipoDocumental.getDocumentos().size());
                
                tipoDocumental.setNomeDocumento(nomeDocumentoPadronizado);
                tipoDocumental.setLeiRegulamentadora(tipoDocumentalDTO.getLeiRegulamentadora());
                tipoDocumental.setStatus(tipoDocumentalDTO.getStatus());
                tipoDocumental.setTempoRetencao(tipoDocumentalDTO.getTempoRetencao());
               

            } else {
                tipoDocumental = TipoDocumentalMapper.INSTANCE.toEntity(tipoDocumentalDTO);
                tipoDocumental.setNomeDocumento(nomeDocumentoPadronizado);
            }

            TipoDocumental savedTipoDocumental = tipoDocumentalRepository.save(tipoDocumental);
            return TipoDocumentalMapper.INSTANCE.toDTO(savedTipoDocumental);
        } else {
            throw new IllegalAccessException("Usuário não tem permissão para criar tipo documental");
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        TipoDocumental tipoDocumental = tipoDocumentalRepository.findById(id)
            .orElseThrow(()-> new EntityNotFoundException("tipo documental não existe"));
            desassociarDocumentos(tipoDocumental);
            tipoDocumentalRepository.deleteById(id);
    }

    private void desassociarDocumentos(TipoDocumental tipoDocumental) {
        List<Documento> documentos = documentoRepository.findByTipoDocumentalId(tipoDocumental.getId());
        for (Documento documento : documentos) {
            tipoDocumental.removeDocumento(documento);
        }
        tipoDocumentalRepository.save(tipoDocumental);
    }
}