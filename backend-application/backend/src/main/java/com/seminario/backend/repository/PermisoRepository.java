package com.seminario.backend.repository;

import com.seminario.backend.model.Permiso;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * User: fcatania
 * Date: 1/4/2019
 * Time: 09:19
 */
public interface PermisoRepository extends JpaRepository<Permiso,Long>{
}