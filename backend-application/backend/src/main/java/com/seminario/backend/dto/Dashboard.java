package com.seminario.backend.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Usuario: Franco
 * Project: backend-application
 * Fecha: 8/6/2019
 **/
public class Dashboard implements Serializable {

    private String type;
    private Data data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Dashboard(){
        this.data = new Data();

    }




}
