package com.seminario.backend.services.abm;

import com.seminario.backend.model.abm.Estado;
import com.seminario.backend.model.abm.Permiso;
import com.seminario.backend.model.abm.Usuario;
import com.seminario.backend.repository.abm.EstadoRepository;
import com.seminario.backend.repository.abm.PermisoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PermisoService {

    @Autowired
    private PermisoRepository permisoRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    public List<Permiso> getAll(Usuario usuarioActual) throws CustomException {
        if (null != permisoRepository.findPermisoWhereUsuarioAndPermiso(usuarioActual.getId(),"CONS-PERMISO")) {
            return permisoRepository.findAll();
        }else {
            throw new CustomException("No cuenta con los permisos para consultar permisos!");
        }
    }

    public List<Permiso> getUserAllpermisos(String nombreUsuario) {
            return permisoRepository.findAllPermisosWhereUsuarioYpass(nombreUsuario);
    }

    public List<Permiso> getAllPermisosWhereUsuario(Usuario usuario){
        return permisoRepository.findAllPermisosWhereUsuario(usuario.getId());
    };

    public Permiso getPermisoByNombre(Usuario usuarioActual, String nombre) throws CustomException {
        Permiso permiso;
        if (permisoRepository.findPermisoWhereUsuarioAndPermiso(usuarioActual.getId(),"CONS-PERMISO") != null) {
            permiso = permisoRepository.findByNombre(nombre);
            if( permiso == null) {
                throw new CustomException("Error al consultar permiso");
            }
        } else {
            throw new CustomException("No cuenta con los permisos para consultar permisos!");
        }
        return permiso;
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
        if (null != permisoRepository.findPermisoWhereUsuarioAndPermiso(usuarioActual.getId(),"ALTA-PERMISO")) {
            permisoNuevo.setId(null);
            if (permisoRepository.save(permisoNuevo) == null){
                throw new CustomException("Error! El permiso ya existe!");
            }
        } else {
            throw new CustomException("No cuenta con los permisos para dar de alta permisos!");
        }
    }

    public void update(Usuario usuarioActual, Permiso permiso) throws CustomException {
        if (null != permisoRepository.findPermisoWhereUsuarioAndPermiso(usuarioActual.getId(),"MODI-PERMISO")) {
            if (permiso.getId() == null) throw new CustomException("Id Permiso NO existente!");
            Permiso permisoTmp = permisoRepository.findById(permiso.getId());
            permisoTmp.setDescripcion(permiso.getDescripcion());
            permisoTmp.setFuncionalidad(permiso.getFuncionalidad());
            if (permisoRepository.save(permisoTmp) != null) {
                throw new CustomException("Error al modificar permiso!");
            }
        } else {
            throw new CustomException("No cuenta con los permisos para modificar permisos!");
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

    public void deletePermisoByNombre(Usuario usuarioActual, String permisoNombre) throws CustomException {
        if (null != permisoRepository.findPermisoWhereUsuarioAndPermiso(usuarioActual.getId(),"BAJA-PERMISO")) {
            Permiso permiso = permisoRepository.findByNombre(permisoNombre);
            permisoRepository.delete(permiso);
        } else {
            throw new CustomException("No cuenta con los permisos para eliminar personas!");
        }
    }
}
