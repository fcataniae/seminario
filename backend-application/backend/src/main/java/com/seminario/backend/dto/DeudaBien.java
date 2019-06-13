package com.seminario.backend.dto;

public class DeudaBien {
    private Long idBI;
    private String tipoBI;
    private String subtipoBI;
    private String descripcionBI;
    private Double deudaMonetaria;
    private Double deudaBultos;

    public DeudaBien(){

    }

    public Long getIdBI() {
        return idBI;
    }

    public void setIdBI(Long idBI) {
        this.idBI = idBI;
    }

    public String getTipoBI() {
        return tipoBI;
    }

    public void setTipoBI(String tipoBI) {
        this.tipoBI = tipoBI;
    }

    public String getSubtipoBI() {
        return subtipoBI;
    }

    public void setSubtipoBI(String subtipoBI) {
        this.subtipoBI = subtipoBI;
    }

    public String getDescripcionBI() {
        return descripcionBI;
    }

    public void setDescripcionBI(String descripcionBI) {
        this.descripcionBI = descripcionBI;
    }

    public Double getDeudaMonetaria() {
        return deudaMonetaria;
    }

    public void setDeudaMonetaria(Double deudaMonetaria) {
        this.deudaMonetaria = deudaMonetaria;
    }

    public Double getDeudaBultos() {
        return deudaBultos;
    }

    public void setDeudaBultos(Double deudaBultos) {
        this.deudaBultos = deudaBultos;
    }
}
