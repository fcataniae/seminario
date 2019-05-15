package com.seminario.backend.model.bienes;

import javax.persistence.*;
import java.util.Set;

/**
 * Usuario: Franco
 * Project: backend-application
 * Fecha: 5/14/2019
 **/
@Entity
@Cacheable(false)
public class ItemMovimiento {


    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Movimiento movimiento;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private BienIntercambiable bienIntercambiable;

    @JoinTable(name = "ITEMMOVIMIENTO_TIPODOC")
    @OneToMany (fetch = FetchType.EAGER)
    private Set<TipoDocumento> tipoDocumentos;



}
