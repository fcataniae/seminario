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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
    TipoAgenteRepository tipoAgenteRepository;
    @Autowired
    DeudaService deudaService;


    private static ZoneId zoneId = ZoneId.of("America/Argentina/Buenos_Aires");

    public void create(Usuario usuarioActual, Movimiento movimientoNuevo) throws CustomException {
        TipoMovimiento ti;
        if (null != permisoRepository.findPermisoWhereUsuarioAndPermiso(usuarioActual.getId(),"ALTA-MOVIMIENTO")) {
            if (null != (ti = tipoMovimientoRepository.findByNombreAndTipoOrigenAndTipoDestino(
                    movimientoNuevo.getTipoMovimiento().getTipo(),movimientoNuevo.getTipoMovimiento().getTipoAgenteOrigen().getNombre(), movimientoNuevo.getTipoMovimiento().getTipoAgenteDestino().getNombre()))) {
                movimientoNuevo.setTipoMovimiento(ti);
                movimientoNuevo.setId(null);
                movimientoNuevo.setUsuarioAlta(usuarioActual);
                validarMovimiento(movimientoNuevo);
                sanitizarMovimiento(movimientoNuevo);
                //cambiarEstadoRecursosAsignados(movimientoNuevo.getRecursosAsignados());
                validarItems(movimientoNuevo.getItemMovimientos());
                if (movimientoRepository.save(movimientoNuevo) == null)
                    throw new CustomException("Error al dar de alta la persona!");
                    
                actualizarStockYDeuda(movimientoNuevo);
            } else {
                throw new CustomException("No se encontró el tipo de movimientoNuevo que desea asignar!");
            }
        } else {
            throw new CustomException("No cuenta con permisos para dar de alta movimientoNuevos!");
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
        for (ItemMovimiento item: items) {
            if (item.getBienIntercambiable() == null) throw new CustomException("ItemMovimiento [" +i+ "] contiene BienIntercambiable en NULL.");

            Long id = item.getBienIntercambiable().getId();
            if (null == (bi = bienIntercambiableRepository.findById(id))) {
                throw new CustomException("ItemMovimiento contiene bien Intercambiable inexistente.");
            } else {
                item.setBienIntercambiable(bi);
            }

            if (item.getEstadoRecurso() == null) throw new CustomException("ItemMovimiento [" +i+ "] contiene EstadoRecurso en NULL.");

            String descripEstado = item.getEstadoRecurso().getDescrip();
            if (null == (eim = estadoRecursoRepository.findByDescrip(descripEstado))) {
                throw new CustomException("ItemMovimiento contiene EstadoRecurso inexistente.");
            } else {
                item.setEstadoRecurso(eim);
            }
            i++;
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
            if (null == proveedorRepository.findByNro(movimiento.getOrigen())){
                throw  new CustomException("Proveedor origen no encontrado.");
            }
        } else if (tipoAgenteOrigen.getNombre().equals("TIENDA") || tipoAgenteOrigen.getNombre().equals("CD")) {
            if (null == localRepository.findByNro(movimiento.getOrigen())){
                throw new CustomException("Local origen no encontrado.");
            }
        }
        // Valido Nros de Destino
        if (tipoAgenteDestino.getNombre().equals("PROVEEDOR")) {
            if (null == proveedorRepository.findByNro(movimiento.getDestino())){
                throw  new CustomException("Proveedor destino no encontrado.");
            }
        } else if (tipoAgenteDestino.getNombre().equals("TIENDA") || tipoAgenteDestino.getNombre().equals("CD")) {
            if (null == localRepository.findByNro(movimiento.getDestino())){
                throw new CustomException("Local destino no encontrado.");
            }
        }
    }

    /*
     *   Sobreescribe campos que se crean al momento del alta del movimiento.
     *
     * */
    private void sanitizarMovimiento(Movimiento movimientoNuevo) {
        movimientoNuevo.setEstado(estadoRepository.findByDescrip("ACTIVO"));
        movimientoNuevo.setEstadoViaje(estadoViajeRepository.findByDescrip("PENDIENTE"));
        movimientoNuevo.setFechaAlta(Date.from(ZonedDateTime.now(zoneId).toInstant()));
    }

    /*
    *   Actualiza el stock dependiendo el tipodeMovimiento
    * */
    private void actualizarStockYDeuda(Movimiento movimiento) {
        // Decido accion a realiza en base al tipo de movimientoNuevo
        Local cd = localRepository.findByNro(new Long(7460));
        Set<ItemMovimiento> items = movimiento.getItemMovimientos();
        if (movimiento.getTipoMovimiento().getTipo().equals("ENVIO")) {
            // Actualizo Stock Origen (-) y Destino (+)
            for (ItemMovimiento item: items) {
                stockBienEnLocalService.restarStockOcupado(movimiento.getOrigen(), item.getBienIntercambiable().getId(), item.getCantidad());
                stockBienEnLocalService.aumentarStockOcupado(movimiento.getDestino(), item.getBienIntercambiable().getId(), item.getCantidad());
            }
        } else if (movimiento.getTipoMovimiento().getTipo().equals("DEVOLUCION")) {
            // Actualizo Stock Origen (-) y Destino (+)
            for (ItemMovimiento item: items) {
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
                    if (item.getEstadoRecurso().getDescrip().equals("LIBRE")) {
                        stockBienEnLocalService.restarStockLibre(movimiento.getOrigen(), item.getBienIntercambiable().getId(), item.getCantidad());
                    } else if (item.getEstadoRecurso().getDescrip().equals("OCUPADO")) {
                        stockBienEnLocalService.restarStockOcupado(movimiento.getOrigen(), item.getBienIntercambiable().getId(), item.getCantidad());
                    }
                } else if (movimiento.getTipoMovimiento().getTipoAgenteOrigen().getNombre().equals("PROVEEDOR")) {
                    // TODO: CREAR VALES
                    deudaService.aumentarDeudaCDaProveedor(cd.getNro(), movimiento.getOrigen(), item.getBienIntercambiable().getId(),item.getCantidad());
                }
            }
        } else if (movimiento.getTipoMovimiento().getTipo().equals("RECEPCIONINTERCAMBIO")) {
            // Actualizo Stock Origen (-) y Destino (+)

            for (ItemMovimiento item: items) {
                if (item.getEstadoRecurso().getDescrip().equals("LIBRE")){
                    stockBienEnLocalService.restarStockLibre(movimiento.getOrigen(), item.getBienIntercambiable().getId(), item.getCantidad());
                } else if (item.getEstadoRecurso().getDescrip().equals("DESTRUIDO")) {
                    stockBienEnLocalService.restarStockDestruido(movimiento.getOrigen(), item.getBienIntercambiable().getId(), item.getCantidad());
                }
                deudaService.aumentarDeudaProveedorACD(cd.getNro(),movimiento.getDestino(), item.getBienIntercambiable().getId(),item.getCantidad());
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




    public List<Agente> getAllAgentes(Usuario usuarioActual) {

       // if (null == permisoRepository.findPermisoWhereUsuarioAndPermiso(usuarioActual.getId(),"CONS-TIPOMOV"))

        List<Agente> agentes = new ArrayList<>();


        proveedorRepository.findAll().forEach( p -> {
            Agente a = new Agente();
            a.setDenominacion(p.getDenominacion());
            a.setDireccion(p.getDireccion());
            a.setNro(p.getNro());
            a.setNombre(p.getNombre());
            a.setTipoAgente(p.getTipoAgente());
            agentes.add(a);
        });

        localRepository.findAll().forEach( l -> {
            Agente a = new Agente();
            a.setDenominacion(l.getDenominacion());
            a.setDireccion(l.getDireccion());
            a.setNro(l.getNro());
            a.setNombre(l.getNombre());
            a.setTipoAgente(l.getTipoAgente());
            agentes.add(a);
        });

        return agentes;
    }

    /**
     * Metodo que busca los movimientos de tipo envio con destino a una tienda parrticular que todavia no fueron entregados (estado pendiente)
     * @param usuarioActual Para validar los permisos
     * @param nroTienda Nro de tienda destino, PK de la tabla local
     * @return List<Movimiento> Lista de los movimientos encontrados
     * @throws CustomException Excepcion custom si no se encuentra parametrizada la tienda, local y/o tipomovimiento
     */
    public List<Movimiento> getEnviosPendientesByTienda(Usuario usuarioActual, Long nroTienda) throws CustomException{

        //TODO VALIDAR USUARIO

        TipoMovimiento tipoEnvio = tipoMovimientoRepository.findByTipo("ENVIO");
        EstadoViaje estadoPendiente = estadoViajeRepository.findByDescrip("PENDIENTE");
        Local localDestino = localRepository.findByNro(nroTienda);
        if(tipoEnvio == null || estadoPendiente == null || localDestino == null)
            throw new CustomException("No se encotro el tipo de envio y/o estado y/o local destino");


        return movimientoRepository.findByTipoMovimientoAndEstadoViajeAndDestino(tipoEnvio,estadoPendiente,localDestino.getNro());
    }
}
