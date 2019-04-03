package com.seminario.backend.repository;

import com.seminario.backend.model.Persona;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 * User: fcatania
 * Date: 1/4/2019
 * Time: 09:17
 */
public interface PersonaRepository extends CrudRepository<Persona, Long>  {
    List<Persona> findByNombre(String nombre);
    Persona findById(Long Id);
}

