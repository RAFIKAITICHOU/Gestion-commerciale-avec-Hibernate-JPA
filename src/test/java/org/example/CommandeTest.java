package org.example;

import org.example.entities.*;
import org.example.service.CommandeService;
import org.example.service.CrudService;
import org.example.service.ProduitService;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.Assert.*;

public class CommandeTest {

    private final CrudService<Categorie, Long> catService = new CrudService<>(Categorie.class);
    private final ProduitService produitService = new ProduitService();
    private final CommandeService commandeService = new CommandeService();

    @Test
    public void testCommandeAvecDeuxProduits() {
        Categorie cat = new Categorie("Cmd-" + UUID.randomUUID());
        catService.create(cat);
        Produit p1 = new Produit("Clavier", new BigDecimal("49.99"), cat);
        Produit p2 = new Produit("Souris", new BigDecimal("19.90"), cat);
        produitService.create(p1);
        produitService.create(p2);

        Commande cmd = new Commande();
        cmd.setDateCommande(LocalDate.now());
        cmd.addProduit(p1, 3);
        cmd.addProduit(p2, 5);
        commandeService.create(cmd);

        assertNotNull(cmd.getId());

        Commande loaded = commandeService.getByIdWithLignes(cmd.getId());
        assertEquals(2, loaded.getLignes().size());
        int totalQuantite = loaded.getLignes().stream().mapToInt(CommandeProduit::getQuantite).sum();
        assertEquals(8, totalQuantite);
        // la clé composée a bien été alimentée par @MapsId
        loaded.getLignes().forEach(l -> {
            assertEquals(loaded.getId(), l.getId().getCommandeId());
            assertNotNull(l.getId().getProduitId());
        });
    }
}
