package com.guardiao.iot.bussines.iservice;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.guardiao.iot.dto.DocumentoDTO;
import com.guardiao.iot.entity.DocumentoEntity.Documento;

public interface DocumentoService {
    List<DocumentoDTO> findAll();
    Optional<DocumentoDTO> findById(Long id);
    DocumentoDTO save(DocumentoDTO documento, MultipartFile file);
    void deleteById(Long id);
    byte[] getDocumentoArquivo(Long id);
}
