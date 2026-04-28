package com.electrimada.controleur;

import com.electrimada.dao.DemandeEnergieDAO;
import com.electrimada.modele.DemandeEnergie;
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

@WebServlet("/api/demandes")
public class DemandeServlet extends HttpServlet {

    private Gson gson = new Gson();
    private DemandeEnergieDAO demandeDAO = new DemandeEnergieDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            String idParam = req.getParameter("id");
            if (idParam != null) {
                DemandeEnergie demande = demandeDAO.findById(idParam);
                if (demande != null) {
                    out.print(gson.toJson(ApiResponse.success(demande)));
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print(gson.toJson(ApiResponse.error("Demande non trouvée")));
                }
            } else {
                List<DemandeEnergie> demandes = demandeDAO.findAll();
                out.print(gson.toJson(ApiResponse.success(demandes)));
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
            DemandeEnergie demande = gson.fromJson(reader, DemandeEnergie.class);
            demandeDAO.save(demande);
            out.print(gson.toJson(ApiResponse.success("Demande créée", demande)));
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(ApiResponse.error("Erreur création demande: " + e.getMessage())));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            BufferedReader reader = req.getReader();
            DemandeEnergie demande = gson.fromJson(reader, DemandeEnergie.class);
            demandeDAO.save(demande);
            out.print(gson.toJson(ApiResponse.success("Demande mise à jour", demande)));
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(ApiResponse.error("Erreur mise à jour demande: " + e.getMessage())));
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
                demandeDAO.delete(idParam);
                out.print(gson.toJson(ApiResponse.success("Demande supprimée", null)));
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(gson.toJson(ApiResponse.error("ID requis")));
            }
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(ApiResponse.error("Erreur suppression demande: " + e.getMessage())));
        }
    }
}
