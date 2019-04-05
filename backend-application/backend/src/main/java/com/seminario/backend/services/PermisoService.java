package com.seminario.backend.services;

import com.seminario.backend.model.Estado;
import com.seminario.backend.model.Permiso;
import com.seminario.backend.model.Usuario;
import com.seminario.backend.repository.PermisoRepository;
import com.seminario.backend.services.interfaces.IPermisoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PermisoService implements IPermisoService {

    @Autowired
    private PermisoRepository permisoRepository;


    @Override
    public List<Permiso> getAllPermisos() {
        List<Permiso> list = new ArrayList<>();
        for(Permiso e : permisoRepository.findAll()) {
            list.add(e);
        }
        return list;
    }

    @Override
    public List<Permiso> getAllPermisosWhereUsuario(Usuario usuario){
        return permisoRepository.findAllPermisosWhereUsuario(usuario.getId());
    };

    @Override
    public Permiso getPermisoByNombre(String nombre) {
        return permisoRepository.findByNombre(nombre);
    }

    @Override
    public Permiso getPermisoById(Long id) {
        return permisoRepository.findById(id);
    }

    @Override
    public boolean cambiarEstado(Estado estado) {
        return false;
    }

    @Override
    public boolean createPermiso(Permiso permiso) {
        if(permisoRepository.findById(permiso.getId()) == null){
            permisoRepository.save(permiso);
            return true;
        }
        return false;
    }

    @Override
    public Permiso updatePermiso(Permiso permiso) {
        return permisoRepository.save(permiso);
    }

    @Override
    public boolean deletePermiso(Long Id) {
        return false;
    }
}
