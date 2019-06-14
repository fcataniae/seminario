package com.seminario.backend.repository.bienes;

import com.seminario.backend.model.bienes.IntercambioProveedor;
import com.seminario.backend.model.bienes.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

public interface IntercambioProveedorRepository extends JpaRepository<IntercambioProveedor, Long> {
    IntercambioProveedor findByNombreIntercambio(String nombreIntercambio);
    IntercambioProveedor findByProveedor(Proveedor p);
    @Query("SELECT * FROM IntercambioProveedor ip WHERE ip.proveedor_nro = ?1 and ip.fechaalta <= ?2 ORDER BY ip.fechaalta LIMIT 1")
    IntercambioProveedor findByProveedorAndFechaLessEquals(Long p, Date fecha);
}
