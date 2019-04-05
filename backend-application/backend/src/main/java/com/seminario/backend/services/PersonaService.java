package com.seminario.backend.services;

import com.seminario.backend.model.Persona;
import com.seminario.backend.repository.PersonaRepository;
import com.seminario.backend.services.interfaces.IPersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@Service
public class PersonaService implements IPersonaService {

    @Autowired
    private PersonaRepository personaRepository;

    @Override
    public List<Persona> getAllPersonas() {
        List<Persona> list = new ArrayList<>();
        for(Persona e : personaRepository.findAll()) {
            list.add(e);
        }
        return list;
    }

    @Override
    public Persona getPersonaById(Long Id) {
        return personaRepository.findById(Id);
    }

    @Override
    public Persona createPersona(Persona persona) {
        if(personaRepository.findByNroDoc(persona.getNroDoc()) == null){
            return personaRepository.save(persona);
        }
        return null;
    }

    @Override
    public Persona updatePersona(Persona persona) {
        Persona personaTmp = personaRepository.findByNroDoc(persona.getNroDoc());
        if(personaTmp != null){
            persona.setId(personaTmp.getId());
            return personaRepository.save(persona);

        }

        return null;
    }

    @Override
    public void deletePersona(Long nroDoc) {
        personaRepository.delete(personaRepository.findByNroDoc(nroDoc));
    }

    @Override
    public Persona getPersonaByDocumento(Long doc) {
        return personaRepository.findByNroDoc(doc);
    }


}