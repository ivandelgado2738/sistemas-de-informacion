package es.unizar.padel.web;

import java.io.IOException;
import java.util.Optional;

import javax.sql.DataSource;

import es.unizar.padel.dao.jdbc.JdbcClubDao;
import es.unizar.padel.dao.jdbc.JdbcFavoritoDao;
import es.unizar.padel.dao.jdbc.JdbcPistaDao;
import es.unizar.padel.db.Database;
import es.unizar.padel.vo.Club;
import es.unizar.padel.vo.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** Segundo paso: pistas de un club (/club?id=ID), con el botón de marcar o quitar de favoritos. */
@WebServlet("/club")
public class ClubServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DataSource ds = Database.getDataSource();
        Optional<Club> club;
        try {
            club = new JdbcClubDao(ds).findById(Long.parseLong(req.getParameter("id")));
        } catch (NumberFormatException e) {
            club = Optional.empty();
        }
        if (club.isEmpty()) {
            resp.sendRedirect("clubes");
            return;
        }
        req.setAttribute("club", club.get());
        // Si hay sesión, se indica si el club ya es favorito (para el botón de favoritos)
        Usuario user = (Usuario) req.getSession().getAttribute(LoginServlet.USUARIO_SESION);
        if (user != null) {
            req.setAttribute("esFavorito",
                    new JdbcFavoritoDao(ds).findById(user.getId(), club.get().getIdClub()).isPresent());
        }
        req.setAttribute("error", req.getSession().getAttribute("error"));
        req.getSession().removeAttribute("error");
        req.setAttribute("pistas", new JdbcPistaDao(ds).findByClub(club.get().getIdClub()));
        req.getRequestDispatcher("/WEB-INF/jsp/club.jsp").forward(req, resp);
    }
}
