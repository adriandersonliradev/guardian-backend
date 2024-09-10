package com.guardiao.iot.infrastructure.irepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.guardiao.iot.entity.DocumentoEntity.Documento;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Long> {
    public List<Documento> findByTipoDocumentalId(Long tipoDocumentalId);
    public List<Documento> findDocumentosExpirados(LocalDate dataAtual);
    boolean existsById(Long id);
}