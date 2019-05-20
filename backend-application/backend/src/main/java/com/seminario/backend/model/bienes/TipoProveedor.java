package com.seminario.backend.model.bienes;

import javax.persistence.*;

/**
 * Usuario: Franco
 * Project: backend-application
 * Fecha: 5/14/2019
 **/
@Entity
@Cacheable(false)
public class TipoProveedor {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)
    private String descrip;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

}
