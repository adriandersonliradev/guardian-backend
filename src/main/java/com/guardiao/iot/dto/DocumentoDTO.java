package com.guardiao.iot.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DocumentoDTO {
    private Long id;
    private LocalDate dataHora;
    private String nomeDocumento;
    private Long tipoDocumentalId;
    private Long usuarioId;
    private byte[] arquivoPdf;
    //private LocalDateTime dataExpiracao;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public LocalDate getDataHora() {
        return dataHora;
    }
    public void setDataHora(LocalDate dataHora) {
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
    public byte[] getArquivoPdf() {
        return arquivoPdf;
    }
    public void setArquivoPdf(byte[] arquivoPdf) {
        this.arquivoPdf = arquivoPdf;
    }
    public String getNomeDocumento() {
        return nomeDocumento;
    }
    public void setNomeDocumento(String nomeDocumento) {
        this.nomeDocumento = nomeDocumento;
    }
    /*public LocalDateTime getDataExpiracao() {
        return dataExpiracao;
    }
    public void setDataExpiracao(LocalDateTime dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }*/
}