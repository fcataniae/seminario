package com.seminario.backend.repository.abm;

import com.seminario.backend.model.abm.Persona;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * User: fcatania
 * Date: 1/4/2019
 * Time: 09:17
 */
public interface PersonaRepository extends JpaRepository<Persona, Long> {
    List<Persona> findByNombre(String nombre);
    Persona findById(Long Id);
    Persona findByNroDoc(Long nroDoc);
}

