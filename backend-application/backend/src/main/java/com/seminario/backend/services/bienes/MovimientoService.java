package com.seminario.backend.services.bienes;

import com.seminario.backend.model.abm.Usuario;
import com.seminario.backend.model.bienes.ItemMovimiento;
import com.seminario.backend.model.bienes.Movimiento;
import com.seminario.backend.model.bienes.TipoLocal;
import com.seminario.backend.model.bienes.TipoMovimiento;
import com.seminario.backend.repository.abm.PermisoRepository;
import com.seminario.backend.repository.abm.EstadoRepository;
import com.seminario.backend.repository.bienes.*;
import com.seminario.backend.services.abm.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

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


    private static ZoneId zoneId = ZoneId.of("America/Argentina/Buenos_Aires");

    public void create(Usuario usuarioActual, Movimiento movimientoNuevo, List<ItemMovimiento> items) throws CustomException {
        if (null != permisoRepository.findPermisoWhereUsuarioAndPermiso(usuarioActual.getId(),"ALTA-MOVIMIENTO")) {
            if (null != tipoMovimientoRepository.findByNombreAndTipoOrigenAndTipoDestino(
                    movimientoNuevo.getTipoMovimiento().getNombre(),movimientoNuevo.getTipoMovimiento().getTipoLocalOrigen(), movimientoNuevo.getTipoMovimiento().getTipoLocalDestino())) {

                movimientoNuevo.setId(null);
                validarMovimiento(movimientoNuevo);
                sanitizarMovimiento(movimientoNuevo);
                if (movimientoRepository.save(movimientoNuevo) == null)
                    throw new CustomException("Error al dar de alta la persona!");
                validarItems(items);
                asignarItemsAMovimientos(movimientoNuevo,items);
                actualizarStockYDeuda(movimientoNuevo, items);
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
    public void validarItems(List<ItemMovimiento> items) throws CustomException {
        for (ItemMovimiento item: items) {
            Long id = item.getBienIntercambiable().getId();
            if (null == bienIntercambiableRepository.findById(id)) {
                throw new CustomException("ItemMovimiento contiende bien Intercambiable inexistente.");
            }
        }
    }

    /*
     *  Valida campos:
     *  - Origen y destino existan.
     *  - TipoDocumento.
     *
     * */
    public void validarMovimiento(Movimiento movimiento) throws CustomException{
        TipoLocal TipoLocalOrigen = movimiento.getTipoMovimiento().getTipoLocalOrigen();
        TipoLocal TipoLocalDestino = movimiento.getTipoMovimiento().getTipoLocalDestino();
        // Valido Nros de Origen
        if (TipoLocalOrigen.getNombre().equals("PROVEEDOR")){
            if (null == proveedorRepository.findByNro(movimiento.getOrigen())){
                throw  new CustomException("Proveedor origen no encontrado.");
            }
        } else if (TipoLocalOrigen.getNombre().equals("TIENDA") || TipoLocalOrigen.getNombre().equals("CD")) {
            if (null == localRepository.findByNro(movimiento.getOrigen())){
                throw new CustomException("Local origen no encontrado.");
            }
        }
        // Valido Nros de Destino
        if (TipoLocalDestino.getNombre().equals("PROVEEDOR")) {
            if (null == proveedorRepository.findByNro(movimiento.getDestino())){
                throw  new CustomException("Proveedor destino no encontrado.");
            }
        } else if (TipoLocalDestino.getNombre().equals("TIENDA") || TipoLocalDestino.getNombre().equals("CD")) {
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
    *   A cada itemMov le asigna el movimiento
    * */
    private void asignarItemsAMovimientos(Movimiento movimiento, List<ItemMovimiento> items) {
        for (ItemMovimiento item : items) {
            item.setMovimiento(movimiento);
        }
    }

    /*
    *   Actualiza el stock dependiendo el tipodeMovimiento
    * */
    private void actualizarStockYDeuda(Movimiento movimiento, List<ItemMovimiento> items) {
        // Decido accion a realiza en base al tipo de movimientoNuevo
        if (movimiento.getTipoMovimiento().getNombre().equals("ENVIO")) {
            // Actualizo Stock Origen (-) y Destino (+)
            for (ItemMovimiento item: items) {
                stockBienEnLocalService.restarStockOcupado(movimiento.getOrigen(), item.getBienIntercambiable().getId(), item.getCantidad());
                stockBienEnLocalService.aumentarStockOcupado(movimiento.getDestino(), item.getBienIntercambiable().getId(), item.getCantidad());
            }
        } else if (movimiento.getTipoMovimiento().getNombre().equals("DEVOLUCION")) {
            // Actualizo Stock Origen (-) y Destino (+)
            for (ItemMovimiento item: items) {
                stockBienEnLocalService.restarStockLibre(movimiento.getOrigen(), item.getBienIntercambiable().getId(), item.getCantidad());
                stockBienEnLocalService.aumentarStockReservado(movimiento.getDestino(), item.getBienIntercambiable().getId(), item.getCantidad());
            }
            // Vales en estado Reservado
        } else if (movimiento.getTipoMovimiento().getNombre().equals("RECEPCION")) {
            // Actualizo Stock Origen (+) y Destino (-)
        } else if (movimiento.getTipoMovimiento().getNombre().equals("INTERCAMBIO")) {
            // Actualizo Stock Origen (-) y Destino (+)

        }

    }

    public List<TipoMovimiento> getTipoMovimientos(Usuario usuarioActual){
        if (null != permisoRepository.findPermisoWhereUsuarioAndPermiso(usuarioActual.getId(),"CONS-TIPOMOV")) {
            return tipoMovimientoRepository.findAll();
        } else {
            throw new CustomException("No cuenta con los permisos para consultar recursos");
        }
    }


}