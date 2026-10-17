package org.example.dao;

import org.example.dto.RapportVente;
import org.example.entities.*;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/** Exercice de synthèse : chiffre d'affaires par catégorie via la Criteria API. */
public class RapportVentesDAO extends GenericDAO<Commande, Long> {

    public RapportVentesDAO() {
        super(Commande.class);
    }

    public List<RapportVente> chiffreAffairesParCategorie(LocalDate debut, LocalDate fin) {
        EntityManager em = createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<RapportVente> cq = cb.createQuery(RapportVente.class);

            Root<CommandeProduit> cp = cq.from(CommandeProduit.class);
            Join<CommandeProduit, Commande> cmd = cp.join("commande");
            Join<CommandeProduit, Produit> prod = cp.join("produit");
            Join<Produit, Categorie> cat = prod.join("categorie");

            // SUM(prix * quantite)
            Expression<BigDecimal> ca = cb.sum(
                    cb.prod(prod.<BigDecimal>get("prix"),
                            cb.toBigDecimal(cp.<Integer>get("quantite"))));

            cq.select(cb.construct(RapportVente.class, cat.<String>get("nom"), ca))
              .where(cb.between(cmd.<LocalDate>get("dateCommande"), debut, fin))
              .groupBy(cat.get("id"), cat.get("nom"))
              .orderBy(cb.asc(cat.get("nom")));

            return em.createQuery(cq).getResultList();
        } finally {
            em.close();
        }
    }
}
