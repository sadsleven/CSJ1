/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.io.Serializable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Movimento;

/**
 *
 * @author abrah
 */
public class MovimentoJpaController implements Serializable {
      
    private EntityManager em;
    private EntityManagerFactory emf;

    public MovimentoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void create(Movimento movimento) {
        em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(movimento);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("movimento" + e);
            em.getTransaction().rollback();
            throw e;
        } 
    }
    
}
