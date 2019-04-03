package com.seminario.backend.services;

import java.util.List;

import com.seminario.backend.model.Estado;
import com.seminario.backend.model.Usuario;
import com.seminario.backend.model.Persona;

public interface IPersonaService {
        List<Persona> getAllPersonas();
        Persona getPersonaById(Long id);
        boolean createPersona(Persona persona);
        // boolean cambiarEstado(Persona persona, Estado estado);
        void updatePersona(Persona persona);
        void deletePersona(Long Id);
}
