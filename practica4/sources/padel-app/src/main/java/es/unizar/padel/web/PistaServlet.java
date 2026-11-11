package es.unizar.padel.web;

import java.io.IOException;
import java.time.LocalDateTime;
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
import es.unizar.padel.vo.Horario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Tercer paso: horarios libres y futuros de una pista (/pista?club=ID&numero=N).
 * Usa GestionReserva.VerHorarios() y se queda con los de esa pista que no están reservados.
 */
@WebServlet("/pista")
public class PistaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long idClub;
        int numero;
        try {
            idClub = Long.parseLong(req.getParameter("club"));
            numero = Integer.parseInt(req.getParameter("numero"));
        } catch (NumberFormatException e) {
            resp.sendRedirect("clubes");
            return;
        }
        DataSource ds = Database.getDataSource();
        GestionReserva caso = new GestionReserva(idClub, new JdbcPistaDao(ds), new JdbcHorarioDao(ds),
                new JdbcPartidoDao(ds), new JdbcJuegaDao(ds));

        LocalDateTime ahora = LocalDateTime.now();
        List<FilaReserva> libres = new ArrayList<>();
        for (Horario h : caso.VerHorarios()) {
            boolean estaPista = h.getNumeroPista() == numero;
            boolean libre = h.getIdReserva() == null;
            boolean futuro = LocalDateTime.of(h.getFecha(), h.getHoraInicio()).isAfter(ahora);
            if (estaPista && libre && futuro) {
                libres.add(new FilaReserva(h, 0));
            }
        }

        req.setAttribute("club", new JdbcClubDao(ds).findById(idClub).orElse(null));
        req.setAttribute("idClub", idClub);
        req.setAttribute("numero", numero);
        req.setAttribute("horarios", libres);
        req.getRequestDispatcher("/WEB-INF/jsp/pista.jsp").forward(req, resp);
    }
}
