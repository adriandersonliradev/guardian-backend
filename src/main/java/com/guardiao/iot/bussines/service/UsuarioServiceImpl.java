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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.cache.annotation.Cacheable;


@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    
    @Cacheable(value = "usuarioCache", key = "#email")
    public Optional<UsuarioDTO> findByEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .map(UsuarioMapper.INSTANCE::toDTO);
    }

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
        if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new IllegalArgumentException("Email já está em uso.");
        }
        Usuario usuario = UsuarioMapper.INSTANCE.toEntity(usuarioDTO);
        Usuario savedUsuario = usuarioRepository.save(usuario);
        return UsuarioMapper.INSTANCE.toDTO(savedUsuario);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    @Transactional
    public UsuarioDTO login(String email, String senha) {
        Optional<UsuarioDTO> usuarioOpt = findByEmail(email);

        if (usuarioOpt.isPresent()) {
            UsuarioDTO usuarioDTO = usuarioOpt.get();

            if (usuarioDTO.getSenha().equals(senha)) {
                return usuarioDTO; 
            } else {
                throw new IllegalArgumentException("Senha incorreta.");
            }
        } else {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }
    }
}