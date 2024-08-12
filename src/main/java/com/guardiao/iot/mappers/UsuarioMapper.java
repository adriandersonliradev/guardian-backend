package com.guardiao.iot.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import com.guardiao.iot.dto.UsuarioDTO;
import com.guardiao.iot.entity.UsuarioEntity.Usuario;

@Mapper
public interface UsuarioMapper {
    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "nome", target = "nome")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "senha", target = "senha")
    @Mapping(source = "admin", target = "admin")
    UsuarioDTO toDTO(Usuario usuario);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "nome", target = "nome")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "senha", target = "senha")
    @Mapping(source = "admin", target = "admin")
    Usuario toEntity(UsuarioDTO usuarioDTO);


}
