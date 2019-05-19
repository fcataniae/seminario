package com.seminario.backend.model.bienes;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Usuario: Franco
 * Project: backend-application
 * Fecha: 5/14/2019
 **/
@Entity
@Cacheable(false)
public class TipoMovimiento {

    @Id
    private Long id;

    @Column
    private String nombre;

    @Column
    private TipoLocal tipoLocalDestino;

    @Column
    private TipoLocal tipoLocalOrigen;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoLocal getTipoLocalDestino() {
        return tipoLocalDestino;
    }

    public void setTipoLocalDestino(TipoLocal tipoLocalDestino) {
        this.tipoLocalDestino = tipoLocalDestino;
    }

    public TipoLocal getTipoLocalOrigen() {
        return tipoLocalOrigen;
    }

    public void setTipoLocalOrigen(TipoLocal tipoLocalOrigen) {
        this.tipoLocalOrigen = tipoLocalOrigen;
    }
}
