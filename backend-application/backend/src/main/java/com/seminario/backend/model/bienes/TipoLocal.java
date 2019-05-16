package com.seminario.backend.model.bienes;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Usuario: Franco
 * Project: backend-application
 * Fecha: 5/14/2019
 **/
@Entity
@Cacheable(false)
public class TipoLocal {

    @Id
    private Long Id;

    @Id
    private String nombre;

}
