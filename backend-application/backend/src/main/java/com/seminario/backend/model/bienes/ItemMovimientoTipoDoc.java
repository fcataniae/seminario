package com.seminario.backend.model.bienes;

import javax.persistence.*;

@Entity
@Cacheable(false)
public class ItemMovimientoTipoDoc {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "ITEM_MOVIMIENTO_ID")
    private ItemMovimiento itemMovimiento;

    @ManyToOne
    @JoinColumn(name = "TIPO_DOCUMENTO_ID")
    private  TipoDocumento tipoDocumento;

    @Column
    private String  nroDocumento;

    public ItemMovimiento getItemMovimiento() {
        return itemMovimiento;
    }

    public void setItemMovimiento(ItemMovimiento itemMovimiento) {
        this.itemMovimiento = itemMovimiento;
    }

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
}
