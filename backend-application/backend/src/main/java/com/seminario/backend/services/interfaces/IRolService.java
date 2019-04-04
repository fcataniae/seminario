package com.seminario.backend.services.interfaces;

import com.seminario.backend.model.Estado;
import com.seminario.backend.model.Permiso;
import com.seminario.backend.model.Rol;
import com.seminario.backend.model.Usuario;

import java.util.List;

public interface IRolService {
        List<Rol> getAllRoles();
        Rol getRolById(Long id);
        boolean addPermiso(Rol rol, Permiso permiso);
        boolean delRol(Rol rol, Permiso permiso);
        boolean cambiarEstado(Rol rol, Estado estado);
        boolean createRol(Rol rol);
        boolean updateRol(Rol rol);
        boolean deleteUsuario(Long Id);
}
