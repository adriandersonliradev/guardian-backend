package com.guardiao.iot.mappers;

import com.guardiao.iot.dto.DocumentoDTO;
import com.guardiao.iot.entity.DocumentoEntity.Documento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DocumentoMapper {
    DocumentoMapper INSTANCE = Mappers.getMapper(DocumentoMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "tipoDocumental.id", target = "tipoDocumentalId")
    @Mapping(source = "arquivoPdf", target = "arquivoPdf", ignore = true)
    DocumentoDTO toDTO(Documento documento);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "tipoDocumentalId", target = "tipoDocumental.id")
    @Mapping(source = "arquivoPdf", target = "arquivoPdf")
    Documento toEntity(DocumentoDTO documentoDTO);
}
