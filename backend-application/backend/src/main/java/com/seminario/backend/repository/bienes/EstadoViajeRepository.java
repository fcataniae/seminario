package com.seminario.backend.repository.bienes;

import com.seminario.backend.model.bienes.EstadoViaje;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadoViajeRepository extends JpaRepository<EstadoViaje, Long> {
    EstadoViaje findById(Long Id);
    EstadoViaje findByDescrip(String descrip);
}
