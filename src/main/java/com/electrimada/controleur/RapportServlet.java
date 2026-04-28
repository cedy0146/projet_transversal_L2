package com.electrimada.controleur;

import com.electrimada.dao.RapportDAO;
import com.electrimada.modele.Rapport;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/rapports")
public class RapportServlet extends HttpServlet {

    private Gson gson = new Gson();
    private RapportDAO rapportDAO = new RapportDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            String idParam = req.getParameter("id");
            if (idParam != null) {
                Rapport rapport = rapportDAO.findById(idParam);
                if (rapport != null) {
                    out.print(gson.toJson(ApiResponse.success(rapport)));
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print(gson.toJson(ApiResponse.error("Rapport non trouvé")));
                }
            } else {
                List<Rapport> rapports = rapportDAO.findAll();
                out.print(gson.toJson(ApiResponse.success(rapports)));
            }
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(ApiResponse.error("Erreur base de données: " + e.getMessage())));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            BufferedReader reader = req.getReader();
            Rapport rapport = gson.fromJson(reader, Rapport.class);
            rapportDAO.save(rapport);
            out.print(gson.toJson(ApiResponse.success("Rapport créé", rapport)));
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(ApiResponse.error("Erreur création rapport: " + e.getMessage())));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            String idParam = req.getParameter("id");
            if (idParam != null) {
                rapportDAO.delete(idParam);
                out.print(gson.toJson(ApiResponse.success("Rapport supprimé", null)));
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(gson.toJson(ApiResponse.error("ID requis")));
            }
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(ApiResponse.error("Erreur suppression rapport: " + e.getMessage())));
        }
    }
}
