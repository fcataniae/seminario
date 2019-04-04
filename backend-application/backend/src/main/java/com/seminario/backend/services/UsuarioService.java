package com.seminario.backend.services;

import com.seminario.backend.model.Estado;
import com.seminario.backend.model.Rol;
import com.seminario.backend.model.Usuario;
import com.seminario.backend.model.Persona;
import com.seminario.backend.repository.UsuarioRepository;
import com.seminario.backend.repository.RolRepository;
import com.seminario.backend.repository.PersonaRepository;
import com.seminario.backend.repository.EstadoRepository;
import com.seminario.backend.services.interfaces.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService implements IUsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PersonaRepository personaRepository;
    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private EstadoRepository estadoRespository;

    @Override
    public List<Usuario> getAllUsuarios() {
        List<Usuario> list = new ArrayList<>();
        for(Usuario e : usuarioRepository.findAll()) {
            list.add(e);
        }
        return list;
    }

    @Override
    public Usuario getUsuarioById(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public boolean addRol(Usuario usuario, Rol rol) {
        Rol rolTmp = rolRepository.findByNombre(rol.getNombre());
        if (rolTmp != null) {
            usuario.addRol(rol);
            usuarioRepository.save(usuario);
        }
        return false;
    }

    @Override
    public boolean delRol(Usuario usuario, Rol rol) {
        return false;
    }

    @Override
    public boolean cambiarEstado(Usuario usuario, Estado estado) {
        Usuario usuarioTmp = usuarioRepository.findByNombreUsuario(usuario.getNombreUsuario());
        Estado estadoTmp = estadoRespository.findById(estado.getId());
        if (usuarioTmp != null && estadoTmp != null){
            usuarioTmp.setEstado(estado);
            usuarioRepository.save(usuarioTmp);
            return true;
        }
        return false;
    }

    @Override
    public boolean createUsuario(Usuario usuario) {
        Persona persona = personaRepository.findById(usuario.getPersona().getId());
        Usuario usuarioTmp = usuarioRepository.findByNombreUsuario(usuario.getNombreUsuario());
        if (persona != null && usuarioTmp == null){
            usuarioRepository.save(usuario);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateUsuario(Usuario usuario) {
        Usuario usuarioTmp = usuarioRepository.findByNombreUsuario(usuario.getNombreUsuario());
        if (usuarioTmp != null) {
            usuarioRepository.save(usuario);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteUsuario(Long Id) {
        Usuario usuarioTmp = usuarioRepository.findById(Id);
        if (usuarioTmp != null) {
            usuarioRepository.delete(getUsuarioById(Id));
            return true;
        }
        return false;
    }

    @Override
    public Usuario getUsuarioByNombre(String nombre) {
        return usuarioRepository.findByNombreUsuario(nombre);
    }

    @Override
    public String deleteUsuarioByNombre(String nombre) {
        Usuario usuarioTmp = usuarioRepository.findByNombreUsuario(nombre);
        if(usuarioTmp != null){
            usuarioRepository.delete(usuarioTmp);
            return "Se elimino el usuario correctamente";
        }
        return "Error al eliminar el usuario";
    }
}
