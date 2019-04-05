package com.seminario.services.rest;



import com.seminario.backend.dto.DTOUser;
import com.seminario.backend.model.*;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;
import com.seminario.backend.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.apache.commons.codec.binary.Base64;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

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
    @Autowired
    private EstadoService estadoService;

    private Usuario base64ToUsuario(String auth) {
        String decodedData = new String(Base64.decodeBase64(auth.split(" ")[1]));
        String[] data = decodedData.split(":");
        return usuarioService.getUsuarioByUsuarioYPass(data[0], data[1]);
    }

    private Usuario asignarRolesUsuario(Usuario usuarioActual, Usuario usuario) {
        Usuario usuarioTmp = usuarioService.getUsuarioByNombre(usuario.getNombreUsuario());
        if (permisoService.getAllPermisosWhereUsuario(usuarioActual).
                contains(permisoService.getPermisoByNombre("MODI-USUARIO"))){
            Set<Rol> roles = usuario.getRoles();
            usuarioTmp.setRoles(new HashSet());
            if (roles != null) {
                for (Rol r : roles) {
                    if (rolService.getRolById(r.getId()) != null)
                        usuarioTmp.addRol(r);
                }
            }
        }
        return usuarioTmp;
    }

    private Rol asignarPermisosRol(Usuario usuarioActual, Rol rol) {
        Rol RolesTmp = rolService.getRolByNombre(rol.getNombre());
        if (permisoService.getAllPermisosWhereUsuario(usuarioActual).
                contains(permisoService.getPermisoByNombre("MODI-ROL"))){
            Set<Permiso> permisos = rol.getPermisos();
            RolesTmp.setPermisos(new HashSet());
            if (permisos != null) {
                for (Permiso p : permisos) {
                    if (permisoService.getPermisoById(p.getId()) != null)
                        RolesTmp.addPermiso(p);
                }
            }
        }
        return RolesTmp;
    }

    /**
     * Dar de Alta un nueva Persona.
     *
     * @param    auth            Credenciales de usuario.
     *                           (debe tener los permisos para ejecutar el método).
     * @param    personaNueva    Es la Persona que se va a dar de alta.
     * */
    @RequestMapping(value="/alta-persona", method = RequestMethod.POST)
    public String createPersona(@RequestHeader("Authorization") String auth, @RequestBody Persona personaNueva) {
        Usuario usuarioActual = base64ToUsuario(auth);
        if (permisoService.getAllPermisosWhereUsuario(usuarioActual).
                contains(permisoService.getPermisoByNombre("ALTA-PERSONA"))){
            personaNueva.setId(null);
            personaNueva.setEstado(estadoService.getEstadoByNombre("ACTIVO"));
            if (personaService.createPersona(personaNueva) != null)
                return "Exito!";
        }
        return "Fail!";
    }


    /**
     * Dar de Alta un nuevo Usuario.
     *
     * @param    auth            Credenciales de usuario.
     *                           (debe tener los permisos para ejecutar el método).
     * @param    usuarioNuevo    Es el Usuario que se va a dar de alta.
     * */
    @RequestMapping(value="/{persona-id}/alta-usuario", method = RequestMethod.POST)
    public String createUsuario(@RequestHeader("Authorization") String auth,
                                @RequestBody Usuario usuarioNuevo,
                                @PathVariable("persona-id") Long Id) {
        Usuario usuarioActual = base64ToUsuario(auth);
        if (permisoService.getAllPermisosWhereUsuario(usuarioActual).
                contains(permisoService.getPermisoByNombre("ALTA-USUARIO"))) {

            Set<Rol> roles = usuarioNuevo.getRoles();
            usuarioNuevo.setId(null);
            usuarioNuevo.setRoles(new HashSet());
            usuarioNuevo.setEstado(estadoService.getEstadoByNombre("ACTIVO"));
            Persona persona = personaService.getPersonaById(Id);
            if (persona == null)
                return "Usuario sin Persona Existente!";
            usuarioNuevo.setPersona(persona);
            if(usuarioService.createUsuario(usuarioNuevo) != null) {
                usuarioNuevo.setRoles(roles);
                Usuario usuarioTmp = asignarRolesUsuario(usuarioActual, usuarioNuevo);
                if (usuarioService.updateUsuario(usuarioTmp) != null) {
                    return "Exito!";
                }
            }
        }
        return "Fail";
    }

    /**
     * Dar de Alta un nuevo Rol.
     *
     * @param    auth            Credenciales de usuario.
     *                           (debe tener los permisos para ejecutar el método).
     * @param    rolNuevo        Es el Rol que se va a dar de alta.
     * */
    @RequestMapping(value="/alta-rol", method = RequestMethod.POST)
    public String createRol(@RequestHeader("Authorization") String auth,
                            @RequestBody Rol rolNuevo) {
        Usuario usuarioActual = base64ToUsuario(auth);
        if (permisoService.getAllPermisosWhereUsuario(usuarioActual).
                contains(permisoService.getPermisoByNombre("ALTA-ROL"))) {

            Set<Permiso> permisos = rolNuevo.getPermisos();
            rolNuevo.setId(null);
            rolNuevo.setPermisos(new HashSet());
            rolNuevo.setEstado(estadoService.getEstadoByNombre("ACTIVO"));
            if(rolService.createRol(rolNuevo) != null) {
                rolNuevo.setPermisos(permisos);
                Rol rolTmp = asignarPermisosRol(usuarioActual, rolNuevo);
                if (rolService.updateRol(rolTmp) != null) {
                    return "Exito!";
                }
            }
        }
        return "Fail";
    }

    /**
     * Dar de Alta un nuevo Permiso.
     *
     * @param    auth            Credenciales de usuario.
     *                           (debe tener los permisos para ejecutar el método).
     * @param    permisoNuevo    Es el Permiso que se va a dar de alta.
     * */
    @RequestMapping(value="/alta-permiso", method =  RequestMethod.POST)
    public String createPermiso(@RequestHeader("Authorization") String auth, @RequestBody Permiso permisoNuevo) {
        Usuario usuarioActual = base64ToUsuario(auth);
        if (permisoService.getAllPermisosWhereUsuario(usuarioActual).
                contains(permisoService.getPermisoByNombre("ALTA-PERMISO"))) {
            permisoNuevo.setId(null);
            if (permisoService.createPermiso(permisoNuevo)){
                    return "Exito!";
            }
        }
        return "Fail";
    }

    /**
     * Actualiza:
     *      -   la nombre, apellido, fechaNacimiento, email
     *
     * El resto de los atributos no se permiten cambiar. A excepción del Estado,
     * pero para este atributo se utiliza otro método.
     *
     * @param    auth       Credenciales de usuario.
     *                      (debe tener los permisos para ejecutar el método).
     * @param    persona    Es el Objeto Usuario que se actualizará.
     * */
    @RequestMapping(value = "/update-persona", method = RequestMethod.PUT)
    public String updatePersona(@RequestHeader("Authorization") String auth,
                                @RequestBody Persona persona)
    {
        Usuario usuarioActual = base64ToUsuario(auth);
        if (permisoService.getAllPermisosWhereUsuario(usuarioActual).
                contains(permisoService.getPermisoByNombre("MODI-PERSONA"))) {
            if (persona.getId() == null) return "Fail";
            Persona personaTmp = personaService.getPersonaById(persona.getId());
            personaTmp.setNombre(persona.getNombre());
            personaTmp.setApellido(persona.getApellido());
            personaTmp.setEmail(persona.getEmail());
            personaTmp.setFecha_nacimiento(persona.getFecha_nacimiento());
            if (personaService.updatePersona(personaTmp) != null) {
                return "Exito!";
            }
        }
        return "Fail";
    }

    /**
     * Actualiza:
     *      -   la lista de Roles de un Usuario.
     *      -   la password.
     *
     * El resto de los atributos no se permiten cambiar. A excepción del Estado,
     * pero para este atributo se utiliza otro método.
     *
     * @param    auth       Credenciales de usuario.
     *                      (debe tener los permisos para ejecutar el método).
     * @param    usuario    Es el Objeto Usuario que se actualizará.
     * */
    @RequestMapping(value = "/update-usuario", method = RequestMethod.PUT)
    public String updateUsuario(@RequestHeader("Authorization") String auth,
                                    @RequestBody Usuario usuario)
    {
        Usuario usuarioActual = base64ToUsuario(auth);
        if (permisoService.getAllPermisosWhereUsuario(usuarioActual).
                contains(permisoService.getPermisoByNombre("MODI-USUARIO"))) {
            Usuario usuarioTmp = asignarRolesUsuario(usuarioActual, usuario);
            usuarioTmp.setPassword(usuario.getPassword());
            if (usuarioService.updateUsuario(usuarioTmp) != null) {
                return "Exito!";
            }
        }
        return "Fail";
    }

    /**
     * Actualiza:
     *      -   la lista de Permisos de un Rol.
     *      -   la descripción.
     *
     * El resto de los atributos no se permiten cambiar. A excepción del Estado,
     * pero para este atributo se utiliza otro método.
     *
     * @param    auth       Credenciales de usuario.
     *                      (debe tener los permisos para ejecutar el método).
     * @param    rol        Es el Objeto Rol que se persistirá.
     * */
    @RequestMapping(value = "/update-rol", method = RequestMethod.PUT)
    public String updateRol(@RequestHeader("Authorization") String auth,
                            @RequestBody Rol rol)
    {
        Usuario usuarioActual = base64ToUsuario(auth);
        if (permisoService.getAllPermisosWhereUsuario(usuarioActual).
                contains(permisoService.getPermisoByNombre("MODI-ROL"))) {
            Rol rolTmp = asignarPermisosRol(usuarioActual, rol);
            rolTmp.setDescripcion(rol.getDescripcion());
            if (rolService.updateRol(rolTmp) != null) {
                return "Exito!";
            }
        }
        return "Fail";
    }

    /**
     * Actualiza:
     *      -   descripcion, funcionalidad
     *
     * El resto de los atributos no se permiten cambiar. A excepción del Estado,
     * pero para este atributo se utiliza otro método.
     *
     * @param    auth       Credenciales de usuario.
     *                      (debe tener los permisos para ejecutar el método).
     * @param    permiso    Es el Objeto Usuario que se actualizará.
     * */
    @RequestMapping(value = "/update-permiso", method = RequestMethod.PUT)
    public String updatePermiso(@RequestHeader("Authorization") String auth,
                                @RequestBody Permiso permiso)
    {
        Usuario usuarioActual = base64ToUsuario(auth);
        if (permisoService.getAllPermisosWhereUsuario(usuarioActual).
                contains(permisoService.getPermisoByNombre("MODI-PERMISO"))) {
            if (permiso.getId() == null) return "Fail";
            Permiso permisoTmp = permisoService.getPermisoById(permiso.getId());
            permisoTmp.setDescripcion(permiso.getDescripcion());
            permisoTmp.setFuncionalidad(permiso.getFuncionalidad());
            if (permisoService.updatePermiso(permisoTmp) != null) {
                return "Exito!";
            }
        }
        return "Fail";
    }

    /**
     * Lista todas las personas.
     *
     * @param    auth       Credenciales de usuario.
     *                      (debe tener los permisos para ejecutar el método).
     **/
    @GetMapping("/listar-personas")
    public List<Persona> listPersonas(@RequestHeader("Authorization") String auth){
        Usuario usuarioActual = base64ToUsuario(auth);
        if (permisoService.getAllPermisosWhereUsuario(usuarioActual).
                contains(permisoService.getPermisoByNombre("CONS-PERSONA"))) {
            return personaService.getAllPersonas();
        }
        return null;
    }

    /**
     * Lista todos los usuarios.
     *
     * @param    auth       Credenciales de usuario.
     *                      (debe tener los permisos para ejecutar el método).
     **/
    @GetMapping("/listar-usuarios")
    public List<Usuario> listUsuarios(@RequestHeader("Authorization") String auth){
        Usuario usuarioActual = base64ToUsuario(auth);
        if (permisoService.getAllPermisosWhereUsuario(usuarioActual).
                contains(permisoService.getPermisoByNombre("CONS-USUARIO"))) {
            return usuarioService.getAllUsuarios();
        }
        return null;
    }

    /**
     * Lista todos los roles.
     *
     * @param    auth       Credenciales de usuario.
     *                      (debe tener los permisos para ejecutar el método).
     **/
    @GetMapping("/listar-roles")
    public List<Rol> listRoles(@RequestHeader("Authorization") String auth){
        Usuario usuarioActual = base64ToUsuario(auth);
        if (permisoService.getAllPermisosWhereUsuario(usuarioActual).
                contains(permisoService.getPermisoByNombre("CONS-ROL"))){
            return rolService.getAllRoles();
        }
        return null;
    }

    /**
     * Lista todos los Permisos.
     *
     * @param    auth       Credenciales de usuario.
     *                      (debe tener los permisos para ejecutar el método).
     **/
    @GetMapping("/listar-permisos")
    public List<Permiso> listPermisos(@RequestHeader("Authorization") String auth){
        Usuario usuarioActual = base64ToUsuario(auth);
        if (permisoService.getAllPermisosWhereUsuario(usuarioActual).
                contains(permisoService.getPermisoByNombre("CONS-PERMISO"))) {
            return permisoService.getAllPermisos();
        }
        return null;
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

    @DeleteMapping("/persona/{documento}")
    public void deletePersona(@PathVariable("documento") Long doc){
        personaService.deletePersona(doc);
    }

    @PutMapping("/persona")
    public Persona updatePersona(@RequestBody Persona persona){
        return personaService.updatePersona(persona);
    }

    @GetMapping("/permisos-usuario/{nombreusuario}")
    public List<Permiso> getPermisosByNombre(@PathVariable("nombreusuario") String nombre){
        return permisoService.getAllPermisosWhereUsuario(usuarioService.getUsuarioByNombre(nombre));
    }

    @PostMapping("/usuario")
    public Usuario updateUsuario(@RequestBody Usuario usuario){
        return usuarioService.updateUsuario(usuario);
    }
}