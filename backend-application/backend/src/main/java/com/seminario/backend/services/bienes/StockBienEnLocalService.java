package com.seminario.backend.services.bienes;


import ch.qos.logback.core.CoreConstants;
import com.seminario.backend.dto.Dashboard;
import com.seminario.backend.dto.DeudaResumen;
import com.seminario.backend.dto.StockBienEnLocal;
import com.seminario.backend.dto.Agente;
import com.seminario.backend.model.abm.Usuario;
import com.seminario.backend.model.bienes.Movimiento;
import com.seminario.backend.model.bienes.TipoAgente;
import com.seminario.backend.model.bienes.TipoMovimiento;
import com.seminario.backend.repository.abm.PermisoRepository;
import com.seminario.backend.repository.bienes.TipoMovimientoRepository;
import com.seminario.backend.services.abm.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.management.Agent;

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
    @Autowired
    TipoMovimientoRepository tipoMovimientoRepository;
    @Autowired
    MovimientoService movimientoService;

    private static ZoneId zoneId = ZoneId.of("America/Argentina/Buenos_Aires");

    public void actualizarStock(Long localNro, Long biId, Long cantNueva, String tipoStock, boolean resta){

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
            boolean permiso = false;
           if (null != permisoRepository.findPermisoWhereUsuarioAndPermiso(usuarioActual.getId(),"CONS-STOCK-TOTAL")) {
               permiso = true;
               System.out.println("El usuario trabaja en el CD (permiso a stock total)");
           }else{
               if (usuarioActual.getLocal().getNro().equals(localNro)){
                    permiso = true;
                    System.out.println("El usuario trabaja en el local que quiere consultar");
               }
           }

           if (permiso) {
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
                   if (stockLocal.size() > 0) {
                       for (Object[] tupla : stockLocal) {
                           StockBienEnLocal bien = new StockBienEnLocal();

                           bien.setIdBI((Long) tupla[2]);
                           bien.setDescripcionBI((String) tupla[3]);
                           bien.setStock_libre((Long) tupla[4]);
                           bien.setStock_ocupado((Long) tupla[5]);
                           bien.setStock_reservado((Long) tupla[6]);
                           bien.setStock_destruido((Long) tupla[7]);
                           System.out.println("Respuesta final el local es el nro :" + tupla[0]);
                           listStockLocal.add(bien);
                       }
                   } else {
                       throw new CustomException("El local " + localNro + " no posee stock de ningun bien");
                   }
               } catch (NoResultException e) {
                   throw new RuntimeException(e);
               }
               System.out.println("Respuesta final bien 1:" + listStockLocal.get(0).getDescripcionBI());
               return listStockLocal;
           }else{
               throw new CustomException("No cuenta con los permisos para consultar el stock de este local.");
           }
    }


    public List<Agente> getDistribucionBienes(Usuario usuarioActual)throws CustomException{

        if (null != permisoRepository.findPermisoWhereUsuarioAndPermiso(usuarioActual.getId(),"CONS-STOCK-TOTAL")) {

            EntityManager em = emf.createEntityManager();
            List<Agente> listStockLocal = new ArrayList<Agente>(); // resultado final
            Long nro = usuarioActual.getLocal().getNro() == 7460? null : usuarioActual.getLocal().getNro();
            String qry = "select SBL.LOCAL_NRO,TA.NOMBRE, L.NOMBRE, SBL.BI_ID, BI.DESCRIPCION," +
                    " SBL.STOCK_LIBRE, SBL.STOCK_OCUPADO, SBL.STOCK_RESERVADO, SBL.STOCK_DESTRUIDO" +
                    " from STOCK_BIEN_EN_LOCAL SBL" +
                    " inner join LOCAL L on L.NRO=SBL.LOCAL_NRO" +
                    " inner join BIENINTERCAMBIABLE BI on BI.ID=SBL.BI_ID" +
                    " inner join TIPOAGENTE TA on TA.ID=L.ID_TIPO_AGENTE "+
                    " WHERE l.nro = ?1 or ?1 is null ";

            try {

                List<Object[]> stockLocal = em.createNativeQuery(qry)
                        .setParameter(1,nro)
                        .getResultList();
                em.close();

                if (stockLocal.size()>0) {
                    Long agenteAnt = (Long) stockLocal.get(0)[0];// codigo negativo invalido para cualquier agente
                    Agente agente = new Agente();
                    agente.setNro((Long) stockLocal.get(0)[0]);
                    TipoAgente ta = new TipoAgente();
                    ta.setNombre((String) stockLocal.get(0)[1]);
                    agente.setTipoAgente(ta);
                    agente.setNombre((String) stockLocal.get(0)[2]);

                    for (Object[] tupla : stockLocal) {

                        StockBienEnLocal bien = new StockBienEnLocal();
                        bien.setIdBI((Long) tupla[3]);
                        bien.setDescripcionBI((String) tupla[4]);
                        bien.setStock_libre((Long) tupla[5]);
                        bien.setStock_ocupado((Long) tupla[6]);
                        bien.setStock_reservado((Long) tupla[7]);
                        bien.setStock_destruido((Long) tupla[8]);

                        if (!agenteAnt.equals((Long) tupla[0])) {
                            listStockLocal.add(agente);
                            System.out.println("[ /bienes/distribucion-bienes/ ]Se agrego Local " + agenteAnt + " con sus bienes");
                            System.out.println(" ");

                            agente = new Agente();
                            agente.setNro((long) tupla[0]);
                            ta = new TipoAgente();
                            ta.setNombre((String) stockLocal.get(0)[1]);
                            agente.setTipoAgente(ta);
                            agente.setNombre((String) tupla[2]);
                            
                            System.out.println("[ /bienes/distribucion-bienes/ ] Local " + agente.getNro() + ": ");
                            System.out.println("       -" + tupla[3]);
                            agente.addStockBien(bien);
                            agenteAnt = (long) tupla[0];
                        } else {
                            System.out.println("       -" + tupla[4]);
                            agente.addStockBien(bien);
                        }
                    }
                    listStockLocal.add(agente);//agrego el ultimo
                    System.out.println(" [ /bienes/distribucion-bienes/ ] Se agrego local " + agente.getNro() + " con sus bienes");
                }else{
                    throw new CustomException("No posee stock de ningun local");
                }
            } catch (NoResultException e) {
                throw new RuntimeException(e);
            }
            //System.out.println("Respuesta final:" + listStockLocal);
            return listStockLocal;
        }else{
            throw new CustomException("No cuenta con los permisos para consultar la distribucion de los bienes.");
        }
    }

    private static final String TYPES[] ={"pie","doughnut"};
    private static final String[] COLORES =
            { "rgba(54, 162, 235, 1)",
              "rgba(75, 192, 192, 1)",
              "rgba(255, 206, 86, 1)",
              "rgba(255, 99, 132, 1)",
              "rgba(54, 162, 235, 1)",
              "rgba(16, 102, 130)",
              "rgba(201, 38, 149)"};
    private static final int INDEX = 7;
    private static final int INDEXC = 2;
    private static final int SUBLIST = 10;
    /**
     * Funcion que devuelve una lista de dashboards de acuerdo al usuario
     * @param usuarioActual
     * @return List Dashboards
     * @throws CustomException Excepcion
     */
    public List<Dashboard> getDashboards(Usuario usuarioActual) throws CustomException {

        List<Dashboard> dashs = new ArrayList<>();
        Long nro = usuarioActual.getLocal().getNro() == 7460 ? null: usuarioActual.getLocal().getNro();
        Dashboard d = new Dashboard();
        Random r = new Random();
        int colorIndx;
        if(nro == null){
            /*
            *  TODO dash para CD
            * */
            List<Agente> distr = getDistribucionBienes(usuarioActual);
            colorIndx = r.nextInt(INDEX);
            for(Agente e : distr){
                d.getData().getLabels().add(e.getNombre());
                Long stock = 0L;
                for(StockBienEnLocal s : e.getStockBienes()){
                    stock += s.getStock_libre() + s.getStock_ocupado() + s.getStock_reservado();


                }
                d.getData().getDataset().getData().add(String.valueOf(stock));
                d.getData().getDataset().getBackgroundColor().add(COLORES[colorIndx]);
                colorIndx = ((++colorIndx) == COLORES.length) ? 0 : colorIndx;
            }
            d.setType(TYPES[r.nextInt(INDEXC)]);
            d.getData().getDataset().setLabel("Distribución de bienes general");

            dashs.add(d);


            List<DeudaResumen> deuda;
            List<DeudaResumen> DeudaProv = movimientoService.getDeudaProveedores(usuarioActual);
            DeudaProv.sort((t1,t2)-> t2.getDeuda().compareTo(t1.getDeuda()));
            deuda = new ArrayList<>( DeudaProv.subList(0,  DeudaProv.size()> SUBLIST ? SUBLIST : DeudaProv.size()));
            d = new Dashboard();
            d.setType("bar");
            d.getData().getDataset().setLabel("Top "+SUBLIST+" Deuda de proveedores");

            for (DeudaResumen dr : deuda) {
                d.getData().getDataset().getData().add(dr.getDeuda().toString());
                d.getData().getLabels().add(dr.getProveedorNro() + " - " + dr.getProveedorNombre().substring(0, dr.getProveedorNombre().length()> 20 ? 20 : dr.getProveedorNombre().length()));
                d.getData().getDataset().getBackgroundColor().add(COLORES[colorIndx]);
                colorIndx = ((++colorIndx) == COLORES.length) ? 0 : colorIndx;
            }

            dashs.add(d);
        }
        /**
         * Dashboard de distribucion de bienes en tienda
         */

        List<StockBienEnLocal> stockLocal = getStockLocal(usuarioActual.getLocal().getNro(),usuarioActual);

        d = new Dashboard();

        d.getData().getDataset().setLabel("Distribución de bienes en " + usuarioActual.getLocal().getDenominacion());
        colorIndx = r.nextInt(INDEX);
        for(StockBienEnLocal s : stockLocal){
            d.getData().getDataset().getData().add(String.valueOf(s.getStock_libre() + s.getStock_ocupado() + s.getStock_reservado()));
            d.getData().getLabels().add(s.getDescripcionBI());
            d.getData().getDataset().getBackgroundColor().add(COLORES[colorIndx]);
            colorIndx = ((++colorIndx) == COLORES.length) ? 0 : colorIndx;
        }
        d.setType(TYPES[r.nextInt(INDEXC)]);
        dashs.add(d);
        /**
         * Dashboards de movimientos en los ultimos 14 dias
         */
        d = new Dashboard();

        List<Movimiento> ultimosMovimientos = movimientoService.getUltimosMovimientosLocal(usuarioActual);
        List<TipoMovimiento> tiposMovimientos =  tipoMovimientoRepository.findAll();

        d.getData().getDataset().setLabel("Movimientos de los últimos 14 días en " + usuarioActual.getLocal().getDenominacion());
        colorIndx = r.nextInt(INDEX);
        for (TipoMovimiento tm : tiposMovimientos) {
            TipoMovimiento currentTM = tm;
            int cont = 0;
            for (Movimiento mov : ultimosMovimientos) {
                if (mov.getTipoMovimiento().getId().equals(currentTM.getId())) {
                    cont++;
                }
            }
            if (cont != 0) {
                d.getData().getDataset().getData().add(String.valueOf(cont));
                d.getData().getLabels().add(tm.getNombre());
                d.getData().getDataset().getBackgroundColor().add(COLORES[colorIndx]);
                colorIndx = ((++colorIndx) == COLORES.length) ? 0 : colorIndx;
            }
        }
        d.setType(TYPES[r.nextInt(INDEXC)]);
        dashs.add(d);

        return  dashs;
    }


}
