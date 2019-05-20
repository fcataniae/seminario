package com.seminario.backend.services.bienes;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * Usuario: Franco
 * Project: backend-application
 * Fecha: 5/15/2019
 **/
@Service
public class StockBienEnLocalService {

    @Autowired
    EntityManagerFactory emf;

    private void actualizarStock(Long localNro, Long biId, Long cantNueva, String tipoStock, boolean resta){

        Long cantActual = findStockByLocalAndBi(tipoStock,  localNro, biId);
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

        Long stock = (Long) em.createNativeQuery("select "+tipoStock+" from STOCK_BIEN_EN_LOCAL " +
                " where LOCAL_NRO = ?1 and BI_ID = ?2")
                .setParameter(1,locaNro)
                .setParameter(2,biId)
                .getSingleResult();
        em.close();
        return stock;

    }

    public void updateStock(String tipoStock, Long cant, Long localNro, Long bienIntercambiableId){

        EntityManager em = emf.createEntityManager();

        em.createNativeQuery(" update STOCK_BIEN_EN_LOCAL SET "+tipoStock+" = ?1 " +
                " where LOCAL_NRO = ?2 and BI_ID = ?3")
                .setParameter(1,cant)
                .setParameter(2,localNro)
                .setParameter(3,bienIntercambiableId)
                .executeUpdate();

        em.close();
    }

}
