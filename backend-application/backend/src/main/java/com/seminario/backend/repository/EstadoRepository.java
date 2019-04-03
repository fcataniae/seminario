package com.seminario.backend.repository;

import com.seminario.backend.model.Estado;
import org.springframework.data.repository.CrudRepository;

public interface EstadoRepository extends CrudRepository<Estado, Long>  {
    Estado findById(Long Id);
}
