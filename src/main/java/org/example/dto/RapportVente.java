package org.example.dto;

import java.math.BigDecimal;

/** Une ligne du rapport : chiffre d'affaires par catégorie. */
public class RapportVente {

    private final String categorie;
    private final BigDecimal chiffreAffaires;

    public RapportVente(String categorie, BigDecimal chiffreAffaires) {
        this.categorie = categorie;
        this.chiffreAffaires = chiffreAffaires;
    }

    public String getCategorie() { return categorie; }
    public BigDecimal getChiffreAffaires() { return chiffreAffaires; }

    @Override
    public String toString() { return categorie + " : " + chiffreAffaires; }
}
