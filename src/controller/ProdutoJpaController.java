/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

/**
 *
 * @author abrah
 */
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import model.Produto;

public class ProdutoJpaController {
    private EntityManager em;
    private EntityManagerFactory emf;

    public ProdutoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
        this.em = emf.createEntityManager();
    }

    public List<Produto> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createNamedQuery("Produto.findAll").getResultList();
        } finally {
            em.close();
        }
    }
    
     public Produto findProduto(int idProduto) {
        try {
            Query query = em.createNamedQuery("Produto.findByIdProduto");
            query.setParameter("idProduto", idProduto);
            return (Produto) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
     
    public void incrementQuantidade(int idProduto, int quantidade) {
        em = emf.createEntityManager();
        try {
            Produto produto = findProduto(idProduto);
            if (produto != null) {
                produto.setQuantidade(produto.getQuantidade() + quantidade);
                em.getTransaction().begin();
                em.merge(produto);
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } 
    }

    public void decrementQuantidade(int idProduto, int quantidade) {
        em = emf.createEntityManager();
        try {
            Produto produto = findProduto(idProduto);
            if (produto != null) {
                produto.setQuantidade(produto.getQuantidade() - quantidade);
                em.getTransaction().begin();
                em.merge(produto);
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } 
    }
}