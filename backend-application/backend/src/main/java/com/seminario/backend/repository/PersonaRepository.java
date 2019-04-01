package com.seminario.backend.repository;

import com.seminario.backend.model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * User: fcatania
 * Date: 1/4/2019
 * Time: 09:17
 */
public interface PersonaRepository extends JpaRepository<Persona, Long>{
}