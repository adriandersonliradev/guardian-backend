package com.guardiao.iot.mappers;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.guardiao.iot.dto.TipoDocumentalDTO;
import com.guardiao.iot.entity.DocumentoEntity.Documento;
import com.guardiao.iot.entity.TipoDocumentoEntity.TipoDocumental;

@Mapper
public interface TipoDocumentalMapper {
    TipoDocumentalMapper INSTANCE = Mappers.getMapper(TipoDocumentalMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "nomeDocumento", target = "nomeDocumento")
    @Mapping(source = "leiRegulamentadora", target = "leiRegulamentadora")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "tempoRetencao", target = "tempoRetencao") 
    @Mapping(source = "dataExpiracao", target = "dataExpiracao") 
    @Mapping(source = "documentos", target = "idDocumentos", qualifiedByName = "mapDocumentosIds")
    TipoDocumentalDTO toDTO(TipoDocumental tipoDocumental);

    

    @Mapping(source = "id", target = "id")
    @Mapping(source = "nomeDocumento", target = "nomeDocumento")
    @Mapping(source = "leiRegulamentadora", target = "leiRegulamentadora")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "tempoRetencao", target = "tempoRetencao") 
    @Mapping(source = "dataExpiracao", target = "dataExpiracao") 
    @Mapping(source = "idDocumentos", target = "documentos", ignore = true)
    TipoDocumental toEntity (TipoDocumentalDTO tipoDocumentalDTO);

    @Named("mapDocumentosIds")
    default List<Long> mapDocumentosIds(List<Documento> documentos) {
        return documentos.stream().map(Documento::getId).collect(Collectors.toList());
    }
}

