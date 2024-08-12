package com.guardiao.iot.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

public class DocumentoDTO {
    private Long id;
    private LocalDateTime dataHora;
    private Long tipoDocumentalId;
    private Long usuarioId;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public LocalDateTime getDataHora() {
        return dataHora;
    }
    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
    public Long getTipoDocumentalId() {
        return tipoDocumentalId;
    }
    public void setTipoDocumentalId(Long tipoDocumentalId) {
        this.tipoDocumentalId = tipoDocumentalId;
    }
    public Long getUsuarioId() {
        return usuarioId;
    }
    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    
}
