package com.seminario.backend.services;

import com.seminario.backend.model.Estado;
import com.seminario.backend.model.Rol;
import com.seminario.backend.model.Usuario;
import com.seminario.backend.model.Persona;
import com.seminario.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UsuarioService{

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PersonaRepository personaRepository;
    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private EstadoRepository estadoRepository;
    @Autowired
    private PermisoRepository permisoRepository;

    private Usuario asignarRolesUsuario(Usuario usuarioActual, Usuario usuario) {
        Usuario usuarioTmp = usuarioRepository.findByNombreUsuario(usuario.getNombreUsuario());
        if (permisoRepository.findAllPermisosWhereUsuario(usuarioActual.getId()).
                contains(permisoRepository.findByNombre("MODI-USUARIO"))){
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
        Estado estadoTmp = estadoRepository.findById(estado.getId());
        if (usuarioTmp != null && estadoTmp != null){
            usuarioTmp.setEstado(estado);
            usuarioRepository.save(usuarioTmp);
            return true;
        }
        return false;
    }

     
    public void create(Usuario usuarioActual, Usuario usuario) throws CustomException {
        if (permisoRepository.findAllPermisosWhereUsuario(usuarioActual.getId()).
                contains(permisoRepository.findByNombre("ALTA-USUARIO"))) {

            Set<Rol> roles = usuario.getRoles();
            usuario.setId(null);
            usuario.setRoles(new HashSet());
            usuario.setEstado(estadoRepository.findByDescrip("ACTIVO"));
            Long nroDocPersona = usuario.getPersona().getNroDoc();
            if (nroDocPersona == null) {
                throw new CustomException("Persona NO existente!");
            }
            Persona persona = personaRepository.findByNroDoc(nroDocPersona);
            if (persona == null)
                throw new CustomException("Persona NO existente!");
            usuario.setPersona(persona);
            if(usuarioRepository.save(usuario) != null) {
                usuario.setRoles(roles);
                Usuario usuarioTmp = asignarRolesUsuario(usuarioActual, usuario);
                if (usuarioRepository.save(usuarioTmp) == null) {
                    throw new CustomException("Error al dar de alta roles!");
                }
            } else { throw new CustomException("Error! El Usuario ya existe!"); }
        } else { throw new CustomException("No cuenta con permisos para dar de alta usuarios!");}
    }

     
    public void update(Usuario usuarioActual, Usuario usuario) {
        if (permisoRepository.findAllPermisosWhereUsuario(usuarioActual.getId()).
                contains(permisoRepository.findByNombre("MODI-USUARIO"))) {
            Usuario usuarioTmp = asignarRolesUsuario(usuarioActual, usuario);
            usuarioTmp.setPassword(usuario.getPassword());
            if (usuarioRepository.save(usuarioTmp) == null) {
                throw new RuntimeException("Error al modificar usuario!");
            }
        } else {
            throw new RuntimeException("No cuenta con los permisos para modificar usuarios!");
        }
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
