package com.seminario.backend.services;

import com.seminario.backend.model.Estado;
import com.seminario.backend.repository.EstadoRepository;
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
