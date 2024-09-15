/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.NoResultException;
import model.Usuario;

/**
 *
 * @author abrah
 */
public class UsuarioJpaController {
    private EntityManager em;

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.em = emf.createEntityManager();
    }

    public Usuario findUsuario(String login, String senha) {
        try {
            Query query = em.createNamedQuery("Usuario.findByLoginSenha");
            query.setParameter("login", login);
            query.setParameter("senha", senha);
            return (Usuario) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}