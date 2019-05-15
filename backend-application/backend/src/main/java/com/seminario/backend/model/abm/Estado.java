package com.seminario.backend.model.abm;

import javax.persistence.*;
import java.io.Serializable;

/**
 * User: mrgrassho
 * Date: 2/4/2019
 * Time: 20:15
 */
@Entity
@Cacheable(false)
public class Estado implements Serializable {
    private static final long serialVersionUID = 2L;
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
