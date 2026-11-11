package es.unizar.padel.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import es.unizar.padel.dao.jdbc.JdbcClubDao;
import es.unizar.padel.dao.jdbc.JdbcHorarioDao;
import es.unizar.padel.dao.jdbc.JdbcJuegaDao;
import es.unizar.padel.dao.jdbc.JdbcPartidoDao;
import es.unizar.padel.dao.jdbc.JdbcPistaDao;
import es.unizar.padel.db.Database;
import es.unizar.padel.funcionalidades.GestionReserva;
import es.unizar.padel.vo.Club;
import es.unizar.padel.vo.Horario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Controlador de la pantalla verReservas (administrador).
 * Lee los horarios del club elegido con GestionReserva y se los pasa al JSP para pintar la tabla.
 * El club se elige con el parámetro ?club=ID; si no se indica, se muestra el primero.
 */
@WebServlet("/verReservas")
public class VerReservasServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DataSource ds = Database.getDataSource();

        List<Club> clubes = new JdbcClubDao(ds).findAll();
        req.setAttribute("clubes", clubes);

        if (clubes.isEmpty()) {
            req.setAttribute("filas", new ArrayList<FilaReserva>());
            req.getRequestDispatcher("/WEB-INF/jsp/verReservas.jsp").forward(req, resp);
            return;
        }

        Long idClub = clubes.get(0).getIdClub();
        String param = req.getParameter("club");
        if (param != null) {
            try {
                idClub = Long.parseLong(param);
            } catch (NumberFormatException e) {
                // parámetro inválido: se queda el primer club
            }
        }

        GestionReserva caso = new GestionReserva(idClub, new JdbcPistaDao(ds), new JdbcHorarioDao(ds),
                new JdbcPartidoDao(ds), new JdbcJuegaDao(ds));

        List<FilaReserva> filas = new ArrayList<>();
        for (Horario h : caso.VerHorarios()) {
            filas.add(new FilaReserva(h, caso.NumeroJugadores(h)));
        }

        req.setAttribute("idClub", idClub);
        req.setAttribute("filas", filas);
        req.getRequestDispatcher("/WEB-INF/jsp/verReservas.jsp").forward(req, resp);
    }
}
