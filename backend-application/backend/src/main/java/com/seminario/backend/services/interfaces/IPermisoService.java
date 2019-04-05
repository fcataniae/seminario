package com.seminario.backend.services.interfaces;

import com.seminario.backend.model.Estado;
import com.seminario.backend.model.Permiso;
import com.seminario.backend.model.Usuario;

import java.util.List;

public interface IPermisoService {
        List<Permiso> getAllPermisos();
        Permiso getPermisoById(Long id);
        boolean createPermiso(Permiso permiso);
        Permiso updatePermiso(Permiso permiso);
        boolean deletePermiso(Long Id);
        List<Permiso> getAllPermisosWhereUsuario(Usuario usuario);
        Permiso getPermisoByNombre(String nombre);
        boolean cambiarEstado(Permiso permiso, Estado estado);
}
