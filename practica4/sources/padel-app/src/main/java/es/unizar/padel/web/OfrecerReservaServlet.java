package es.unizar.padel.web;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import es.unizar.padel.dao.jdbc.JdbcClubDao;
import es.unizar.padel.dao.jdbc.JdbcHorarioDao;
import es.unizar.padel.dao.jdbc.JdbcJuegaDao;
import es.unizar.padel.dao.jdbc.JdbcPartidoDao;
import es.unizar.padel.dao.jdbc.JdbcPistaDao;
import es.unizar.padel.db.Database;
import es.unizar.padel.funcionalidades.GestionReserva;
import es.unizar.padel.funcionalidades.ModeloException;
import es.unizar.padel.vo.Club;
import es.unizar.padel.vo.Pista;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Controlador de ofrecerReserva (administrador).
 * GET  : muestra el formulario con el desplegable de pistas sacado de la base de datos.
 * POST : crea el horario con GestionReserva.OfrecerHorario y vuelve a verReservas de ese club.
 */
@WebServlet("/ofrecerReserva")
public class OfrecerReservaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        mostrarFormulario(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            // El desplegable envía "idClub-numero"
            String[] pista = req.getParameter("pista").split("-");
            long idClub = Long.parseLong(pista[0]);
            int numero = Integer.parseInt(pista[1]);
            LocalDate fecha = LocalDate.parse(req.getParameter("fecha"));
            LocalTime inicio = LocalTime.parse(req.getParameter("horaInicio"));
            LocalTime fin = LocalTime.parse(req.getParameter("horaFin"));
            int precio = Integer.parseInt(req.getParameter("precio"));

            DataSource ds = Database.getDataSource();
            new GestionReserva(idClub, new JdbcPistaDao(ds), new JdbcHorarioDao(ds),
                    new JdbcPartidoDao(ds), new JdbcJuegaDao(ds))
                    .OfrecerHorario(numero, fecha, inicio, fin, precio);

            resp.sendRedirect("verReservas?club=" + idClub);

        } catch (ModeloException e) {
            req.setAttribute("error", e.getMessage());
            mostrarFormulario(req, resp);
        } catch (NumberFormatException | DateTimeParseException | NullPointerException
                 | ArrayIndexOutOfBoundsException e) {
            req.setAttribute("error", "Revisa los datos del formulario");
            mostrarFormulario(req, resp);
        }
    }

    /** Prepara el desplegable de pistas ("Club - Pista N") y muestra el JSP. */
    private void mostrarFormulario(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        DataSource ds = Database.getDataSource();
        Map<Long, String> nombres = new HashMap<>();
        for (Club c : new JdbcClubDao(ds).findAll()) {
            nombres.put(c.getIdClub(), c.getNombre());
        }
        List<OpcionPista> opciones = new ArrayList<>();
        for (Pista p : new JdbcPistaDao(ds).findAll()) {
            opciones.add(new OpcionPista(p.getIdClub(), p.getNumero(), nombres.getOrDefault(p.getIdClub(), "Club")));
        }
        req.setAttribute("pistas", opciones);
        req.getRequestDispatcher("/WEB-INF/jsp/ofrecerReserva.jsp").forward(req, resp);
    }
}
