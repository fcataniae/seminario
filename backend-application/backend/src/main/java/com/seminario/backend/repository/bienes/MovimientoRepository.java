package com.seminario.backend.repository.bienes;

import com.seminario.backend.model.bienes.EstadoViaje;
import com.seminario.backend.model.bienes.Movimiento;
import com.seminario.backend.model.bienes.TipoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {


    List<Movimiento> findByTipoMovimientoAndEstadoViajeAndDestino(TipoMovimiento tipo, EstadoViaje estado, Long nroLocalDestino);
}
