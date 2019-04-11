package com.seminario.services.rest;


import com.seminario.backend.model.Usuario;
import com.seminario.backend.repository.UsuarioRepository;
import com.seminario.services.auth.Token;
import com.seminario.services.auth.UserAuth;
import com.seminario.services.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UsuarioRepository users;

    @PostMapping("/login/")
    public Token signin(@RequestBody UserAuth data) {

        try {
            String username = data.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));
            Usuario u = users.findByNombreUsuarioPassword(username,data.getPassword());
            if (u == null) throw new UsernameNotFoundException("Not found");

            String token = jwtTokenProvider.createToken(username, u.rolesToArrayString());

            Token tk = new Token();
            tk.setToken(token);
            tk.setUsername(username);

            return tk;
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }
}