package es.unizar.padel.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import es.unizar.padel.dao.JuegaDao;
import es.unizar.padel.dao.jdbc.JdbcClubDao;
import es.unizar.padel.dao.jdbc.JdbcHorarioDao;
import es.unizar.padel.dao.jdbc.JdbcJuegaDao;
import es.unizar.padel.dao.jdbc.JdbcPartidoDao;
import es.unizar.padel.db.Database;
import es.unizar.padel.funcionalidades.CancelarReserva;
import es.unizar.padel.vo.Club;
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
 * Pantalla "Mis partidos": los partidos en los que juega el usuario de la sesión,
 * con su club, pista, fecha y hora, y el estado de la cancelación si hay una en curso.
 */
@WebServlet("/partidos")
public class PartidosServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession sesion = req.getSession();
        Usuario user = (Usuario) sesion.getAttribute(LoginServlet.USUARIO_SESION);
        DataSource ds = Database.getDataSource();
        JuegaDao juegaDao = new JdbcJuegaDao(ds);

        // Horarios y clubes indexados para no consultar la base de datos una vez por partido
        Map<Long, Horario> horarioPorReserva = new HashMap<>();
        for (Horario h : new JdbcHorarioDao(ds).findAll()) {
            if (h.getIdReserva() != null) {
                horarioPorReserva.put(h.getIdReserva(), h);
            }
        }
        Map<Long, String> nombreClub = new HashMap<>();
        for (Club c : new JdbcClubDao(ds).findAll()) {
            nombreClub.put(c.getIdClub(), c.getNombre());
        }
        Map<Long, CancelarReserva> enCurso = Cancelaciones.de(getServletContext());

        List<FilaPartido> filas = new ArrayList<>();
        JdbcPartidoDao partidoDao = new JdbcPartidoDao(ds);
        for (Juega j : juegaDao.findAll()) {
            if (j.getIdUsuario() != user.getId()) {
                continue;
            }
            Partido p = partidoDao.findById(j.getIdPartido()).orElse(null);
            Horario h = (p == null) ? null : horarioPorReserva.get(p.getIdReserva());
            if (h == null) {
                continue;
            }
            int jugadores = juegaDao.findByPartido(p.getIdPartido()).size();
            CancelarReserva c = enCurso.get(p.getIdReserva());
            int aceptados = (c == null) ? 0 : c.NumeroAceptados();
            boolean yo = c != null && c.HaAceptado(user.getId());
            filas.add(new FilaPartido(p, h, nombreClub.getOrDefault(h.getIdClub(), "Club"),
                    jugadores, aceptados, yo));
        }
        filas.sort(Comparator.comparing(FilaPartido::getIdReserva));

        // Mensaje de la última acción (lo deja CancelarReservaServlet) y se borra tras mostrarlo
        req.setAttribute("mensaje", sesion.getAttribute("mensaje"));
        req.setAttribute("error", sesion.getAttribute("error"));
        sesion.removeAttribute("mensaje");
        sesion.removeAttribute("error");

        req.setAttribute("partidos", filas);
        req.getRequestDispatcher("/WEB-INF/jsp/partidos.jsp").forward(req, resp);
    }
}
