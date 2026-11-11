package es.unizar.padel.web;

import java.io.IOException;
import java.util.Map;

import javax.sql.DataSource;

import es.unizar.padel.dao.jdbc.JdbcHorarioDao;
import es.unizar.padel.dao.jdbc.JdbcJuegaDao;
import es.unizar.padel.dao.jdbc.JdbcPartidoDao;
import es.unizar.padel.dao.jdbc.JdbcReservaDao;
import es.unizar.padel.db.Database;
import es.unizar.padel.funcionalidades.CancelarReserva;
import es.unizar.padel.funcionalidades.ModeloException;
import es.unizar.padel.vo.Usuario;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Botón "Cancelar reserva" de Mis partidos.
 * El jugador da su conformidad (CancelarReserva.Aceptar). Si con él ya han aceptado todos,
 * se ejecuta la cancelación; si no, queda pendiente hasta que acepten los demás.
 */
@WebServlet("/cancelarReserva")
public class CancelarReservaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession sesion = req.getSession();
        Usuario user = (Usuario) sesion.getAttribute(LoginServlet.USUARIO_SESION);
        Map<Long, CancelarReserva> enCurso = Cancelaciones.de(getServletContext());

        try {
            long idReserva = Long.parseLong(req.getParameter("reserva"));
            DataSource ds = Database.getDataSource();

            CancelarReserva caso;
            synchronized (enCurso) {
                caso = enCurso.get(idReserva);
                if (caso == null) {
                    caso = new CancelarReserva(idReserva, new JdbcReservaDao(ds), new JdbcHorarioDao(ds),
                            new JdbcPartidoDao(ds), new JdbcJuegaDao(ds));
                    enCurso.put(idReserva, caso);
                }
            }

            caso.Aceptar(user.getId());
            if (caso.TodosDeAcuerdo()) {
                caso.Ejecutar();
                enCurso.remove(idReserva);
                sesion.setAttribute("mensaje", "Reserva cancelada. El horario vuelve a estar libre.");
            } else {
                sesion.setAttribute("mensaje", "Has aceptado cancelar la reserva. Falta que acepten los demás jugadores.");
            }
        } catch (ModeloException e) {
            sesion.setAttribute("error", e.getMessage());
        } catch (NumberFormatException e) {
            sesion.setAttribute("error", "Reserva no válida");
        }
        resp.sendRedirect("partidos");
    }
}
