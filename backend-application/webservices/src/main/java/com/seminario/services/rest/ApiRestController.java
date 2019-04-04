package com.seminario.services.rest;



import com.seminario.backend.dto.DTOUser;
import com.seminario.backend.model.Persona;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import com.seminario.backend.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.google.gson.Gson;

import javax.json.Json;

/**
 * User: fcatania
 * Date: 27/3/2019
 * Time: 08:35
 */

@RestController
@RequestMapping("/service")
@CrossOrigin
@Scope("session")
public class ApiRestController {

    private ApiRestController(){};
    @Autowired
    private PersonaService personaService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private PermisoService permisoService;
    @Autowired
    private RolService rolService;

    @GetMapping("/login")
    public DTOUser login(@RequestHeader("Authorization") String auth) {

        System.out.println(auth);
        DTOUser user = new DTOUser("Franco", "Catania", "cataniafrane@gmail.com", "137854");

        return user;
    }

    @GetMapping("/prueba")
    public String prueba(){
        return "la prueba funciona!";
    }


    @RequestMapping("/alta-persona")
    public String createPersona(@RequestBody Persona persona) {
        Gson gson = new Gson();
        //throw new IllegalArgumentException("The 'name' parameter must not be null or empty");
        if (personaService.createPersona(persona))
            return "Exito!";
        else
            return "Fail";
    }

    @GetMapping("/listar-personas")
    public String listPersonas(){
        Gson gson = new Gson();
        return gson.toJson(personaService.getAllPersonas());
    }

    @GetMapping("/listar-roles")
    public String listRoles(){
        Gson gson = new Gson();
        return gson.toJson(rolService.getAllRoles());
    }

    @GetMapping("/listar-usuarios")
    public String listUsuarios(){
        Gson gson = new Gson();
        return gson.toJson(usuarioService.getAllUsuarios());
    }

    @GetMapping("/listar-permisos")
    public String listPermisos(){
        Gson gson = new Gson();
        return gson.toJson(permisoService.getAllPermisos());
    }

}