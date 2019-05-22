package com.seminario.backend.services.abm;


import com.seminario.backend.model.abm.Estado;
import com.seminario.backend.model.abm.Usuario;
import com.seminario.backend.repository.abm.EstadoRepository;
import com.seminario.backend.repository.abm.PermisoRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Service
public class EstadoService {

    @Autowired
    private EstadoRepository estadoRepository;


    @Autowired
    PermisoRepository permisoRepository;

    public Estado getEstadoByNombre(String nombre){
        return estadoRepository.findByDescrip(nombre);
    }

    public List<Estado> getAllEstados(Usuario usuarioActual){
        if (null != permisoRepository.findPermisoWhereUsuarioAndPermiso(usuarioActual.getId(),"CONS-ESTADO")) {
            return estadoRepository.findAll();
        } else {
            throw new CustomException("No cuenta con los permisos para consultar los estados");
        }
    }
}
