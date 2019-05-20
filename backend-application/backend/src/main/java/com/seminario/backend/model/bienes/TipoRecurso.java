package com.seminario.backend.model.bienes;

import javax.persistence.*;

/**
 * Usuario: Franco
 * Project: backend-application
 * Fecha: 5/14/2019
 **/
@Entity
@Cacheable(false)
public class TipoRecurso {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;
}
