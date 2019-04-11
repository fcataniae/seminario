package com.seminario.services.rest;

import com.seminario.backend.model.*;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.seminario.backend.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 * UserAuth: fcatania
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


    private Usuario asignarRolesUsuario(Usuario usuarioActual, Usuario usuario) {
        Usuario usuarioTmp = usuarioService.getUsuarioByNombre(usuario.getNombreUsuario());
        if (permisoService.getAllPermisosWhereUsuario(usuarioActual).
                contains(permisoService.getPermisoByNombre("MODI-USUARIO"))){
            Set<Rol> roles = usuario.getRoles();
            usuarioTmp.setRoles(new HashSet());
            if (roles != null) {
                for (Rol r : roles) {
                    Rol rValid = rolService.getRolById(r.getId());
                    if (rValid != null)
                        usuarioTmp.addRol(rValid);
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
                    Permiso pValid = permisoService.getPermisoById(p.getId());
                    if (pValid != null)
                        RolesTmp.addPermiso(pValid);
                }
            }
        }
        return RolesTmp;
    }
    /**
     * Dar de Alta un nueva Persona.
     *
     * @param    userDetails            Credenciales de usuario.
     *                           (debe tener los permisos para ejecutar el método).
     * @param    personaNueva    Es la Persona que se va a dar de alta.
     * */
    @RequestMapping(value="/alta-persona", method = RequestMethod.POST)
    public void createPersona(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Persona personaNueva) {
        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());
        if (permisoService.getAllPermisosWhereUsuario(usuarioActual).
                contains(permisoService.getPermisoByNombre("ALTA-PERSONA"))){
            personaNueva.setId(null);
            personaNueva.setEstado(estadoService.getEstadoByNombre("ACTIVO"));
            if (personaService.createPersona(personaNueva) == null)
                throw new RuntimeException("Error al dar de alta la persona!");
        } else {
            throw new RuntimeException("No cuenta con permisos para dar de alta personas!");
        }
    }


    /**
     * Dar de Alta un nuevo Usuario.
     *
     * @param    userDetails            Credenciales de usuario.
     *                           (debe tener los permisos para ejecutar el método).
     * @param    usuario    Es el Usuario que se va a dar de alta.
     * */
    @RequestMapping(value="/alta-usuario", method = RequestMethod.POST)
    public void createUsuario(@AuthenticationPrincipal UserDetails userDetails,
                                @RequestBody Usuario usuario) {
        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());
        System.out.println(usuario);
        if (permisoService.getAllPermisosWhereUsuario(usuarioActual).
                contains(permisoService.getPermisoByNombre("ALTA-USUARIO"))) {

            Set<Rol> roles = usuario.getRoles();
            usuario.setId(null);
            usuario.setRoles(new HashSet());
            usuario.setEstado(estadoService.getEstadoByNombre("ACTIVO"));
            Long nroDocPersona = usuario.getPersona().getNroDoc();
            if (nroDocPersona == null) {
                throw new RuntimeException("Persona NO existente!");
            }
            Persona persona = personaService.getPersonaByDocumento(nroDocPersona);
            if (persona == null)
                throw new RuntimeException("Persona NO existente!");
            usuario.setPersona(persona);
            if(usuarioService.createUsuario(usuario) != null) {
                usuario.setRoles(roles);
                Usuario usuarioTmp = asignarRolesUsuario(usuarioActual, usuario);
                if (usuarioService.updateUsuario(usuarioTmp) == null) {
                    throw new RuntimeException("Error al dar de alta roles!");
                }
            } else { throw new RuntimeException("Error! El Usuario ya existe!"); }
        } else { throw new RuntimeException("No cuenta con permisos para dar de alta usuarios!");}
    }

    /**
     * Dar de Alta un nuevo Rol.
     *
     * @param    userDetails            Credenciales de usuario.
     *                           (debe tener los permisos para ejecutar el método).
     * @param    rolNuevo        Es el Rol que se va a dar de alta.
     * */
    @RequestMapping(value="/alta-rol", method = RequestMethod.POST)
    public void createRol(@AuthenticationPrincipal UserDetails userDetails,
                            @RequestBody Rol rolNuevo) {
        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());
        if (permisoService.getAllPermisosWhereUsuario(usuarioActual).
                contains(permisoService.getPermisoByNombre("ALTA-ROL"))) {

            Set<Permiso> permisos = rolNuevo.getPermisos();
            rolNuevo.setId(null);
            rolNuevo.setPermisos(new HashSet());
            rolNuevo.setEstado(estadoService.getEstadoByNombre("ACTIVO"));
            if(rolService.createRol(rolNuevo) != null) {
                rolNuevo.setPermisos(permisos);
                Rol rolTmp = asignarPermisosRol(usuarioActual, rolNuevo);
                if (rolService.updateRol(rolTmp) == null) {
                    throw new RuntimeException("Error al actualizar el rol!");
                }
            } else { throw new RuntimeException("Error! El Rol ya existe!"); }
        } else { throw new RuntimeException("No cuenta con los permisos para dar de alta roles!"); }
    }

    /**
     * Dar de Alta un nuevo Permiso.
     *
     * @param    userDetails            Credenciales de usuario.
     *                           (debe tener los permisos para ejecutar el método).
     * @param    permisoNuevo    Es el Permiso que se va a dar de alta.
     * */
    @RequestMapping(value="/alta-permiso", method =  RequestMethod.POST)
    public void createPermiso(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Permiso permisoNuevo) {
        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());
        if (permisoService.getAllPermisosWhereUsuario(usuarioActual).
                contains(permisoService.getPermisoByNombre("ALTA-PERMISO"))) {
            permisoNuevo.setId(null);
            if (!permisoService.createPermiso(permisoNuevo)){
                throw new RuntimeException("Error! El permiso ya existe!");
            }
        } else {
            throw new RuntimeException("No cuenta con los permisos para dar de alta permisos!");
        }
    }

    /**
     * Actualiza:
     *      -   la nombre, apellido, fechaNacimiento, email
     *
     * El resto de los atributos no se permiten cambiar. A excepción del Estado,
     * pero para este atributo se utiliza otro método.
     *
     * @param    userDetails       Credenciales de usuario.
     *                      (debe tener los permisos para ejecutar el método).
     * @param    persona    Es el Objeto Usuario que se actualizará.
     * */
    @RequestMapping(value = "/update-persona", method = RequestMethod.PUT)
    public void updatePersona(@AuthenticationPrincipal UserDetails userDetails,
                                @RequestBody Persona persona)
    {
        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());
        if (permisoService.getAllPermisosWhereUsuario(usuarioActual).
                contains(permisoService.getPermisoByNombre("MODI-PERSONA"))) {
            if (persona.getId() == null) throw new RuntimeException("Id Persona NO existente!");
            Persona personaTmp = personaService.getPersonaById(persona.getId());
            personaTmp.setNombre(persona.getNombre());
            personaTmp.setApellido(persona.getApellido());
            personaTmp.setEmail(persona.getEmail());
            personaTmp.setFecha_nacimiento(persona.getFecha_nacimiento());
            if (personaService.updatePersona(personaTmp) == null) {
                throw new RuntimeException("Error al modificar la persona!");
            }
        } else {
            throw new RuntimeException("No cuenta con los permisos para modificar personas!");
        }
    }

    /**
     * Actualiza:
     *      -   la lista de Roles de un Usuario.
     *      -   la password.
     *
     * El resto de los atributos no se permiten cambiar. A excepción del Estado,
     * pero para este atributo se utiliza otro método.
     *
     * @param    userDetails       Credenciales de usuario.
     *                      (debe tener los permisos para ejecutar el método).
     * @param    usuario    Es el Objeto Usuario que se actualizará.
     * */
    @RequestMapping(value = "/update-usuario", method = RequestMethod.PUT)
    public void updateUsuario(@AuthenticationPrincipal UserDetails userDetails,
                                    @RequestBody Usuario usuario)
    {
        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());
        if (permisoService.getAllPermisosWhereUsuario(usuarioActual).
                contains(permisoService.getPermisoByNombre("MODI-USUARIO"))) {
            Usuario usuarioTmp = asignarRolesUsuario(usuarioActual, usuario);
            usuarioTmp.setPassword(usuario.getPassword());
            if (usuarioService.updateUsuario(usuarioTmp) == null) {
                throw new RuntimeException("Error al modificar usuario!");
            }
        } else {
            throw new RuntimeException("No cuenta con los permisos para modificar usuarios!");
        }
    }

    /**
     * Actualiza:
     *      -   la lista de Permisos de un Rol.
     *      -   la descripción.
     *
     * El resto de los atributos no se permiten cambiar. A excepción del Estado,
     * pero para este atributo se utiliza otro método.
     *
     * @param    userDetails       Credenciales de usuario.
     *                      (debe tener los permisos para ejecutar el método).
     * @param    rol        Es el Objeto Rol que se persistirá.
     * */
    @RequestMapping(value = "/update-rol", method = RequestMethod.PUT)
    public void updateRol(@AuthenticationPrincipal UserDetails userDetails,
                            @RequestBody Rol rol)
    {
        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());
        if (permisoService.getAllPermisosWhereUsuario(usuarioActual).
                contains(permisoService.getPermisoByNombre("MODI-ROL"))) {
            Rol rolTmp = asignarPermisosRol(usuarioActual, rol);
            rolTmp.setDescripcion(rol.getDescripcion());
            if (rolService.updateRol(rolTmp) == null) {
                throw new RuntimeException("Error al modificar roles!");
            }
        } else {
            throw new RuntimeException("No cuenta con los permisos para modificar usuarios!");
        }
    }

    /**
     * Actualiza:
     *      -   descripcion, funcionalidad
     *
     * El resto de los atributos no se permiten cambiar. A excepción del Estado,
     * pero para este atributo se utiliza otro método.
     *
     * @param    userDetails       Credenciales de usuario.
     *                      (debe tener los permisos para ejecutar el método).
     * @param    permiso    Es el Objeto Usuario que se actualizará.
     * */
    @RequestMapping(value = "/update-permiso", method = RequestMethod.PUT)
    public void updatePermiso(@AuthenticationPrincipal UserDetails userDetails,
                                @RequestBody Permiso permiso)
    {
        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());
        if (permisoService.getAllPermisosWhereUsuario(usuarioActual).
                contains(permisoService.getPermisoByNombre("MODI-PERMISO"))) {
            if (permiso.getId() == null) throw new RuntimeException("Id Permiso NO existente!");
            Permiso permisoTmp = permisoService.getPermisoById(permiso.getId());
            permisoTmp.setDescripcion(permiso.getDescripcion());
            permisoTmp.setFuncionalidad(permiso.getFuncionalidad());
            if (permisoService.updatePermiso(permisoTmp) != null) {
                throw new RuntimeException("Error al modificar roles!");
            }
        } else {
            throw new RuntimeException("No cuenta con los permisos para modificar permisos!");
        }
    }

    /**
     * Lista todas las personas.
     *
     * @param    userDetails       Credenciales de usuario.
     *                      (debe tener los permisos para ejecutar el método).
     **/
    @GetMapping("/listar-personas")
    public List<Persona> listPersonas(@AuthenticationPrincipal UserDetails userDetails){


        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());


        if (permisoService.getAllPermisosWhereUsuario(usuarioActual).
                contains(permisoService.getPermisoByNombre("CONS-PERSONA"))) {
            return personaService.getAllPersonas();
        } else {
            throw new RuntimeException("No cuenta con los permisos para consultar personas!");
        }
    }

    /**
     * Lista todos los usuarios.
     *
     * @param    userDetails       Credenciales de usuario.
     *                      (debe tener los permisos para ejecutar el método).
     **/
    @GetMapping("/listar-usuarios")
    public List<Usuario> listUsuarios(@AuthenticationPrincipal UserDetails userDetails){


        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());

        if (permisoService.getAllPermisosWhereUsuario(usuarioActual).
                contains(permisoService.getPermisoByNombre("CONS-USUARIO"))) {
            return usuarioService.getAllUsuarios();
        } else {
            throw new RuntimeException("No cuenta con los permisos para consultar usuario!");
        }
    }

    /**
     * Lista todos los roles.
     *
     * @param    userDetails       Credenciales de usuario.
     *                      (debe tener los permisos para ejecutar el método).
     **/
    @GetMapping("/listar-roles")
    public List<Rol> listRoles(@AuthenticationPrincipal UserDetails userDetails){


        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());

        if (permisoService.getAllPermisosWhereUsuario(usuarioActual).
                contains(permisoService.getPermisoByNombre("CONS-ROL"))){
            return rolService.getAllRoles();
        }else {
            throw new RuntimeException("No cuenta con los permisos para consultar roles!");
        }
    }

    /**
     * Lista todos los Permisos.
     *
     * @param    userDetails       Credenciales de usuario.
     *                      (debe tener los permisos para ejecutar el método).
     **/
    @GetMapping("/listar-permisos")
    public List<Permiso> listPermisos(@AuthenticationPrincipal UserDetails userDetails){


        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());

        if (permisoService.getAllPermisosWhereUsuario(usuarioActual).
                contains(permisoService.getPermisoByNombre("CONS-PERMISO"))) {
            return permisoService.getAllPermisos();
        }else {
            throw new RuntimeException("No cuenta con los permisos para consultar permisos!");
        }
    }

    /**
     * Baja de persona.
     *
     *  @param    userDetails       Credenciales de usuario.
     *                       (debe tener los permisos para ejecutar el método).
     *  @param    doc        Nro de documento de la persona.
     **/
    @DeleteMapping("/delete-persona/{documento}")
    public void deletePersona(@AuthenticationPrincipal UserDetails userDetails,
                              @PathVariable("documento") Long doc){

        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());
        if (permisoService.getAllPermisosWhereUsuario(usuarioActual).
                contains(permisoService.getPermisoByNombre("BAJA-PERSONA"))) {
            /*
              TODO:     FALTA OBTENER TODOS LOS USUARIOS DE
                        UNA PERSONA Y DARLOS DE BAJA ó HACER ON DELETE CASCADE.
            */
            personaService.deletePersona(doc);
        } else {
            throw new RuntimeException("No cuenta con los permisos para eliminar personas!");
        }
    }

    /**
     * Baja de usuario
     *
     * @param    userDetails           Credenciales de usuario.
     *                          (debe tener los permisos para ejecutar el método).
     * @param    nombreUsuario  nombreUsuario del Usuario.
     **/
    @DeleteMapping("/delete-usuario/{nombre-usuario}")
    public void deleteUsuario(@AuthenticationPrincipal UserDetails userDetails,
                              @PathVariable("nombre-usuario") String nombreUsuario){
        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());
        if (permisoService.getAllPermisosWhereUsuario(usuarioActual).
                contains(permisoService.getPermisoByNombre("BAJA-USUARIO"))) {
            usuarioService.deleteUsuarioByNombre(nombreUsuario);
        } else {
            throw new RuntimeException("No cuenta con los permisos para eliminar usuarios!");
        }
    }

    /**
     * Baja de rol
     *
     * @param    userDetails           Credenciales de usuario.
     *                          (debe tener los permisos para ejecutar el método).
     * @param    rol            nombre del rol
     **/

    @DeleteMapping("/delete-rol/{rol}")
    public void deleteRol(@AuthenticationPrincipal UserDetails userDetails,
                          @PathVariable("rol") String rol){
        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());

        if (permisoService.getAllPermisosWhereUsuario(usuarioActual).
                contains(permisoService.getPermisoByNombre("BAJA-ROL"))) {
            rolService.deleteRolByNombre(rol);
        } else {
            throw new RuntimeException("No cuenta con los permisos para eliminar roles!");
        }
    }

    /**
     * Baja de rol
     *
     * @param    userDetails           Credenciales de usuario.
     *                          (debe tener los permisos para ejecutar el método).
     * @param    permiso        Nombre del permiso
     **/
    @DeleteMapping("/delete-permiso/{permiso-nombre}")
    public void deletePermiso(@AuthenticationPrincipal UserDetails userDetails,
                              @PathVariable("permiso-nombre") String permiso){
        // TODO:
    }

    /**
     * Obtiene un Persona por documento
     *
     * @param   userDetails    Credenciales de usuario.
     *                  (debe tener los permisos para ejecutar el método).
     * @param   doc     Numero de Documento
     * @return  Persona
     */
    @GetMapping("/get-persona/{documento}")
    public Persona getPersonaByDocumento(@AuthenticationPrincipal UserDetails userDetails,
                                         @PathVariable("documento") Long doc){
        Persona persona = null;
        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());
        if (permisoService.getAllPermisosWhereUsuario(usuarioActual).
                contains(permisoService.getPermisoByNombre("CONS-USUARIO"))) {
            persona = personaService.getPersonaByDocumento(doc);
            if( persona == null) {
                throw new RuntimeException("Error al actualizar persona");
            }
        } else {
            throw new RuntimeException("No cuenta con los permisos para consultar personas!");
        }
        return persona;
    }


    /**
     * Obtiene un Usuario por Nombre
     *
     * @param   userDetails        Credenciales de usuario.
     *                      (debe tener los permisos para ejecutar el método).
     * @param   nombre      Nombre del Usuario
     * @return  Usuario
     */
    @GetMapping("/get-usuario/{nombreusuario}")
    public Usuario getUsuarioByNombre(@AuthenticationPrincipal UserDetails userDetails,
                                      @PathVariable("nombreusuario") String nombre){
        Usuario usuario;
        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());
        if (permisoService.getAllPermisosWhereUsuario(usuarioActual).
                contains(permisoService.getPermisoByNombre("CONS-USUARIO"))) {
            usuario = usuarioService.getUsuarioByNombre(nombre);
            if( usuario == null) {
                throw new RuntimeException("Error al actualizar usuario");
            }
        } else {
            throw new RuntimeException("No cuenta con los permisos para consultar usuarios!");
        }
        return usuario;
    }


    /**
     * Obtiene un Rol por Nombre
     *
     * @param   userDetails        Credenciales de usuario.
     *                      (debe tener los permisos para ejecutar el método).
     * @param   nombre      Nombre del Rol
     * @return  Rol
     */
    @GetMapping("/get-rol/{rol-nombre}")
    public Rol getRolByNombre(@AuthenticationPrincipal UserDetails userDetails,
                              @PathVariable("rol-nombre") String nombre){
        Rol rol;
        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());
        if (permisoService.getAllPermisosWhereUsuario(usuarioActual).
                contains(permisoService.getPermisoByNombre("CONS-USUARIO"))) {
            rol = rolService.getRolByNombre(nombre);
            if( rol == null) {
                throw new RuntimeException("Error al actualizar usuario");
            }
        } else {
            throw new RuntimeException("No cuenta con los permisos para consultar usuarios!");
        }
        return rol;
    }

    /**
     * Obtiene un Permiso por Nombre
     *
     * @param   userDetails        Credenciales de usuario.
     *                      (debe tener los permisos para ejecutar el método).
     * @param   nombre      Nombre del Permiso
     * @return  Permiso
     */
    @GetMapping("/get-permiso/{permiso-nombre}")
    public Permiso getPermisoByNombre(@AuthenticationPrincipal UserDetails userDetails,
                                      @PathVariable("rol-permiso") String nombre){
        Permiso permiso;
        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());
        if (permisoService.getAllPermisosWhereUsuario(usuarioActual).
                contains(permisoService.getPermisoByNombre("CONS-USUARIO"))) {
            permiso = permisoService.getPermisoByNombre(nombre);
            if( permiso == null) {
                throw new RuntimeException("Error al actualizar usuario");
            }
        } else {
            throw new RuntimeException("No cuenta con los permisos para consultar usuarios!");
        }
        return permiso;
    }


}