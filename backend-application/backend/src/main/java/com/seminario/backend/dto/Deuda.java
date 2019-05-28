package com.seminario.backend.dto;

public class Deuda {
    private Long id_agente_origen;
    private Long id_agente_destino;
    private Long tipo_agente_origen;
    private Long tipo_agente_destino;
    private Long BI_id;
    private Long deuda_cant;
    private Double deuda_monetaria;
    private Long ultima_fecha_actualizacion;

    public Long getId_agente_origen() {
        return id_agente_origen;
    }

    public void setId_agente_origen(Long id_agente_origen) {
        this.id_agente_origen = id_agente_origen;
    }

    public Long getId_agente_destino() {
        return id_agente_destino;
    }

    public void setId_agente_destino(Long id_agente_destino) {
        this.id_agente_destino = id_agente_destino;
    }

    public Long getTipo_agente_origen() {
        return tipo_agente_origen;
    }

    public void setTipo_agente_origen(Long tipo_agente_origen) {
        this.tipo_agente_origen = tipo_agente_origen;
    }

    public Long getTipo_agente_destino() {
        return tipo_agente_destino;
    }

    public void setTipo_agente_destino(Long tipo_agente_destino) {
        this.tipo_agente_destino = tipo_agente_destino;
    }

    public Long getBI_id() {
        return BI_id;
    }

    public void setBI_id(Long BI_id) {
        this.BI_id = BI_id;
    }

    public Long getDeuda_cant() {
        return deuda_cant;
    }

    public void setDeuda_cant(Long deuda_cant) {
        this.deuda_cant = deuda_cant;
    }

    public Double getDeuda_monetaria() {
        return deuda_monetaria;
    }

    public void setDeuda_monetaria(Double deuda_monetaria) {
        this.deuda_monetaria = deuda_monetaria;
    }

    public Long getUltima_fecha_actualizacion() {
        return ultima_fecha_actualizacion;
    }

    public void setUltima_fecha_actualizacion(Long ultima_fecha_actualizacion) {
        this.ultima_fecha_actualizacion = ultima_fecha_actualizacion;
    }
}
