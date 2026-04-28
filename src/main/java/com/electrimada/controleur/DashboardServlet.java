package com.electrimada.controleur;

import com.electrimada.dao.DemandeEnergieDAO;
import com.electrimada.dao.FoyerDAO;
import com.electrimada.modele.DemandeEnergie;
import com.electrimada.service.BatterieService;
import com.electrimada.service.PrevisionService;
import com.electrimada.service.AlerteService;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/dashboard")
public class DashboardServlet extends HttpServlet {

    private Gson gson = new Gson();
    private BatterieService batterieService = new BatterieService();
    private PrevisionService previsionService = new PrevisionService();
    private AlerteService alerteService = new AlerteService();
    private FoyerDAO foyerDAO = new FoyerDAO();
    private DemandeEnergieDAO demandeDAO = new DemandeEnergieDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            Map<String, Object> dashboard = new HashMap<>();

            // État de la batterie
            dashboard.put("batterie", batterieService.getEtatBatterie());

            // Prévision solaire
            dashboard.put("prevision", previsionService.prevoirProductionDemain());

            // Alertes actives
            dashboard.put("alertes", alerteService.getToutesAlertes());

            // Statistiques réelles (Optimisation : un seul appel findAll)
            List<DemandeEnergie> demandes = demandeDAO.findAll();
            Map<String, Object> stats = new HashMap<>();
            stats.put("foyersTotal", foyerDAO.findAll().size());
            stats.put("demandesAujourdhui", demandes.size());
            
            long acceptees = 0;
            for (DemandeEnergie d : demandes) {
                if (d.isEstAcceptee()) acceptees++;
            }
            int tauxSatisfaction = demandes.isEmpty() ? 0 : (int) ((acceptees * 100) / demandes.size());
            stats.put("tauxSatisfaction", tauxSatisfaction);
            
            dashboard.put("stats", stats);

            out.print(gson.toJson(ApiResponse.success("Dashboard chargé", dashboard)));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(ApiResponse.error("Erreur dashboard: " + e.getMessage())));
        }
    }
}
