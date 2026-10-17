package org.example;

import org.example.entities.Categorie;
import org.example.service.CrudService;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class CategorieServiceTest {

    private static CrudService<Categorie, Long> catService;

    @BeforeClass
    public static void init() {
        catService = new CrudService<>(Categorie.class);
    }

    private static String unique(String base) { return base + "-" + UUID.randomUUID(); }

    @Test
    public void testCreateEtFind() {
        Categorie cat = new Categorie(unique("Informatique"));
        catService.create(cat);
        assertNotNull(cat.getId());
        assertEquals(cat.getNom(), catService.getById(cat.getId()).getNom());
    }

    @Test
    public void testUpdate() {
        Categorie cat = new Categorie(unique("Avant"));
        catService.create(cat);
        cat.setNom(unique("Apres"));
        Categorie merged = catService.update(cat);
        assertEquals(cat.getNom(), catService.getById(merged.getId()).getNom());
    }

    @Test
    public void testDelete() {
        Categorie cat = new Categorie(unique("ASupprimer"));
        catService.create(cat);
        catService.delete(cat);
        assertNull(catService.getById(cat.getId()));
    }

    @Test
    public void testFindAll() {
        Categorie cat = new Categorie(unique("Liste"));
        catService.create(cat);
        assertTrue(catService.getAll().stream().anyMatch(c -> c.getId().equals(cat.getId())));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateNullLeveException() {
        catService.create(null);
    }
}
