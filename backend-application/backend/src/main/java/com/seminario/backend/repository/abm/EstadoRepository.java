package com.seminario.backend.repository.abm;

import com.seminario.backend.model.abm.Estado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadoRepository extends JpaRepository<Estado, Long> {
    Estado findById(Long Id);
    Estado findByDescrip(String descrip);
}
