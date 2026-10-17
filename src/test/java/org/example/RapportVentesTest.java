package org.example;

import org.example.dto.RapportVente;
import org.example.entities.Categorie;
import org.example.entities.Commande;
import org.example.entities.Produit;
import org.example.service.CommandeService;
import org.example.service.CrudService;
import org.example.service.ProduitService;
import org.example.service.RapportVentesService;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

/** Exercice de synthèse : CA par catégorie sur le mois précédent. */
public class RapportVentesTest {

    @Test
    public void testChiffreAffairesMoisPrecedent() {
        CrudService<Categorie, Long> catService = new CrudService<>(Categorie.class);
        ProduitService produitService = new ProduitService();
        CommandeService commandeService = new CommandeService();
        RapportVentesService rapportService = new RapportVentesService();

        Categorie cat = new Categorie("Rapport-" + UUID.randomUUID());
        catService.create(cat);
        Produit p1 = new Produit("P1", new BigDecimal("10.00"), cat);
        Produit p2 = new Produit("P2", new BigDecimal("5.50"), cat);
        produitService.create(p1);
        produitService.create(p2);

        YearMonth moisPrecedent = YearMonth.now().minusMonths(1);

        Commande dansPeriode = new Commande();
        dansPeriode.setDateCommande(moisPrecedent.atDay(15));
        dansPeriode.addProduit(p1, 3);   // 30.00
        dansPeriode.addProduit(p2, 2);   // 11.00
        commandeService.create(dansPeriode);

        Commande horsPeriode = new Commande();
        horsPeriode.setDateCommande(YearMonth.now().atDay(1));
        horsPeriode.addProduit(p1, 100); // ne doit pas être comptée
        commandeService.create(horsPeriode);

        List<RapportVente> rapport = rapportService.rapportMoisPrecedent();
        RapportVente ligne = rapport.stream()
                .filter(r -> r.getCategorie().equals(cat.getNom()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Catégorie absente du rapport"));

        assertEquals(0, new BigDecimal("41.00").compareTo(ligne.getChiffreAffaires()));
    }
}
