package es.unizar.padel.web;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import es.unizar.padel.funcionalidades.CancelarReserva;
import jakarta.servlet.ServletContext;

/**
 * Cancelaciones de reserva en curso, compartidas por toda la aplicación.
 * CancelarReserva va apuntando qué jugadores han aceptado; como cada clic es una petición
 * distinta, el objeto se guarda aquí (en el ServletContext) para que los "sí" se acumulen.
 * Nota: se pierden si se reinicia Tomcat (no se guardan en la base de datos).
 */
public final class Cancelaciones {
    private static final String ATRIBUTO = "cancelaciones";

    private Cancelaciones() {
    }

    @SuppressWarnings("unchecked")
    public static synchronized Map<Long, CancelarReserva> de(ServletContext ctx) {
        Map<Long, CancelarReserva> mapa = (Map<Long, CancelarReserva>) ctx.getAttribute(ATRIBUTO);
        if (mapa == null) {
            mapa = new ConcurrentHashMap<>();
            ctx.setAttribute(ATRIBUTO, mapa);
        }
        return mapa;
    }
}
