package com.seminario.backend.services.bienes;

import com.seminario.backend.model.bienes.Local;
import com.seminario.backend.model.bienes.BienIntercambiable;
import com.seminario.backend.repository.bienes.StockBienEnLocalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Usuario: Franco
 * Project: backend-application
 * Fecha: 5/15/2019
 **/
@Service
public class StockBienEnLocalService {

    @Autowired
    StockBienEnLocalRepository stockBienEnLocalRepository;

    private void actualizarStock(Long localNro, Long biId, Long cantNueva, String tipoStock, boolean resta){

        Long cantActual = stockBienEnLocalRepository.findStockLibre(tipoStock,  localNro, biId);
        if (resta) {
            cantActual -= cantNueva;
            cantActual = (cantActual < 0) ? 0 : cantActual;
        } else {
            cantActual += cantNueva;
        }
        stockBienEnLocalRepository.updateStock(tipoStock, cantActual, localNro, biId);
    }

    public void aumentarStockDestruido(Long localNro, Long biId,  Long cant){
        this.actualizarStock(localNro, biId, cant, "STOCK_DESTRUIDO", false);
    }

    public void aumentarStockLibre(Long localNro, Long biId,  Long cant){
        this.actualizarStock(localNro, biId, cant, "STOCK_LIBRE", false );
    }

    public void aumentarStockOcupado(Long localNro, Long biId,  Long cant){
        this.actualizarStock(localNro, biId, cant, "STOCK_OCUPADO", false);
    }

    public void aumentarStockReservado(Long localNro, Long biId,  Long cant){
        this.actualizarStock(localNro, biId, cant, "STOCK_RESERVADO", false);
    }

    public void restarStockDestruido(Long localNro, Long biId,  Long cant){
        this.actualizarStock(localNro, biId, cant, "STOCK_DESTRUIDO", true);
    }

    public void restarStockLibre(Long localNro, Long biId,  Long cant){
        this.actualizarStock(localNro, biId, cant, "STOCK_LIBRE", true );
    }

    public void restarStockOcupado(Long localNro, Long biId,  Long cant){
        this.actualizarStock(localNro, biId, cant, "STOCK_OCUPADO", true);
    }

    public void restarStockReservado(Long localNro, Long biId,  Long cant){
        this.actualizarStock(localNro, biId, cant, "STOCK_RESERVADO", true);
    }





}
