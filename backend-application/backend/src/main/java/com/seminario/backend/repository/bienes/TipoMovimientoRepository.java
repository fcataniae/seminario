package com.seminario.backend.repository.bienes;

import com.seminario.backend.model.bienes.TipoAgente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.seminario.backend.model.bienes.TipoMovimiento;

public interface TipoMovimientoRepository extends JpaRepository<TipoMovimiento, Long> {

    @Query("SELECT t from TipoMovimiento t " +
            "WHERE t.nombre = ?1 and t.tipoAgenteOrigen = ?2 and t.tipoAgenteDestino = ?3")
    TipoMovimiento findByNombreAndTipoOrigenAndTipoDestino(String tipoMovimientoNombre, TipoAgente origen, TipoAgente destino);
}
