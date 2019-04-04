package com.seminario.services.rest;



import com.seminario.backend.dto.DTOUser;
import com.seminario.backend.model.Permiso;
import com.seminario.backend.model.Persona;
import com.seminario.backend.model.Rol;
import com.seminario.backend.model.Usuario;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import com.seminario.backend.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

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
        if (personaService.createPersona(persona))
            return "Exito!";
        else
            return "Fail";
    }

    @RequestMapping("/alta-usuario")
    public String createUsuario(@RequestBody Usuario usuario) {
        if (usuarioService.createUsuario(usuario))
            return "Exito!";
        else
            return "Fail";
    }


    @GetMapping("/listar-personas")
    public List<Persona> listPersonas(){
        return personaService.getAllPersonas();
    }

    @GetMapping("/listar-roles")
    public List<Rol> listRoles(){
        return rolService.getAllRoles();
    }

    @GetMapping("/listar-usuarios")
    public List<Usuario> listUsuarios(){
        return usuarioService.getAllUsuarios();
    }

    @GetMapping("/listar-permisos")
    public List<Permiso> listPermisos(){
        return permisoService.getAllPermisos();
    }

    @GetMapping("/usuario/{nombreusuario}")
    public Usuario getUsuarioByNombre(@PathVariable("nombreusuario") String nombre){
        return usuarioService.getUsuarioByNombre(nombre);
    }
    @DeleteMapping("/usuario/{nombreusuario}")
    public String deleteUsuarioByNombre(@PathVariable("nombreusuario") String nombre){
        return usuarioService.deleteUsuarioByNombre(nombre);
    }
    @GetMapping("/persona/{documento}")
    public Persona getPersonaByDocumento(@PathVariable("documento") Long doc){
        return personaService.getPersonaByDocumento(doc);
    }
    @DeleteMapping("/persona")
    public Persona deletePersona(@RequestBody Persona persona){
        return personaService.deletePersona(persona);
    }
    @PutMapping("/persona")
    public Persona updatePersona(@RequestBody Persona persona){
        return personaService.updatePersona(persona);
    }
}