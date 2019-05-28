package com.seminario.backend.model.bienes;


import javax.persistence.*;

@Entity
@Cacheable(false)
public class TipoMovTipoDoc {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "TIPO_MOVIMIENTO_ID")
    private TipoMovimiento tipoMovimiento;

    @ManyToOne
    @JoinColumn(name = "TIPO_DOCUMENTO_ID")
    private  TipoDocumento tipoDocumento;

    public TipoMovTipoDoc() {
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public TipoMovimiento getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }
}
