package com.seminario.backend.model.bienes;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.seminario.backend.model.abm.Estado;

import javax.persistence.*;
import java.util.Date;

/**
 * Usuario: Franco
 * Project: backend-application
 * Fecha: 5/14/2019
 **/

@Entity
@Cacheable(false)
public class Proveedor {

    @Id
    private Long nro;

    @Column
    private String email;

    @Column(unique = true)
    private String nombre;

    @Column
    private String denominacion;

    @Column
    private String direccion;

    @Column
    private Long direccion_nro;

    @Column
    private String usuarioAlta;

    @ManyToOne
    @JoinColumn(name = "id_tipo_agente")
    private TipoAgente tipoAgente;

    public TipoAgente getTipoAgente() {
        return tipoAgente;
    }

    public void setTipoAgente(TipoAgente tipoAgente) {
        this.tipoAgente = tipoAgente;
    }

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Estado estado;

    @Column
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date fechaAlta;

    public TipoProveedor getTipoProveedor() {
        return tipoProveedor;
    }

    public void setTipoProveedor(TipoProveedor tipoProveedor) {
        this.tipoProveedor = tipoProveedor;
    }

    @ManyToOne
    @JoinColumn(name = "ID_TIPO_PROVEEDOR")
    private TipoProveedor tipoProveedor;

    @Override
    public String toString() {
        return "Proveedor{" +
                "nro=" + nro +
                ", nombre='" + nombre + '\'' +
                ", denominacion='" + denominacion + '\'' +
                ", direccion='" + direccion + '\'' +
                ", direccion_nro=" + direccion_nro +
                '}';
    }

    public Long getNro() {
        return nro;
    }

    public void setNro(Long nro) {
        this.nro = nro;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDenominacion() {
        return denominacion;
    }

    public void setDenominacion(String denominacion) {
        this.denominacion = denominacion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Long getDireccion_nro() {
        return direccion_nro;
    }

    public void setDireccion_nro(Long direccion_nro) {
        this.direccion_nro = direccion_nro;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsuarioAlta() {
        return usuarioAlta;
    }

    public void setUsuarioAlta(String usuarioAlta) {
        this.usuarioAlta = usuarioAlta;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}
