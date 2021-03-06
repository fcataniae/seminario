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
public class IntercambioProveedor {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(cascade = { CascadeType.MERGE})
    private Proveedor proveedor;

    @ManyToOne(cascade = { CascadeType.MERGE})
    private BienIntercambiable bienIntercambiableEntregado;

    @ManyToOne(cascade = { CascadeType.MERGE})
    private BienIntercambiable bienIntercambiableRecibido;

    @Column
    private Long cantidadRecibida;

    @Column
    private Long cantidadEntregada;

    @Column
    private String nombreIntercambio;

    @Column
    private String usuarioAlta;


    @ManyToOne(cascade = { CascadeType.MERGE})
    private Estado estado;

    @Column
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date fechaAlta;

    public IntercambioProveedor() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public Long getCantidadEntregada() {
        return cantidadEntregada;
    }

    public void setCantidadEntregada(Long cantidadEntregada) {
        this.cantidadEntregada = cantidadEntregada;
    }

    public Long getCantidadRecibida() {
        return cantidadRecibida;
    }

    public void setCantidadRecibida(Long cantidadRecibida) {
        this.cantidadRecibida = cantidadRecibida;
    }

    public String getUsuarioAlta() {
        return usuarioAlta;
    }

    public void setUsuarioAlta(String usuarioAlta) {
        this.usuarioAlta = usuarioAlta;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public String getNombreIntercambio() {
        return nombreIntercambio;
    }

    public void setNombreIntercambio(String nombreIntercambio) {
        this.nombreIntercambio = nombreIntercambio;
    }

    public BienIntercambiable getBienIntercambiableEntregado() {
        return bienIntercambiableEntregado;
    }

    public void setBienIntercambiableEntregado(BienIntercambiable bienIntercambiableEntrega) {
        this.bienIntercambiableEntregado = bienIntercambiableEntrega;
    }

    public BienIntercambiable getBienIntercambiableRecibido() {
        return bienIntercambiableRecibido;
    }

    public void setBienIntercambiableRecibido(BienIntercambiable bienIntercambiableRecibe) {
        this.bienIntercambiableRecibido = bienIntercambiableRecibe;
    }
}
