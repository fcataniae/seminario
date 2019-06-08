package com.seminario.backend.repository.bienes;

import com.seminario.backend.model.bienes.EstadoViaje;
import com.seminario.backend.model.bienes.Movimiento;
import com.seminario.backend.model.bienes.TipoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    Movimiento findById(Long id);
    List<Movimiento> findByTipoMovimientoAndEstadoViajeAndDestino(TipoMovimiento tipo, EstadoViaje estado, Long nroLocalDestino);

    @Query(value = "select * from Movimiento where (origen = ?1 or destino = ?1 or ?1 is null)" , nativeQuery = true)
    List<Movimiento> findAllByLocal(Long nro);
}
