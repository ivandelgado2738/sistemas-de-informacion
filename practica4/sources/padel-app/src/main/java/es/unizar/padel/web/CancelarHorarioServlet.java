package es.unizar.padel.web;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import javax.sql.DataSource;

import es.unizar.padel.dao.jdbc.JdbcHorarioDao;
import es.unizar.padel.dao.jdbc.JdbcJuegaDao;
import es.unizar.padel.dao.jdbc.JdbcPartidoDao;
import es.unizar.padel.dao.jdbc.JdbcPistaDao;
import es.unizar.padel.db.Database;
import es.unizar.padel.funcionalidades.GestionReserva;
import es.unizar.padel.funcionalidades.ModeloException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Controlador del botón "Cancelar" de verReservas.
 * Llama a GestionReserva.CancelarHorario y muestra cancelar_exito o cancelar_error.
 */
@WebServlet("/cancelarHorario")
public class CancelarHorarioServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String club = req.getParameter("club");
        req.setAttribute("idClub", club);
        try {
            long idClub = Long.parseLong(club);
            int pista = Integer.parseInt(req.getParameter("pista"));
            LocalDate fecha = LocalDate.parse(req.getParameter("fecha"));
            LocalTime hora = LocalTime.parse(req.getParameter("hora"));

            DataSource ds = Database.getDataSource();
            GestionReserva caso = new GestionReserva(idClub, new JdbcPistaDao(ds), new JdbcHorarioDao(ds),
                    new JdbcPartidoDao(ds), new JdbcJuegaDao(ds));

            caso.CancelarHorario(pista, fecha, hora);

            req.setAttribute("mensaje", "El horario de la pista " + pista + " del día "
                    + req.getParameter("fecha") + " a las " + req.getParameter("hora")
                    + " ha sido cancelado correctamente.");
            req.getRequestDispatcher("/WEB-INF/jsp/cancelar_exito.jsp").forward(req, resp);

        } catch (ModeloException e) {
            req.setAttribute("mensaje", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/jsp/cancelar_error.jsp").forward(req, resp);
        } catch (NumberFormatException | DateTimeParseException | NullPointerException e) {
            req.setAttribute("mensaje", "Datos del horario no válidos");
            req.getRequestDispatcher("/WEB-INF/jsp/cancelar_error.jsp").forward(req, resp);
        }
    }
}
