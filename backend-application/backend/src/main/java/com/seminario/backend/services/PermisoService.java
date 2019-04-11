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


    public void create(Usuario usuarioActual, Permiso permisoNuevo) throws CustomException {
        if (permisoRepository.findAllPermisosWhereUsuario(usuarioActual.getId()).
                contains(permisoRepository.findByNombre("ALTA-PERMISO"))) {
            permisoNuevo.setId(null);
            if (permisoRepository.save(permisoNuevo) == null){
                throw new CustomException("Error! El permiso ya existe!");
            }
        } else {
            throw new CustomException("No cuenta con los permisos para dar de alta permisos!");
        }
    }

    public void update(Usuario usuarioActual, Permiso permiso) throws CustomException {
        if (permisoRepository.findAllPermisosWhereUsuario(usuarioActual.getId()).
                contains(permisoRepository.findByNombre("MODI-PERMISO"))) {
            if (permiso.getId() == null) throw new RuntimeException("Id Permiso NO existente!");
            Permiso permisoTmp = permisoRepository.findById(permiso.getId());
            permisoTmp.setDescripcion(permiso.getDescripcion());
            permisoTmp.setFuncionalidad(permiso.getFuncionalidad());
            if (permisoRepository.save(permisoTmp) != null) {
                throw new RuntimeException("Error al modificar permiso!");
            }
        } else {
            throw new RuntimeException("No cuenta con los permisos para modificar permisos!");
        }
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
