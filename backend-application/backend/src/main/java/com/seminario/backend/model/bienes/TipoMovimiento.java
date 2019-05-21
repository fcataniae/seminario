package com.seminario.backend.model.bienes;

import javax.persistence.*;

/**
 * Usuario: Franco
 * Project: backend-application
 * Fecha: 5/14/2019
 **/
@Entity
@Cacheable(false)
public class TipoMovimiento {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String tipo;

    @Column
    private String nombre;

    @JoinColumn(name = "id_tipo_destino")
    private TipoLocal tipoLocalDestino;

    @JoinColumn(name = "id_tipo_origen")
    private TipoLocal tipoLocalOrigen;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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
