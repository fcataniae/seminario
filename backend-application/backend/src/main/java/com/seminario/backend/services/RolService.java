package com.seminario.backend.services;

import com.seminario.backend.model.Estado;
import com.seminario.backend.model.Permiso;
import com.seminario.backend.model.Rol;
import com.seminario.backend.model.Usuario;
import com.seminario.backend.repository.PermisoRepository;
import com.seminario.backend.repository.PersonaRepository;
import com.seminario.backend.repository.RolRepository;
import com.seminario.backend.repository.EstadoRepository;
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
        if (permisoRepository.findAllPermisosWhereUsuario(usuarioActual.getId()).
                contains(permisoRepository.findByNombre("MODI-ROL"))){
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
        if (permisoRepository.findAllPermisosWhereUsuario(usuarioActual.getId()).
                contains(permisoRepository.findByNombre("CONS-ROL"))){
            return rolRepository.findAll();
        }else {
            throw new CustomException("No cuenta con los permisos para consultar roles!");
        }

    }

    public Rol getRolById(Long id) {
        return rolRepository.findById(id);
    }

    public Rol getRolByNombre(String nombre) {
        return rolRepository.findByNombre(nombre);
    }

    public void create(Usuario usuarioActual, Rol rolNuevo) throws CustomException{
        if (permisoRepository.findAllPermisosWhereUsuario(usuarioActual.getId()).
                contains(permisoRepository.findByNombre("ALTA-ROL"))) {

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
        if (permisoRepository.findAllPermisosWhereUsuario(usuarioActual.getId()).
                contains(permisoRepository.findByNombre("MODI-ROL"))) {
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
        if (permisoRepository.findAllPermisosWhereUsuario(usuarioActual.getId()).
                contains(permisoRepository.findByNombre("BAJA-ROL"))) {
            Rol rol = rolRepository.findByNombre(nombre);
            rolRepository.delete(rol);
        } else {
            throw new CustomException("No cuenta con los permisos para eliminar roles!");
        }
    }
}
