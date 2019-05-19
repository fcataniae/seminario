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
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Movimiento movimiento;

    @Id
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private BienIntercambiable bienIntercambiable;

    @JoinTable(name = "ITEMMOVIMIENTO_TIPODOC")
    @OneToMany (fetch = FetchType.EAGER)
    private Set<TipoDocumento> tipoDocumentos;

    public ItemMovimiento(){

    }

    public Movimiento getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(Movimiento movimiento) {
        this.movimiento = movimiento;
    }

    public BienIntercambiable getBienIntercambiable() {
        return bienIntercambiable;
    }

    public void setBienIntercambiable(BienIntercambiable bienIntercambiable) {
        this.bienIntercambiable = bienIntercambiable;
    }

    public Set<TipoDocumento> getTipoDocumentos() {
        return tipoDocumentos;
    }

    public void setTipoDocumentos(Set<TipoDocumento> tipoDocumentos) {
        this.tipoDocumentos = tipoDocumentos;
    }
}
