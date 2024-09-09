package com.guardiao.iot.dto;

import java.time.LocalDateTime;
import java.util.List;

public class TipoDocumentalDTO {
    private Long id;
    private String nomeDocumento;
    private String leiRegulamentadora;
    private boolean status;
    private String tempoRetencao;
    private List<Long> idDocumentos;
    private int quantidadeDocumentos;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNomeDocumento() {
        return nomeDocumento;
    }
    public void setNomeDocumento(String nomeDocumento) {
        this.nomeDocumento = nomeDocumento;
    }
    public String getLeiRegulamentadora() {
        return leiRegulamentadora;
    }
    public void setLeiRegulamentadora(String leiRegulamentadora) {
        this.leiRegulamentadora = leiRegulamentadora;
    }
    public boolean getStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
    public String getTempoRetencao() {
        return tempoRetencao;
    }
    public void setTempoRetencao(String tempoRetencao) {
        this.tempoRetencao = tempoRetencao;
    }
    public List<Long> getIdDocumentos() {
        return idDocumentos;
    }
    public void setIdDocumentos(List<Long> idDocumentos) {
        this.idDocumentos = idDocumentos;
    }

    public int getQuantidadeDocumentos() {
        return (idDocumentos != null) ? idDocumentos.size() : 0;
    }
    public void setQuantidadeDocumentos(int quantidadeDocumentos) {
        this.quantidadeDocumentos = quantidadeDocumentos;
    }
    
    
}
