package com.seminario.backend.repository.bienes;

import com.seminario.backend.model.bienes.Local;
import com.seminario.backend.model.bienes.TipoAgente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface LocalRepository extends JpaRepository<Local, Long> {
    Local findByNro(Long nro);
    Local findByNombre(String nombre);
    List<Local> findAllByTipoAgente(TipoAgente tipoAgente);


    @Query(value = "SELECT aux.TIENDA, IFNULL(CAST(aux.CANT_RECIBIDA as SIGNED),0) as CANT_RECIBIDA, IFNULL(CAST(aux2.CANT_ENVIADA as SIGNED),0) as CANT_ENVIADA FROM ( \n " +
            "\t SELECT m.DESTINO AS TIENDA, SUM(im.CANTIDAD) AS CANT_RECIBIDA  \n " +
            "\t FROM seminario.movimiento m \n " +
            "\t LEFT JOIN seminario.itemmovimiento im on m.id = im.movimiento_id  \n " +
            "\t WHERE m.id_tipo_movimiento = 1 -- ENVIO \n " +
            "\t AND ( FECHASALIDA > ?1 or ?1 is null) AND (FECHASALIDA < ?2 or ?2 is null)  \n " +
            "\t GROUP BY TIENDA  \n " +
            ") aux LEFT JOIN ( \n " +
            "\t SELECT m.ORIGEN AS TIENDA, SUM(im.CANTIDAD) AS CANT_ENVIADA  \n " +
            "\t FROM seminario.movimiento m \n " +
            "\t LEFT JOIN seminario.itemmovimiento im on m.id = im.movimiento_id  \n " +
            "\t WHERE m.id_tipo_movimiento = 7 -- DEVOLUCION desde Tienda \n " +
            "\t AND ( FECHASALIDA > ?1 or ?1 is null) AND (FECHASALIDA < ?2 or ?2 is null) \n " +
            "\t GROUP BY TIENDA \n " +
            ") aux2 on aux.TIENDA = aux2.TIENDA \n " +
            "UNION \n " +
            "SELECT aux.TIENDA, IFNULL(CAST(aux.CANT_RECIBIDA as SIGNED),0) as CANT_RECIBIDA , IFNULL(CAST(aux2.CANT_ENVIADA as SIGNED),0) as CANT_ENVIADA FROM ( \n " +
            "\t SELECT m.DESTINO AS TIENDA, SUM(im.CANTIDAD) AS CANT_RECIBIDA  \n " +
            "\t FROM seminario.movimiento m \n " +
            "\t LEFT JOIN seminario.itemmovimiento im on m.id = im.movimiento_id  \n " +
            "\t WHERE m.id_tipo_movimiento = 1 -- ENVIO \n " +
            "\t AND ( FECHASALIDA > ?1 or ?1 is null) AND (FECHASALIDA < ?2 or ?2 is null) \n " +
            "\t GROUP BY TIENDA \n " +
            ") aux RIGHT JOIN ( \n " +
            "\t SELECT m.ORIGEN AS TIENDA, SUM(im.CANTIDAD) AS CANT_ENVIADA  \n " +
            "\t FROM seminario.movimiento m \n " +
            "\t LEFT JOIN seminario.itemmovimiento im on m.id = im.movimiento_id  \n " +
            "\t WHERE m.id_tipo_movimiento = 7 -- DEVOLUCION desde Tienda \n " +
            "\t AND ( FECHASALIDA > ?1 or ?1 is null) AND (FECHASALIDA < ?2 or ?2 is null) \n " +
            "\t GROUP BY TIENDA \n " +
            ") aux2 on aux.TIENDA = aux2.TIENDA", nativeQuery = true)
    List<Object[]> findAllCantidadEnviadaYRecibida(Date fechaDesde, Date fechaHasta);

    @Query(value = "SELECT aux.TIENDA, IFNULL(aux.COSTO_CANT_RECIBIDA,0) as COSTO_CANT_RECIBIDA , IFNULL(aux2.COSTO_CANT_ENVIADA,0) as COSTO_CANT_ENVIADA FROM (\n" +
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
            "SELECT aux.TIENDA, IFNULL(aux.COSTO_CANT_RECIBIDA,0) as COSTO_CANT_RECIBIDA, IFNULL(aux2.COSTO_CANT_ENVIADA,0) as COSTO_CANT_ENVIADA FROM (\n" +
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


    @Query(value = "SELECT aux.TIENDA, IFNULL(aux.COSTO_CANT_RECIBIDA,0) as COSTO_CANT_RECIBIDA , IFNULL(aux2.COSTO_CANT_ENVIADA,0) as COSTO_CANT_ENVIADA FROM (\n" +
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
            "SELECT aux.TIENDA, IFNULL(aux.COSTO_CANT_RECIBIDA,0) as COSTO_CANT_RECIBIDA, IFNULL(aux2.COSTO_CANT_ENVIADA,0) as COSTO_CANT_ENVIADA FROM (\n" +
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

    @Query(value =  "SELECT * FROM LOCAL WHERE local.nro = ?1 OR ?1 is null" ,nativeQuery = true)
    List<Local> findAllByNro(Long nro);
}
