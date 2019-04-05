package com.seminario.backend.repository;

import com.seminario.backend.model.Estado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadoRepository extends JpaRepository<Estado, Long> {
    Estado findById(Long Id);
    Estado findByDescrip(String descrip);
}
