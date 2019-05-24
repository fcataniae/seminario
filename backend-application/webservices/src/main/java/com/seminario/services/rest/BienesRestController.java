package com.seminario.services.rest;

import com.seminario.backend.dto.Agente;
import com.seminario.backend.model.abm.Usuario;
import com.seminario.backend.model.bienes.BienIntercambiable;
import com.seminario.backend.model.bienes.Movimiento;
import com.seminario.backend.model.bienes.Recurso;
import com.seminario.backend.model.bienes.TipoMovimiento;
import com.seminario.backend.services.abm.CustomException;
import com.seminario.backend.services.abm.UsuarioService;
import com.seminario.backend.services.bienes.BienIntercambiableService;
import com.seminario.backend.services.bienes.MovimientoService;
import com.seminario.backend.services.bienes.RecursoService;
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
}
