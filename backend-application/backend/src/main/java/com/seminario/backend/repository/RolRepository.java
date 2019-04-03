package com.seminario.backend.repository;

import com.seminario.backend.model.Rol;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * User: fcatania
 * Date: 1/4/2019
 * Time: 09:19
 */
public interface RolRepository extends CrudRepository<Rol, Long> {
    Rol findByNombre(String nombre);
    Rol findById(Long Id);
}