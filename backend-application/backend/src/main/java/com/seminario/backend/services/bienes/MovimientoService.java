package com.seminario.backend.services.bienes;

import com.seminario.backend.model.abm.Usuario;
import com.seminario.backend.model.bienes.ItemMovimiento;
import com.seminario.backend.model.bienes.Movimiento;
import com.seminario.backend.repository.abm.PermisoRepository;
import com.seminario.backend.repository.abm.EstadoRepository;
import com.seminario.backend.repository.bienes.EstadoViajeRepository;
import com.seminario.backend.repository.bienes.TipoMovimientoRepository;
import com.seminario.backend.repository.bienes.MovimientoRepository;
import com.seminario.backend.services.abm.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void create(Usuario usuarioActual, Movimiento movimientoNuevo, List<ItemMovimiento> items) throws CustomException {
        if (null != permisoRepository.findPermisoWhereUsuarioAndPermiso(usuarioActual.getId(),"ALTA-MOVIMIENTO")) {
            if (null != tipoMovimientoRepository.findTipoMovByNombreAndOrigenAndDestino(
                    movimientoNuevo.getTipoMovimiento().getNombre(),movimientoNuevo.getTipoLocalOrigen(), movimientoNuevo.getTipoLocalDestino())) {

                movimientoNuevo.setId(null);
                validarMovimiento(movimientoNuevo);
                sanitizarMovimiento(movimientoNuevo);
                if (movimientoRepository.save(movimientoNuevo) == null)
                    throw new CustomException("Error al dar de alta la persona!");
                validarItems(items);
                asignarItemsAMovimientos(movimientoNuevo,items);
                actualizarStock(movimientoNuevo, items);
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
    public void validarItems(List<ItemMovimiento> items){
        for (ItemMovimiento item: items) {
            //
            //item.getBienIntercambiable().getId();

        }
    }

    /*
     *  Valida campos:
     *  - Origen y destino existan.
     *  - TipoDocumento.
     *
     * */
    public void validarMovimiento(Movimiento movimiento) throws CustomException{
        // TODO: Hacer las validaciones
        // - Verficar existencia de Origen, Destino
        // -
        throw new CustomException("Movimiento con datos invalidos.");
    }

    /*
     *   Sobreescribe campos que se crean al momento del alta del movimiento.
     *
     * */
    private void sanitizarMovimiento(Movimiento movimientoNuevo) {
        movimientoNuevo.setEstado(estadoRepository.findByDescrip("ACTIVO"));
        movimientoNuevo.setEstadoViaje(estadoViajeRepository.findByDescrip("PENDIENTE"));
        //movimientoNuevo.setFechaAlta('TODAY');
    }

    /*
    *   A cada itemMov le asigna el movimiento
    * */
    private void asignarItemsAMovimientos(Movimiento movimiento, List<ItemMovimiento> items) {
        //
    }

    /*
    *   Actualiza el stock dependiendo el tipodeMovimiento
    * */
    private void actualizarStock(Movimiento movimiento, List<ItemMovimiento> items) {
        // Decido accion a realiza en base al tipo de movimientoNuevo
        if (movimiento.getTipoMovimiento().getNombre().equals("ENVIO")) {
            // Actualizo Stock Origen (-) y Destino (+)
        } else if (movimiento.getTipoMovimiento().getNombre().equals("DEVOLUCION")) {
            // Actualizo Stock Origen (-) y Destino (+)
            // Vales en estado Reservado
        } else if (movimiento.getTipoMovimiento().getNombre().equals("RECEPCION")) {
            // Actualizo Stock Origen (+) y Destino (-)
        } else if (movimiento.getTipoMovimiento().getNombre().equals("INTERCAMBIO")) {
            // Actualizo Stock Origen (-) y Destino (+)

        }

    }


}
