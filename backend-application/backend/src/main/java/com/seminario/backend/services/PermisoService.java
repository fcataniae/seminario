package com.seminario.backend.services;

import com.seminario.backend.model.Estado;
import com.seminario.backend.model.Permiso;
import com.seminario.backend.repository.PermisoRepository;
import com.seminario.backend.services.interfaces.IPermisoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PermisoService implements IPermisoService {

    @Autowired
    private PermisoRepository permisoRepository;


    @Override
    public List<Permiso> getAllPermisos() {
        List<Permiso> list = new ArrayList<>();
        for(Permiso e : permisoRepository.findAll()) {
            list.add(e);
        }
        return list;
    }

    @Override
    public Permiso getPermisoById(Long id) {
        return null;
    }

    @Override
    public boolean cambiarEstado(Estado estado) {
        return false;
    }

    @Override
    public boolean createRol(Permiso rol) {
        return false;
    }

    @Override
    public boolean updateRol(Permiso rol) {
        return false;
    }

    @Override
    public boolean deleteUsuario(Long Id) {
        return false;
    }
}
