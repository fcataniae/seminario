package com.seminario.backend.repository.bienes;

import com.seminario.backend.model.bienes.TipoAgente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.seminario.backend.model.bienes.TipoMovimiento;

public interface TipoMovimientoRepository extends JpaRepository<TipoMovimiento, Long> {

    @Query(value = "SELECT t from TipoMovimiento t " +
            "WHERE t.tipo = ?1 and t.tipoAgenteOrigen.nombre = ?2 and  t.tipoAgenteDestino.nombre  = ?3")
    TipoMovimiento findByNombreAndTipoOrigenAndTipoDestino(String tipoMovimientoNombre, String TipoAgenteOrigen, String TipoAgenteDestino);

    TipoMovimiento findByTipo(String tipo);
}
