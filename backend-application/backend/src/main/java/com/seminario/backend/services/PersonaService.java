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
    
    public List<Persona> getAllPersonas() {
        List<Persona> list = new ArrayList<>();
        for(Persona e : personaRepository.findAll()) {
            list.add(e);
        }
        return list;
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
            if (persona.getId() == null) throw new RuntimeException("Id Persona NO existente!");
            Persona personaTmp = personaRepository.findById(persona.getId());
            personaTmp.setNombre(persona.getNombre());
            personaTmp.setApellido(persona.getApellido());
            personaTmp.setEmail(persona.getEmail());
            personaTmp.setFecha_nacimiento(persona.getFecha_nacimiento());
            if (personaRepository.save(personaTmp) == null) {
                throw new RuntimeException("Error al modificar la persona!");
            }
        } else {
            throw new RuntimeException("No cuenta con los permisos para modificar personas!");
        }
    }

     
    public void deletePersona(Long nroDoc) {
        personaRepository.delete(personaRepository.findByNroDoc(nroDoc));
    }

     
    public Persona getPersonaByDocumento(Long doc) {
        return personaRepository.findByNroDoc(doc);
    }


}