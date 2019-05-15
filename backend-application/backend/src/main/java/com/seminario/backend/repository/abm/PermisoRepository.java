package com.seminario.backend.repository.abm;

import com.seminario.backend.model.abm.Permiso;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * User: fcatania
 * Date: 1/4/2019
 * Time: 09:19
 */
public interface PermisoRepository extends JpaRepository<Permiso,Long> {
    Permiso findByNombre(String nombre);
    Permiso findById(Long Id);

    @Query("SELECT p FROM Usuario u " +
            "JOIN u.roles r " +
            "JOIN r.permisos p " +
            "WHERE u.id = ?1")
    List<Permiso> findAllPermisosWhereUsuario(Long id);

    @Query("SELECT distinct(p) FROM Usuario u " +
            "JOIN u.roles r " +
            "JOIN r.permisos p " +
            "WHERE u.id = ?1 and p.nombre= ?2")
    Permiso findPermisoWhereUsuarioAndPermiso(Long id, String nombre);
}