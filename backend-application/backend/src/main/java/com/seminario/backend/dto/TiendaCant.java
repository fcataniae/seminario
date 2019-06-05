package com.seminario.backend.dto;

public class TiendaCant {
    private Long tiendaId;
    private Long cantRecibida;
    private Long cantEnviada;

    public TiendaCant(){

    }

    public Long getTiendaId() {
        return tiendaId;
    }

    public void setTiendaId(Long tiendaId) {
        this.tiendaId = tiendaId;
    }

    public Long getCantRecibida() {
        return cantRecibida;
    }

    public void setCantRecibida(Long cantRecibida) {
        this.cantRecibida = cantRecibida;
    }

    public Long getCantEnviada() {
        return cantEnviada;
    }

    public void setCantEnviada(Long cantEnviada) {
        this.cantEnviada = cantEnviada;
    }
}
