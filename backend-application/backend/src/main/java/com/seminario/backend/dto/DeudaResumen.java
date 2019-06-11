package com.seminario.backend.dto;

public class DeudaResumen {
    private Long proveedorNro;
    private String proveedorNombre;
    private Double deuda;

    public Long getProveedorNro() {
        return proveedorNro;
    }

    public void setProveedorNro(Long proveedorNro) {
        this.proveedorNro = proveedorNro;
    }

    public Double getDeuda() {
        return deuda;
    }

    public void setDeuda(Double deuda) {
        this.deuda = deuda;
    }

    public String getProveedorNombre() {
        return proveedorNombre;
    }

    public void setProveedorNombre(String proveedorNombre) {
        this.proveedorNombre = proveedorNombre;
    }
}
