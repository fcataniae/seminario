package com.seminario.backend.services.bienes;

import com.seminario.backend.model.abm.Usuario;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

/**
 * Usuario: Franco
 * Project: backend-application
 * Fecha: 5/15/2019
 **/
@Service
public class TablasCalculadasService {

    @PersistenceContext
    EntityManagerFactory emf;

    public void actualizarStock(){
        EntityManager em = emf.createEntityManager();
        em.createNativeQuery("select * from usuario where id = ?1", Usuario.class)
                .setParameter(1,"ads").getResultList();
    }

}
