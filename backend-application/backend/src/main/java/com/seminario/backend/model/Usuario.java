package com.seminario.backend.model;

import javax.persistence.*;
import java.util.Set;

/**
 * User: fcatania
 * Date: 1/4/2019
 * Time: 09:11
 */

@Entity
@Cacheable(false)
public class Usuario {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nombreUsuario;

    @Column
    private String password;

    @OneToMany
    private Set<Rol> roles;

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}