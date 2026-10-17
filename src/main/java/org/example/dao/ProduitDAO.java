package org.example.dao;

import org.example.entities.Produit;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProduitDAO extends GenericDAO<Produit, Long> {

    public ProduitDAO() {
        super(Produit.class);
    }

    /** Named Query. */
    public List<Produit> findByCategorie(Long catId) {
        EntityManager em = createEntityManager();
        try {
            return em.createNamedQuery("Produit.findByCategorie", Produit.class)
                     .setParameter("catId", catId)
                     .getResultList();
        } finally {
            em.close();
        }
    }

    /** Named Query. */
    public List<Produit> searchByLibelle(String term) {
        EntityManager em = createEntityManager();
        try {
            return em.createNamedQuery("Produit.searchByLibelle", Produit.class)
                     .setParameter("term", term)
                     .getResultList();
        } finally {
            em.close();
        }
    }

    /** JOIN FETCH : évite la LazyInitializationException sur la catégorie. */
    public Produit findByIdWithCategorie(Long id) {
        EntityManager em = createEntityManager();
        try {
            return em.createNamedQuery("Produit.findByIdWithCategorie", Produit.class)
                     .setParameter("id", id)
                     .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    /** Criteria API dynamique (tous les filtres sont optionnels). */
    public List<Produit> search(String term, BigDecimal minPrix, BigDecimal maxPrix) {
        EntityManager em = createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Produit> cq = cb.createQuery(Produit.class);
            Root<Produit> root = cq.from(Produit.class);

            List<Predicate> predicates = new ArrayList<>();
            if (term != null && !term.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.<String>get("libelle")), "%" + term.toLowerCase() + "%"));
            }
            if (minPrix != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.<BigDecimal>get("prix"), minPrix));
            }
            if (maxPrix != null) {
                predicates.add(cb.lessThanOrEqualTo(root.<BigDecimal>get("prix"), maxPrix));
            }
            cq.select(root).where(predicates.toArray(new Predicate[0]));
            return em.createQuery(cq).getResultList();
        } finally {
            em.close();
        }
    }

    /** Pagination JPQL (pageNumber commence à 1). */
    public List<Produit> findPage(int pageNumber, int pageSize) {
        EntityManager em = createEntityManager();
        try {
            return em.createQuery("SELECT p FROM Produit p ORDER BY p.id", Produit.class)
                     .setFirstResult((pageNumber - 1) * pageSize)
                     .setMaxResults(pageSize)
                     .getResultList();
        } finally {
            em.close();
        }
    }
}
