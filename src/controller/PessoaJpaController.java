/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import model.Pessoa;

/**
 *
 * @author abrah
 */

public class PessoaJpaController {
    private EntityManager em;

    public PessoaJpaController(EntityManagerFactory emf) {
        this.em = emf.createEntityManager();
    }

    public Pessoa findPessoa(int idPessoa) {
        try {
            Query query = em.createNamedQuery("Pessoa.findByIdPessoa");
            query.setParameter("idPessoa", idPessoa);
            return (Pessoa) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}