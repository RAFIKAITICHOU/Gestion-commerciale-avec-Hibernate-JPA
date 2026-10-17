package org.example.dao;

import org.example.entities.Commande;
import org.example.entities.CommandeProduit;
import org.example.entities.Produit;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;

public class CommandeDAO extends GenericDAO<Commande, Long> {

    public CommandeDAO() {
        super(Commande.class);
    }

    /**
     * Avec @MapsId, Hibernate propage le persist vers le Produit : un produit "detached"
     * provoque "detached entity passed to persist". On rattache donc chaque ligne
     * à une référence gérée par l'EntityManager courant.
     */
    @Override
    public void create(Commande commande) {
        EntityManager em = createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            for (CommandeProduit ligne : commande.getLignes()) {
                ligne.setProduit(em.getReference(Produit.class, ligne.getProduit().getId()));
            }
            em.persist(commande);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    /** Charge la commande avec ses lignes et leurs produits (évite le lazy loading hors session). */
    public Commande findByIdWithLignes(Long id) {
        EntityManager em = createEntityManager();
        try {
            return em.createQuery(
                    "SELECT DISTINCT c FROM Commande c " +
                    "LEFT JOIN FETCH c.lignes l LEFT JOIN FETCH l.produit WHERE c.id = :id",
                    Commande.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}
