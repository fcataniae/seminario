package com.seminario.backend.repository.abm;

import com.seminario.backend.model.abm.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * User: fcatania
 * Date: 1/4/2019
 * Time: 09:19
 */
public interface RolRepository extends JpaRepository<Rol, Long> {
    Rol findByNombre(String nombre);
    Rol findById(Long Id);
}