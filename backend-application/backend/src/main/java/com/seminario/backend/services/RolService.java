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
        List<Rol> list = new ArrayList<>();
        for(Rol e : rolRepository.findAll()) {
            list.add(e);
        }
        return list;
    }

    @Override
    public Rol getRolById(Long id) {
        return null;
    }

    @Override
    public boolean addRol(Rol rol, Permiso permiso) {
        Permiso permisoTmp = permisoRepository.findByNombre(permiso.getNombre());
        if (permisoTmp != null) {
            rol.addPermiso(permiso);
            rolRepository.save(rol);
        }
        return false;
    }

    @Override
    public boolean delRol(Rol rol, Permiso permiso) {
        return false;
    }

    @Override
    public boolean cambiarEstado(Rol rol, Estado estado) {
        return false;
    }

    @Override
    public Rol createRol(Rol rol) {
        if(rolRepository.findById(rol.getId()) == null){
            return rolRepository.save(rol);
        }
        return null;
    }

    @Override
    public Rol updateRol(Rol rol) {
        return rolRepository.save(rol);
    }

    @Override
    public void deleteUsuario(Long Id) {
    }
}
