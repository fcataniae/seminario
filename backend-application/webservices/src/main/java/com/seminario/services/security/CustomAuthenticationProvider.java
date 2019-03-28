package com.seminario.services.security;

import com.seminario.backend.services.IdentityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.function.BooleanSupplier;

/**
 * User: fcatania
 * Date: 28/3/2019
 * Time: 12:53
 */
@Component("customAuthenticationProvider")
public class CustomAuthenticationProvider implements AuthenticationProvider {



    private IdentityManager identityManager;

    @Autowired
    public CustomAuthenticationProvider(IdentityManager identityManager){
        this.identityManager = identityManager;
    }

    @Override
    public Authentication authenticate(Authentication authentication){

        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        Authentication auth;


        if( identityManager.authenticateUser(name, password)) {
            auth = new UsernamePasswordAuthenticationToken(name, password, new ArrayList<>());

        } else {
            throw new AuthenticationServiceException("Datos Incorrectos");
        }


        return auth;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(
                UsernamePasswordAuthenticationToken.class);
    }
}