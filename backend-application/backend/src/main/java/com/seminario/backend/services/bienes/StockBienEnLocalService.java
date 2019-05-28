package com.seminario.backend.services.bienes;


import com.seminario.backend.dto.StockBienEnLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

/**
 * Usuario: Franco
 * Project: backend-application
 * Fecha: 5/15/2019
 **/
@Service
public class StockBienEnLocalService {

    @Autowired
    EntityManagerFactory emf;

    private static ZoneId zoneId = ZoneId.of("America/Argentina/Buenos_Aires");

    private void actualizarStock(Long localNro, Long biId, Long cantNueva, String tipoStock, boolean resta){

        Long cantActual = findStockByLocalAndBi(tipoStock, localNro, biId);
        if (cantActual == null) {
            insertStock(localNro, biId);
            cantActual = new Long(0);
        }
        if (resta) {
            cantActual -= cantNueva;
            cantActual = (cantActual < 0) ? 0 : cantActual;
        } else {
            cantActual += cantNueva;
        }
        updateStock(tipoStock, cantActual, localNro, biId);
    }

    public void aumentarStockDestruido(Long localNro, Long biId,  Long cant){
        this.actualizarStock(localNro, biId, cant, "STOCK_DESTRUIDO", false);
    }

    public void aumentarStockLibre(Long localNro, Long biId,  Long cant){
        this.actualizarStock(localNro, biId, cant, "STOCK_LIBRE", false );
    }

    public void aumentarStockOcupado(Long localNro, Long biId,  Long cant){
        this.actualizarStock(localNro, biId, cant, "STOCK_OCUPADO", false);
    }

    public void aumentarStockReservado(Long localNro, Long biId,  Long cant){
        this.actualizarStock(localNro, biId, cant, "STOCK_RESERVADO", false);
    }

    public void restarStockDestruido(Long localNro, Long biId,  Long cant){
        this.actualizarStock(localNro, biId, cant, "STOCK_DESTRUIDO", true);
    }

    public void restarStockLibre(Long localNro, Long biId,  Long cant){
        this.actualizarStock(localNro, biId, cant, "STOCK_LIBRE", true );
    }

    public void restarStockOcupado(Long localNro, Long biId,  Long cant){
        this.actualizarStock(localNro, biId, cant, "STOCK_OCUPADO", true);
    }

    public void restarStockReservado(Long localNro, Long biId,  Long cant){
        this.actualizarStock(localNro, biId, cant, "STOCK_RESERVADO", true);
    }

    public Long findStockByLocalAndBi(String tipoStock, Long locaNro, Long biId){
        EntityManager em = emf.createEntityManager();
        Long stock = null;
        try {
            stock = (Long) em.createNativeQuery("select " + tipoStock + " from STOCK_BIEN_EN_LOCAL " +
                    " where LOCAL_NRO = ?1 and BI_ID = ?2")
                    .setParameter(1, locaNro)
                    .setParameter(2, biId)
                    .getSingleResult();
        } catch (NoResultException e) {
            System.err.println("No se obtuvo resultado.");
        }
        em.close();
        return stock;

    }

    public void insertStock(Long localNro, Long bienIntercambiableId){

        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        et.begin();
        em.createNativeQuery(" INSERT INTO STOCK_BIEN_EN_LOCAL " +
                "(LOCAL_NRO, BI_ID, STOCK_LIBRE, STOCK_OCUPADO, STOCK_RESERVADO, STOCK_DESTRUIDO, ULTIMA_FECHA_ACTUALIZACION)" +
                "VALUES(?1, ?2, 0, 0, 0, 0, ?3)")
                .setParameter(1,localNro)
                .setParameter(2,bienIntercambiableId)
                .setParameter(3, Date.from(ZonedDateTime.now(zoneId).toInstant()))
                .executeUpdate();
        et.commit();
        em.close();
    }

    public void updateStock(String tipoStock, Long cant, Long localNro, Long bienIntercambiableId){

        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        et.begin();
        em.createNativeQuery(" update STOCK_BIEN_EN_LOCAL SET "+tipoStock+" = ?1, " +
                " ULTIMA_FECHA_ACTUALIZACION = ?2 where LOCAL_NRO = ?3 and BI_ID = ?4")
                .setParameter(1,cant)
                .setParameter(2,Date.from(ZonedDateTime.now(zoneId).toInstant()))
                .setParameter(3,localNro)
                .setParameter(4,bienIntercambiableId)
                .executeUpdate();
        et.commit();
        em.close();
    }


    public List<Object> getStockLocal(Long localNro){

        EntityManager em = emf.createEntityManager();
        List<Object> stockLocal;
        String qry ="select SBL.LOCAL_NRO, L.DENOMINACION, SBL.BI_ID, BI.DESCRIPCION, " +
                "SBL.STOCK_LIBRE, SBL.STOCK_OCUPADO, SBL.STOCK_RESERVADO, SBL.STOCK_DESTRUIDO " +
                " from STOCK_BIEN_EN_LOCAL SBL" +
                "inner join LOCAL L on L.NRO=SBL.LOCAL_NRO and SBL.LOCAL_NRO=?1" +
                "inner join BIENINTERCAMBIABLE BI on BI.ID=SBL.BI_ID";
        try {
             stockLocal = em.createNativeQuery(qry)
                     .setParameter(1, localNro)
                     .getResultList();
            em.close();
            return stockLocal;
        } catch (NoResultException e) {
            throw new RuntimeException(e);
        }

    }

}
