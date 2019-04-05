package com.seminario.backend.services;

import com.seminario.backend.model.Estado;
import com.seminario.backend.repository.EstadoRepository;
import com.seminario.backend.services.interfaces.IEstadoService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class EstadoService implements IEstadoService {

    @Autowired
    private EstadoRepository estadoRepository;

    @Override
    public Estado getEstadoByNombre(String nombre){
        return estadoRepository.findByDescrip(nombre);
    }
}
