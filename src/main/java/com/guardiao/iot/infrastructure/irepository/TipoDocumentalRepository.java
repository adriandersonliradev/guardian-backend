package com.guardiao.iot.infrastructure.irepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardiao.iot.entity.TipoDocumentoEntity.TipoDocumental;

@Repository
public interface TipoDocumentalRepository extends JpaRepository<TipoDocumental, Long> {
    boolean existsByNomeDocumento(String nomeDocumento);
    Optional<TipoDocumental> findByNomeDocumento(String nomeDocumento);
} 