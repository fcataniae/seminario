package com.seminario.backend.repository.bienes;

import com.seminario.backend.model.bienes.Local;
import com.seminario.backend.model.bienes.TipoAgente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocalRepository extends JpaRepository<Local, Long> {
    Local findByNro(Long nro);
    Local findByNombre(String nombre);
    List<Local> findAllByTipoAgente(TipoAgente tipoAgente);
}
