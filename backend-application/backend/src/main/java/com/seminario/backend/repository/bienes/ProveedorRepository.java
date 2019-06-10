package com.seminario.backend.repository.bienes;

import com.seminario.backend.model.bienes.Proveedor;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * User: mrgrassho
 * Date: 15/5/2019
 * Time: 19:15
 */
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    Proveedor findByNombre(String nombre);
    Proveedor findByNro(Long nro);


    @Query(value = "SELECT aux.PROVEEDOR, (COSTO_CANT_ENVIADA - COSTO_CANT_RECIBIDA) as DEUDA FROM (\n" +
            "\t\tSELECT m.DESTINO AS PROVEEDOR, SUM(im.PRECIO) AS COSTO_CANT_RECIBIDA \n" +
            "\t\tFROM seminario.movimiento m\n" +
            "\t\tLEFT JOIN seminario.itemmovimiento im on m.id = im.movimiento_id \n" +
            "\t\tWHERE m.id_tipo_movimiento in (5, 7, 8) -- (Intercambio, Devolucion desde Tienda, Devolucion de CD)\n" +
            "\t\tGROUP BY PROVEEDOR\n" +
            ") aux LEFT JOIN (\n" +
            "\t\tSELECT m.ORIGEN AS PROVEEDOR, SUM(im.PRECIO) AS COSTO_CANT_ENVIADA \n" +
            "\t\tFROM seminario.movimiento m\n" +
            "\t\tLEFT JOIN seminario.itemmovimiento im on m.id = im.movimiento_id\n" +
            "\t\tWHERE m.id_tipo_movimiento in (6, 3) -- Recepcion desde Proveedor y Recepcion de Intercambio\n" +
            "\t\tGROUP BY PROVEEDOR\n" +
            ") aux2 on aux.PROVEEDOR = aux2.PROVEEDOR\n" +
            "ORDER BY PROVEEDOR LIMIT 10", nativeQuery = true)
    List<Proveedor> findDeuda();
}
