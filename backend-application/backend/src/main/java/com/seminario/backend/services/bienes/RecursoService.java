package com.seminario.backend.services.bienes;

import com.seminario.backend.model.abm.Usuario;
import com.seminario.backend.model.bienes.Recurso;
import com.seminario.backend.repository.abm.PermisoRepository;
import com.seminario.backend.repository.bienes.RecursoRepository;
import com.seminario.backend.services.abm.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
public class RecursoService {

    @Autowired
    RecursoRepository recursoRepository;

    @Autowired
    PermisoRepository permisoRepository;

    public List<Recurso> getRecursos(Usuario usuarioActual) throws CustomException {
        if (null != permisoRepository.findPermisoWhereUsuarioAndPermiso(usuarioActual.getId(),"CONS-RECURSO")) {
            List<Recurso> r = recursoRepository.findAll();

            r.removeIf(r3 -> !r3.getEstadoRecurso().getDescrip().equalsIgnoreCase("LIBRE"));

            return r;

        } else {
            throw new CustomException("No cuenta con los permisos para consultar recursos");
        }
    }

    public Recurso getRecursoById(Usuario usuarioActual, Long id) throws CustomException {
        Recurso r;
        if (null != permisoRepository.findPermisoWhereUsuarioAndPermiso(usuarioActual.getId(), "CONS-RECURSO")) {
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
