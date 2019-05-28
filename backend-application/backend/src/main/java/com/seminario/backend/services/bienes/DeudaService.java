package com.seminario.backend.services.bienes;

import com.seminario.backend.model.bienes.TipoAgente;
import com.seminario.backend.repository.bienes.TipoAgenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Service
public class DeudaService {

    @Autowired
    EntityManagerFactory emf;

    @Autowired
    TipoAgenteRepository tipoAgenteRepository;

    private static ZoneId zoneId = ZoneId.of("America/Argentina/Buenos_Aires");

    public void aumentarDeudaCDaProveedor(Long LocalOrigen, Long ProveedorDestino, Long biId, Long cantNueva)   {
        TipoAgente tipoAgenteOrigen = tipoAgenteRepository.findByNombre("CD");
        TipoAgente tipoAgenteDestino = tipoAgenteRepository.findByNombre("PROVEEDOR");
        actualizarDeuda(LocalOrigen, ProveedorDestino, tipoAgenteOrigen, tipoAgenteDestino, biId, cantNueva, false);
    }

    public void restarDeudaCDaProveedor(Long LocalOrigen, Long ProveedorDestino, Long biId, Long cantNueva)   {
        TipoAgente tipoAgenteOrigen = tipoAgenteRepository.findByNombre("CD");
        TipoAgente tipoAgenteDestino = tipoAgenteRepository.findByNombre("PROVEEDOR");
        actualizarDeuda(LocalOrigen, ProveedorDestino, tipoAgenteOrigen, tipoAgenteDestino, biId, cantNueva, true);
    }

    public void aumentarDeudaProveedorACD(Long CD, Long Proveedor, Long biId, Long cantNueva)   {
        TipoAgente tipoAgenteOrigen = tipoAgenteRepository.findByNombre("PROVEEDOR");
        TipoAgente tipoAgenteDestino = tipoAgenteRepository.findByNombre("CD");
        actualizarDeuda(Proveedor, CD, tipoAgenteOrigen, tipoAgenteDestino, biId, cantNueva, false);
    }

    public void restarDeudaProveedorACD(Long CD, Long Proveedor, Long biId, Long cantNueva)   {
        TipoAgente tipoAgenteOrigen = tipoAgenteRepository.findByNombre("PROVEEDOR");
        TipoAgente tipoAgenteDestino = tipoAgenteRepository.findByNombre("CD");
        actualizarDeuda(Proveedor, CD, tipoAgenteOrigen, tipoAgenteDestino, biId, cantNueva, true);
    }

    private void actualizarDeuda(Long AgenteOrigen, Long AgenteDestino,
                                 TipoAgente tipoAgenteOrigen, TipoAgente tipoAgenteDestino,
                                 Long biId, Long cantNueva, boolean resta){

        Double montoNuevo = cantNueva * this.getUltimaCotizacion(biId);
        montoNuevo = (montoNuevo == null) ? 0: montoNuevo;
        List<Object>  deudas = findDeudaCantByLocalAndBi( AgenteOrigen, AgenteDestino, tipoAgenteOrigen.getId(), tipoAgenteDestino.getId(), biId);

        Double deudaPlata = new Double(0);
        Long deudaCant = new Long(0);
        if (deudas == null) {
            insertDeuda(AgenteOrigen, AgenteDestino, tipoAgenteOrigen.getId(), tipoAgenteDestino.getId(), biId);
        } else {
            deudaPlata = (Double) deudas.get(1);
            deudaCant = (Long) deudas.get(0);
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
                "tipo_agente_origen, tipo_agente_destino, BI_id, ultima_fecha_actualizacion)" +
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
        et.begin();
        em.close();
    }

    private List<Object> findDeudaCantByLocalAndBi(Long AgenteOrigen, Long AgenteDestino, Long tipoAgenteOrigen, Long tipoAgenteDestino, Long biId) {
        EntityManager em = emf.createEntityManager();
        String qry = "SELECT d.deuda_cant, d.deuda_monetaria from DEUDA d" +
                     "WHERE d.id_agente_origen = ?1 AND d.id_agente_destino = ?2 " +
                     "AND d.tipo_agente_origen = ?3 AND d.tipo_agente_destino = ?4 " +
                     "AND d.BI_id = ?5";
        List<Object> d = em.createNativeQuery(qry)
                .setParameter(1, AgenteOrigen)
                .setParameter(2, AgenteDestino)
                .setParameter(3, tipoAgenteOrigen)
                .setParameter(4, tipoAgenteDestino)
                .setParameter(5, biId)
                .getResultList();
        em.close();
        return d;
    }


    private Double getUltimaCotizacion(Long BI_id){
        EntityManager em = emf.createEntityManager();
        String qry = "SELECT first c.precio FROM COTIZACION c " +
                     "WHERE c.BI_ID = ?1 ORDER BY fecha_alta desc";
        Double d = (Double) em.createNativeQuery(qry)
                .setParameter(1, BI_id).getSingleResult();

        em.close();
        return d;
    }


}
