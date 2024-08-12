package com.guardiao.iot.bussines.iservice;

import java.util.List;
import java.util.Optional;

import com.guardiao.iot.dto.TipoDocumentalDTO;
import com.guardiao.iot.dto.UsuarioDTO;

public interface UsuarioService {
    List<UsuarioDTO> findAll();
    Optional<UsuarioDTO> findById(Long id);
    UsuarioDTO save(UsuarioDTO tipoDocumentalDTO);
    void deleteById(Long id);
    
} 