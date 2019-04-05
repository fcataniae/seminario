package com.seminario.backend.model;


import javax.persistence.*;
import java.util.Set;

/**
 * User: fcatania
 * Date: 1/4/2019
 * Time: 09:13
 */

@Entity
@Cacheable(false)
public class Rol {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)
    private String nombre;

    @Column
    private String descripcion;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "ROL_PERMISO")
    private Set<Permiso> permisos;

    @Override
    public String toString() {
        return "Rol{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Estado estado;

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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public void addPermiso(Permiso p){
        permisos.add(p);
    }

    public void delPermiso(Permiso p){
        permisos.remove(p);
    }

    public Set<Permiso> getPermisos() {
        return this.permisos;
    }

    public void setPermisos(Set<Permiso> permisos) {
        this.permisos = permisos;
    }
}