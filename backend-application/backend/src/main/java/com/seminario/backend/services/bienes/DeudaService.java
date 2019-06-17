package com.seminario.backend.services.bienes;

import com.seminario.backend.dto.Agente;
import com.seminario.backend.dto.DeudaBien;
import com.seminario.backend.model.abm.Usuario;
import com.seminario.backend.model.bienes.BienIntercambiable;
import com.seminario.backend.model.bienes.Proveedor;
import com.seminario.backend.model.bienes.TipoAgente;
import com.seminario.backend.repository.abm.PermisoRepository;
import com.seminario.backend.repository.bienes.BienIntercambiableRepository;
import com.seminario.backend.repository.bienes.ProveedorRepository;
import com.seminario.backend.repository.bienes.TipoAgenteRepository;
import com.seminario.backend.services.abm.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class DeudaService {

    @Autowired
    EntityManagerFactory emf;

    @Autowired
    TipoAgenteRepository tipoAgenteRepository;

    @Autowired
    ProveedorRepository proveedorRepository;

    @Autowired
    PermisoRepository permisoRepository;

    @Autowired
    BienIntercambiableRepository bienIntercambiableRepository;

    private static ZoneId zoneId = ZoneId.of("America/Argentina/Buenos_Aires");

    public void aumentarDeudaCDaProveedor(Long LocalOrigen, Long ProveedorDestino, Long biId, Long cantNueva, Date CotizacionFecha)   {
        TipoAgente tipoAgenteOrigen = tipoAgenteRepository.findByNombre("CD");
        TipoAgente tipoAgenteDestino = tipoAgenteRepository.findByNombre("PROVEEDOR");
        actualizarDeuda(LocalOrigen, ProveedorDestino, tipoAgenteOrigen, tipoAgenteDestino, biId, cantNueva, false, CotizacionFecha);
    }

    public void restarDeudaCDaProveedor(Long LocalOrigen, Long ProveedorDestino, Long biId, Long cantNueva, Date CotizacionFecha)   {
        TipoAgente tipoAgenteOrigen = tipoAgenteRepository.findByNombre("CD");
        TipoAgente tipoAgenteDestino = tipoAgenteRepository.findByNombre("PROVEEDOR");
        actualizarDeuda(LocalOrigen, ProveedorDestino, tipoAgenteOrigen, tipoAgenteDestino, biId, cantNueva, true, CotizacionFecha);
    }

    private void actualizarDeuda(Long AgenteOrigen, Long AgenteDestino,
                                 TipoAgente tipoAgenteOrigen, TipoAgente tipoAgenteDestino,
                                 Long biId, Long cantNueva, boolean resta, Date CotizacionFecha){

        Double montoNuevo = cantNueva * this.getCotizacionEnFecha(biId, CotizacionFecha);
        montoNuevo = (montoNuevo == null) ? 0: montoNuevo;
        Object[]  deudas = findDeudaCantByLocalAndBi( AgenteOrigen, AgenteDestino, tipoAgenteOrigen.getId(), tipoAgenteDestino.getId(), biId);

        Double deudaPlata = new Double(0);
        Long deudaCant = new Long(0);
        if (deudas == null) {
            insertDeuda(AgenteOrigen, AgenteDestino, tipoAgenteOrigen.getId(), tipoAgenteDestino.getId(), biId);
        } else {
            deudaPlata = (Double) deudas[1];
            deudaCant = (Long) deudas[0];
        }

        if (resta) {
            deudaCant -= cantNueva;
            deudaPlata -= montoNuevo;
        } else {
            deudaCant += cantNueva;
            deudaPlata += montoNuevo;
        }
        this.updateDeuda(AgenteOrigen, AgenteDestino, tipoAgenteOrigen.getId(), tipoAgenteDestino.getId(), biId, deudaCant, deudaPlata);
    }

    private void insertDeuda(Long AgenteOrigen, Long AgenteDestino, Long tipoAgenteOrigen, Long tipoAgenteDestino, Long biId) {
        EntityManager em = emf.createEntityManager();
        String qry = "INSERT INTO DEUDA (deuda_cant, deuda_monetaria, " +
                "id_agente_origen, id_agente_destino," +
                "tipo_agente_origen, tipo_agente_destino, BI_id, ultima_fecha_actualizacion) " +
                "VALUES(0,0.0,?1,?2,?3,?4,?5, ?6)";
        EntityTransaction et = em.getTransaction();
        et.begin();
        em.createNativeQuery(qry)
                .setParameter(1, AgenteOrigen)
                .setParameter(2, AgenteDestino)
                .setParameter(3, tipoAgenteOrigen)
                .setParameter(4, tipoAgenteDestino)
                .setParameter(5, biId)
                .setParameter(6, Date.from(ZonedDateTime.now(zoneId).toInstant()))
                .executeUpdate();
        et.commit();
        em.close();
    }

    private void updateDeuda(Long AgenteOrigen, Long AgenteDestino, Long tipoAgenteOrigen, Long tipoAgenteDestino, Long biId, Long deudaCant, Double deudaPlata) {
        EntityManager em = emf.createEntityManager();
        String qry = "UPDATE DEUDA SET deuda_cant = ?6, deuda_monetaria = ?7, ultima_fecha_actualizacion = ?8 " +
                "WHERE id_agente_origen = ?1 AND id_agente_destino = ?2 " +
                "AND tipo_agente_origen = ?3 AND tipo_agente_destino = ?4 " +
                "AND BI_id = ?5";
        EntityTransaction et = em.getTransaction();
        et.begin();
        em.createNativeQuery(qry)
                .setParameter(1, AgenteOrigen)
                .setParameter(2, AgenteDestino)
                .setParameter(3, tipoAgenteOrigen)
                .setParameter(4, tipoAgenteDestino)
                .setParameter(5, biId)
                .setParameter(6, deudaCant)
                .setParameter(7, deudaPlata)
                .setParameter(8, Date.from(ZonedDateTime.now(zoneId).toInstant()))
                .executeUpdate();
        et.commit();
        em.close();
    }

    private Object[] findDeudaCantByLocalAndBi(Long AgenteOrigen, Long AgenteDestino, Long tipoAgenteOrigen, Long tipoAgenteDestino, Long biId) {
        EntityManager em = emf.createEntityManager();
        String qry = "SELECT d.deuda_cant, d.deuda_monetaria from DEUDA d " +
                     "WHERE d.id_agente_origen = ?1 AND d.id_agente_destino = ?2 " +
                     "AND d.tipo_agente_origen = ?3 AND d.tipo_agente_destino = ?4 " +
                     "AND d.BI_id = ?5";
        List<Object[]> d = em.createNativeQuery(qry)
                .setParameter(1, AgenteOrigen)
                .setParameter(2, AgenteDestino)
                .setParameter(3, tipoAgenteOrigen)
                .setParameter(4, tipoAgenteDestino)
                .setParameter(5, biId)
                .getResultList();
        Object [] r = (!d.isEmpty()) ? d.get(0): null;
        em.close();
        return r;
    }


    public Double getUltimaCotizacion(Long BI_id){
        EntityManager em = emf.createEntityManager();
        String qry = "SELECT COTIZACION FROM COTIZACION " +
                "c WHERE c.BIENINTERCAMBIABLE_ID = ?1 " +
                "ORDER BY fechaalta desc LIMIT 1";
        Double d = (Double) em.createNativeQuery(qry)
                .setParameter(1, BI_id).getSingleResult();
        em.close();
        return d;
    }

    private Double getCotizacionEnFecha(Long BI_id, Date fecha){
        EntityManager em = emf.createEntityManager();
        String qry = "SELECT COTIZACION FROM COTIZACION " +
                "c WHERE c.BIENINTERCAMBIABLE_ID =?1 and c.fechaalta <= ?2 " +
                "ORDER BY fechaalta desc LIMIT 1";
        Double d = (Double) em.createNativeQuery(qry)
                .setParameter(1,BI_id)
                .setParameter(2, fecha).getSingleResult();
        em.close();
        return d;
    }

    public List<Agente> getAllDeuda(Usuario usuarioActual, Long proveedorNro, Long Bi_id, Double montoMin, Double montoMax, Long cantMin, Long cantMax) throws CustomException {
        if (null != permisoRepository.findPermisoWhereUsuarioAndPermiso(usuarioActual.getId(),"CONS-AGENTE")) {
            EntityManager em = emf.createEntityManager();
            String qry = "SELECT \n" +
                    " \tPROVEEDOR_ID,\n" +
                    " \tBI_id,\n" +
                    " \tDEUDA_MON,\n" +
                    " \tDEUDA_BULTOS\n" +
                    " FROM (    \n" +
                    "      SELECT \tCASE WHEN (aux.id is NULL) THEN aux2.id \n" +
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
                    "\t\t) as aux2 ON aux.id = aux2.id and aux.BI_id = aux2.BI_id\n" +
                    ") as aux4 WHERE (PROVEEDOR_ID = ?1 OR ?1 IS NULL) \n" +
                    "AND (BI_id = ?2 OR ?2 IS NULL)\n" +
                    "AND (DEUDA_MON >= ?3 OR ?3 IS NULL)\n" +
                    "AND (DEUDA_MON <= ?4 OR ?4 IS NULL)\n" +
                    "AND (DEUDA_BULTOS >= ?5 OR ?5 IS NULL)\n" +
                    "AND (DEUDA_BULTOS <= ?6 OR ?6 IS NULL) ";
            List<Object[]> objs = em.createNativeQuery(qry)
                    .setParameter(1, proveedorNro)
                    .setParameter(2, Bi_id)
                    .setParameter(3, montoMin)
                    .setParameter(4, montoMax)
                    .setParameter(5, cantMin)
                    .setParameter(6, cantMax).getResultList();
            List<Agente> ls = new ArrayList<Agente>();
            objs.forEach(obj -> {
                Agente agente = null;
                Proveedor prov = null;
                Boolean f = false;
                for (Agente a : ls) {
                    if (a.getNro() == obj[0]) {
                        agente = a;
                        f = true;
                    }
                }
                if (agente == null) {
                    prov = proveedorRepository.findByNro((Long) obj[0]);
                    agente = new Agente();
                    agente.setNro(prov.getNro());
                    agente.setNombre(prov.getNombre());
                    agente.setDenominacion(prov.getDenominacion());
                    agente.setDireccion(prov.getDireccion());
                    agente.setEmail(prov.getEmail());
                    agente.setDireccion_nro(prov.getDireccion_nro());
                    agente.setTipoAgente(tipoAgenteRepository.findByNombre("PROVEEDOR"));
                }
                DeudaBien db = new DeudaBien();
                BienIntercambiable bi = bienIntercambiableRepository.findById((Long) obj[1]);
                db.setIdBI(bi.getId());
                db.setDescripcionBI(bi.getDescripcion());
                db.setTipoBI(bi.getTipo());
                db.setSubtipoBI(bi.getSubtipo());
                db.setDeudaMonetaria((Double) obj[2]);
                db.setDeudaBultos((Double) obj[3]);
                agente.addDeudaBien(db);
                if (!f) ls.add(agente);
            });

            return ls;
        } else {
            throw new CustomException("No cuenta con los permisos para consultar agentes!");
        }
    }

}
