package com.seminario.backend.repository;

import com.seminario.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * User: fcatania
 * Date: 1/4/2019
 * Time: 09:18
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long>  {

    Usuario findByNombreUsuario(String name);
    Usuario findById(Long Id);
}