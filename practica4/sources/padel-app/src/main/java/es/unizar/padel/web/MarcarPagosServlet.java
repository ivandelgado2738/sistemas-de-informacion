package es.unizar.padel.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.sql.DataSource;

import es.unizar.padel.dao.UsuarioDao;
import es.unizar.padel.dao.jdbc.JdbcClubDao;
import es.unizar.padel.dao.jdbc.JdbcHorarioDao;
import es.unizar.padel.dao.jdbc.JdbcJuegaDao;
import es.unizar.padel.dao.jdbc.JdbcPartidoDao;
import es.unizar.padel.dao.jdbc.JdbcUsuarioDao;
import es.unizar.padel.db.Database;
import es.unizar.padel.funcionalidades.GestionPagos;
import es.unizar.padel.funcionalidades.ModeloException;
import es.unizar.padel.vo.Horario;
import es.unizar.padel.vo.Juega;
import es.unizar.padel.vo.Partido;
import es.unizar.padel.vo.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Pantalla de pagos de una reserva (administrador): /marcarPagos?reserva=ID.
 * GET  : jugadores del partido con su estado de pago (GestionPagos.VerPagos).
 * POST : marca a un jugador como pagado o no pagado y vuelve a la misma pantalla.
 */
@WebServlet("/marcarPagos")
public class MarcarPagosServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DataSource ds = Database.getDataSource();
        Optional<Partido> partido = partidoDeReserva(req.getParameter("reserva"), ds);
        if (partido.isEmpty()) {
            resp.sendRedirect("verReservas");
            return;
        }
        long idReserva = partido.get().getIdReserva();
        Horario horario = new JdbcHorarioDao(ds).findAll().stream()
                .filter(h -> h.getIdReserva() != null && h.getIdReserva() == idReserva)
                .findFirst().orElse(null);

        GestionPagos caso = new GestionPagos(partido.get().getIdPartido(), new JdbcPartidoDao(ds), new JdbcJuegaDao(ds));
        List<Juega> jugadores = caso.VerPagos();

        // El precio de la pista se reparte entre los jugadores apuntados
        String importe = "-";
        if (horario != null && !jugadores.isEmpty()) {
            importe = String.format(Locale.forLanguageTag("es-ES"), "%.2f €",
                    (double) horario.getPrecio() / jugadores.size());
        }
        UsuarioDao usuarioDao = new JdbcUsuarioDao(ds);
        List<FilaPago> filas = new ArrayList<>();
        for (Juega j : jugadores) {
            Usuario u = usuarioDao.findById(j.getIdUsuario()).orElse(null);
            filas.add(new FilaPago(j.getIdUsuario(), u == null ? "?" : u.getNombre(),
                    u == null ? "" : u.getEmail(), importe, j.isHaPagado()));
        }

        if (horario != null) {
            req.setAttribute("horario", new FilaReserva(horario, jugadores.size()));
            req.setAttribute("club", new JdbcClubDao(ds).findById(horario.getIdClub()).orElse(null));
        }
        HttpSession sesion = req.getSession();
        req.setAttribute("error", sesion.getAttribute("error"));
        sesion.removeAttribute("error");
        req.setAttribute("idReserva", idReserva);
        req.setAttribute("todosPagado", caso.TodosHanPagado());
        req.setAttribute("jugadores", filas);
        req.getRequestDispatcher("/WEB-INF/jsp/marcarPagos.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String reserva = req.getParameter("reserva");
        DataSource ds = Database.getDataSource();
        try {
            Partido partido = partidoDeReserva(reserva, ds)
                    .orElseThrow(() -> new ModeloException("La reserva no tiene partido"));
            long idUsuario = Long.parseLong(req.getParameter("usuario"));
            GestionPagos caso = new GestionPagos(partido.getIdPartido(), new JdbcPartidoDao(ds), new JdbcJuegaDao(ds));
            if ("pagado".equals(req.getParameter("accion"))) {
                caso.MarcarPagado(idUsuario);
            } else {
                caso.MarcarNoPagado(idUsuario);
            }
        } catch (ModeloException e) {
            req.getSession().setAttribute("error", e.getMessage());
        } catch (NumberFormatException e) {
            req.getSession().setAttribute("error", "Datos no válidos");
        }
        resp.sendRedirect("marcarPagos?reserva=" + (reserva == null ? "" : reserva.replaceAll("\\D", "")));
    }

    /** Partido asociado a una reserva (vacío si el parámetro no es válido o no existe). */
    private Optional<Partido> partidoDeReserva(String param, DataSource ds) {
        try {
            long idReserva = Long.parseLong(param);
            return new JdbcPartidoDao(ds).findAll().stream()
                    .filter(p -> p.getIdReserva() == idReserva)
                    .findFirst();
        } catch (NumberFormatException | NullPointerException e) {
            return Optional.empty();
        }
    }
}
