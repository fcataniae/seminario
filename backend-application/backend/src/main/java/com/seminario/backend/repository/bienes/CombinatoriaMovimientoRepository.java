package com.seminario.backend.repository.bienes;

import com.seminario.backend.model.bienes.CombinatoriaMovimientos;
import com.seminario.backend.model.bienes.TipoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CombinatoriaMovimientoRepository extends JpaRepository<CombinatoriaMovimientos, Long> {

    @Query("SELECT c from CombinatoriaMovimientos c "+
            "JOIN TipoMovimiento t" +
            "WHERE t.nombre = ?1 and c.origen = ?2  and c.destino = ?3")
    CombinatoriaMovimientos findCombinatoriaByTipoMovWhereOrigenAndDestino(String tipoMovimientoNombre, String origen, String destino);
}
