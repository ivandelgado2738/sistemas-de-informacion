package es.unizar.padel.web;

import java.io.IOException;

import es.unizar.padel.dao.jdbc.JdbcUsuarioDao;
import es.unizar.padel.db.Database;
import es.unizar.padel.funcionalidades.GestionPerfil;
import es.unizar.padel.funcionalidades.ModeloException;
import es.unizar.padel.vo.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Baja de la cuenta del usuario de la sesión.
 * GET  /baja : pantalla de confirmación.
 * POST /baja : comprueba la contraseña, borra la cuenta (y en cascada sus reservas, favoritos...)
 *              y cierra la sesión.
 */
@WebServlet("/baja")
public class BajaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/confirmarBaja.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession sesion = req.getSession();
        Usuario user = (Usuario) sesion.getAttribute(LoginServlet.USUARIO_SESION);
        try {
            new GestionPerfil(user.getId(), new JdbcUsuarioDao(Database.getDataSource()))
                    .DarseDeBaja(req.getParameter("password"));
            sesion.invalidate();
            resp.sendRedirect("login?baja=1");
        } catch (ModeloException e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/jsp/confirmarBaja.jsp").forward(req, resp);
        }
    }
}
