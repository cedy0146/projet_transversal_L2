package com.electrimada.controleur;

import com.electrimada.service.AllocationService;
import com.google.gson.Gson;

import java.util.List;
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

@WebServlet("/api/allocations")
public class AllocationServlet extends HttpServlet {

    private Gson gson = new Gson();
    private AllocationService allocationService = new AllocationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            String mode = req.getParameter("mode");
            Map<String, Object> result = new HashMap<>();

            if ("compare".equalsIgnoreCase(mode)) {
                Map<String, Object> metrics = allocationService.comparerAllocations();
                result.put("metrics", metrics);
                result.put("mode", "compare");
            } else {
                long debut = System.nanoTime();
                List<AllocationService.DemandePriorisee> opt = allocationService.allouerEnergieOptimise();
                long fin = System.nanoTime();
                double tempsOpt = (fin - debut) / 1_000_000.0;

                result.put("allocations", opt);
                result.put("tempsMs", tempsOpt);
                result.put("mode", "optimise");
            }

            out.print(gson.toJson(ApiResponse.success("Allocations calculées", result)));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(ApiResponse.error("Erreur allocation: " + e.getMessage())));
        }
    }
}
