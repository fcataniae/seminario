package com.seminario.backend.repository.bienes;

import com.seminario.backend.model.bienes.BienIntercambiable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BienIntercambiableRepository  extends JpaRepository<BienIntercambiable, Long> {
    BienIntercambiable findById(Long id);
}
