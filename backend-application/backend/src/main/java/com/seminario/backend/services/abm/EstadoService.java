package com.seminario.backend.services.abm;

import com.seminario.backend.model.abm.Estado;
import com.seminario.backend.repository.abm.EstadoRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class EstadoService {

    @Autowired
    private EstadoRepository estadoRepository;

    public Estado getEstadoByNombre(String nombre){
        return estadoRepository.findByDescrip(nombre);
    }


}
