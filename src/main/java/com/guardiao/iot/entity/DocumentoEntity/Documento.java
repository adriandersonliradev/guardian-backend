package com.guardiao.iot.entity.DocumentoEntity;

import javax.persistence.*;

import com.guardiao.iot.entity.TipoDocumentoEntity.TipoDocumental;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Documento {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDateTime dataHora;

    @OneToOne
    @JoinColumn(name = "tipo_documental_id")
    private TipoDocumental tipoDocumental;

    // Getters and Setters
}