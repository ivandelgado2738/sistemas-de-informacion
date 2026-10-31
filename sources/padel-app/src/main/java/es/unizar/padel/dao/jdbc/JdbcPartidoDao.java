package es.unizar.padel.dao.jdbc;

import es.unizar.padel.dao.PartidoDao;
import es.unizar.padel.vo.Partido;
import es.unizar.padel.dao.DaoException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Implementación de PartidoDao con JDBC. */
public final class JdbcPartidoDao implements PartidoDao {

    private static final String COLUMNAS = "id_partido, resultado, id_reserva";

    private final DataSource dataSource;

    public JdbcPartidoDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Partido create(Partido p) {
        final String sql = "INSERT INTO partido (resultado, id_reserva) VALUES (?, ?) RETURNING " + COLUMNAS;
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getResultado());
            ps.setLong(2, p.getIdReserva());
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new DaoException("El INSERT no devolvió el partido creado", null);
                }
                return mapear(rs);
            }
        } catch (SQLException e) {
            throw new DaoException("No se pudo crear el partido de la reserva " + p.getIdReserva(), e);
        }
    }

    @Override
    public Optional<Partido> findById(long idPartido) {
        final String sql = "SELECT " + COLUMNAS + " FROM partido WHERE id_partido = ?";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, idPartido);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DaoException("No se pudo leer el partido con id " + idPartido, e);
        }
    }

    @Override
    public List<Partido> findAll() {
        final String sql = "SELECT " + COLUMNAS + " FROM partido ORDER BY id_partido";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Partido> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new DaoException("No se pudo listar los partidos", e);
        }
    }

    @Override
    public boolean update(Partido p) {
        final String sql = "UPDATE partido SET resultado = ?, id_reserva = ? WHERE id_partido = ?";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getResultado());
            ps.setLong(2, p.getIdReserva());
            ps.setLong(3, p.getIdPartido());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("No se pudo actualizar el partido con id " + p.getIdPartido(), e);
        }
    }

    @Override
    public boolean delete(long idPartido) {
        final String sql = "DELETE FROM partido WHERE id_partido = ?";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, idPartido);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("No se pudo borrar el partido con id " + idPartido, e);
        }
    }

    private Partido mapear(ResultSet rs) throws SQLException {
        return new Partido(rs.getLong("id_partido"), rs.getString("resultado"), rs.getLong("id_reserva"));
    }
}
