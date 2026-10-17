package org.example;

import org.example.entities.Categorie;
import org.example.entities.Produit;
import org.example.service.CrudService;
import org.example.service.ProduitService;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class ProduitDaoTest {

    private static CrudService<Categorie, Long> catService;
    private static ProduitService prodService;

    @BeforeClass
    public static void init() {
        catService = new CrudService<>(Categorie.class);
        prodService = new ProduitService();
    }

    private Categorie newCategorie(String base) {
        Categorie c = new Categorie(base + "-" + UUID.randomUUID());
        catService.create(c);
        return c;
    }

    @Test
    public void testCreateProduitAvecCategorie() {
        Categorie cat = newCategorie("Papeterie");
        Produit p = new Produit("Stylo", new BigDecimal("1.20"), cat);
        prodService.create(p);
        assertNotNull(p.getId());

        // JOIN FETCH : la catégorie (LAZY) est lisible après fermeture de l'EntityManager
        Produit fetched = prodService.getByIdWithCategorie(p.getId());
        assertEquals(cat.getNom(), fetched.getCategorie().getNom());
    }

    @Test
    public void testNamedQueryFindByCategorie() {
        Categorie cat = newCategorie("NQ");
        prodService.create(new Produit("A", new BigDecimal("1.00"), cat));
        prodService.create(new Produit("B", new BigDecimal("2.00"), cat));
        assertEquals(2, prodService.findByCategorie(cat.getId()).size());
    }

    @Test
    public void testNamedQuerySearchByLibelle() {
        Categorie cat = newCategorie("Recherche");
        String marker = "Zorglub" + UUID.randomUUID().toString().substring(0, 8);
        prodService.create(new Produit(marker + " Pro", new BigDecimal("5.00"), cat));
        assertEquals(1, prodService.searchByLibelle(marker.toLowerCase()).size());
    }

    @Test
    public void testCriteriaSearch() {
        Categorie cat = newCategorie("Criteria");
        String marker = "Crit" + UUID.randomUUID().toString().substring(0, 8);
        prodService.create(new Produit(marker + "1", new BigDecimal("10.00"), cat));
        prodService.create(new Produit(marker + "2", new BigDecimal("50.00"), cat));
        prodService.create(new Produit(marker + "3", new BigDecimal("100.00"), cat));

        assertEquals(3, prodService.search(marker, null, null).size());
        assertEquals(2, prodService.search(marker, new BigDecimal("20"), null).size());
        List<Produit> milieu = prodService.search(marker, new BigDecimal("20"), new BigDecimal("60"));
        assertEquals(1, milieu.size());
        assertEquals(marker + "2", milieu.get(0).getLibelle());
    }

    @Test
    public void testPagination() {
        Categorie cat = newCategorie("Pagination");
        for (int i = 0; i < 5; i++) {
            prodService.create(new Produit("P" + i, new BigDecimal("1.00"), cat));
        }
        List<Produit> page1 = prodService.findPage(1, 3);
        List<Produit> page2 = prodService.findPage(2, 3);
        assertEquals(3, page1.size());
        assertFalse(page2.isEmpty());
        assertNotEquals(page1.get(0).getId(), page2.get(0).getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrixNegatifRefuse() {
        Categorie cat = newCategorie("Negatif");
        prodService.create(new Produit("Bug", new BigDecimal("-1"), cat));
    }

    @Test
    public void testCascadeCategorieProduits() {
        Categorie cat = new Categorie("Cascade-" + UUID.randomUUID());
        cat.addProduit(new Produit("Enfant", new BigDecimal("3.00"), null));
        catService.create(cat); // cascade ALL -> le produit est persisté aussi
        assertEquals(1, prodService.findByCategorie(cat.getId()).size());
    }
}
