package com.seminario.services.rest;

import com.seminario.backend.dto.Agente;
import com.seminario.backend.model.abm.Estado;
import com.seminario.backend.model.abm.Usuario;
import com.seminario.backend.model.bienes.*;
import com.seminario.backend.services.abm.CustomException;
import com.seminario.backend.services.abm.UsuarioService;
import com.seminario.backend.services.bienes.BienIntercambiableService;
import com.seminario.backend.services.bienes.MovimientoService;
import com.seminario.backend.services.bienes.RecursoService;
import com.seminario.backend.services.bienes.StockBienEnLocalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Usuario: Franco
 * Project: backend-application
 * Fecha: 5/14/2019
 **/


@RestController
@RequestMapping("/bienes")
@CrossOrigin
@Scope("session")
public class BienesRestController {

    private BienesRestController(){};
    @Autowired
    MovimientoService movimientoService;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    BienIntercambiableService bienIntercambiableService;
    @Autowired
    RecursoService recursoService;
    @Autowired
    StockBienEnLocalService stockBienEnLocalService;


    /**
     * Crea un nuevo movimiento.
     *
     * @param    userDetails       Credenciales de usuario.
     *                             (debe tener los permisos para ejecutar el método).
     **/
    @PostMapping("/alta-movimiento")
    public void crearMovimiento(@AuthenticationPrincipal UserDetails userDetails,
                                @RequestBody Movimiento movimiento) throws CustomException
    {
        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());
        movimientoService.create(usuarioActual, movimiento);
    }

    /**
     * Confirmar un movimiento.
     *
     * @param    userDetails       Credenciales de usuario.
     *                             (debe tener los permisos para ejecutar el método).
     * @param    id                Id del movimiento a confirmar.
     **/
    @PutMapping("/confirmar-movimiento/{id}")
    public void confirmarMovimiento(@AuthenticationPrincipal UserDetails userDetails,
                                    @PathVariable("id") Long id,
                                    @RequestBody String comentario) throws CustomException
    {
        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());
        movimientoService.confirmar(usuarioActual,id,comentario);
    }

    /**
     * Lista todos los movimientos.
     *
     * @param    userDetails       Credenciales de usuario.
     *                             (debe tener los permisos para ejecutar el método).
     **/
    @GetMapping("/listar-movimientos")
    public List<TipoMovimiento> getTipoMovimientos(@AuthenticationPrincipal UserDetails userDetails) throws CustomException
    {
        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());
        return movimientoService.getTipoMovimientos(usuarioActual);
    }



    /**
     * Lista todos los bienes intercambiables.
     *
     * @param    userDetails       Credenciales de usuario.
     *                             (debe tener los permisos para ejecutar el método).
     **/
    @GetMapping("/listar-bi")
    public List<BienIntercambiable> getBI(@AuthenticationPrincipal UserDetails userDetails) throws CustomException
    {
        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());
        return bienIntercambiableService.getBienesIntercambiables(usuarioActual);
    }

    /**
     * Lista el stock (ocupado, libre, destruido, reservado) de todos los bienes de un Local
     * @param userDetails Credenciales de usuario
     * @param nroLocal Nro local
     * @return List<StockBienEnLocal> Todos los bienes del local con su stock
     * @throws CustomException Excepcion custom
     */
    @GetMapping("/stock-local/{nro-local}")
    public List<com.seminario.backend.dto.StockBienEnLocal> getStockLocal(@AuthenticationPrincipal UserDetails userDetails,
                                                @PathVariable("nro-local") Long nroLocal)  throws CustomException {
        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());
        return stockBienEnLocalService.getStockLocal(nroLocal,usuarioActual);
    }

    /**
     * Metodo que devuelve todos los estados posibles de los viajes para seleccionar al confirmar la recepcion de un envio
     * @param userDetails detalles de iusuario
     * @return List con todos los estados posibles
     * @throws CustomException excepcion custom
     */
    @GetMapping("/estado-viaje")
    public List<EstadoViaje> getAllEstadosViajes(@AuthenticationPrincipal UserDetails userDetails) throws CustomException{
        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());
        return movimientoService.getAllEstadosViajes(usuarioActual);
    }

    /**
     * Lista todos los recursos.
     *
     * @param    userDetails       Credenciales de usuario.
     *                             (debe tener los permisos para ejecutar el método).
     **/
    @GetMapping("/listar-recursos")
    public List<Recurso> getRecursos(@AuthenticationPrincipal UserDetails userDetails) throws CustomException
    {
        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());
        return recursoService.getRecursos(usuarioActual);
    }

    @GetMapping("/listar-agentes")
    public List<Agente> getAgentes(@AuthenticationPrincipal UserDetails userDetails) throws CustomException{
        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());
        return movimientoService.getAllAgentes(usuarioActual);
    }

    /**
     * Metodo de la api para listar todos los envios pendientes a una tienda de destino
     * @param userDetails Credenciales de usuario
     * @param nroTienda Nro tienda destino
     * @return List<Movimiento> lista con los movimientos encontrados
     * @throws CustomException Excepcion custom
     */

    @GetMapping("/listar-envios-pendientes/{nro-tienda}")
    public List<Movimiento> getEnviosPendientesByTienda(@AuthenticationPrincipal UserDetails userDetails,
                                                @PathVariable("nro-tienda") Long nroTienda)  throws CustomException
    {

        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());
        return movimientoService.getEnviosPendientesByTienda(usuarioActual,nroTienda);
    }

    @GetMapping("/estado-bien")
    public List<EstadoRecurso> getAllEstadoBien(@AuthenticationPrincipal UserDetails userDetails) throws  CustomException{
        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());
        return movimientoService.getAllEstadoBien(usuarioActual);

    }
}
