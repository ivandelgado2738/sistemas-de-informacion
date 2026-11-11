package es.unizar.padel.web;

import java.io.IOException;

import es.unizar.padel.dao.DaoException;
import es.unizar.padel.dao.jdbc.JdbcUsuarioDao;
import es.unizar.padel.db.Database;
import es.unizar.padel.funcionalidades.GestionUsuarios;
import es.unizar.padel.funcionalidades.ModeloException;
import es.unizar.padel.vo.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Edición de un usuario por el administrador (/editarUsuario?id=ID).
 * GET  : formulario con nombre, email y estado actuales.
 * POST : guarda nombre y email (GestionUsuarios.Editar) y, si cambia, el estado (Bloquear/Activar).
 */
@WebServlet("/editarUsuario")
public class EditarUsuarioServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("u", caso().Ver(Long.parseLong(req.getParameter("id"))));
            req.getRequestDispatcher("/WEB-INF/jsp/editarUsuario.jsp").forward(req, resp);
        } catch (ModeloException | NumberFormatException e) {
            resp.sendRedirect("gestionUsuarios");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Usuario admin = (Usuario) req.getSession().getAttribute(LoginServlet.USUARIO_SESION);
        long id;
        try {
            id = Long.parseLong(req.getParameter("id"));
        } catch (NumberFormatException e) {
            resp.sendRedirect("gestionUsuarios");
            return;
        }
        GestionUsuarios caso = caso();
        try {
            caso.Editar(id, req.getParameter("nombre"), req.getParameter("email"));

            String estado = req.getParameter("estado");
            Usuario actual = caso.Ver(id);
            if (estado != null && !estado.equals(actual.getEstado())) {
                if (id == admin.getId()) {
                    throw new ModeloException("No puedes bloquear tu propia cuenta");
                }
                if (GestionUsuarios.ESTADO_BLOQUEADO.equals(estado)) {
                    caso.Bloquear(id);
                } else {
                    caso.Activar(id);
                }
            }
            req.getSession().setAttribute("mensaje", "Cambios guardados.");
            resp.sendRedirect("gestionUsuarios");

        } catch (ModeloException e) {
            volverConError(req, resp, caso, id, e.getMessage());
        } catch (DaoException e) {
            // El email es UNIQUE en la base de datos
            volverConError(req, resp, caso, id, "Ese email ya lo usa otra cuenta");
        }
    }

    private void volverConError(HttpServletRequest req, HttpServletResponse resp, GestionUsuarios caso,
                                long id, String error) throws ServletException, IOException {
        req.setAttribute("error", error);
        req.setAttribute("u", caso.Ver(id));
        req.getRequestDispatcher("/WEB-INF/jsp/editarUsuario.jsp").forward(req, resp);
    }

    private GestionUsuarios caso() {
        return new GestionUsuarios(new JdbcUsuarioDao(Database.getDataSource()));
    }
}
