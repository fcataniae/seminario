package com.seminario.backend.services.bienes;

import com.seminario.backend.dto.Transportista;
import com.seminario.backend.model.abm.Usuario;
import com.seminario.backend.repository.abm.PermisoRepository;
import com.seminario.backend.services.abm.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Usuario: Franco
 * Project: backend-application
 * Fecha: 28/05/2019
 **/
@Service
public class SecondaryDbService {
    @Autowired
    PermisoRepository permisoRepository;

    @PersistenceUnit(unitName = "waltmartem")
    EntityManagerFactory entityManagerFactory;

    public void agregar (){
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        em.createNativeQuery("insert into tabla1 values (1)").executeUpdate();
        em.getTransaction().commit();
        System.out.println(em.createNativeQuery("select* from tabla1").getResultList().toString());
    }

    public Transportista findTransportistaById(Long id){
        EntityManager em = entityManagerFactory.createEntityManager();
        Transportista t = new Transportista();
        String qry = "SELECT id, nombre from transportista where id = ?1";
        List<Object[]> obj = em.createNativeQuery(qry)
                .setParameter(1, id)
                .getResultList();
        em.close();
        if (!obj.isEmpty()) {
            t.setId((Long) obj.get(0)[0]);
            t.setNombre((String) obj.get(0)[1]);
        } else{
            t = null;
        }
        return t;
    }

    public List<Transportista> findAllTransportista(){
        EntityManager em = entityManagerFactory.createEntityManager();
        List<Transportista> ls = new ArrayList<>();
        String qry = "SELECT id, nombre from transportista";
        List<Object[]> obj = em.createNativeQuery(qry).getResultList();
        em.close();
        obj.forEach( p -> {
            Transportista t = new Transportista();
            t.setId((Long) p[0]);
            t.setNombre((String) p[1]);
            ls.add(t);
        });
        return ls;
    }

    public List<Transportista> getAllTransportista(Usuario usuarioActual) throws CustomException {
        if (null != permisoRepository.findPermisoWhereUsuarioAndPermiso(usuarioActual.getId(), "CONS-BD-EXTERNA")) {
            return this.findAllTransportista();
        } else {
            throw new CustomException("No cuenta con los permisos para consultar transportistas");
        }
    }
}
