package com.electrimada.controleur;

import com.electrimada.modele.Batterie;
import com.electrimada.service.BatterieService;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/batterie")
public class BatterieServlet extends HttpServlet {

    private Gson gson = new Gson();
    private BatterieService batterieService = new BatterieService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            Batterie batterie = batterieService.getEtatBatterie();
            if (batterie != null) {
                Map<String, Object> etat = new HashMap<>();
                etat.put("batterie", batterie);
                etat.put("pourcentage", batterieService.getPourcentageCharge(batterie));
                etat.put("seuilCritique", batterieService.estSeuilCritique(batterie));
                etat.put("modeEcoForce", batterieService.estModeEcoForce(batterie));
                out.print(gson.toJson(ApiResponse.success(etat)));
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print(gson.toJson(ApiResponse.error("Aucune batterie trouvée")));
            }
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(ApiResponse.error("Erreur batterie: " + e.getMessage())));
        }
    }
}
