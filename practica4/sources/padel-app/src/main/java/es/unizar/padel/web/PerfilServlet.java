package es.unizar.padel.web;

import java.io.IOException;

import es.unizar.padel.dao.DaoException;
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
 * Perfil del usuario de la sesión.
 * GET  /perfil : sus datos y los formularios para cambiarlos.
 * POST /perfil : cambia el nombre (accion=nombre), el email (accion=email) o la contraseña (accion=contrasena).
 */
@WebServlet("/perfil")
public class PerfilServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession sesion = req.getSession();
        req.setAttribute("u", caso(sesion).Ver());
        Mensajes.pasarAlJsp(sesion, req);
        req.getRequestDispatcher("/WEB-INF/jsp/perfil.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession sesion = req.getSession();
        GestionPerfil caso = caso(sesion);
        try {
            switch (String.valueOf(req.getParameter("accion"))) {
                case "nombre" -> caso.CambiarNombre(req.getParameter("nombre"));
                case "email" -> caso.CambiarEmail(req.getParameter("email"));
                case "contrasena" -> {
                    if (!req.getParameter("nueva").equals(req.getParameter("nueva2"))) {
                        throw new ModeloException("Las contraseñas nuevas no coinciden");
                    }
                    caso.CambiarContrasena(req.getParameter("actual"), req.getParameter("nueva"));
                }
                default -> throw new ModeloException("Acción no válida");
            }
            // Se actualiza el usuario guardado en la sesión (la cabecera muestra su nombre)
            sesion.setAttribute(LoginServlet.USUARIO_SESION, caso.Ver());
            sesion.setAttribute("mensaje", "Cambios guardados.");
        } catch (ModeloException e) {
            sesion.setAttribute("error", e.getMessage());
        } catch (DaoException e) {
            sesion.setAttribute("error", "Ese email ya lo usa otra cuenta");
        }
        resp.sendRedirect("perfil");
    }

    private GestionPerfil caso(HttpSession sesion) {
        Usuario user = (Usuario) sesion.getAttribute(LoginServlet.USUARIO_SESION);
        return new GestionPerfil(user.getId(), new JdbcUsuarioDao(Database.getDataSource()));
    }
}
