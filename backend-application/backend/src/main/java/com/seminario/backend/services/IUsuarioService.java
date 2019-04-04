package com.seminario.backend.services;

import com.seminario.backend.model.Rol;
import com.seminario.backend.model.Usuario;
import com.seminario.backend.model.Estado;

import java.util.List;

public interface IUsuarioService {
        List<Usuario> getAllUsuarios();
        Usuario getUsuarioById(Long id);
        boolean addRol(Usuario usuario, Rol rol);
        boolean delRol(Usuario usuario, Rol rol);
        boolean cambiarEstado(Usuario usuario, Estado estado);
        boolean createUsuario(Usuario usuario);
        boolean updateUsuario(Usuario usuario);
        boolean deleteUsuario(Long Id);

    Usuario getUsuarioByNombre(String nombre);

        String deleteUsuarioByNombre(String nombre);
}
