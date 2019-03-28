package com.seminario.backend.dto;

/**
 * User: fcatania
 * Date: 28/3/2019
 * Time: 15:11
 */
public class DTOUser {

    private String nombre;
    private String apellido;
    private String email;
    private String legajo;

    public DTOUser() {
    }

    public String getNombre() {

        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLegajo() {
        return legajo;
    }

    public void setLegajo(String legajo) {
        this.legajo = legajo;
    }

    public DTOUser(String nombre, String apellido, String email, String legajo) {

        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.legajo = legajo;
    }
}