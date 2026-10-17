package org.example.service;

import org.example.dao.RapportVentesDAO;
import org.example.dto.RapportVente;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class RapportVentesService {

    private final RapportVentesDAO dao = new RapportVentesDAO();

    public List<RapportVente> rapportMoisPrecedent() {
        YearMonth mois = YearMonth.now().minusMonths(1);
        return rapportPourPeriode(mois.atDay(1), mois.atEndOfMonth());
    }

    public List<RapportVente> rapportPourPeriode(LocalDate debut, LocalDate fin) {
        return dao.chiffreAffairesParCategorie(debut, fin);
    }
}
