package com.seminario.backend.repository.bienes;

import com.seminario.backend.model.bienes.BienIntercambiable;
import com.seminario.backend.model.bienes.IntercambioProveedor;
import com.seminario.backend.model.bienes.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface IntercambioProveedorRepository extends JpaRepository<IntercambioProveedor, Long> {
    IntercambioProveedor findByNombreIntercambio(String nombreIntercambio);
    IntercambioProveedor findByProveedorAndBienIntercambiableEntregado(Proveedor p, BienIntercambiable bienIntercambiableEntregado);
    @Query(value = "SELECT * FROM IntercambioProveedor ip WHERE ip.proveedor_nro = ?1 and " +
            "ip.BIENINTERCAMBIABLEENTREGADO_ID = ?2 and " +
            "ip.fechaalta <= ?3 ORDER BY ip.fechaalta", nativeQuery = true)
    IntercambioProveedor findByProveedorAndFechaLessEquals(Long p, Long id,  Date fecha);

    List<IntercambioProveedor> findByProveedor(Proveedor proveedor);
}
