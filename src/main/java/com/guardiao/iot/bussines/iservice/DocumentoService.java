package com.guardiao.iot.bussines.iservice;

import java.util.List;
import java.util.Optional;

import com.guardiao.iot.dto.DocumentoDTO;
import com.guardiao.iot.entity.DocumentoEntity.Documento;

public interface DocumentoService {
    List<DocumentoDTO> findAll();
    Optional<DocumentoDTO> findById(Long id);
    DocumentoDTO save(DocumentoDTO documento);
    void deleteById(Long id);
}
