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


    @Query(value = "SELECT p.nro, p.nombre, sum(DEUDA_MON) as DEUDA_MON, sum(DEUDA_BULTOS) as DEUDA_BULTOS FROM ( \n" +
            "SELECT \tCASE WHEN (aux.id is NULL) THEN aux2.id \n" +
            "\t\tELSE aux.id end as PROVEEDOR_ID, \n" +
            "\t\tCASE WHEN (aux.BI_id is NULL) THEN aux2.BI_id \n" +
            "\t\tELSE aux.BI_id end as BI_id,  \n" +
            "\t\tCASE WHEN (DEUDA_MON_SUMA is NULL) THEN CAST(-1*DEUDA_MON_RESTA as DOUBLE)\n" +
            "\t\t\t WHEN (DEUDA_MON_RESTA is NULL) THEN CAST(DEUDA_MON_SUMA as DOUBLE)\n" +
            "\t\tELSE CAST(DEUDA_MON_SUMA - DEUDA_MON_RESTA as DOUBLE) END as DEUDA_MON,\n" +
            "\t\tCASE WHEN (DEUDA_CANT_SUMA is NULL) THEN CAST(-1*DEUDA_CANT_RESTA as DOUBLE)\n" +
            "\t\t\t WHEN (DEUDA_CANT_RESTA is NULL) THEN CAST(DEUDA_CANT_SUMA as DOUBLE)\n" +
            "\t\tELSE CAST(DEUDA_CANT_SUMA - DEUDA_CANT_RESTA as DOUBLE) END as DEUDA_BULTOS\n" +
            "\t\tFROM (\n" +
            "\t\t\tSELECT d.id_agente_origen as id, d.BI_id, SUM(deuda_monetaria) as DEUDA_MON_RESTA, SUM(deuda_cant) as DEUDA_CANT_RESTA FROM seminario.deuda d\n" +
            "\t\t\tINNER JOIN seminario.bienintercambiable bi ON d.BI_id = bi.ID\n" +
            "\t\t\tWHERE tipo_agente_origen = 3\n" +
            "\t\t\tGROUP BY id, d.BI_id\n" +
            "\t\t) as aux LEFT JOIN (\n" +
            "\t\t\tSELECT d.id_agente_destino as id, d.BI_id, SUM(deuda_monetaria) as DEUDA_MON_SUMA, SUM(deuda_cant) as DEUDA_CANT_SUMA FROM seminario.deuda d\n" +
            "\t\t\tINNER JOIN seminario.bienintercambiable bi ON d.BI_id = bi.ID\n" +
            "\t\t\tWHERE tipo_agente_destino = 3\n" +
            "\t\t\tGROUP BY id, d.BI_id\n" +
            "\t\t) as aux2 ON aux.id = aux2.id and aux.BI_id = aux2.BI_id \n" +
            "UNION \n" +
            "SELECT \tCASE WHEN (aux.id is NULL) THEN aux2.id \n" +
            "\t\tELSE aux.id end as PROVEEDOR_ID, \n" +
            "\t\tCASE WHEN (aux.BI_id is NULL) THEN aux2.BI_id \n" +
            "\t\tELSE aux.BI_id end as BI_id,  \n" +
            "\t\tCASE WHEN (DEUDA_MON_SUMA is NULL) THEN CAST(-1*DEUDA_MON_RESTA as DOUBLE)\n" +
            "\t\t\t WHEN (DEUDA_MON_RESTA is NULL) THEN CAST(DEUDA_MON_SUMA as DOUBLE)\n" +
            "\t\tELSE CAST(DEUDA_MON_SUMA - DEUDA_MON_RESTA as DOUBLE) END as DEUDA_MON,\n" +
            "\t\tCASE WHEN (DEUDA_CANT_SUMA is NULL) THEN CAST(-1*DEUDA_CANT_RESTA as DOUBLE)\n" +
            "\t\t\t WHEN (DEUDA_CANT_RESTA is NULL) THEN CAST(DEUDA_CANT_SUMA as DOUBLE)\n" +
            "\t\tELSE CAST(DEUDA_CANT_SUMA - DEUDA_CANT_RESTA as DOUBLE) END as DEUDA_BULTOS\n" +
            "\t\tFROM (\n" +
            "\t\t\tSELECT d.id_agente_origen as id, d.BI_id, SUM(deuda_monetaria) as DEUDA_MON_RESTA, SUM(deuda_cant) as DEUDA_CANT_RESTA FROM seminario.deuda d\n" +
            "\t\t\tINNER JOIN seminario.bienintercambiable bi ON d.BI_id = bi.ID\n" +
            "\t\t\tWHERE tipo_agente_origen = 3\n" +
            "\t\t\tGROUP BY id, d.BI_id\n" +
            "\t\t) as aux RIGHT JOIN (\n" +
            "\t\t\tSELECT d.id_agente_destino as id, d.BI_id, SUM(deuda_monetaria) as DEUDA_MON_SUMA, SUM(deuda_cant) as DEUDA_CANT_SUMA FROM seminario.deuda d\n" +
            "\t\t\tINNER JOIN seminario.bienintercambiable bi ON d.BI_id = bi.ID\n" +
            "\t\t\tWHERE tipo_agente_destino = 3\n" +
            "\t\t\tGROUP BY id, d.BI_id\n" +
            "\t\t) as aux2 ON aux.id = aux2.id and aux.BI_id = aux2.BI_id \n" +
            ") as aux3 INNER JOIN seminario.PROVEEDOR p ON aux3.proveedor_id = p.nro GROUP BY p.NRO, p.NOMBRE\n" +
            "ORDER BY DEUDA_MON DESC", nativeQuery = true)
    List<Object[]> findAllDeudas();
}
