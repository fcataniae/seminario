package com.seminario.backend.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Usuario: Franco
 * Project: backend-application
 * Fecha: 8/6/2019
 **/
public class DataSet{
    private String label;
    private List<String> data;
    private List<String> backgroundColor;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public List<String> getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(List<String> backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public DataSet(){
        this.data = new ArrayList<>();
        this.backgroundColor = new ArrayList<>();
    }
}
