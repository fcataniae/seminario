package com.seminario.backend.model.bienes;

import javax.persistence.*;

@Entity(name = "estadoviaje")
@Cacheable(false)
public class EstadoViaje {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)
    private String descrip;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }
}
