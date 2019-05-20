package com.seminario.backend.services.bienes;

import com.seminario.backend.model.abm.Usuario;
import com.seminario.backend.model.bienes.Recurso;
import com.seminario.backend.repository.abm.PermisoRepository;
import com.seminario.backend.repository.bienes.RecursoRepository;
import com.seminario.backend.services.abm.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecursoService {

    @Autowired
    RecursoRepository recursoRepository;

    @Autowired
    PermisoRepository permisoRepository;

    public List<Recurso> getRecursos(Usuario usuarioActual) {
        if (null != permisoRepository.findPermisoWhereUsuarioAndPermiso(usuarioActual.getId(),"CONS-RECURSO")) {
            return recursoRepository.findAll();
        } else {
            throw new CustomException("No cuenta con los permisos para consultar recursos");
        }
    }

    public Recurso getRecursoById(Usuario usuarioActual, Long id) {
        Recurso r;
        if (null != permisoRepository.findPermisoWhereUsuarioAndPermiso(usuarioActual.getId(), "CONS-RECUSO")) {
            r = recursoRepository.findById(id);
            if (r == null) {
                throw new CustomException("El recurso no existe!");
            }
        } else {
            throw new CustomException("No cuenta con los permisos para consultar recursos");
        }
        return r;
    }

}
