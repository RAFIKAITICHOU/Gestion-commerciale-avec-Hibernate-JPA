package org.example;

import org.example.dao.GenericDAO;
import org.example.entities.Categorie;
import org.example.entities.Commande;
import org.example.entities.Produit;
import org.example.service.CommandeService;
import org.example.service.CrudService;
import org.example.service.ProduitService;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Démo en ligne de commande (nécessite MySQL, unité "defaultPU"). */
public class Main {
    public static void main(String[] args) {
        CrudService<Categorie, Long> catService = new CrudService<>(Categorie.class);
        ProduitService produitService = new ProduitService();
        CommandeService commandeService = new CommandeService();

        Categorie cat = new Categorie("Électronique-" + System.currentTimeMillis());
        catService.create(cat);
        System.out.println("Catégorie créée avec l'ID : " + cat.getId());

        Produit p1 = new Produit("Clavier", new BigDecimal("49.99"), cat);
        Produit p2 = new Produit("Souris", new BigDecimal("19.90"), cat);
        produitService.create(p1);
        produitService.create(p2);

        Commande cmd = new Commande();
        cmd.setDateCommande(LocalDate.now());
        cmd.addProduit(p1, 3);
        cmd.addProduit(p2, 5);
        commandeService.create(cmd);
        System.out.println("Commande créée avec ID = " + cmd.getId());

        GenericDAO.closeEntityManagerFactory();
    }
}
