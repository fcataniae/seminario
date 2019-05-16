package com.seminario.backend.model.bienes;

import javax.persistence.*;

@Entity
@Cacheable(false)
public class CombinatoriaMovimientos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private TipoMovimiento tipoMovimiento;

    @Column
    private TipoLocal tipoLocalDestino;

    @Column
    private TipoLocal tipoLocalOrigen;
}
