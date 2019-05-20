package com.seminario.backend.repository.bienes;

import com.seminario.backend.model.bienes.Recurso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecursoRepository extends JpaRepository<Recurso, Long> {
    Recurso findById(Long id);
}
