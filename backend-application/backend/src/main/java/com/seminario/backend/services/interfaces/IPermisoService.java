package com.seminario.backend.services.interfaces;

import com.seminario.backend.model.Estado;
import com.seminario.backend.model.Permiso;

import java.util.List;

public interface IPermisoService {
        List<Permiso> getAllPermisos();
        Permiso getPermisoById(Long id);
        boolean cambiarEstado(Estado estado);
        boolean createRol(Permiso rol);
        boolean updateRol(Permiso rol);
        boolean deleteUsuario(Long Id);
}
