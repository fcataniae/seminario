package com.seminario.services.auth.service;


import com.seminario.backend.model.abm.Usuario;
import com.seminario.backend.services.abm.CustomException;
import com.seminario.backend.services.abm.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthService implements UserDetailsService{

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Usuario u = null;
        try {
            u = usuarioService.getUsuarioByNombre(username);
        } catch (CustomException e) {
            e.printStackTrace();
        }

        if(u == null){
            throw new UsernameNotFoundException("Not valid user!");
        }

        return new User(u.getNombreUsuario(),u.getPassword(), Collections.emptyList());

    }
}