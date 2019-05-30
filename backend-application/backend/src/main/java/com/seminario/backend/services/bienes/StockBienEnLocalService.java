package com.seminario.backend.services.bienes;


import ch.qos.logback.core.CoreConstants;
import com.seminario.backend.dto.StockBienEnLocal;
import com.seminario.backend.model.abm.Usuario;
import com.seminario.backend.repository.abm.PermisoRepository;
import com.seminario.backend.services.abm.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * Usuario: Franco
 * Project: backend-application
 * Fecha: 5/15/2019
 **/
@Service
public class StockBienEnLocalService {

    @Autowired
    EntityManagerFactory emf;
    @Autowired
    PermisoRepository permisoRepository;

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


       public List<StockBienEnLocal> getStockLocal(Long localNro, Usuario usuarioActual)throws CustomException{

           if (null != permisoRepository.findPermisoWhereUsuarioAndPermiso(usuarioActual.getId(),"CONS-STOCK-TIENDA")) {
               //&& (usuarioActual.getLocal()== localNro)){

               EntityManager em = emf.createEntityManager();
               List<StockBienEnLocal> listStockLocal = new ArrayList<StockBienEnLocal>(); // resultado final

               String qry = "select SBL.LOCAL_NRO, L.NOMBRE, SBL.BI_ID, BI.DESCRIPCION," +
                       " SBL.STOCK_LIBRE, SBL.STOCK_OCUPADO, SBL.STOCK_RESERVADO, SBL.STOCK_DESTRUIDO" +
                       " from STOCK_BIEN_EN_LOCAL SBL" +
                       " inner join LOCAL L on L.NRO=SBL.LOCAL_NRO and SBL.LOCAL_NRO=?1" +
                       " inner join BIENINTERCAMBIABLE BI on BI.ID=SBL.BI_ID";

               try {
                   List<Object[]> stockLocal = em.createNativeQuery(qry)
                           .setParameter(1, localNro)
                           .getResultList();
                   em.close();
                  // System.out.println("Respuesta objeto bien 1:" + stockLocal.get(0).toString();
                   for (Object[] tupla : stockLocal) {
                       StockBienEnLocal bien = new StockBienEnLocal();

                       bien.setNombreLocal((String) tupla[1]);
                      // bien.setIdBI(new Long((Long) tupla[2]));
                       bien.setDescripcionBI((String) tupla[3]);
                       //bien.setStock_libre(new Long((Long) tupla[4]));
                       //bien.setStock_ocupado(new Long((Long) tupla[5]));
                       //bien.setStock_reservado(new Long((Long) tupla[6]));
                       //bien.setStock_destruido(new Long((Long) tupla[7]));
                       bien.setNroLocal(new Long((Long) tupla[0]));
                       System.out.println("Respuesta final el local es el nro :" + tupla[0]);
                       listStockLocal.add(bien);
                   }
               } catch (NoResultException e) {
                   throw new RuntimeException(e);
               }
               System.out.println("Respuesta final bien 1:" + listStockLocal.get(0).getDescripcionBI());
               return listStockLocal;
           }else{
               throw new CustomException("No cuenta con los permisos para consultar el stock de este Local.");
           }
    }

}
