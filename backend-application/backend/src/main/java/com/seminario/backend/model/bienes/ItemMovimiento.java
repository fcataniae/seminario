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

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne(cascade = { CascadeType.MERGE})
    private BienIntercambiable bienIntercambiable;


    @JoinColumn(name = "ITEM_MOVIMIENTO_ID")
    @OneToMany( cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<ItemMovimientoTipoDoc> itemMovimientoTipoDoc;

    @ManyToOne(cascade = {CascadeType.MERGE})
    private EstadoRecurso estadoRecurso;

    @Column
    private Long cantidad;

    @Column
    private Double precio;

    public ItemMovimiento(){

    }

    public BienIntercambiable getBienIntercambiable() {
        return bienIntercambiable;
    }

    public void setBienIntercambiable(BienIntercambiable bienIntercambiable) {
        this.bienIntercambiable = bienIntercambiable;
    }

    public Set<ItemMovimientoTipoDoc> getItemMovimientoTipoDoc() {
        return itemMovimientoTipoDoc;
    }

    public void setItemMovimientoTipoDoc(Set<ItemMovimientoTipoDoc> itemMovimientoTipoDoc) {
        this.itemMovimientoTipoDoc = itemMovimientoTipoDoc;
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public EstadoRecurso getEstadoRecurso() {
        return estadoRecurso;
    }

    public void setEstadoRecurso(EstadoRecurso estadoRecurso) {
        this.estadoRecurso = estadoRecurso;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }
}
