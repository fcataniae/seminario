package com.seminario.backend.repository.bienes;

import com.seminario.backend.model.bienes.BienIntercambiable;
import com.seminario.backend.model.bienes.ItemMovimiento;
import com.seminario.backend.model.bienes.Movimiento;
import com.seminario.backend.model.bienes.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemMovimientoRepository  extends JpaRepository<ItemMovimiento, Long> {
    List<ItemMovimiento> findAllWhereBI(BienIntercambiable bi);
    List<ItemMovimiento> findAllWhereMovimiento(Movimiento movimiento);
}

