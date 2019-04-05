package com.seminario.backend.services;

import com.seminario.backend.model.Estado;
import com.seminario.backend.model.Permiso;
import com.seminario.backend.model.Usuario;
import com.seminario.backend.repository.EstadoRepository;
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

    @Autowired
    private EstadoRepository estadoRepository;

    @Override
    public List<Permiso> getAllPermisos() {
        return permisoRepository.findAll();
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
    public boolean cambiarEstado(Permiso permiso, Estado estado) {
        Permiso permisoTmp = permisoRepository.findByNombre(permiso.getNombre());
        Estado estadoTmp = estadoRepository.findById(estado.getId());
        if (permisoTmp != null && estadoTmp != null){
            permisoTmp.setEstado(estado);
            permisoRepository.save(permisoTmp);
            return true;
        }
        return false;
    }


    @Override
    public boolean createPermiso(Permiso permiso) {
        if(permisoRepository.findByNombre(permiso.getNombre()) == null){
            permisoRepository.save(permiso);
            return true;
        }
        return false;
    }

    @Override
    public Permiso updatePermiso(Permiso permiso) {
        Permiso permisoTmp = permisoRepository.findByNombre(permiso.getNombre());
        if(permisoTmp != null){
            permiso.setId(permiso.getId());
            return permisoRepository.save(permiso);
        }
        return null;
    }

    @Override
    public boolean deletePermiso(Long Id) {
        Permiso permisoTmp = permisoRepository.findById(Id);
        if(permisoTmp != null){
            permisoRepository.delete(permisoTmp);
            return true;
        }
        return false;
    }
}
