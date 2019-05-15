package com.seminario.services.rest;

import com.seminario.backend.model.abm.Permiso;
import com.seminario.backend.model.abm.Persona;
import com.seminario.backend.model.abm.Rol;
import com.seminario.backend.model.abm.Usuario;
import com.seminario.backend.services.abm.*;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

/**
 * UserAuth: fcatania
 * Date: 27/3/2019
 * Time: 08:35
 */

@RestController
@RequestMapping("/service")
@CrossOrigin
@Scope("session")
public class AbmRestController {

    private AbmRestController(){};
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

    /**
     * Dar de Alta un nueva Persona.
     *
     * @param    userDetails    Credenciales de usuario.
     * @param    persona        Es la Persona que se va a dar de alta.
     * */
    @RequestMapping(value="/alta-persona", method = RequestMethod.POST)
    public void createPersona(@AuthenticationPrincipal UserDetails userDetails,
                              @RequestBody Persona persona) throws CustomException
    {
        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());
        personaService.create(usuarioActual, persona);
    }


    /**
     * Dar de Alta un nuevo Usuario.
     *
     * @param    userDetails    Credenciales de usuario.
     * @param    usuario        Es el Usuario que se va a dar de alta.
     * */
    @RequestMapping(value="/alta-usuario", method = RequestMethod.POST)
    public void createUsuario(@AuthenticationPrincipal UserDetails userDetails,
                              @RequestBody Usuario usuario) throws CustomException
    {
        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());
        usuarioService.create(usuarioActual, usuario);
    }

    /**
     * Dar de Alta un nuevo Rol.
     *
     * @param    userDetails     Credenciales de usuario.
     * @param    rolNuevo        Es el Rol que se va a dar de alta.
     * */
    @RequestMapping(value="/alta-rol", method = RequestMethod.POST)
    public void createRol(@AuthenticationPrincipal UserDetails userDetails,
                            @RequestBody Rol rolNuevo) throws CustomException
    {
        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());
        rolService.create(usuarioActual, rolNuevo);
    }

    /**
     * Dar de Alta un nuevo Permiso.
     *
     * @param    userDetails     Credenciales de usuario.
     * @param    permisoNuevo    Es el Permiso que se va a dar de alta.
     * */
    @RequestMapping(value="/alta-permiso", method =  RequestMethod.POST)
    public void createPermiso(@AuthenticationPrincipal UserDetails userDetails,
                              @RequestBody Permiso permisoNuevo) throws CustomException
    {
        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());
        permisoService.create(usuarioActual, permisoNuevo);
    }

    /**
     * Actualiza:
     *      -   el nombre, apellido, fechaNacimiento, email
     *
     * El resto de los atributos no se permiten cambiar. A excepción del Estado,
     * pero para este atributo se utiliza otro método.
     *
     * @param    userDetails    Credenciales de usuario.
     * @param    persona        Es el Objeto Usuario que se actualizará.
     * */
    @RequestMapping(value = "/update-persona", method = RequestMethod.PUT)
    public void updatePersona(@AuthenticationPrincipal UserDetails userDetails,
                                @RequestBody Persona persona) throws CustomException
    {
        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());
        personaService.update(usuarioActual, persona);
    }

    /**
     * Actualiza:
     *      -   la lista de Roles de un Usuario.
     *      -   la password.
     *
     * El resto de los atributos no se permiten cambiar. A excepción del Estado,
     * pero para este atributo se utiliza otro método.
     *
     * @param    userDetails    Credenciales de usuario.
     * @param    usuario        Es el Objeto Usuario que se actualizará.
     * */
    @RequestMapping(value = "/update-usuario", method = RequestMethod.PUT)
    public void updateUsuario(@AuthenticationPrincipal UserDetails userDetails,
                                    @RequestBody Usuario usuario) throws CustomException
    {
        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());
        usuarioService.update(usuarioActual,usuario);
    }

    /**
     * Actualiza:
     *      -   la lista de Permisos de un Rol.
     *      -   la descripción.
     *
     * El resto de los atributos no se permiten cambiar. A excepción del Estado,
     * pero para este atributo se utiliza otro método.
     *
     * @param    userDetails    Credenciales de usuario.
     * @param    rol            Es el Objeto Rol que se actualizará.
     * */
    @RequestMapping(value = "/update-rol", method = RequestMethod.PUT)
    public void updateRol(@AuthenticationPrincipal UserDetails userDetails,
                            @RequestBody Rol rol) throws CustomException
    {
        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());
        rolService.update(usuarioActual,rol);
    }

    /**
     * Actualiza:
     *      -   descripcion, funcionalidad
     *
     * El resto de los atributos no se permiten cambiar. A excepción del Estado,
     * pero para este atributo se utiliza otro método.
     *
     * @param    userDetails    Credenciales de usuario.
     * @param    permiso        Es el Objeto Usuario que se actualizará.
     * */
    @RequestMapping(value = "/update-permiso", method = RequestMethod.PUT)
    public void updatePermiso(@AuthenticationPrincipal UserDetails userDetails,
                                @RequestBody Permiso permiso) throws CustomException
    {
        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());
        permisoService.update(usuarioActual, permiso);
    }

    /**
     * Lista todas las personas.
     *
     * @param    userDetails       Credenciales de usuario.
     *                      (debe tener los permisos para ejecutar el método).
     **/
    @GetMapping("/listar-personas")
    public List<Persona> listPersonas(@AuthenticationPrincipal UserDetails userDetails) throws CustomException
    {
        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());
        return personaService.getAll(usuarioActual);
    }

    /**
     * Lista todos los usuarios.
     *
     * @param    userDetails       Credenciales de usuario.
     *                      (debe tener los permisos para ejecutar el método).
     **/
    @GetMapping("/listar-usuarios")
    public List<Usuario> listUsuarios(@AuthenticationPrincipal UserDetails userDetails) throws CustomException
    {
        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());
        return usuarioService.getAll(usuarioActual);
    }

    /**
     * Lista todos los roles.
     *
     * @param    userDetails       Credenciales de usuario.
     *                      (debe tener los permisos para ejecutar el método).
     **/
    @GetMapping("/listar-roles")
    public List<Rol> listRoles(@AuthenticationPrincipal UserDetails userDetails) throws CustomException
    {
        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());
        return rolService.getAll(usuarioActual);
    }

    /**
     * Lista todos los Permisos.
     *
     * @param    userDetails       Credenciales de usuario.
     *                      (debe tener los permisos para ejecutar el método).
     **/
    @GetMapping("/listar-permisos")
    public List<Permiso> listPermisos(@AuthenticationPrincipal UserDetails userDetails) throws CustomException
    {
        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());
        return permisoService.getAll(usuarioActual);
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
                              @PathVariable("documento") Long doc) throws CustomException {

        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());
        personaService.delete(usuarioActual, doc);
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
                              @PathVariable("nombre-usuario") String nombreUsuario) throws CustomException {
        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());
        usuarioService.deleteUsuarioByNombre(usuarioActual, nombreUsuario);
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
                          @PathVariable("rol") String rol) throws CustomException {
        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());
        rolService.deleteRolByNombre(usuarioActual, rol);
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
                              @PathVariable("permiso-nombre") String permiso) throws CustomException {
        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());
        permisoService.deletePermisoByNombre(usuarioActual, permiso);
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
                                         @PathVariable("documento") Long doc) throws CustomException {
        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());
        return personaService.getPersonaByDocumento(usuarioActual, doc);
    }


    /**
     * Obtiene un Usuario por Nombre
     *
     * @param   userDetails     Credenciales de usuario.
     * @param   nombre          Nombre del Usuario
     * @return  Usuario
     */
    @GetMapping("/get-usuario/{nombreusuario}")
    public Usuario getUsuarioByNombre(@AuthenticationPrincipal UserDetails userDetails,
                                      @PathVariable("nombreusuario") String nombre) throws CustomException {
        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());
        return usuarioService.getUsuarioByNombre(usuarioActual, nombre);
    }


    /**
     * Obtiene un Rol por Nombre
     *
     * @param   userDetails     Credenciales de usuario.
     * @param   nombre          Nombre del Rol
     * @return  Rol
     */
    @GetMapping("/get-rol/{rol-nombre}")
    public Rol getRolByNombre(@AuthenticationPrincipal UserDetails userDetails,
                              @PathVariable("rol-nombre") String nombre) throws CustomException {
        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());
        return rolService.getRolByNombre(usuarioActual, nombre);
    }

    /**
     * Obtiene un Permiso por Nombre
     *
     * @param   userDetails        Credenciales de usuario.
     * @param   nombre      Nombre del Permiso
     * @return  Permiso
     */
    @GetMapping("/get-permiso/{permiso-nombre}")
    public Permiso getPermisoByNombre(@AuthenticationPrincipal UserDetails userDetails,
                                      @PathVariable("rol-permiso") String nombre) throws CustomException {
        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());
        return permisoService.getPermisoByNombre(usuarioActual, nombre);
    }

    @GetMapping("/get-personas")
    public List<Persona> getAllPersonas(@AuthenticationPrincipal UserDetails userDetails){
        Usuario usuarioActual = usuarioService.getUsuarioByNombre(userDetails.getUsername());
        return personaService.getAll(usuarioActual);
    }

}