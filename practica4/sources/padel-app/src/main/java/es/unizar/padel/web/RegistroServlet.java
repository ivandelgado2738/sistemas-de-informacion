package es.unizar.padel.web;

import java.io.IOException;

import es.unizar.padel.dao.jdbc.JdbcUsuarioDao;
import es.unizar.padel.db.Database;
import es.unizar.padel.funcionalidades.ModeloException;
import es.unizar.padel.funcionalidades.Registrarse;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Controlador del registro de nuevos clientes.
 * GET  /registro : muestra el formulario.
 * POST /registro : comprueba que las dos contraseñas coinciden y crea la cuenta con Registrarse.
 *                  Si va bien, manda al login con un aviso; si no, vuelve al formulario con el error.
 */
@WebServlet("/registro")
public class RegistroServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/registro.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String nombre = req.getParameter("nombre");
        String email = req.getParameter("email");
        String contrasena = req.getParameter("password");
        String repetida = req.getParameter("password2");

        try {
            // Validación propia de la vista: el modelo no sabe que en el formulario se escribe dos veces
            if (contrasena == null || !contrasena.equals(repetida)) {
                throw new ModeloException("Las contraseñas no coinciden");
            }
            new Registrarse(new JdbcUsuarioDao(Database.getDataSource()), nombre, email, contrasena).Registro();
            resp.sendRedirect("login?registrado=1");

        } catch (ModeloException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("nombre", nombre);
            req.setAttribute("email", email);
            req.getRequestDispatcher("/WEB-INF/jsp/registro.jsp").forward(req, resp);
        }
    }
}
