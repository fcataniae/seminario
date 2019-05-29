package com.seminario.backend.services.bienes;

import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

/**
 * Usuario: Franco
 * Project: backend-application
 * Fecha: 28/05/2019
 **/
@Service
public class SecondaryDbService {

    @PersistenceUnit(unitName = "waltmartem")
    EntityManagerFactory entityManagerFactory;

    public void agregar (){
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        em.createNativeQuery("insert into tabla1 values (1)").executeUpdate();
        em.getTransaction().commit();
        System.out.println(em.createNativeQuery("select* from tabla1").getResultList().toString());

    }
}
