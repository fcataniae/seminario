package com.seminario.backend.services;

import com.seminario.backend.model.Estado;
import com.seminario.backend.model.Permiso;
import com.seminario.backend.model.Rol;
import com.seminario.backend.model.Usuario;
import com.seminario.backend.repository.PermisoRepository;
import com.seminario.backend.repository.PersonaRepository;
import com.seminario.backend.repository.RolRepository;
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

    public List<Rol> getAllRoles() {
        return rolRepository.findAll();
    }

    public Rol getRolById(Long id) {
        return rolRepository.findById(id);
    }

    public Rol getRolByNombre(String nombre) {
        return rolRepository.findByNombre(nombre);
    }

    public Rol createRol(Rol rol) {
        if(rolRepository.findByNombre(rol.getNombre()) == null){
            return rolRepository.save(rol);
        }
        return null;
    }

    public Rol updateRol(Rol rol) {
        return rolRepository.save(rol);
    }

    public void deleteRolByNombre(String nombre) {
        rolRepository.delete(rolRepository.findByNombre(nombre));
    }
}
