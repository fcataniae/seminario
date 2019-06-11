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


    @Query(value = "SELECT \n" +
            "\t\tCASE WHEN (aux.PROVEEDOR is NULL) THEN aux2.PROVEEDOR\n" +
            "\t\tELSE aux.PROVEEDOR END as PROVEEDOR,\n" +
            "\t\tCASE WHEN (aux.NOMBRE is NULL) THEN aux2.NOMBRE\n" +
            "\t \tELSE aux.NOMBRE END as NOMBRE,\n" +
            "\t\tCASE WHEN (COSTO_CANT_RECIBIDA is NULL) THEN COSTO_CANT_ENVIADA\n" +
            "\t\t\t WHEN (COSTO_CANT_ENVIADA is NULL) THEN -1*COSTO_CANT_RECIBIDA\n" +
            "\t\tELSE COSTO_CANT_ENVIADA - COSTO_CANT_RECIBIDA END as DEUDA FROM (\n" +
            "\t\tSELECT m.DESTINO AS PROVEEDOR, p.NOMBRE, SUM(im.PRECIO * im.CANTIDAD) AS COSTO_CANT_RECIBIDA \n" +
            "\t\tFROM seminario.movimiento m\n" +
            "\t\tLEFT JOIN seminario.itemmovimiento im on m.id = im.movimiento_id \n" +
            "\t\tINNER JOIN seminario.proveedor p on m.DESTINO = p.nro \n" +
            "\t\tWHERE m.id_tipo_movimiento in (5, 7, 8) -- (Intercambio, Devolucion desde Tienda, Devolucion de CD)\n" +
            "\t\tGROUP BY PROVEEDOR\n" +
            ") aux LEFT JOIN (\n" +
            "\t\tSELECT m.ORIGEN AS PROVEEDOR, p.NOMBRE, SUM(im.PRECIO * im.CANTIDAD) AS COSTO_CANT_ENVIADA \n" +
            "\t\tFROM seminario.movimiento m\n" +
            "\t\tLEFT JOIN seminario.itemmovimiento im on m.id = im.movimiento_id\n" +
            "\t\tINNER JOIN seminario.proveedor p on m.ORIGEN = p.nro \n" +
            "\t\tWHERE m.id_tipo_movimiento in (6, 3) -- Recepcion desde Proveedor y Recepcion de Intercambio\n" +
            "\t\tGROUP BY PROVEEDOR\n" +
            ") aux2 on aux.PROVEEDOR = aux2.PROVEEDOR\n" +
            "UNION SELECT \n" +
            "\t\tCASE WHEN (aux.PROVEEDOR is NULL) THEN aux2.PROVEEDOR\n" +
            "\t\tELSE aux.PROVEEDOR END as PROVEEDOR,\n" +
            "\t\tCASE WHEN (aux.NOMBRE is NULL) THEN aux2.NOMBRE\n" +
            "\t \tELSE aux.NOMBRE END as NOMBRE,\n" +
            "\t\tCASE WHEN (COSTO_CANT_RECIBIDA is NULL) THEN COSTO_CANT_ENVIADA\n" +
            "\t\t\t WHEN (COSTO_CANT_ENVIADA is NULL) THEN -1*COSTO_CANT_RECIBIDA\n" +
            "\t\tELSE COSTO_CANT_ENVIADA - COSTO_CANT_RECIBIDA END as DEUDA FROM (\n" +
            "\t\tSELECT m.DESTINO AS PROVEEDOR, p.NOMBRE, SUM(im.PRECIO * im.CANTIDAD) AS COSTO_CANT_RECIBIDA \n" +
            "\t\tFROM seminario.movimiento m\n" +
            "\t\tLEFT JOIN seminario.itemmovimiento im on m.id = im.movimiento_id \n" +
            "\t\tINNER JOIN seminario.proveedor p on m.DESTINO = p.nro \n" +
            "\t\tWHERE m.id_tipo_movimiento in (5, 7, 8) -- (Intercambio, Devolucion desde Tienda, Devolucion de CD)\n" +
            "\t\tGROUP BY PROVEEDOR\n" +
            ") aux RIGHT JOIN (\n" +
            "\t\tSELECT m.ORIGEN AS PROVEEDOR, p.NOMBRE, SUM(im.PRECIO * im.CANTIDAD) AS COSTO_CANT_ENVIADA \n" +
            "\t\tFROM seminario.movimiento m\n" +
            "\t\tLEFT JOIN seminario.itemmovimiento im on m.id = im.movimiento_id\n" +
            "\t\tINNER JOIN seminario.proveedor p on m.ORIGEN = p.nro \n" +
            "\t\tWHERE m.id_tipo_movimiento in (6, 3) -- Recepcion desde Proveedor y Recepcion de Intercambio\n" +
            "\t\tGROUP BY PROVEEDOR\n" +
            ") aux2 on aux.PROVEEDOR = aux2.PROVEEDOR\n" +
            "ORDER BY DEUDA DESC", nativeQuery = true)
    List<Object[]> findAllDeudas();
}
