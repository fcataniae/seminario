package com.seminario.backend.model.bienes;

import javax.persistence.*;

@Entity
@Cacheable(false)
public class ItemMovimientoTipoDoc {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long Id;


    @ManyToOne(cascade = { CascadeType.MERGE})
    @JoinColumn(name = "TIPO_DOCUMENTO_ID")
    private  TipoDocumento tipoDocumento;

    @Column
    private String  nroDocumento;


    public String getNroDocumento() {
        return nroDocumento;
    }

    public void setNroDocumento(String nroDocumento) {
        this.nroDocumento = nroDocumento;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }
}
