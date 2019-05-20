package com.seminario.backend.model.bienes;

import javax.persistence.*;

@Entity
@Cacheable(false)
public class EstadoViaje {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)
    private String descrip;
}
