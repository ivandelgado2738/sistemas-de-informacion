package es.unizar.padel.web;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import javax.sql.DataSource;

import es.unizar.padel.dao.jdbc.JdbcClubDao;
import es.unizar.padel.dao.jdbc.JdbcHorarioDao;
import es.unizar.padel.dao.jdbc.JdbcJuegaDao;
import es.unizar.padel.dao.jdbc.JdbcPartidoDao;
import es.unizar.padel.dao.jdbc.JdbcPistaDao;
import es.unizar.padel.dao.jdbc.JdbcReservaDao;
import es.unizar.padel.dao.jdbc.JdbcUsuarioDao;
import es.unizar.padel.db.Database;
import es.unizar.padel.funcionalidades.ModeloException;
import es.unizar.padel.funcionalidades.reservar;
import es.unizar.padel.vo.Horario;
import es.unizar.padel.vo.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Último paso: confirmar y hacer la reserva (/reserva?club=ID&numero=N&fecha=AAAA-MM-DD&hora=HH:MM).
 * GET  : muestra los datos del horario (club, pista, fecha, hora, precio) para confirmar.
 * POST : hace la reserva con el caso de uso "reservar" para el usuario de la sesión.
 * Requiere sesión iniciada (lo comprueba FiltroSesion).
 */
@WebServlet("/reserva")
public class ReservaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!cargarDatos(req)) {
            resp.sendRedirect("clubes");
            return;
        }
        req.getRequestDispatcher("/WEB-INF/jsp/reserva.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        if (!cargarDatos(req)) {
            resp.sendRedirect("clubes");
            return;
        }
        Usuario user = (Usuario) req.getSession().getAttribute(LoginServlet.USUARIO_SESION);
        boolean privado = "invitacion".equals(req.getParameter("tipo_partido"));

        DataSource ds = Database.getDataSource();
        reservar caso = new reservar(new JdbcUsuarioDao(ds), new JdbcHorarioDao(ds), new JdbcReservaDao(ds),
                new JdbcPartidoDao(ds), new JdbcJuegaDao(ds), user.getId(),
                (Long) req.getAttribute("idClub"), (Integer) req.getAttribute("numero"),
                LocalDate.parse(req.getParameter("fecha")), LocalTime.parse(req.getParameter("hora")));
        try {
            caso.Reservar(privado);
            req.getRequestDispatcher("/WEB-INF/jsp/reservaConfirmada.jsp").forward(req, resp);
        } catch (ModeloException e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/jsp/reserva.jsp").forward(req, resp);
        }
    }

    /**
     * Lee los parámetros, busca el horario, el club y la pista, y los deja como atributos para el JSP.
     * @return false si los parámetros no son válidos o el horario no existe
     */
    private boolean cargarDatos(HttpServletRequest req) {
        try {
            long idClub = Long.parseLong(req.getParameter("club"));
            int numero = Integer.parseInt(req.getParameter("numero"));
            LocalDate fecha = LocalDate.parse(req.getParameter("fecha"));
            LocalTime hora = LocalTime.parse(req.getParameter("hora"));

            DataSource ds = Database.getDataSource();
            Optional<Horario> horario = new JdbcHorarioDao(ds).findById(idClub, numero, fecha, hora);
            if (horario.isEmpty()) {
                return false;
            }
            req.setAttribute("idClub", idClub);
            req.setAttribute("numero", numero);
            req.setAttribute("horario", new FilaReserva(horario.get(), 0));
            req.setAttribute("club", new JdbcClubDao(ds).findById(idClub).orElse(null));
            req.setAttribute("pista", new JdbcPistaDao(ds).findById(idClub, numero).orElse(null));
            return true;
        } catch (NumberFormatException | DateTimeParseException | NullPointerException e) {
            return false;
        }
    }
}
