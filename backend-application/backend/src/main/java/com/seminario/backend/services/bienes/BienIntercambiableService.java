package com.seminario.backend.services.bienes;

import com.seminario.backend.model.abm.Usuario;
import com.seminario.backend.model.bienes.BienIntercambiable;
import com.seminario.backend.repository.abm.PermisoRepository;
import com.seminario.backend.repository.bienes.BienIntercambiableRepository;
import com.seminario.backend.services.abm.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BienIntercambiableService {

    @Autowired
    BienIntercambiableRepository bienIntercambiableRepository;

    @Autowired
    PermisoRepository permisoRepository;

    public List<BienIntercambiable> getBienesIntercambiables(Usuario usuarioActual) throws CustomException {
        if (null != permisoRepository.findPermisoWhereUsuarioAndPermiso(usuarioActual.getId(),"CONS-BI")) {
            return bienIntercambiableRepository.findAll();
        } else {
            throw new CustomException("No cuenta con los permisos para consultar bien intercambiable");
        }
    }

    public BienIntercambiable getBieneIntercambiableById(Usuario usuarioActual, Long id) throws CustomException {
        BienIntercambiable bi = null;
        if (null != permisoRepository.findPermisoWhereUsuarioAndPermiso(usuarioActual.getId(),"CONS-BI")) {
            bi = bienIntercambiableRepository.findById(id);
            if (bi == null) {
                throw new CustomException("Bien intercambiable no existe!");
            }
        } else {
            throw new CustomException("No cuenta con los permisos para consultar bien intercambiable");
        }
        return bi;
    }
}
