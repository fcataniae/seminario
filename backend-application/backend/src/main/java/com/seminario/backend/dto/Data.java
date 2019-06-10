package com.seminario.backend.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Usuario: Franco
 * Project: backend-application
 * Fecha: 8/6/2019
 **/
public class Data{

    private List<String> labels;
    private DataSet dataset;

    public Data(){
        this.labels = new ArrayList<>();
        this.dataset = new DataSet();
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public DataSet getDataset() {
        return dataset;
    }

    public void setDataset(DataSet dataset) {
        this.dataset = dataset;
    }
}
