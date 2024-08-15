package com.guardiao.iot.bussines.service;

import com.guardiao.iot.bussines.iservice.UsuarioService;
import com.guardiao.iot.dto.UsuarioDTO;
import com.guardiao.iot.entity.UsuarioEntity.Usuario;
import com.guardiao.iot.infrastructure.irepository.UsuarioRepository;
import com.guardiao.iot.mappers.UsuarioMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<UsuarioDTO> findAll() {
        return usuarioRepository.findAll().stream()
                .map(UsuarioMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UsuarioDTO> findById(Long id) {
        return usuarioRepository.findById(id)
                .map(UsuarioMapper.INSTANCE::toDTO);
    }

    @Override
    @Transactional
    public UsuarioDTO save(UsuarioDTO usuarioDTO) {
        Usuario usuario = UsuarioMapper.INSTANCE.toEntity(usuarioDTO);
        Usuario savedUsuario = usuarioRepository.save(usuario);
        return UsuarioMapper.INSTANCE.toDTO(savedUsuario);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        usuarioRepository.deleteById(id);
    }
}