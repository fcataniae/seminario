package com.seminario.backend.repository.bienes;

import com.seminario.backend.model.bienes.Local;
import com.seminario.backend.model.bienes.TipoLocal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocalRepository extends JpaRepository<Local, Long> {
    Local findById(Long Id);
    Local findByNombre(String nombre);
    List<Local> findAllWhereTipo(TipoLocal tipoLocal);
}
