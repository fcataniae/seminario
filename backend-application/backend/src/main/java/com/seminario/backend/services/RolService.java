package com.seminario.backend.services;

import com.seminario.backend.model.Estado;
import com.seminario.backend.model.Permiso;
import com.seminario.backend.model.Rol;
import com.seminario.backend.repository.PermisoRepository;
import com.seminario.backend.repository.RolRepository;
import com.seminario.backend.services.interfaces.IRolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@Service
public class RolService implements IRolService {

    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private PermisoRepository permisoRepository;

    @Override
    public List<Rol> getAllRoles() {
        return rolRepository.findAll();
    }

    @Override
    public Rol getRolById(Long id) {
        return rolRepository.findById(id);
    }

    @Override
    public Rol getRolByNombre(String nombre) {
        return rolRepository.findByNombre(nombre);
    }

    @Override
    public Rol createRol(Rol rol) {
        if(rolRepository.findByNombre(rol.getNombre()) == null){
            return rolRepository.save(rol);
        }
        return null;
    }

    @Override
    public Rol updateRol(Rol rol) {
        return rolRepository.save(rol);
    }

    @Override
    public void deleteRolByNombre(String nombre) {
        rolRepository.delete(rolRepository.findByNombre(nombre));
    }
}
