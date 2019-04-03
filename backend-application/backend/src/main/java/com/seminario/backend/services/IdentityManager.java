package com.seminario.backend.services;

import com.seminario.backend.model.Usuario;
import com.seminario.backend.repository.UsuarioRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import java.util.ArrayList;

/**
 * User: fcatania
 * Date: 28/3/2019
 * Time: 13:04
 */
@Service
public class IdentityManager {


    private UsuarioRepository usuarioRepository;

    @Autowired
    public IdentityManager(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    public boolean authenticateUser(String name, String password) {
        boolean auth = false;
        Usuario user = usuarioRepository.findByNombreUsuario(name);
        if(user != null){
            auth = user.getPassword().equalsIgnoreCase(password); //desencriptar
        }
        return auth;
    }
}