package com.guardiao.iot.entity.DocumentoEntity;

import javax.persistence.*;

import com.guardiao.iot.entity.TipoDocumentoEntity.TipoDocumental;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    private String nomeDocumento;
    private LocalDate dataHora;

    @OneToOne
    @JoinColumn(name = "tipo_documental_id")
    private TipoDocumental tipoDocumental;

    @Lob
    private byte[] arquivoPdf;

    //private LocalDate dataExpiracao;
}