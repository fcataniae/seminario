package com.seminario.backend.model.bienes;

import javax.persistence.*;

@Entity(name = "STOCK_BIEN_EN_LOCAL")
@Cacheable(false)
public class StockBienEnLocal {

    @Id
    @ManyToOne
    @Column(name = "LOCAL_NRO")
    Local local;

    @Id
    @Column(name = "BI_ID")
    BienIntercambiable bienIntercambiable;

    @Column(name = "STOCK_LIBRE")
    Long StockLibre;

    @Column(name = "STOCK_OCUPADO")
    Long StockOcupado;

    @Column(name = "STOCK_DESTRUIDO")
    Long StockDestruido;

    @Column(name = "STOCK_RESERVADO")
    Long StockReservado;

    public Local getLocal() {
        return local;
    }

    public void setLocal(Local local) {
        this.local = local;
    }

    public BienIntercambiable getBienIntercambiable() {
        return bienIntercambiable;
    }

    public void setBienIntercambiable(BienIntercambiable bienIntercambiable) {
        this.bienIntercambiable = bienIntercambiable;
    }

    public Long getStockLibre() {
        return StockLibre;
    }

    public void setStockLibre(Long stockLibre) {
        StockLibre = stockLibre;
    }

    public Long getStockOcupado() {
        return StockOcupado;
    }

    public void setStockOcupado(Long stockOcupado) {
        StockOcupado = stockOcupado;
    }

    public Long getStockDestruido() {
        return StockDestruido;
    }

    public void setStockDestruido(Long stockDestruido) {
        StockDestruido = stockDestruido;
    }

    public Long getStockReservado() {
        return StockReservado;
    }

    public void setStockReservado(Long stockReservado) {
        StockReservado = stockReservado;
    }

}
