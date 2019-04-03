package com.seminario.backend.model;

import javax.persistence.*;

/**
 * User: fcatania
 * Date: 1/4/2019
 * Time: 09:15
 */
@Entity
@Cacheable(false)
public class Permiso {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nombre;

    @Column
    private String descripcion;

    @Column
    private String funcionalidad;

    @Column
    private boolean readPriv;

    @Column
    private boolean writePriv;


    @ManyToOne()
    private Estado estado;


    @Override
    public String toString() {
        return "Permiso{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" +this.descripcion + "\'" +
                ", funcionalidad='" +this.funcionalidad + "\'" +
                ", write='" + this.writePriv + "\'" +
                ", read='" + this.readPriv + "\'" +
                '}';
    }

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

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}