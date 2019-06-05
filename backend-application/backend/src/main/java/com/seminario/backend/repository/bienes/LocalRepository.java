package com.seminario.backend.repository.bienes;

import com.seminario.backend.model.bienes.Local;
import com.seminario.backend.model.bienes.TipoAgente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LocalRepository extends JpaRepository<Local, Long> {
    Local findByNro(Long nro);
    Local findByNombre(String nombre);
    List<Local> findAllByTipoAgente(TipoAgente tipoAgente);


    @Query(value = "SELECT aux.TIENDA, aux.CANT_RECIBIDA, aux2.CANT_ENVIADA FROM (\n" +
            "\tSELECT m.DESTINO AS TIENDA, SUM(im.CANTIDAD) AS CANT_RECIBIDA \n" +
            "\tFROM seminario.movimiento m\n" +
            "\tLEFT JOIN seminario.itemmovimiento im on m.id = im.movimiento_id \n" +
            "\tWHERE m.id_tipo_movimiento = 1 -- ENVIO\n" +
            "\tAND FECHASALIDA BETWEEN ?1 AND ?2\n" +
            "\tGROUP BY TIENDA\n" +
            ") aux LEFT JOIN (\n" +
            "\tSELECT m.ORIGEN AS TIENDA, SUM(im.CANTIDAD) AS CANT_ENVIADA \n" +
            "\tFROM seminario.movimiento m\n" +
            "\tLEFT JOIN seminario.itemmovimiento im on m.id = im.movimiento_id \n" +
            "\tWHERE m.id_tipo_movimiento = 7 -- DEVOLUCION desde Tienda\n" +
            "\tAND FECHASALIDA BETWEEN ?1 AND ?2\n" +
            "\tGROUP BY TIENDA\n" +
            ") aux2 on aux.TIENDA = aux2.TIENDA\n" +
            "UNION\n" +
            "SELECT aux.TIENDA, aux.CANT_RECIBIDA, aux2.CANT_ENVIADA FROM (\n" +
            "\tSELECT m.DESTINO AS TIENDA, SUM(im.CANTIDAD) AS CANT_RECIBIDA \n" +
            "\tFROM seminario.movimiento m\n" +
            "\tLEFT JOIN seminario.itemmovimiento im on m.id = im.movimiento_id \n" +
            "\tWHERE m.id_tipo_movimiento = 1 -- ENVIO\n" +
            "\tAND FECHASALIDA BETWEEN ?1 AND ?2\n" +
            "\tGROUP BY TIENDA\n" +
            ") aux RIGHT JOIN (\n" +
            "\tSELECT m.ORIGEN AS TIENDA, SUM(im.CANTIDAD) AS CANT_ENVIADA \n" +
            "\tFROM seminario.movimiento m\n" +
            "\tLEFT JOIN seminario.itemmovimiento im on m.id = im.movimiento_id \n" +
            "\tWHERE m.id_tipo_movimiento = 7 -- DEVOLUCION desde Tienda\n" +
            "\tAND FECHASALIDA BETWEEN ?1 AND ?2\n" +
            "\tGROUP BY TIENDA\n" +
            ") aux2 on aux.TIENDA = aux2.TIENDA\n", nativeQuery = true)
    List<Object[]> findAllCantidadEnviadaYRecibida(String fechaDesde, String fechaHasta);

    @Query(value = "SELECT aux.TIENDA, aux.CANT_RECIBIDA, aux2.CANT_ENVIADA FROM (\n" +
            "\tSELECT m.DESTINO AS TIENDA, SUM(im.CANTIDAD) AS CANT_RECIBIDA \n" +
            "\tFROM seminario.movimiento m\n" +
            "\tLEFT JOIN seminario.itemmovimiento im on m.id = im.movimiento_id \n" +
            "\tWHERE m.id_tipo_movimiento = 1 -- ENVIO\n" +
            "\tGROUP BY TIENDA\n" +
            ") aux LEFT JOIN (\n" +
            "\tSELECT m.ORIGEN AS TIENDA, SUM(im.CANTIDAD) AS CANT_ENVIADA \n" +
            "\tFROM seminario.movimiento m\n" +
            "\tLEFT JOIN seminario.itemmovimiento im on m.id = im.movimiento_id \n" +
            "\tWHERE m.id_tipo_movimiento = 7 -- DEVOLUCION desde Tienda\n" +
            "\tGROUP BY TIENDA\n" +
            ") aux2 on aux.TIENDA = aux2.TIENDA\n" +
            "UNION\n" +
            "SELECT aux.TIENDA, aux.CANT_RECIBIDA, aux2.CANT_ENVIADA FROM (\n" +
            "\tSELECT m.DESTINO AS TIENDA, SUM(im.CANTIDAD) AS CANT_RECIBIDA \n" +
            "\tFROM seminario.movimiento m\n" +
            "\tLEFT JOIN seminario.itemmovimiento im on m.id = im.movimiento_id \n" +
            "\tWHERE m.id_tipo_movimiento = 1 -- ENVIO\n" +
            "\tGROUP BY TIENDA\n" +
            ") aux RIGHT JOIN (\n" +
            "\tSELECT m.ORIGEN AS TIENDA, SUM(im.CANTIDAD) AS CANT_ENVIADA \n" +
            "\tFROM seminario.movimiento m\n" +
            "\tLEFT JOIN seminario.itemmovimiento im on m.id = im.movimiento_id \n" +
            "\tWHERE m.id_tipo_movimiento = 7 -- DEVOLUCION desde Tienda\n" +
            "\tGROUP BY TIENDA\n" +
            ") aux2 on aux.TIENDA = aux2.TIENDA", nativeQuery = true)
    List<Object[]> findAllCantidadEnviadaYRecibida();



    @Query(value = "SELECT aux.TIENDA, aux.COSTO_CANT_RECIBIDA, aux2.COSTO_CANT_ENVIADA FROM (\n" +
            "\tSELECT m.DESTINO AS TIENDA, SUM(im.PRECIO) AS COSTO_CANT_RECIBIDA \n" +
            "\tFROM seminario.movimiento m\n" +
            "\tLEFT JOIN seminario.itemmovimiento im on m.id = im.movimiento_id \n" +
            "\tWHERE m.id_tipo_movimiento = 1 -- ENVIO\n" +
            "\tAND FECHASALIDA BETWEEN ?1 AND ?2\n" +
            "\tGROUP BY TIENDA\n" +
            ") aux LEFT JOIN (\n" +
            "\tSELECT m.ORIGEN AS TIENDA, SUM(im.PRECIO) AS COSTO_CANT_ENVIADA \n" +
            "\tFROM seminario.movimiento m\n" +
            "\tLEFT JOIN seminario.itemmovimiento im on m.id = im.movimiento_id \n" +
            "\tWHERE m.id_tipo_movimiento = 7 -- DEVOLUCION desde Tienda\n" +
            "\tAND FECHASALIDA BETWEEN ?1 AND ?2\n" +
            "\tGROUP BY TIENDA\n" +
            ") aux2 on aux.TIENDA = aux2.TIENDA\n" +
            "UNION\n" +
            "SELECT aux.TIENDA, aux.COSTO_CANT_RECIBIDA, aux2.COSTO_CANT_ENVIADA FROM (\n" +
            "\tSELECT m.DESTINO AS TIENDA, SUM(im.PRECIO) AS COSTO_CANT_RECIBIDA \n" +
            "\tFROM seminario.movimiento m\n" +
            "\tLEFT JOIN seminario.itemmovimiento im on m.id = im.movimiento_id \n" +
            "\tWHERE m.id_tipo_movimiento = 1 -- ENVIO\n" +
            "\tAND FECHASALIDA BETWEEN ?1 AND ?2\n" +
            "\tGROUP BY TIENDA\n" +
            ") aux RIGHT JOIN (\n" +
            "\tSELECT m.ORIGEN AS TIENDA, SUM(im.PRECIO) AS COSTO_CANT_ENVIADA \n" +
            "\tFROM seminario.movimiento m\n" +
            "\tLEFT JOIN seminario.itemmovimiento im on m.id = im.movimiento_id \n" +
            "\tWHERE m.id_tipo_movimiento = 7 -- DEVOLUCION desde Tienda\n" +
            "\tAND FECHASALIDA BETWEEN ?1 AND ?2\n" +
            "\tGROUP BY TIENDA\n" +
            ") aux2 on aux.TIENDA = aux2.TIENDA", nativeQuery = true)
    List<Object[]> findAllCantidadEnviadaYRecibidaMonetaria(String fechaDesde, String fechaHasta);


    @Query(value = "SELECT aux.TIENDA, aux.COSTO_CANT_RECIBIDA, aux2.COSTO_CANT_ENVIADA FROM (\n" +
            "\tSELECT m.DESTINO AS TIENDA, SUM(im.PRECIO) AS COSTO_CANT_RECIBIDA \n" +
            "\tFROM seminario.movimiento m\n" +
            "\tLEFT JOIN seminario.itemmovimiento im on m.id = im.movimiento_id \n" +
            "\tWHERE m.id_tipo_movimiento = 1 -- ENVIO\n" +
            "\tGROUP BY TIENDA\n" +
            ") aux LEFT JOIN (\n" +
            "\tSELECT m.ORIGEN AS TIENDA, SUM(im.PRECIO) AS COSTO_CANT_ENVIADA \n" +
            "\tFROM seminario.movimiento m\n" +
            "\tLEFT JOIN seminario.itemmovimiento im on m.id = im.movimiento_id \n" +
            "\tWHERE m.id_tipo_movimiento = 7 -- DEVOLUCION desde Tienda\n" +
            "\tGROUP BY TIENDA\n" +
            ") aux2 on aux.TIENDA = aux2.TIENDA\n" +
            "UNION\n" +
            "SELECT aux.TIENDA, aux.COSTO_CANT_RECIBIDA, aux2.COSTO_CANT_ENVIADA FROM (\n" +
            "\tSELECT m.DESTINO AS TIENDA, SUM(im.PRECIO) AS COSTO_CANT_RECIBIDA \n" +
            "\tFROM seminario.movimiento m\n" +
            "\tLEFT JOIN seminario.itemmovimiento im on m.id = im.movimiento_id \n" +
            "\tWHERE m.id_tipo_movimiento = 1 -- ENVIO\n" +
            "\tGROUP BY TIENDA\n" +
            ") aux RIGHT JOIN (\n" +
            "\tSELECT m.ORIGEN AS TIENDA, SUM(im.PRECIO) AS COSTO_CANT_ENVIADA \n" +
            "\tFROM seminario.movimiento m\n" +
            "\tLEFT JOIN seminario.itemmovimiento im on m.id = im.movimiento_id \n" +
            "\tWHERE m.id_tipo_movimiento = 7 -- DEVOLUCION desde Tienda\n" +
            "\tGROUP BY TIENDA\n" +
            ") aux2 on aux.TIENDA = aux2.TIENDA", nativeQuery = true)
    List<Object[]> findAllCantidadEnviadaYRecibidaMonetaria();
}
