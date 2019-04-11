package com.seminario.backend.services;

import com.seminario.backend.model.Estado;
import com.seminario.backend.model.Permiso;
import com.seminario.backend.model.Usuario;
import com.seminario.backend.repository.EstadoRepository;
import com.seminario.backend.repository.PermisoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PermisoService {

    @Autowired
    private PermisoRepository permisoRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    public List<Permiso> getAllPermisos() {
        return permisoRepository.findAll();
    }

    public List<Permiso> getAllPermisosWhereUsuario(Usuario usuario){
        return permisoRepository.findAllPermisosWhereUsuario(usuario.getId());
    };

    public Permiso getPermisoByNombre(String nombre) {
        return permisoRepository.findByNombre(nombre);
    }

    public Permiso getPermisoById(Long id) {
        return permisoRepository.findById(id);
    }

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


    public boolean createPermiso(Permiso permiso) {
        if(permisoRepository.findByNombre(permiso.getNombre()) == null){
            permisoRepository.save(permiso);
            return true;
        }
        return false;
    }

    public Permiso updatePermiso(Permiso permiso) {
        Permiso permisoTmp = permisoRepository.findByNombre(permiso.getNombre());
        if(permisoTmp != null){
            permiso.setId(permiso.getId());
            return permisoRepository.save(permiso);
        }
        return null;
    }

    public boolean deletePermiso(Long Id) {
        Permiso permisoTmp = permisoRepository.findById(Id);
        if(permisoTmp != null){
            permisoRepository.delete(permisoTmp);
            return true;
        }
        return false;
    }
}
