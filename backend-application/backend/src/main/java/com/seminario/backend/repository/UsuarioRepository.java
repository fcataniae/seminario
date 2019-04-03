package com.seminario.backend.repository;

import com.seminario.backend.model.Rol;
import com.seminario.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * User: fcatania
 * Date: 1/4/2019
 * Time: 09:18
 */
public interface UsuarioRepository extends CrudRepository<Usuario, Long>  {

    Usuario findByNombreUsuario(String name);
    Usuario findById(Long Id);
}