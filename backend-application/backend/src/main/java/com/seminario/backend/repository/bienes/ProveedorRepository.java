package com.seminario.backend.repository.bienes;

import com.seminario.backend.model.bienes.Proveedor;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * User: mrgrassho
 * Date: 15/5/2019
 * Time: 19:15
 */
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    List<Proveedor> findByNombre(String nombre);
    Proveedor findById(Long Id);
    Proveedor findByNro(Long nro);
}