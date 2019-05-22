package com.seminario.backend.services.bienes;

import com.seminario.backend.model.bienes.TipoAgente;
import com.seminario.backend.repository.bienes.TipoAgenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

@Service
public class DeudaService {

    @Autowired
    EntityManagerFactory emf;

    @Autowired
    TipoAgenteRepository tipoLocalRepository;

    public void aumentarDeudaCDaProveedor(Long LocalOrigen, Long ProveedorDestino, Long biId, Long cantNueva)   {
        TipoAgente tipoLocalOrigen = tipoLocalRepository.findByNombre("CD");
        TipoAgente tipoLocalDestino = tipoLocalRepository.findByNombre("PROVEEDOR");
        actualizarDeuda(LocalOrigen, ProveedorDestino, tipoLocalOrigen, tipoLocalDestino, biId, cantNueva, false);
    }

    public void restarDeudaCDaProveedor(Long LocalOrigen, Long ProveedorDestino, Long biId, Long cantNueva)   {
        TipoAgente tipoLocalOrigen = tipoLocalRepository.findByNombre("CD");
        TipoAgente tipoLocalDestino = tipoLocalRepository.findByNombre("PROVEEDOR");
        actualizarDeuda(LocalOrigen, ProveedorDestino, tipoLocalOrigen, tipoLocalDestino, biId, cantNueva, true);
    }

    public void aumentarDeudaProveedorACD(Long LocalOrigen, Long ProveedorDestino, Long biId, Long cantNueva)   {
        TipoAgente tipoLocalOrigen = tipoLocalRepository.findByNombre("PROVEEDOR");
        TipoAgente tipoLocalDestino = tipoLocalRepository.findByNombre("CD");
        actualizarDeuda(LocalOrigen, ProveedorDestino, tipoLocalOrigen, tipoLocalDestino, biId, cantNueva, false);
    }

    public void restarDeudaProveedorACD(Long LocalOrigen, Long ProveedorDestino, Long biId, Long cantNueva)   {
        TipoAgente tipoLocalOrigen = tipoLocalRepository.findByNombre("PROVEEDOR");
        TipoAgente tipoLocalDestino = tipoLocalRepository.findByNombre("CD");
        actualizarDeuda(LocalOrigen, ProveedorDestino, tipoLocalOrigen, tipoLocalDestino, biId, cantNueva, true);
    }

    private void actualizarDeuda(Long AgenteOrigen, Long AgenteDestino,
                                 TipoAgente tipoAgenteOrigen, TipoAgente tipoAgenteDestino,
                                 Long biId, Long cantNueva, boolean resta){

        Double montoNuevo = cantNueva * this.getUltimaCotizacion(biId);
        List<Object>  deudas = findDeudaCantByLocalAndBi( AgenteOrigen, AgenteDestino, tipoAgenteOrigen.getId(), tipoAgenteDestino.getId(), biId);
        Long deudaCant = (Long) deudas.get(0);
        Double deudaPlata = (Double) deudas.get(1);
        if (resta) {
            deudaCant -= cantNueva;
            deudaPlata -= montoNuevo;
        } else {
            deudaCant += cantNueva;
            deudaPlata += montoNuevo;
        }
        this.updateDeuda(AgenteOrigen, AgenteDestino, tipoAgenteOrigen.getId(), tipoAgenteDestino.getId(), biId, deudaCant, deudaPlata);
    }

    private void updateDeuda(Long AgenteOrigen, Long AgenteDestino, Long tipoAgenteOrigen, Long tipoAgenteDestino, Long biId, Long deudaCant, Double deudaPlata) {
        EntityManager em = emf.createEntityManager();
        String qry = "UPDATE DEUDA SET d.deuda_cant = ?6, d.deuda_monetaria = ?7" +
                "WHERE d.id_agente_origen = 1? AND d.id_agente_destino = ?2 " +
                "AND d.tipo_agente_origen = 3? AND d.tipo_agente_destino = ?4 " +
                "AND d.BI_id = ?5";
        List<Object> d = em.createNativeQuery(qry)
                .setParameter(1, AgenteOrigen)
                .setParameter(1, AgenteOrigen)
                .setParameter(2, AgenteDestino)
                .setParameter(3, tipoAgenteOrigen)
                .setParameter(4, tipoAgenteDestino)
                .setParameter(5, biId)
                .setParameter(6, deudaCant)
                .setParameter(7, deudaPlata)
                .getResultList();
        em.close();
    }

    private List<Object> findDeudaCantByLocalAndBi(Long AgenteOrigen, Long AgenteDestino, Long tipoAgenteOrigen, Long tipoAgenteDestino, Long biId) {
        EntityManager em = emf.createEntityManager();
        String qry = "SELECT d.deuda_cant, d.deuda_monetaria from DEUDA d" +
                     "WHERE d.id_agente_origen = 1? AND d.id_agente_destino = ?2 " +
                     "AND d.tipo_agente_origen = 3? AND d.tipo_agente_destino = ?4 " +
                     "AND d.BI_id = ?5";
        List<Object> d = em.createNativeQuery(qry)
                .setParameter(1, AgenteOrigen)
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
