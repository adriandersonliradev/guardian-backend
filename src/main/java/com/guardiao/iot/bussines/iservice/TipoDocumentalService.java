package com.guardiao.iot.bussines.iservice;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.guardiao.iot.dto.TipoDocumentalDTO;

public interface TipoDocumentalService {
    List<TipoDocumentalDTO> findAll();
    Optional<TipoDocumentalDTO> findById(Long id);
    TipoDocumentalDTO save(TipoDocumentalDTO tipoDocumentalDTO, Long idUsuario) throws IllegalAccessException;
    void deleteById(Long id);
    TipoDocumentalDTO classificarDocumentoEVerificar(MultipartFile file);
}