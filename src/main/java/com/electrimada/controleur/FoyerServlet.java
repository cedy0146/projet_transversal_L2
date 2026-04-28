package com.electrimada.controleur;

import com.electrimada.dao.FoyerDAO;
import com.electrimada.modele.Foyer;
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

@WebServlet("/api/foyers")
public class FoyerServlet extends HttpServlet {

    private Gson gson = new Gson();
    private FoyerDAO foyerDAO = new FoyerDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            String idParam = req.getParameter("id");
            if (idParam != null) {
                Foyer foyer = foyerDAO.findById(idParam);
                if (foyer != null) {
                    out.print(gson.toJson(ApiResponse.success(foyer)));
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print(gson.toJson(ApiResponse.error("Foyer non trouvé")));
                }
            } else {
                List<Foyer> foyers = foyerDAO.findAll();
                out.print(gson.toJson(ApiResponse.success(foyers)));
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
            Foyer foyer = gson.fromJson(reader, Foyer.class);
            foyerDAO.save(foyer);
            out.print(gson.toJson(ApiResponse.success("Foyer créé", foyer)));
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(ApiResponse.error("Erreur création foyer: " + e.getMessage())));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            BufferedReader reader = req.getReader();
            Foyer foyer = gson.fromJson(reader, Foyer.class);
            foyerDAO.save(foyer);
            out.print(gson.toJson(ApiResponse.success("Foyer mis à jour", foyer)));
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(ApiResponse.error("Erreur mise à jour foyer: " + e.getMessage())));
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
                foyerDAO.delete(idParam);
                out.print(gson.toJson(ApiResponse.success("Foyer supprimé", null)));
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(gson.toJson(ApiResponse.error("ID requis")));
            }
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(ApiResponse.error("Erreur suppression foyer: " + e.getMessage())));
        }
    }
}
