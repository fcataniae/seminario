package com.seminario.backend.services.abm;

import com.seminario.backend.model.abm.Permiso;
import com.seminario.backend.model.abm.Rol;
import com.seminario.backend.model.abm.Usuario;
import com.seminario.backend.repository.abm.PermisoRepository;
import com.seminario.backend.repository.abm.PersonaRepository;
import com.seminario.backend.repository.abm.RolRepository;
import com.seminario.backend.repository.abm.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RolService {

    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private PermisoRepository permisoRepository;
    @Autowired
    private PersonaRepository personaRepository;
    @Autowired
    private EstadoRepository estadoRepository;

    private Rol asignarPermisosRol(Usuario usuarioActual, Rol rol)  {
        Rol RolesTmp = rolRepository.findByNombre(rol.getNombre());
        if (null != permisoRepository.findPermisoWhereUsuarioAndPermiso(usuarioActual.getId(),"MODI-ROL")) {
            Set<Permiso> permisos = rol.getPermisos();
            RolesTmp.setPermisos(new HashSet());
            if (permisos != null) {
                for (Permiso p : permisos) {
                    Permiso pValid = permisoRepository.findById(p.getId());
                    if (pValid != null)
                        RolesTmp.addPermiso(pValid);
                }
            }
        }
        return RolesTmp;
    }

    public List<Rol> getAll(Usuario usuarioActual) throws CustomException {
        if (null != permisoRepository.findPermisoWhereUsuarioAndPermiso(usuarioActual.getId(),"CONS-ROL")) {
            return rolRepository.findAll();
        }else {
            throw new CustomException("No cuenta con los permisos para consultar roles!");
        }

    }

    public Rol getRolById(Long id) {
        return rolRepository.findById(id);
    }

    public Rol getRolByNombre(Usuario usuarioActual, String nombre) throws CustomException {
        Rol rol;
        if (null != permisoRepository.findPermisoWhereUsuarioAndPermiso(usuarioActual.getId(),"CONS-ROL")) {
            rol = rolRepository.findByNombre(nombre);
            if( rol == null) {
                throw new CustomException("Error al consultar rol");
            }
        } else {
            throw new CustomException("No cuenta con los permisos para consultar roles!");
        }
        return rol;
    }

    public void create(Usuario usuarioActual, Rol rolNuevo) throws CustomException{
        if (null != permisoRepository.findPermisoWhereUsuarioAndPermiso(usuarioActual.getId(),"ALTA-ROL")) {

            Set<Permiso> permisos = rolNuevo.getPermisos();
            rolNuevo.setId(null);
            rolNuevo.setPermisos(new HashSet());
            rolNuevo.setEstado(estadoRepository.findByDescrip("ACTIVO"));
            if(rolRepository.save(rolNuevo) != null) {
                rolNuevo.setPermisos(permisos);
                Rol rolTmp = asignarPermisosRol(usuarioActual, rolNuevo);
                if (rolRepository.save(rolTmp) == null) {
                    throw new CustomException("Error al actualizar el rol!");
                }
            } else { throw new CustomException("Error! El Rol ya existe!"); }
        } else { throw new CustomException("No cuenta con los permisos para dar de alta roles!"); }

    }

    public void update(Usuario usuarioActual, Rol rol) throws CustomException{
        if (null != permisoRepository.findPermisoWhereUsuarioAndPermiso(usuarioActual.getId(),"MODI-ROL")) {
            Rol rolTmp = asignarPermisosRol(usuarioActual, rol);
            rolTmp.setDescripcion(rol.getDescripcion());
            if (rolRepository.save(rolTmp) == null) {
                throw new CustomException("Error al modificar roles!");
            }
        } else {
            throw new CustomException("No cuenta con los permisos para modificar roles!");
        }
    }

    public void deleteRolByNombre(Usuario usuarioActual, String nombre) throws CustomException {
        if (null != permisoRepository.findPermisoWhereUsuarioAndPermiso(usuarioActual.getId(),"BAJA-ROL")) {
            Rol rol = rolRepository.findByNombre(nombre);
            rolRepository.delete(rol);
        } else {
            throw new CustomException("No cuenta con los permisos para eliminar roles!");
        }
    }
}
