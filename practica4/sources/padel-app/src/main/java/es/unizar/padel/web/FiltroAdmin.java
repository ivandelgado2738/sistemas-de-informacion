package es.unizar.padel.web;

import java.io.IOException;

import es.unizar.padel.funcionalidades.IniciarSesion;
import es.unizar.padel.vo.Usuario;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Filtro de seguridad: solo deja pasar a las pantallas de administración
 * si hay una sesión iniciada con un usuario de rol ADMIN. Si no, manda al inicio de sesión.
 */
@WebFilter(urlPatterns = {
        "/principalAdmin.html",
        "/gestionUsuarios",
        "/editarUsuario",
        "/marcarPagos",
        "/ofrecerReserva",
        "/verReservas",
        "/cancelarHorario"
})
public class FiltroAdmin implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession s = req.getSession(false);
        Usuario user = (s == null) ? null : (Usuario) s.getAttribute(LoginServlet.USUARIO_SESION);

        if (user == null || !IniciarSesion.ROL_ADMIN.equals(user.getRol())) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        chain.doFilter(request, response);
    }
}
