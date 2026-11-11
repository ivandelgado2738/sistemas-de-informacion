package es.unizar.padel.web;

import java.io.IOException;

import javax.sql.DataSource;

import es.unizar.padel.dao.jdbc.JdbcClubDao;
import es.unizar.padel.dao.jdbc.JdbcFavoritoDao;
import es.unizar.padel.dao.jdbc.JdbcUsuarioDao;
import es.unizar.padel.db.Database;
import es.unizar.padel.funcionalidades.GestionFavoritos;
import es.unizar.padel.funcionalidades.ModeloException;
import es.unizar.padel.vo.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Clubes favoritos del usuario de la sesión.
 * GET  /favoritos : lista sus clubes favoritos.
 * POST /favoritos : marca (accion=marcar) o quita (accion=quitar) un club y vuelve a la página indicada en "volver".
 */
@WebServlet("/favoritos")
public class FavoritosServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession sesion = req.getSession();
        req.setAttribute("clubes", caso(req).Listar());
        req.setAttribute("error", sesion.getAttribute("error"));
        sesion.removeAttribute("error");
        req.getRequestDispatcher("/WEB-INF/jsp/favoritos.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            long idClub = Long.parseLong(req.getParameter("club"));
            if ("quitar".equals(req.getParameter("accion"))) {
                caso(req).Quitar(idClub);
            } else {
                caso(req).Marcar(idClub);
            }
        } catch (ModeloException e) {
            req.getSession().setAttribute("error", e.getMessage());
        } catch (NumberFormatException e) {
            req.getSession().setAttribute("error", "Club no válido");
        }
        // Solo se permite volver a páginas internas de la aplicación
        String volver = req.getParameter("volver");
        resp.sendRedirect(volver != null && volver.matches("(favoritos|club\\?id=\\d+)") ? volver : "favoritos");
    }

    private GestionFavoritos caso(HttpServletRequest req) {
        Usuario user = (Usuario) req.getSession().getAttribute(LoginServlet.USUARIO_SESION);
        DataSource ds = Database.getDataSource();
        return new GestionFavoritos(user.getId(), new JdbcFavoritoDao(ds), new JdbcUsuarioDao(ds), new JdbcClubDao(ds));
    }
}
