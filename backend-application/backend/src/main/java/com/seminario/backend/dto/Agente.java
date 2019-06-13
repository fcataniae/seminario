package com.seminario.backend.dto;

import com.seminario.backend.model.bienes.TipoAgente;

import java.util.*;
/**
 * User: fcatania
 * Date: 21/5/2019
 * Time: 13:51
 */
public class Agente {

    private Long nro;
    private String email;
    private String nombre;
    private String denominacion;
    private String direccion;
    private Long direccion_nro;
    private TipoAgente tipoAgente;
    private List<StockBienEnLocal> stockBienes;
    private List<DeudaBien> deudaBienes;


    public Agente() {
        this.stockBienes = new ArrayList<StockBienEnLocal>();
        this.deudaBienes = new ArrayList<>();
    }

    public TipoAgente getTipoAgente() { return tipoAgente; }
    public void setTipoAgente(TipoAgente tipoAgente) { this.tipoAgente = tipoAgente; }

    public Long getNro() { return nro; }
    public void setNro(Long nro) { this.nro = nro; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) {this.nombre = nombre; }

    public String getDenominacion() { return denominacion; }
    public void setDenominacion(String denominacion) { this.denominacion = denominacion; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) {this.direccion = direccion;}

    public Long getDireccion_nro() { return direccion_nro; }
    public void setDireccion_nro(Long direccion_nro) { this.direccion_nro = direccion_nro; }

    public List<StockBienEnLocal> getStockBienes() { return stockBienes;}

    public List<DeudaBien> getDeudaBienes() { return deudaBienes;}
    public void setStockBienes(List<StockBienEnLocal> stockBienes) { this.stockBienes = stockBienes; }
    public void addStockBien(StockBienEnLocal stockBien){this.stockBienes.add(stockBien);}


    public void addDeudaBien(DeudaBien d) {
        this.deudaBienes.add(d);
    }

    public void setDeudaBienes(List<DeudaBien> deudaBienes) {
        this.deudaBienes = deudaBienes;
    }
}