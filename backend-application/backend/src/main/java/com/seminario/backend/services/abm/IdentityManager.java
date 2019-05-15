package com.seminario.backend.services.abm;

import com.seminario.backend.model.abm.Usuario;
import com.seminario.backend.repository.abm.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * User: fcatania
 * Date: 28/3/2019
 * Time: 13:04
 */
@Service
public class IdentityManager {

    @Autowired
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