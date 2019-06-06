package com.seminario.backend.services.abm;

import com.seminario.backend.model.abm.Estado;
import com.seminario.backend.model.abm.Rol;
import com.seminario.backend.model.abm.Usuario;
import com.seminario.backend.model.abm.Persona;
import com.seminario.backend.repository.abm.*;
import com.seminario.backend.repository.bienes.LocalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    @Autowired
    private LocalRepository localRepository;

    private Usuario asignarRolesUsuario(Usuario usuarioActual, Usuario usuario) {
        Usuario usuarioTmp = usuarioRepository.findByNombreUsuario(usuario.getNombreUsuario());
        if (null != permisoRepository.findPermisoWhereUsuarioAndPermiso(usuarioActual.getId(),"MODI-USUARIO")) {
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

     
    public List<Usuario> getAll(Usuario usuarioActual) throws CustomException {
        if (null != permisoRepository.findPermisoWhereUsuarioAndPermiso(usuarioActual.getId(),"CONS-USUARIO")) {
            return usuarioRepository.findAll();
        } else {
            throw new CustomException("No cuenta con los permisos para consultar usuarios!");
        }
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
        if (null != permisoRepository.findPermisoWhereUsuarioAndPermiso(usuarioActual.getId(),"ALTA-USUARIO")) {

            Set<Rol> roles = usuario.getRoles();
            usuario.setId(null);
            usuario.setRoles(new HashSet());
            usuario.setEstado(estadoRepository.findByDescrip("ACTIVO"));
            Long nroDocPersona = usuario.getPersona().getNroDoc();
            if (nroDocPersona == null) {
                throw new CustomException("Persona NO existente!");
            }
            if (localRepository.findByNro(usuario.getLocal().getNro()) == null) {
                throw new CustomException("Local del usuario NO existente!");
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

     
    public void update(Usuario usuarioActual, Usuario usuario) throws CustomException {
        if (null != permisoRepository.findPermisoWhereUsuarioAndPermiso(usuarioActual.getId(),"MODI-USUARIO")) {
            Usuario usuarioTmp = asignarRolesUsuario(usuarioActual, usuario);
            usuarioTmp.setPassword(usuario.getPassword());
            if (usuarioRepository.save(usuarioTmp) == null) {
                throw new CustomException("Error al modificar usuario!");
            }
        } else {
            throw new CustomException("No cuenta con los permisos para modificar usuarios!");
        }
    }

     
    public boolean delete(Long Id) {
        Usuario usuarioTmp = usuarioRepository.findById(Id);
        if (usuarioTmp != null) {
            usuarioRepository.delete(usuarioTmp);
            return true;
        }
        return false;
    }

     
    public Usuario getUsuarioByNombre(Usuario usuarioActual, String nombre) throws CustomException {
        Usuario usuario;
        if (null != permisoRepository.findPermisoWhereUsuarioAndPermiso(usuarioActual.getId(),"CONS-USUARIO")) {
            usuario = usuarioRepository.findByNombreUsuario(nombre);
            if( usuario == null) {
                throw new CustomException("Error al consultar usuario");
            }
        } else {
            throw new CustomException("No cuenta con los permisos para consultar usuarios!");
        }
        return usuario;
    }

    public Usuario getUsuarioByNombre(String nombre) throws CustomException {
        return usuarioRepository.findByNombreUsuario(nombre);
    }

     
    public void deleteUsuarioByNombre(Usuario usuarioActual, String nombre) throws CustomException {
      if (null != permisoRepository.findPermisoWhereUsuarioAndPermiso(usuarioActual.getId(),"BAJA-USUARIO")) {
            Usuario usuarioTmp = usuarioRepository.findByNombreUsuario(nombre);
            usuarioRepository.delete(usuarioTmp);
        } else {
            throw new CustomException("No cuenta con los permisos para eliminar usuarios!");
        }
    }

     
    public Usuario getUsuarioByUsuarioYPass(String usuario, String pass) {
        return usuarioRepository.findByNombreUsuarioPassword(usuario, pass);
    }
}
