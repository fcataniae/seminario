package com.seminario.backend.repository.bienes;

import com.seminario.backend.model.bienes.TipoLocal;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * User: fcatania
 * Date: 21/5/2019
 * Time: 15:25
 */
public interface TipoLocalRepository extends JpaRepository<TipoLocal,Long>{

    TipoLocal findByNombre(String nombre);
}