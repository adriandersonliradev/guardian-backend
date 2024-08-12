package com.guardiao.iot.infrastructure.irepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardiao.iot.entity.TipoDocumentoEntity.TipoDocumental;

@Repository
public interface TipoDocumentalRepository extends JpaRepository<TipoDocumental, Long> {

    
} 