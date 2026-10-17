package org.example.web;

import org.example.dto.RapportVente;
import org.example.service.RapportVentesService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.YearMonth;
import java.util.List;

@WebServlet("/rapport-ventes")
public class RapportVentesServlet extends HttpServlet {

    private final RapportVentesService service = new RapportVentesService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<RapportVente> lignes = service.rapportMoisPrecedent();
        resp.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            out.println("<html><body>");
            out.println("<h2>Chiffre d'affaires par catégorie — " + YearMonth.now().minusMonths(1) + "</h2>");
            out.println("<table border='1' cellpadding='6'><tr><th>Catégorie</th><th>CA total</th></tr>");
            for (RapportVente l : lignes) {
                out.println("<tr><td>" + escape(l.getCategorie()) + "</td><td>"
                        + l.getChiffreAffaires() + "</td></tr>");
            }
            out.println("</table>");
            if (lignes.isEmpty()) out.println("<p>Aucune vente sur la période.</p>");
            out.println("</body></html>");
        }
    }

    private static String escape(String s) {
        return s == null ? "" : s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
