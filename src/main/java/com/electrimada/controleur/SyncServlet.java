package com.electrimada.controleur;
import com.electrimada.service.SyncService;
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

@WebServlet("/api/sync")
public class SyncServlet extends HttpServlet {

    private Gson gson = new Gson();
    private SyncService syncService = new SyncService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            Map<String, Object> status = new HashMap<>();
            status.put("status", "ready");
            status.put("pendingCount", syncService.getNombreEnAttente());
            status.put("timestamp", System.currentTimeMillis());
            out.print(gson.toJson(ApiResponse.success("État de synchronisation", status)));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(ApiResponse.error("Erreur sync: " + e.getMessage())));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            int count = syncService.synchroniser();
            Map<String, Object> result = new HashMap<>();
            result.put("synced", true);
            result.put("count", count);
            out.print(gson.toJson(ApiResponse.success("Synchronisation effectuée", result)));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(ApiResponse.error("Erreur synchronisation: " + e.getMessage())));
        }
    }
}
