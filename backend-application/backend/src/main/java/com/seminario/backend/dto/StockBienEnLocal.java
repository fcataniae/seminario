package com.seminario.backend.dto;

import com.seminario.backend.model.bienes.TipoAgente;

public class StockBienEnLocal {

    private Long nroLocal;

    private String nombreLocal;

    private Long idBI;

    private String tipoBI;

    private String subtipoBI;

    private String descripcionBI;

    private long stock_ocupado;

    private long stock_libre;

    private long stock_reservado;

    private long stock_destruido;


    public StockBienEnLocal() {
    }

    //getters
    public Long getNroLocal() {return nroLocal; }

    public String getNombreLocal() { return nombreLocal; }

    public Long getIdBI() { return idBI; }

    public String getTipoBI() { return tipoBI; }

    public String getSubtipoBI() { return subtipoBI; }

    public String getDescripcionBI() { return descripcionBI; }

    public long getStock_ocupado() { return stock_ocupado; }

    public long getStock_libre() { return stock_libre; }

    public long getStock_reservado() { return stock_reservado; }

    public long getStock_destruido() { return stock_destruido; }

    //setters
    public void setNroLocal(Long nroLocal) { this.nroLocal = nroLocal; }

    public void setNombreLocal(String nombreLocal) { this.nombreLocal = nombreLocal; }

    public void setIdBI(Long idBI) { this.idBI = idBI; }

    public void setTipoBI(String tipoBI) { this.tipoBI = tipoBI; }

    public void setSubtipoBI(String subtipoBI) { this.subtipoBI = subtipoBI; }

    public void setDescripcionBI(String descripcionBI) { this.descripcionBI = descripcionBI; }

    public void setStock_ocupado(long stock_ocupado) { this.stock_ocupado = stock_ocupado; }

    public void setStock_libre(long stock_libre) { this.stock_libre = stock_libre; }

    public void setStock_reservado(long stock_reservado) { this.stock_reservado = stock_reservado; }

    public void setStock_destruido(long stock_destruido) { this.stock_destruido = stock_destruido; }
}

