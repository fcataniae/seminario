package com.seminario.services.rest;

import com.seminario.backend.model.bienes.TipoMovimiento;
import com.seminario.backend.services.bienes.MovimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


    @Autowired
    MovimientoService movimientoService;

    @GetMapping("/get-movimientos")
    public List<TipoMovimiento> getMovimientos(){
        return movimientoService.getMovimientos();
    }
}
