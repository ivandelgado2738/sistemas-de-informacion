package es.unizar.padel.web;

import java.io.IOException;

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
 * Filtro de seguridad para las pantallas que necesitan un usuario identificado (cualquier rol).
 * Si no hay sesión iniciada, manda al inicio de sesión.
 */
@WebFilter(urlPatterns = { "/reserva", "/principalRegistrado.html", "/partidos", "/cancelarReserva", "/favoritos", "/perfil", "/baja" })
public class FiltroSesion implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession s = req.getSession(false);
        if (s == null || s.getAttribute(LoginServlet.USUARIO_SESION) == null) {
            ((HttpServletResponse) response).sendRedirect(req.getContextPath() + "/login");
            return;
        }
        chain.doFilter(request, response);
    }
}
