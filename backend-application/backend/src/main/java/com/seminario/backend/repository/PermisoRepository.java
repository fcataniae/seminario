package com.seminario.backend.repository;

import com.seminario.backend.model.Permiso;
import java.util.List;

import com.seminario.backend.model.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * User: fcatania
 * Date: 1/4/2019
 * Time: 09:19
 */
public interface PermisoRepository extends CrudRepository<Permiso,Long>{
    Permiso findByNombre(String nombre);
    Permiso findById(Long Id);

    @Query("SELECT p FROM Usuario u " +
            "JOIN u.roles r " +
            "JOIN r.permisos p " +
            "WHERE u.id = ?1")
    List<Permiso> findAllPermisosWhereUsuario(Long id);
}