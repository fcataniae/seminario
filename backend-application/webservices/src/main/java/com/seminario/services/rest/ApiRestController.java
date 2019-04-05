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

    /**
     * Dar de Alta un nueva Persona.
     *
     * @param    usuarioActual   Es el Usuario que quiere dar de alta una nueva persona
     *                           (debe tener los permisos para ejecutar el método).
     * @param    personaNueva    Es la Persona que se va a dar de alta.
     * */
    @RequestMapping("/alta-persona")
    public String createPersona(@RequestBody Usuario usuarioActual, Persona personaNueva) {
        if (permisoService.getAllPermisosWhereUsuario(usuarioActual).
                contains(permisoService.getPermisoByNombre("ALTA-PERSONA"))){
            if (personaService.createPersona(personaNueva) != null)
                return "Exito!";
        }
        return "Fail";
    }


    /**
     * Dar de Alta un nuevo Usuario.
     *
     * @param    usuarioActual   Es el Usuario que quiere dar de alta un nuevo usuario
     *                           (debe tener los permisos para ejecutar el método).
     * @param    usuarioNuevo    Es el Usuario que se va a dar de alta.
     * */
    @RequestMapping("/alta-usuario")
    public String createUsuario(@RequestBody Usuario usuarioActual, Usuario usuarioNuevo) {
        if (personaService.getPersonaById(usuarioNuevo.getPersona().getId()) != null) {
            if (permisoService.getAllPermisosWhereUsuario(usuarioActual).
                    contains(permisoService.getPermisoByNombre("ALTA-USUARIO"))) {
                if (usuarioService.createUsuario(usuarioNuevo) != null)
                    return "Exito!";
            }
        }
        return "Fail";

    }

    /**
     * Dar de Alta un nuevo Rol.
     *
     * @param    usuarioActual   Es el Usuario que quiere dar de alta un nuevo usuario
     *                           (debe tener los permisos para ejecutar el método).
     * @param    rolNuevo        Es el Rol que se va a dar de alta.
     * */
    @RequestMapping("/alta-rol")
    public String createRol(@RequestBody Usuario usuarioActual, Rol rolNuevo) {
        if (rolService.getRolById(rolNuevo.getId()) == null) {
            if (permisoService.getAllPermisosWhereUsuario(usuarioActual).
                    contains(permisoService.getPermisoByNombre("ALTA-ROL"))) {
                if (rolService.createRol(rolNuevo) != null)
                    return "Exito!";
            }
        }
        return "Fail";

    }

    /**
     * Dar de Alta un nuevo Permiso.
     *
     * @param    usuarioActual   Es el Usuario que quiere dar de alta un nuevo usuario
     *                           (debe tener los permisos para ejecutar el método).
     * @param    permisoNuevo    Es el Permiso que se va a dar de alta.
     * */
    @RequestMapping("/alta-usuario")
    public String createPermiso(@RequestBody Usuario usuarioActual, Permiso permisoNuevo) {
        if (permisoService.getPermisoById(permisoNuevo.getId()) == null) {
            if (permisoService.getAllPermisosWhereUsuario(usuarioActual).
                    contains(permisoService.getPermisoByNombre("ALTA-PERMISO"))) {
                if (permisoService.createPermiso(permisoNuevo))
                    return "Exito!";
            }
        }
        return "Fail";

    }

    /**
     * Asginar un Rol a un Usuario.
     *
     * @param    usuarioActual  Es el Usuario que quiere dar de alta un nuevo usuario
     *                          (debe tener los permisos para ejecutar el método).
     * @param    IdUsuario      Es el ID del Usuario al cual se le asignará el ROL.
     * @param    IdRol          El Rol que se va a asignar
     * */
    @GetMapping("asignar-rol/{id-usuario}/{id-rol}")
    public String asignarRolUsuario(@PathVariable("id-usuario") Long IdUsuario,
                                    @PathVariable("id-rol") Long IdRol,
                                    @RequestBody Usuario usuarioActual)
    {
        Usuario u = usuarioService.getUsuarioById(IdUsuario);
        Rol r = rolService.getRolById(IdRol);
        if ((u != null) && ( r != null)) {
            if (permisoService.getAllPermisosWhereUsuario(usuarioActual).
                    contains(permisoService.getPermisoByNombre("ASIGNAR-ROL"))) {
                u.addRol(r);
                if (usuarioService.updateUsuario(u) != null) return "Exito!";
            }
        }
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

    @GetMapping("/permisos-usuario/{nombreusuario}")
    public List<Permiso> getPermisosByNombre(@PathVariable("nombreusuario") String nombre){
        return permisoService.getAllPermisosWhereUsuario(usuarioService.getUsuarioByNombre(nombre));
    }

}