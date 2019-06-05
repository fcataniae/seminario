package com.seminario.backend.dto;

import com.seminario.backend.model.bienes.TipoAgente;

public class StockBienEnLocal {

    private Long idBI;

    private String tipoBI;

    private String subtipoBI;

    private String descripcionBI;

    private Long stock_ocupado;

    private Long stock_libre;

    private Long stock_reservado;

    private Long stock_destruido;


    public StockBienEnLocal() {
    }

    //getters

    public Long getIdBI() { return idBI; }

    public String getTipoBI() { return tipoBI; }

    public String getSubtipoBI() { return subtipoBI; }

    public String getDescripcionBI() { return descripcionBI; }

    public Long getStock_ocupado() { return stock_ocupado; }

    public Long getStock_libre() { return stock_libre; }

    public Long getStock_reservado() { return stock_reservado; }

    public Long getStock_destruido() { return stock_destruido; }

    //setters

    public void setIdBI(Long idBI) { this.idBI = idBI; }

    public void setTipoBI(String tipoBI) { this.tipoBI = tipoBI; }

    public void setSubtipoBI(String subtipoBI) { this.subtipoBI = subtipoBI; }

    public void setDescripcionBI(String descripcionBI) { this.descripcionBI = descripcionBI; }

    public void setStock_ocupado(Long stock_ocupado) { this.stock_ocupado = stock_ocupado; }

    public void setStock_libre(Long stock_libre) { this.stock_libre = stock_libre; }

    public void setStock_reservado(Long stock_reservado) { this.stock_reservado = stock_reservado; }

    public void setStock_destruido(Long stock_destruido) { this.stock_destruido = stock_destruido; }
}

