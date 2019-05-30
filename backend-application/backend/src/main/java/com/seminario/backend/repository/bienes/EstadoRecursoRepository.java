package com.seminario.backend.repository.bienes;

import com.seminario.backend.model.bienes.EstadoRecurso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadoRecursoRepository extends JpaRepository<EstadoRecurso, Long> {
    EstadoRecurso findByDescrip(String descrip);
}
