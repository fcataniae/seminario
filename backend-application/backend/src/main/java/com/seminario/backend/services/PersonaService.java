package com.seminario.backend.services;

import com.seminario.backend.model.Persona;
import com.seminario.backend.repository.PersonaRepository;
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
    public boolean createPersona(Persona persona) {
        if(personaRepository.findById(persona.getId()) == null){
            personaRepository.save(persona);
            return true;
        }
        return false;
    }

    @Override
    public Persona updatePersona(Persona persona) {
        return personaRepository.save(persona);
    }

    @Override
    public void deletePersona(Long Id) {
        personaRepository.delete(getPersonaById(Id));
    }

    @Override
    public Persona getPersonaByDocumento(Long doc) {
        return personaRepository.findByNroDoc(doc);
    }

    @Override
    public Persona deletePersona(Persona persona) {
        Persona personaTmp = personaRepository.findByNroDoc(persona.getNroDoc());
        if(personaTmp != null){
            personaRepository.delete(personaTmp);

        }
        return null;
    }
}