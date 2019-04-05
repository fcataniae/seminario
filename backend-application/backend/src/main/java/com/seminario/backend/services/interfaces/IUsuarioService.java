package com.seminario.backend.services.interfaces;

import com.seminario.backend.model.Rol;
import com.seminario.backend.model.Usuario;
import com.seminario.backend.model.Estado;

import java.util.List;

public interface IUsuarioService {
        List<Usuario> getAllUsuarios();
        Usuario getUsuarioById(Long id);
        Usuario createUsuario(Usuario usuario);
        Usuario updateUsuario(Usuario usuario);
        boolean deleteUsuario(Long Id);
        Usuario getUsuarioByNombre(String nombre);
        String deleteUsuarioByNombre(String nombre);
        Usuario getUsuarioByUsuarioYPass(String usuario, String pass);
        boolean cambiarEstado(Usuario usuario, Estado estado);
}
