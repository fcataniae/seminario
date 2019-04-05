package com.seminario.backend.services.interfaces;

import com.seminario.backend.model.Estado;
import com.seminario.backend.model.Permiso;
import com.seminario.backend.model.Rol;
import com.seminario.backend.model.Usuario;

import java.util.List;

public interface IRolService {
        List<Rol> getAllRoles();
        Rol getRolById(Long id);
        Rol getRolByNombre(String nombre);
        boolean addRol(Rol rol, Permiso permiso);
        boolean delRol(Rol rol, Permiso permiso);
        boolean cambiarEstado(Rol rol, Estado estado);
        Rol createRol(Rol rol);
        Rol updateRol(Rol rol);
        void deleteUsuario(Long Id);
}
