package com.seminario.services.auth;

import com.seminario.backend.model.abm.Permiso;

import java.util.ArrayList;
import java.util.List;

public class Token {

    private String username;
    private String token;
    private List<Permiso> permisos;

    public Token() {
        this.permisos = new ArrayList<>();
    }

    public List<Permiso> getPermisos() {
        return permisos;
    }

    public void setPermisos(List<Permiso> permisos) {
        this.permisos = permisos;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}