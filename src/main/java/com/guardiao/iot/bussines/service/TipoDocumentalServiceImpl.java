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
import com.guardiao.iot.entity.TipoDocumentoEntity.TipoDocumental;
import com.guardiao.iot.entity.UsuarioEntity.Usuario;
import com.guardiao.iot.infrastructure.irepository.TipoDocumentalRepository;
import com.guardiao.iot.infrastructure.irepository.UsuarioRepository;
import com.guardiao.iot.mappers.TipoDocumentalMapper;

@Service
public class TipoDocumentalServiceImpl implements TipoDocumentalService {

    @Autowired
    private TipoDocumentalRepository tipoDocumentalRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

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
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);
        if (usuarioOpt.isPresent() && usuarioOpt.get().getAdmin()) {
            TipoDocumental tipoDocumental;

            if (tipoDocumentalDTO.getId() != null) {
                tipoDocumental = tipoDocumentalRepository.findById(tipoDocumentalDTO.getId())
                        .orElseThrow(() -> new EntityNotFoundException("Tipo Documental não existe"));

               
                System.out.println(tipoDocumental.getDocumentos().size());
                
                tipoDocumental.setNomeDocumento(tipoDocumentalDTO.getNomeDocumento());
                tipoDocumental.setLeiRegulamentadora(tipoDocumentalDTO.getLeiRegulamentadora());
                tipoDocumental.setStatus(tipoDocumentalDTO.getStatus());
                tipoDocumental.setTempoRetencao(tipoDocumentalDTO.getTempoRetencao());
               

            } else {
                tipoDocumental = TipoDocumentalMapper.INSTANCE.toEntity(tipoDocumentalDTO);
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
        tipoDocumentalRepository.deleteById(id);
    }
}