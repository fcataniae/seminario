package com.seminario.backend.repository.bienes;

import com.seminario.backend.model.bienes.StockBienEnLocal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StockBienEnLocalRepository extends JpaRepository<StockBienEnLocal, Long> {

    @Query(name = "select ?1 from STOCK_BIEN_EN_LOCAL " +
                  "where LOCAL_NRO = ?2 and BI_ID = ?3", nativeQuery = true)
    Long findStockLibre(String TipoStock, Long localNro, Long bienIntercambiableId);

    @Query(name ="update STOCK_BIEN_EN_LOCAL SET ?1 = ?2" +
                 "where LOCAL_NRO = ?3 and BI_ID = ?4", nativeQuery = true)
    Long updateStock(String TipoStock, Long cant, Long localNro, Long bienIntercambiableId);

}
