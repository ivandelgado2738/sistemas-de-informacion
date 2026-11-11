package es.unizar.padel.web;

import java.io.IOException;

import es.unizar.padel.dao.jdbc.JdbcUsuarioDao;
import es.unizar.padel.db.Database;
import es.unizar.padel.funcionalidades.IniciarSesion;
import es.unizar.padel.funcionalidades.ModeloException;
import es.unizar.padel.vo.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Controlador del inicio de sesión.
 * GET  /login : muestra el formulario.
 * POST /login : comprueba email y contraseña con IniciarSesion; si son correctos guarda el usuario
 *               en la sesión y redirige a la pantalla principal de admin o de cliente.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /** Nombre con el que se guarda el usuario en la sesión. Lo usan el filtro y el resto de servlets. */
    public static final String USUARIO_SESION = "usuario";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/inicioSesion.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String contrasena = req.getParameter("password");

        IniciarSesion caso = new IniciarSesion(new JdbcUsuarioDao(Database.getDataSource()));
        try {
            Usuario user = caso.Entrar(email, contrasena);

            // Sesión nueva al entrar (evita reutilizar un identificador de sesión anterior)
            HttpSession vieja = req.getSession(false);
            if (vieja != null) {
                vieja.invalidate();
            }
            req.getSession(true).setAttribute(USUARIO_SESION, user);

            String destino = caso.EsAdministrador(user) ? "principalAdmin.html" : "principalRegistrado.html";
            resp.sendRedirect(destino);

        } catch (ModeloException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("email", email);
            req.getRequestDispatcher("/WEB-INF/jsp/inicioSesion.jsp").forward(req, resp);
        }
    }
}
