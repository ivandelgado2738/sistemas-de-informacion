package es.unizar.padel.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * Mensajes de una sola vez ("Cambios guardados", errores...) que sobreviven a una redirección.
 * El servlet que procesa el formulario los deja en la sesión; el que pinta la página
 * los pasa al JSP y los borra, para que no se vuelvan a mostrar al recargar.
 */
public final class Mensajes {
    private Mensajes() {
    }

    public static void pasarAlJsp(HttpSession sesion, HttpServletRequest req) {
        req.setAttribute("mensaje", sesion.getAttribute("mensaje"));
        req.setAttribute("error", sesion.getAttribute("error"));
        sesion.removeAttribute("mensaje");
        sesion.removeAttribute("error");
    }
}
