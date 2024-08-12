package com.guardiao.iot.entity.TipoDocumentoEntity;

import javax.persistence.*;

import com.guardiao.iot.entity.DocumentoEntity.Documento;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoDocumental {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nomeDocumento;
    private String leiRegulamentadora;
    private String status;
    private String tempoRetencao;

    @OneToMany(mappedBy = "tipoDocumental", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Documento> documentos = new ArrayList<>();

    // Getters e Setters

    public void addDocumento(Documento documento) {
        if (documento == null) {
            documentos = new ArrayList<>();
        }
        if (!documentos.contains(documento)) {
            documentos.add(documento);
        }
    }

    public void removeDocumento(Documento documento) {
        if (documento != null && documentos.contains(documento)) {
            documentos.remove(documento);
        }
    }
}
