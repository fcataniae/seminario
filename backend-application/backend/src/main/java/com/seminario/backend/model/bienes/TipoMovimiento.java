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
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private TipoAgente tipoAgenteDestino;

    @JoinColumn(name = "id_tipo_origen")
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private TipoAgente tipoAgenteOrigen;

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

    public TipoAgente getTipoAgenteDestino() {
        return tipoAgenteDestino;
    }

    public void setTipoAgenteDestino(TipoAgente tipoAgenteDestino) {
        this.tipoAgenteDestino = tipoAgenteDestino;
    }

    public TipoAgente getTipoAgenteOrigen() {
        return tipoAgenteOrigen;
    }

    public void setTipoAgenteOrigen(TipoAgente tipoAgenteOrigen) {
        this.tipoAgenteOrigen = tipoAgenteOrigen;
    }
}
