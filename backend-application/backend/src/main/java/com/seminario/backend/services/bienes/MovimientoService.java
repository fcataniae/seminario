package com.seminario.backend.services.bienes;

import com.seminario.backend.dto.Agente;
import com.seminario.backend.model.abm.Usuario;
import com.seminario.backend.model.bienes.*;
import com.seminario.backend.repository.abm.PermisoRepository;
import com.seminario.backend.repository.abm.EstadoRepository;
import com.seminario.backend.repository.bienes.*;
import com.seminario.backend.services.abm.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class MovimientoService {

    @Autowired
    TipoMovimientoRepository tipoMovimientoRepository;
    @Autowired
    MovimientoRepository movimientoRepository;
    @Autowired
    PermisoRepository permisoRepository;
    @Autowired
    EstadoRepository estadoRepository;
    @Autowired
    EstadoViajeRepository estadoViajeRepository;
    @Autowired
    BienIntercambiableRepository bienIntercambiableRepository;
    @Autowired
    LocalRepository localRepository;
    @Autowired
    ProveedorRepository proveedorRepository;
    @Autowired
    StockBienEnLocalService stockBienEnLocalService;
    @Autowired
    EstadoRecursoRepository estadoRecursoRepository;
    @Autowired
    IntercambioProveedorRepository intercambioProveedorRepository;
    @Autowired
    DeudaService deudaService;

    @Autowired
    RecursoRepository recursoRepository;

    private static ZoneId zoneId = ZoneId.of("America/Argentina/Buenos_Aires");

    public void create(Usuario usuarioActual, Movimiento movimientoNuevo) throws CustomException {
        TipoMovimiento ti;
        if (null != permisoRepository.findPermisoWhereUsuarioAndPermiso(usuarioActual.getId(),"ALTA-MOVIMIENTO")) {

            if (movimientoNuevo.getTipoMovimiento() == null)
                throw new CustomException("Error el movimiento NO tiene un Tipo Movimiento!");

            if (movimientoNuevo.getOrigen() == null)
                throw new CustomException("Error el movimiento NO contiene agente origen!");

            if (movimientoNuevo.getDestino() == null)
                throw new CustomException("Error el movimiento NO contiene agente destino!");

            if (null != (ti = tipoMovimientoRepository.findByNombreAndTipoOrigenAndTipoDestino(
                    movimientoNuevo.getTipoMovimiento().getTipo(),movimientoNuevo.getTipoMovimiento().getTipoAgenteOrigen().getNombre(),
                    movimientoNuevo.getTipoMovimiento().getTipoAgenteDestino().getNombre()))) {

                movimientoNuevo.setTipoMovimiento(ti);
                movimientoNuevo.setId(null);
                movimientoNuevo.setUsuarioAlta(usuarioActual);
                validarMovimiento(movimientoNuevo);
                sanitizarMovimiento(movimientoNuevo);
                movimientoNuevo.setRecursosAsignados(cambiarEstadoRecursosAsignados(movimientoNuevo.getRecursosAsignados(), "OCUPADO","LIBRE"));
                validarItems(movimientoNuevo.getItemMovimientos());
                if (movimientoRepository.save(movimientoNuevo) == null)
                    throw new CustomException("Error al dar de alta la persona!");

                actualizarStockYDeuda(movimientoNuevo);
            } else {
                throw new CustomException("No se encontró el tipo de movimiento asignado. " +
                        "No se puede realizar un "+  movimientoNuevo.getTipoMovimiento().getTipo()+
                        " de "+ movimientoNuevo.getTipoMovimiento().getTipoAgenteOrigen().getNombre() +" a "+ movimientoNuevo.getTipoMovimiento().getTipoAgenteDestino().getNombre());
            }
        } else {
            throw new CustomException("No cuenta con permisos para dar de alta movimientos nuevos!");
        }
    }

    public Set<Recurso> cambiarEstadoRecursosAsignados(Set<Recurso> recursos, String estadoRecurso, String estadoExcepcion) throws CustomException {
        EstadoRecurso e = estadoRecursoRepository.findByDescrip(estadoRecurso);
        int i = 1;
        Set<Recurso> set = new HashSet<>();
        if (!recursos.isEmpty()) {

            Iterator<Recurso> itr = recursos.iterator();

            while(itr.hasNext()){
                Recurso r = itr.next();

                Recurso rDB  = recursoRepository.findByNroRecurso(r.getNroRecurso());

                if(rDB == null)
                    throw new CustomException("No existe el recurso en la base");

                if(!rDB.getEstadoRecurso().getDescrip().equalsIgnoreCase(estadoExcepcion))
                    throw new CustomException("El recurso " + rDB.getNroRecurso() + " se encuentra en estado " + rDB.getEstadoRecurso().getDescrip());

                itr.remove();
                rDB.setEstadoRecurso(e);
                set.add(rDB);
            }

          /*  for (Recurso r : recursos) {
                Recurso rBd = recursoRepository.findByNroRecurso(r.getNroRecurso());
                if (rBd == null || !rBd.getEstadoRecurso().getDescrip().equalsIgnoreCase("LIBRE")) {
                    throw new CustomException("El recurso [" + i + "] se encruentra en estado " + r.getEstado().getDescrip() + "!");
                } else {
                    rBd.setEstadoRecurso(e);
                    r = rBd;
                }
                i++;
            }

           */
        }

        return set;
    }

    public void confirmar(Usuario usuarioActual, Long idMov, String comentario) throws CustomException{
        Movimiento m;
        if (null != permisoRepository.findPermisoWhereUsuarioAndPermiso(usuarioActual.getId(),"MODI-MOVIMIENTO")) {
            if (null != (m = movimientoRepository.findById(idMov))){
                if (m.getEstadoViaje().getDescrip().equals("PENDIENTE")) {
                    m.setEstadoViaje(estadoViajeRepository.findByDescrip("ENTREGADO"));
                    cambiarEstadoRecursosAsignados(m.getRecursosAsignados(), "LIBRE","OCUPADO");
                    confimarMovimiento(m);
                    movimientoRepository.save(m);
                } else {
                    throw new CustomException("El movimiento no esta en estado " + m.getEstadoViaje().getDescrip());
                }
            } else {
                throw new CustomException("No se encontró el movimiento que desea confirmar!");
            }
        } else {
            throw new CustomException("No cuenta con permisos para modificar movimientos!");
        }
    }

    private void confimarMovimiento(Movimiento movimiento) {
        Local cd = localRepository.findByNro(new Long("7460"));
        Set<ItemMovimiento> items = movimiento.getItemMovimientos();
        String tipoMov = movimiento.getTipoMovimiento().getTipo();
        if (tipoMov.equals("DEVOLUCION")) {
            // Actualizo StockReservado
            for (ItemMovimiento item: items) {
                stockBienEnLocalService.restarStockReservado(movimiento.getDestino(), item.getBienIntercambiable().getId(), item.getCantidad());
                if (item.getBienIntercambiable().getTipo().equals("ENVASE")) {
                    deudaService.aumentarDeudaProveedorACD(movimiento.getDestino(),movimiento.getDestino(), item.getBienIntercambiable().getId(),item.getCantidad());
                } else {
                    deudaService.restarDeudaCDaProveedor(cd.getNro(),movimiento.getDestino(), item.getBienIntercambiable().getId(),item.getCantidad());
                }
            }
            // Vales en estado Reservado
        } else if (tipoMov.equals("ENVIOINTERCAMBIO")) {
            for (ItemMovimiento item: items) {
                stockBienEnLocalService.restarStockReservado(movimiento.getDestino(), item.getBienIntercambiable().getId(), item.getCantidad());
                deudaService.aumentarDeudaProveedorACD(movimiento.getDestino(),movimiento.getDestino(), item.getBienIntercambiable().getId(),item.getCantidad());
            }
        } else if (tipoMov.equals("ENVIO")){
            for (ItemMovimiento item: items) {
                stockBienEnLocalService.restarStockReservado(movimiento.getDestino(), item.getBienIntercambiable().getId(), item.getCantidad());
                stockBienEnLocalService.aumentarStockOcupado(movimiento.getDestino(), item.getBienIntercambiable().getId(), item.getCantidad());
            }
        }
    }

    /*
     *  Valida que el BienItemcambiable del ItemMovimiento exista.
     *
     * */
    public void validarItems(Set<ItemMovimiento> items) throws CustomException {
        BienIntercambiable bi;
        EstadoRecurso eim;
        int i=0;
        if (!items.isEmpty()) {
            for (ItemMovimiento item: items) {
                if (item.getBienIntercambiable() == null) throw new CustomException("ItemMovimiento [" +i+ "] contiene BienIntercambiable en NULL.");

                Long id = item.getBienIntercambiable().getId();
                if (null == (bi = bienIntercambiableRepository.findById(id))) {
                    throw new CustomException("ItemMovimiento [" +i+ "] contiene un bien Intercambiable inexistente.");
                } else {
                    item.setBienIntercambiable(bi);
                }

                if (item.getEstadoRecurso() == null) throw new CustomException("ItemMovimiento [" +i+ "] contiene un EstadoRecurso en NULL.");


                if (null == (eim = estadoRecursoRepository.findByDescrip(item.getEstadoRecurso().getDescrip()))) {
                    throw new CustomException("ItemMovimiento [" +i+ "] contiene un EstadoRecurso inexistente.");
                } else {
                    item.setEstadoRecurso(eim);
                }
                i++;
            }
        } else {
            throw new CustomException("El movimiento NO contiene items.");
        }
    }

    /*
     *  Valida campos:
     *  - Origen y destino existan.
     *  - TipoDocumento.
     *
     * */
    public void validarMovimiento(Movimiento movimiento) throws CustomException{
        TipoAgente tipoAgenteOrigen = movimiento.getTipoMovimiento().getTipoAgenteOrigen();
        TipoAgente tipoAgenteDestino = movimiento.getTipoMovimiento().getTipoAgenteDestino();
        // Valido Nros de Origen
        if (tipoAgenteOrigen.getNombre().equals("PROVEEDOR")){
            if (null == proveedorRepository.findOne(movimiento.getOrigen())){
                throw  new CustomException("Proveedor origen no encontrado.");
            }
        } else if (tipoAgenteOrigen.getNombre().equals("TIENDA") || tipoAgenteOrigen.getNombre().equals("CD")) {
            Local l = localRepository.findOne(movimiento.getOrigen());
            if (l != null) {
                if (!l.getTipoAgente().getNombre().equals(tipoAgenteOrigen.getNombre()))
                throw new CustomException(tipoAgenteOrigen.getNombre() + " origen no encontrado.");
            } else {
                throw new CustomException(tipoAgenteOrigen.getNombre() + " origen no encontrado.");
            }
        }
        // Valido Nros de Destino
        if (tipoAgenteDestino.getNombre().equals("PROVEEDOR")) {
            if (null == proveedorRepository.findByNro(movimiento.getDestino())){
                throw  new CustomException("Proveedor destino no encontrado.");
            }
        } else if (tipoAgenteDestino.getNombre().equals("TIENDA") || tipoAgenteDestino.getNombre().equals("CD")) {
            Local l = localRepository.findByNro(movimiento.getDestino());
            if (l != null) {
                if (!l.getTipoAgente().getNombre().equals(tipoAgenteDestino.getNombre()))
                    throw new CustomException(tipoAgenteDestino.getNombre() + " origen no encontrado.");
            } else {
                throw new CustomException(tipoAgenteDestino.getNombre() + " origen no encontrado.");
            }
        }
    }

    /*
     *   Sobreescribe campos que se crean al momento del alta del movimiento.
     *
     * */
    private void sanitizarMovimiento(Movimiento movimientoNuevo) {
        Date today = Date.from(ZonedDateTime.now(zoneId).toInstant());
        if (movimientoNuevo.getFechaSalida() == null) movimientoNuevo.setFechaSalida(today);
        movimientoNuevo.setEstado(estadoRepository.findByDescrip("ACTIVO"));
        String m = movimientoNuevo.getTipoMovimiento().getTipo();
        if (m.equals("ENVIO") || m.equals("DEVOLUCION") || m.equals("ENVIOINTERCAMBIO")){
            movimientoNuevo.setEstadoViaje(estadoViajeRepository.findByDescrip("PENDIENTE"));
        } else {
            movimientoNuevo.setEstadoViaje(estadoViajeRepository.findByDescrip("ENTREGADO"));
        }
        movimientoNuevo.setFechaAlta(today);
    }

    /*
    *   Actualiza el stock dependiendo el tipodeMovimiento
    * */
    private void actualizarStockYDeuda(Movimiento movimiento) throws CustomException {
        // Decido accion a realiza en base al tipo de movimientoNuevo
        Local cd = localRepository.findByNro(new Long(7460));
        Proveedor ifco = proveedorRepository.findByNombre("IFCO");
        Proveedor chep = proveedorRepository.findByNombre("CHEP");
        Set<ItemMovimiento> items = movimiento.getItemMovimientos();
        if (movimiento.getTipoMovimiento().getTipo().equals("ENVIO")) {
            // Actualizo Stock Origen (-) y Destino (+)
            for (ItemMovimiento item: items) {
                validarStock(item, movimiento.getOrigen());
                stockBienEnLocalService.restarStockOcupado(movimiento.getOrigen(), item.getBienIntercambiable().getId(), item.getCantidad());
                stockBienEnLocalService.aumentarStockReservado(movimiento.getDestino(), item.getBienIntercambiable().getId(), item.getCantidad());
            }
        } else if (movimiento.getTipoMovimiento().getTipo().equals("DEVOLUCION")) {
            // Actualizo Stock Origen (-) y Destino (+)
            for (ItemMovimiento item: items) {
                validarStock(item, movimiento.getOrigen());
                stockBienEnLocalService.restarStockLibre(movimiento.getOrigen(), item.getBienIntercambiable().getId(), item.getCantidad());
                stockBienEnLocalService.aumentarStockReservado(movimiento.getDestino(), item.getBienIntercambiable().getId(), item.getCantidad());
                // TODO: ACTUALIZAR VALES
            }
            // Vales en estado Reservado
        } else if (movimiento.getTipoMovimiento().getTipo().equals("RECEPCION")) {
            // Actualizo Stock Origen (+) y Destino (-)
            for (ItemMovimiento item: items) {
                if (item.getEstadoRecurso().getDescrip().equals("LIBRE")){
                    stockBienEnLocalService.aumentarStockLibre(movimiento.getDestino(), item.getBienIntercambiable().getId(), item.getCantidad());
                } else if (item.getEstadoRecurso().getDescrip().equals("OCUPADO")) {
                    stockBienEnLocalService.aumentarStockOcupado(movimiento.getDestino(), item.getBienIntercambiable().getId(), item.getCantidad());
                }

                if (movimiento.getTipoMovimiento().getTipoAgenteOrigen().getNombre().equals("TIENDA")) {
                    validarStock(item, movimiento.getOrigen());
                    if (item.getEstadoRecurso().getDescrip().equals("LIBRE")) {
                        stockBienEnLocalService.restarStockLibre(movimiento.getOrigen(), item.getBienIntercambiable().getId(), item.getCantidad());
                    } else if (item.getEstadoRecurso().getDescrip().equals("OCUPADO")) {
                        stockBienEnLocalService.restarStockOcupado(movimiento.getOrigen(), item.getBienIntercambiable().getId(), item.getCantidad());
                    }
                } else if (movimiento.getTipoMovimiento().getTipoAgenteOrigen().getNombre().equals("PROVEEDOR")) {
                    // TODO: CREAR VALES

                    // SOLO aumentamos la DEUDA del CD al Proveedor cuando NO se trata de ENVASES,
                    // ya que los envases se cobran con el producto, a diferencia de los PALLETS que
                    // se "adeudan" al momento que el proveedor realiza una entrega (o RECEPCION del punto de vista del CD).
                    Long ProveedorId = movimiento.getOrigen();
                    if (item.getBienIntercambiable().getTipo().equals("IFCO")) {
                        ProveedorId = ifco.getNro();
                    } else if (item.getBienIntercambiable().getSubtipo().equals("CHEP")) {
                        ProveedorId = chep.getNro();
                    }
                    if (!item.getBienIntercambiable().getTipo().equals("ENVASE")) {
                        deudaService.aumentarDeudaCDaProveedor(cd.getNro(),ProveedorId, item.getBienIntercambiable().getId(), item.getCantidad());
                    }
                }
            }
        } else if (movimiento.getTipoMovimiento().getTipo().equals("ENVIOINTERCAMBIO")) {
            // Actualizo Stock Origen (-) y Destino (+)

            for (ItemMovimiento item: items) {
                validarStock(item, movimiento.getOrigen());
                if (item.getEstadoRecurso().getDescrip().equals("LIBRE")) {
                    stockBienEnLocalService.restarStockLibre(movimiento.getOrigen(), item.getBienIntercambiable().getId(), item.getCantidad());
                } else if (item.getEstadoRecurso().getDescrip().equals("DESTRUIDO")) {
                    stockBienEnLocalService.restarStockDestruido(movimiento.getOrigen(), item.getBienIntercambiable().getId(), item.getCantidad());
                }
                stockBienEnLocalService.aumentarStockReservado(movimiento.getDestino(), item.getBienIntercambiable().getId(), item.getCantidad());
            }

        } else if (movimiento.getTipoMovimiento().getTipo().equals("RECEPCIONINTERCAMBIO")) {

            for (ItemMovimiento item: items) {
                if (item.getEstadoRecurso().getDescrip().equals("LIBRE")) {
                    stockBienEnLocalService.aumentarStockLibre(movimiento.getDestino(), item.getBienIntercambiable().getId(), item.getCantidad());
                }
                deudaService.restarDeudaProveedorACD(cd.getNro(),movimiento.getDestino(), item.getBienIntercambiable().getId(),item.getCantidad());
            }
        } else if (movimiento.getTipoMovimiento().getTipo().equals("DESTRUCCION")) {
            for (ItemMovimiento item: items) {
                validarStock(item, movimiento.getOrigen());
                String stk = "STOCK_" + item.getEstadoRecurso().getDescrip();
                if (stk.equals("STOCK_OCUPADO") ||stk.equals("STOCK_LIBRE") || stk.equals("STOCK_DESTRUIDO")){
                    stockBienEnLocalService.actualizarStock(movimiento.getOrigen(), item.getBienIntercambiable().getId(), item.getCantidad(), stk, true);
                }
            }
        }
    }


    public List<TipoMovimiento> getTipoMovimientos(Usuario usuarioActual) throws CustomException {
        if (null != permisoRepository.findPermisoWhereUsuarioAndPermiso(usuarioActual.getId(),"CONS-TIPOMOV")) {
            return tipoMovimientoRepository.findAll();
        } else {
            throw new CustomException("No cuenta con los permisos para consultar recursos");
        }
    }


    public void validarStock(ItemMovimiento item, Long localOrigen) throws CustomException {
        String stk = "STOCK_";
        stk += item.getEstadoRecurso().getDescrip();
        if (stk.equals("STOCK_OCUPADO") ||stk.equals("STOCK_LIBRE") || stk.equals("STOCK_DESTRUIDO") ||stk.equals("STOCK_RESERVADO")) {
            Long cant = stockBienEnLocalService.findStockByLocalAndBi(stk, localOrigen, item.getBienIntercambiable().getId());
            cant = (cant == null) ? 0:cant;
            if (cant < item.getCantidad()) {
                throw new CustomException(stk+" insuficiente para " + item.getBienIntercambiable().getTipo() + "-" + item.getBienIntercambiable().getSubtipo());
            }
        } else  {
            throw new CustomException(stk+" inexistente");
        }
    }



    public List<Agente> getAllAgentes(Usuario usuarioActual) throws CustomException {
        List<Agente> agentes;
        if (null != permisoRepository.findPermisoWhereUsuarioAndPermiso(usuarioActual.getId(),"CONS-AGENTE")) {

            agentes= new ArrayList<>();
            proveedorRepository.findAll().forEach(p -> {
                Agente a = new Agente();
                a.setDenominacion(p.getDenominacion());
                a.setDireccion(p.getDireccion());
                a.setNro(p.getNro());
                a.setNombre(p.getNombre());
                a.setTipoAgente(p.getTipoAgente());
                agentes.add(a);
            });
            localRepository.findAll().forEach(l -> {
                Agente a = new Agente();
                a.setDenominacion(l.getDenominacion());
                a.setDireccion(l.getDireccion());
                a.setNro(l.getNro());
                a.setNombre(l.getNombre());
                a.setTipoAgente(l.getTipoAgente());
                agentes.add(a);
            });
            return agentes;
        } else {
            throw new CustomException("No cuenta con los permisos para consultar agentes!");
        }
    }

    /**
     * Metodo que busca los movimientos de tipo envio con destino a una tienda parrticular que todavia no fueron entregados (estado pendiente)
     * @param usuarioActual Para validar los permisos
     * @param nroTienda Nro de tienda destino, PK de la tabla local
     * @return List<Movimiento> Lista de los movimientos encontrados
     * @throws CustomException Excepcion custom si no se encuentra parametrizada la tienda, local y/o tipomovimiento
     */
    public List<Movimiento> getEnviosPendientesByTienda(Usuario usuarioActual, Long nroTienda) throws CustomException{
        if (null != permisoRepository.findPermisoWhereUsuarioAndPermiso(usuarioActual.getId(),"CONS-AGENTE")) {

            TipoMovimiento tipoEnvio = tipoMovimientoRepository.findByTipo("ENVIO");
            EstadoViaje estadoPendiente = estadoViajeRepository.findByDescrip("PENDIENTE");
            Local localDestino = localRepository.findByNro(nroTienda);
            if (tipoEnvio == null || estadoPendiente == null || localDestino == null)
                throw new CustomException("No se encontró el tipo de envio y/o estado y/o local destino");

            return movimientoRepository.findByTipoMovimientoAndEstadoViajeAndDestino(tipoEnvio, estadoPendiente, localDestino.getNro());
        } else {
            throw new CustomException("No cuenta con los permisos para consultar agentes");
        }
    }

    public List<EstadoViaje> getAllEstadosViajes(Usuario usuarioActual) throws CustomException{
        if (null != permisoRepository.findPermisoWhereUsuarioAndPermiso(usuarioActual.getId(),"CONS-ESTADOVIAJE")) {
            List<EstadoViaje> estados = new ArrayList<>();
            estados = estadoViajeRepository.findAll();
            return estados;
        } else {
            throw new CustomException("No cuenta con los permisos para consultar estado de viajes!");
        }
    }

    public List<EstadoRecurso> getAllEstadoBien(Usuario usuarioActual) throws CustomException{
        if (null != permisoRepository.findPermisoWhereUsuarioAndPermiso(usuarioActual.getId(),"CONS-ESTADORECURSO")) {
            return estadoRecursoRepository.findAll();
        } else {
            throw new CustomException("No cuenta con los permisos para consultar estado de recursos!");
        }
    }

    public List<IntercambioProveedor> getAllIntercambioProveedor(Usuario usuarioActual) throws CustomException{
        if (null != permisoRepository.findPermisoWhereUsuarioAndPermiso(usuarioActual.getId(),"CONS-AGENTES")) {
            return intercambioProveedorRepository.findAll();
        } else {
            throw new CustomException("No cuenta con los permisos para consultar intercambios de proveedores!");
        }
    }
}
