package com.seminario.backend.services;

import com.seminario.backend.model.Persona;
import com.seminario.backend.model.Usuario;
import com.seminario.backend.repository.PermisoRepository;
import com.seminario.backend.repository.PersonaRepository;
import com.seminario.backend.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@Service
public class PersonaService {

    @Autowired
    private PersonaRepository personaRepository;
    @Autowired
    private PermisoRepository permisoRepository;
    @Autowired
    private EstadoRepository estadoRepository;
    
    public List<Persona> getAll(Usuario usuarioActual) throws CustomException {
        if (permisoRepository.findAllPermisosWhereUsuario(usuarioActual.getId()).
                contains(permisoRepository.findByNombre("CONS-PERSONA"))) {
            return personaRepository.findAll();
        } else {
            throw new CustomException("No cuenta con los permisos para consultar usuarios!");
        }
    }
    
    public Persona getPersonaById(Long Id) {
        return personaRepository.findById(Id);
    }
    
    public void create(Usuario usuarioActual, Persona personaNueva) throws CustomException {
        if (permisoRepository.findAllPermisosWhereUsuario(usuarioActual.getId()).
                contains(permisoRepository.findByNombre("ALTA-PERSONA"))){
            personaNueva.setId(null);
            personaNueva.setEstado(estadoRepository.findByDescrip("ACTIVO"));
            if (personaRepository.save(personaNueva) == null)
                throw new CustomException("Error al dar de alta la persona!");
        } else {
            throw new CustomException("No cuenta con permisos para dar de alta personas!");
        }
    }

     
    public void update(Usuario usuarioActual, Persona persona) throws CustomException {
        if (permisoRepository.findAllPermisosWhereUsuario(usuarioActual.getId()).
                contains(permisoRepository.findByNombre("MODI-PERSONA"))) {
            if (persona.getId() == null) throw new CustomException("Id Persona NO existente!");
            Persona personaTmp = personaRepository.findById(persona.getId());
            personaTmp.setNombre(persona.getNombre());
            personaTmp.setApellido(persona.getApellido());
            personaTmp.setEmail(persona.getEmail());
            personaTmp.setFecha_nacimiento(persona.getFecha_nacimiento());
            if (personaRepository.save(personaTmp) == null) {
                throw new CustomException("Error al modificar la persona!");
            }
        } else {
            throw new CustomException("No cuenta con los permisos para modificar personas!");
        }
    }

     
    public void delete(Usuario usuarioActual, Long nroDoc) throws CustomException {
        if (permisoRepository.findAllPermisosWhereUsuario(usuarioActual.getId()).
                contains(permisoRepository.findByNombre("BAJA-PERSONA"))) {
            /*
              TODO:     FALTA OBTENER TODOS LOS USUARIOS DE
                        UNA PERSONA Y DARLOS DE BAJA ó HACER ON DELETE CASCADE.
            */
            personaRepository.delete(personaRepository.findByNroDoc(nroDoc));
        } else {
            throw new CustomException("No cuenta con los permisos para eliminar personas!");
        }
    }

     
    public Persona getPersonaByDocumento(Usuario usuarioActual, Long doc) throws CustomException {
        Persona persona;
        if (permisoRepository.findAllPermisosWhereUsuario(usuarioActual.getId()).
                contains(permisoRepository.findByNombre("CONS-PERSONA"))) {
            persona = personaRepository.findByNroDoc(doc);
            if( persona == null) {
                throw new CustomException("Error al consultar persona");
            }
        } else {
            throw new CustomException("No cuenta con los permisos para consultar personas!");
        }
        return persona;
    }


}