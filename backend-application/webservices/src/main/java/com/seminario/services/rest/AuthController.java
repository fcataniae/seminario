package com.seminario.services.rest;


import com.seminario.backend.model.abm.Usuario;
import com.seminario.backend.repository.abm.UsuarioRepository;
import com.seminario.services.auth.Token;
import com.seminario.services.auth.UserAuth;
import com.seminario.services.auth.cipher.EncryptManager;
import com.seminario.services.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, EncryptManager.encryptWord(data.getPassword())));
            Usuario u = users.findByNombreUsuarioPassword(username, EncryptManager.encryptWord(data.getPassword()));
            if (u == null) throw new UsernameNotFoundException("Not found");

            String token = jwtTokenProvider.createToken(username, u.rolesToArrayString());

            Token tk = new Token();
            tk.setToken(token);
            tk.setUsername(username);
            u.getRoles().forEach( r -> r.getPermisos().forEach(p -> tk.getPermisos().add(p)));

            return tk;
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }
    @PostMapping("logout")
    public void logout(@AuthenticationPrincipal UserDetails userDetails){

    }

}