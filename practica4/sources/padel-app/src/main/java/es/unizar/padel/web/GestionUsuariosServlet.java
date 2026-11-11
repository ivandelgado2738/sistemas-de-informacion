package es.unizar.padel.web;

import java.io.IOException;

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
import jakarta.servlet.http.HttpSession;

/**
 * Gestión de usuarios (administrador).
 * GET  /gestionUsuarios : listado de todos los usuarios.
 * POST /gestionUsuarios : bloquea (accion=bloquear) o activa (accion=activar) un usuario.
 * Un usuario bloqueado no puede iniciar sesión (lo comprueba IniciarSesion).
 */
@WebServlet("/gestionUsuarios")
public class GestionUsuariosServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession sesion = req.getSession();
        req.setAttribute("usuarios", caso().Listar());
        Mensajes.pasarAlJsp(sesion, req);
        req.getRequestDispatcher("/WEB-INF/jsp/gestionUsuarios.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession sesion = req.getSession();
        Usuario admin = (Usuario) sesion.getAttribute(LoginServlet.USUARIO_SESION);
        try {
            long id = Long.parseLong(req.getParameter("usuario"));
            if (id == admin.getId()) {
                throw new ModeloException("No puedes bloquear tu propia cuenta");
            }
            if ("bloquear".equals(req.getParameter("accion"))) {
                caso().Bloquear(id);
                sesion.setAttribute("mensaje", "Usuario bloqueado: ya no puede iniciar sesión.");
            } else {
                caso().Activar(id);
                sesion.setAttribute("mensaje", "Usuario activado.");
            }
        } catch (ModeloException e) {
            sesion.setAttribute("error", e.getMessage());
        } catch (NumberFormatException e) {
            sesion.setAttribute("error", "Usuario no válido");
        }
        resp.sendRedirect("gestionUsuarios");
    }

    private GestionUsuarios caso() {
        return new GestionUsuarios(new JdbcUsuarioDao(Database.getDataSource()));
    }
}
