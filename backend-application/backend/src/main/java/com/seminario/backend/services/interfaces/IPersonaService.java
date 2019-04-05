package com.seminario.backend.services.interfaces;

import java.util.List;

import com.seminario.backend.model.Estado;
import com.seminario.backend.model.Usuario;
import com.seminario.backend.model.Persona;

public interface IPersonaService {
        List<Persona> getAllPersonas();
        Persona getPersonaById(Long id);
        Persona createPersona(Persona persona);
        // boolean cambiarEstado(Persona persona, Estado estado);
        Persona updatePersona(Persona persona);
        void deletePersona(Long nroDoc);

        Persona getPersonaByDocumento(Long doc);

}
