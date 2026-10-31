package es.unizar.padel.dao.jdbc;

import es.unizar.padel.dao.ReservaDao;
import es.unizar.padel.vo.Reserva;
import es.unizar.padel.dao.DaoException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Implementación de ReservaDao con JDBC. */
public final class JdbcReservaDao implements ReservaDao {

    private static final String COLUMNAS = "id_reserva, id_usuario, estado";

    private final DataSource dataSource;

    public JdbcReservaDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Reserva create(Reserva r) {
        final String sql = "INSERT INTO reserva (id_usuario, estado) VALUES (?, COALESCE(?, 'PENDIENTE')) "
                + "RETURNING " + COLUMNAS;
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, r.getIdUsuario());
            ps.setString(2, r.getEstado());
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new DaoException("El INSERT no devolvió la reserva creada", null);
                }
                return mapear(rs);
            }
        } catch (SQLException e) {
            throw new DaoException("No se pudo crear la reserva del usuario " + r.getIdUsuario(), e);
        }
    }

    @Override
    public Optional<Reserva> findById(long idReserva) {
        final String sql = "SELECT " + COLUMNAS + " FROM reserva WHERE id_reserva = ?";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, idReserva);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DaoException("No se pudo leer la reserva con id " + idReserva, e);
        }
    }

    @Override
    public List<Reserva> findAll() {
        final String sql = "SELECT " + COLUMNAS + " FROM reserva ORDER BY id_reserva";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Reserva> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new DaoException("No se pudo listar las reservas", e);
        }
    }

    @Override
    public boolean update(Reserva r) {
        final String sql = "UPDATE reserva SET id_usuario = ?, estado = ? WHERE id_reserva = ?";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, r.getIdUsuario());
            ps.setString(2, r.getEstado());
            ps.setLong(3, r.getIdReserva());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("No se pudo actualizar la reserva con id " + r.getIdReserva(), e);
        }
    }

    @Override
    public boolean delete(long idReserva) {
        final String sql = "DELETE FROM reserva WHERE id_reserva = ?";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, idReserva);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("No se pudo borrar la reserva con id " + idReserva, e);
        }
    }

    private Reserva mapear(ResultSet rs) throws SQLException {
        return new Reserva(rs.getLong("id_reserva"), rs.getLong("id_usuario"), rs.getString("estado"));
    }
}
