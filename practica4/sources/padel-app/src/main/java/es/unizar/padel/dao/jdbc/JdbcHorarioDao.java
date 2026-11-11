package es.unizar.padel.dao.jdbc;

import es.unizar.padel.dao.HorarioDao;
import es.unizar.padel.vo.Horario;
import es.unizar.padel.dao.DaoException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalTime;

/** Implementación de HorarioDao con JDBC. */
public final class JdbcHorarioDao implements HorarioDao {

    private static final String COLUMNAS =
            "id_club, numero_pista, fecha, hora_inicio, hora_fin, precio, id_reserva";

    private final DataSource dataSource;

    public JdbcHorarioDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Horario create(Horario h) {
        final String sql = "INSERT INTO horario (" + COLUMNAS + ") VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, h.getIdClub());
            ps.setInt(2, h.getNumeroPista());
            ps.setObject(3, h.getFecha());
            ps.setObject(4, h.getHoraInicio());
            ps.setObject(5, h.getHoraFin());
            ps.setInt(6, h.getPrecio());
            setIdReserva(ps, 7, h.getIdReserva());
            ps.executeUpdate();
            return h;
        } catch (SQLException e) {
            throw new DaoException("No se pudo crear el horario " + h.getFecha() + " "
                    + h.getHoraInicio() + " (pista " + h.getNumeroPista()
                    + ", club " + h.getIdClub() + ")", e);
        }
    }

    @Override
    public Optional<Horario> findById(long idClub, int numeroPista, LocalDate fecha, LocalTime horaInicio) {
        final String sql = "SELECT " + COLUMNAS + " FROM horario "
                + "WHERE id_club = ? AND numero_pista = ? AND fecha = ? AND hora_inicio = ?";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, idClub);
            ps.setInt(2, numeroPista);
            ps.setObject(3, fecha);
            ps.setObject(4, horaInicio);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DaoException("No se pudo leer el horario " + fecha + " " + horaInicio, e);
        }
    }

    @Override
    public List<Horario> findLibres(long idClub, LocalDate fecha) {
        final String sql = "SELECT " + COLUMNAS + " FROM horario "
                + "WHERE id_club = ? AND fecha = ? AND id_reserva IS NULL "
                + "ORDER BY numero_pista, hora_inicio";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, idClub);
            ps.setObject(2, fecha);
            try (ResultSet rs = ps.executeQuery()) {
                List<Horario> lista = new ArrayList<>();
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
                return lista;
            }
        } catch (SQLException e) {
            throw new DaoException("No se pudo listar los horarios libres del club " + idClub, e);
        }
    }

    @Override
    public List<Horario> findAll() {
        final String sql = "SELECT " + COLUMNAS + " FROM horario "
                + "ORDER BY id_club, numero_pista, fecha, hora_inicio";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Horario> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new DaoException("No se pudo listar los horarios", e);
        }
    }

    @Override
    public boolean update(Horario h) {
        final String sql = "UPDATE horario SET hora_fin = ?, precio = ?, id_reserva = ? "
                + "WHERE id_club = ? AND numero_pista = ? AND fecha = ? AND hora_inicio = ?";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, h.getHoraFin());
            ps.setInt(2, h.getPrecio());
            setIdReserva(ps, 3, h.getIdReserva());
            ps.setLong(4, h.getIdClub());
            ps.setInt(5, h.getNumeroPista());
            ps.setObject(6, h.getFecha());
            ps.setObject(7, h.getHoraInicio());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("No se pudo actualizar el horario " + h.getFecha() + " "
                    + h.getHoraInicio(), e);
        }
    }

    @Override
    public boolean delete(long idClub, int numeroPista, LocalDate fecha, LocalTime horaInicio) {
        final String sql = "DELETE FROM horario "
                + "WHERE id_club = ? AND numero_pista = ? AND fecha = ? AND hora_inicio = ?";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, idClub);
            ps.setInt(2, numeroPista);
            ps.setObject(3, fecha);
            ps.setObject(4, horaInicio);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("No se pudo borrar el horario " + fecha + " " + horaInicio, e);
        }
    }

    /** id_reserva admite NULL (horario libre), así que hay que distinguirlo. */
    private static void setIdReserva(PreparedStatement ps, int pos, Long idReserva) throws SQLException {
        if (idReserva == null) {
            ps.setNull(pos, Types.BIGINT);
        } else {
            ps.setLong(pos, idReserva);
        }
    }

    private Horario mapear(ResultSet rs) throws SQLException {
        return new Horario(
                rs.getLong("id_club"),
                rs.getInt("numero_pista"),
                rs.getObject("fecha", LocalDate.class),
                rs.getObject("hora_inicio", LocalTime.class),
                rs.getObject("hora_fin", LocalTime.class),
                rs.getInt("precio"),
                rs.getObject("id_reserva", Long.class));
    }
}
