package es.unizar.padel.web;

import java.io.IOException;

import es.unizar.padel.dao.jdbc.JdbcClubDao;
import es.unizar.padel.db.Database;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** Primer paso para reservar: lista de todos los clubes. Es pública (no hace falta sesión). */
@WebServlet("/clubes")
public class ClubesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("clubes", new JdbcClubDao(Database.getDataSource()).findAll());
        req.getRequestDispatcher("/WEB-INF/jsp/clubes.jsp").forward(req, resp);
    }
}
