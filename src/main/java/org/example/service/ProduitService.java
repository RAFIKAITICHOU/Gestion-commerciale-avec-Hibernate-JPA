package org.example.service;

import org.example.dao.ProduitDAO;
import org.example.entities.Produit;

import java.math.BigDecimal;
import java.util.List;

public class ProduitService extends CrudService<Produit, Long> {

    private final ProduitDAO produitDAO;

    public ProduitService() {
        this(new ProduitDAO());
    }

    private ProduitService(ProduitDAO dao) {
        super(dao);
        this.produitDAO = dao;
    }

    @Override
    public void create(Produit p) {
        if (p != null && p.getPrix() != null && p.getPrix().signum() < 0) {
            throw new IllegalArgumentException("Le prix ne peut pas être négatif");
        }
        super.create(p);
    }

    public Produit getByIdWithCategorie(Long id) { return produitDAO.findByIdWithCategorie(id); }
    public List<Produit> findByCategorie(Long catId) { return produitDAO.findByCategorie(catId); }
    public List<Produit> searchByLibelle(String term) { return produitDAO.searchByLibelle(term); }
    public List<Produit> search(String term, BigDecimal min, BigDecimal max) { return produitDAO.search(term, min, max); }
    public List<Produit> findPage(int page, int size) { return produitDAO.findPage(page, size); }
}
