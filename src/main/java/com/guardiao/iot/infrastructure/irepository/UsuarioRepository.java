package com.guardiao.iot.infrastructure.irepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardiao.iot.entity.UsuarioEntity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}