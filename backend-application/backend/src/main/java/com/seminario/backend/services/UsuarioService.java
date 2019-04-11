package com.seminario.backend.services;

import com.seminario.backend.model.Estado;
import com.seminario.backend.model.Rol;
import com.seminario.backend.model.Usuario;
import com.seminario.backend.model.Persona;
import com.seminario.backend.repository.*;
import com.seminario.backend.services.interfaces.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @Autowired
    private PermisoRepository permisosRepository;

    private Usuario asignarRolesUsuario(Usuario usuarioActual, Usuario usuario) {
        Usuario usuarioTmp = usuarioRepository.findByNombreUsuario(usuario.getNombreUsuario());
        if (permisosRepository.findAllPermisosWhereUsuario(usuarioActual.getId()).
                contains(permisosRepository.findByNombre("MODI-USUARIO"))){
            Set<Rol> roles = usuario.getRoles();
            usuarioTmp.setRoles(new HashSet());
            if (roles != null) {
                for (Rol r : roles) {
                    Rol rValid = rolRepository.findById(r.getId());
                    if (rValid != null)
                        usuarioTmp.addRol(rValid);
                }
            }
        }
        return usuarioTmp;
    }

     
    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

     
    public Usuario getUsuarioById(Long id) {
        return usuarioRepository.findById(id);
    }

     
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

     
    public Usuario createUsuario(Usuario usuario) {
        Usuario usuarioTmp = usuarioRepository.findByNombreUsuario(usuario.getNombreUsuario());
        if (usuarioTmp == null){
            return usuarioRepository.save(usuario);
        }
        return null;
    }

     
    public Usuario updateUsuario(Usuario usuario) {
        Usuario usuarioTmp = usuarioRepository.findByNombreUsuario(usuario.getNombreUsuario());
        if (usuarioTmp != null) {
            usuario.setId(usuarioTmp.getId());
            return usuarioRepository.save(usuario);
        }
        return null;
    }

     
    public boolean deleteUsuario(Long Id) {
        Usuario usuarioTmp = usuarioRepository.findById(Id);
        if (usuarioTmp != null) {
            usuarioRepository.delete(usuarioTmp);
            return true;
        }
        return false;
    }

     
    public Usuario getUsuarioByNombre(String nombre) {
        return usuarioRepository.findByNombreUsuario(nombre);
    }

     
    public String deleteUsuarioByNombre(String nombre) {
        Usuario usuarioTmp = usuarioRepository.findByNombreUsuario(nombre);
        if(usuarioTmp != null){
            usuarioRepository.delete(usuarioTmp);
            return "Se elimino el usuario correctamente";
        }
        return "Error al eliminar el usuario";
    }

     
    public Usuario getUsuarioByUsuarioYPass(String usuario, String pass) {
        return usuarioRepository.findByNombreUsuarioPassword(usuario, pass);
    }
}
