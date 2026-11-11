package es.unizar.padel.dao.jdbc;

import es.unizar.padel.dao.AmistadDao;
import es.unizar.padel.vo.Amistad;
import es.unizar.padel.dao.DaoException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Implementación de AmistadDao con JDBC. Tabla de relación: solo tiene la clave, no hay update. */
public final class JdbcAmistadDao implements AmistadDao {

    private final DataSource dataSource;

    public JdbcAmistadDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Amistad create(Amistad rel) {
        final String sql = "INSERT INTO amistad (id_usuario, id_amigo) VALUES (?, ?)";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, rel.getIdUsuario());
            ps.setLong(2, rel.getIdAmigo());
            ps.executeUpdate();
            return rel;
        } catch (SQLException e) {
            throw new DaoException("No se pudo crear la relación amistad (" + rel.getIdUsuario() + ", " + rel.getIdAmigo() + ")", e);
        }
    }

    @Override
    public Optional<Amistad> findById(long a, long b) {
        final String sql = "SELECT id_usuario, id_amigo FROM amistad WHERE id_usuario = ? AND id_amigo = ?";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, a);
            ps.setLong(2, b);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DaoException("No se pudo leer la relación amistad (" + a + ", " + b + ")", e);
        }
    }

    @Override
    public List<Amistad> findByUsuario(long idUsuario) {
        final String sql = "SELECT id_usuario, id_amigo FROM amistad WHERE id_usuario = ? ORDER BY id_amigo";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                List<Amistad> lista = new ArrayList<>();
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
                return lista;
            }
        } catch (SQLException e) {
            throw new DaoException("No se pudo listar amistad del usuario " + idUsuario, e);
        }
    }

    @Override
    public List<Amistad> findAll() {
        final String sql = "SELECT id_usuario, id_amigo FROM amistad ORDER BY id_usuario, id_amigo";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Amistad> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new DaoException("No se pudo listar la tabla amistad", e);
        }
    }

    @Override
    public boolean delete(long a, long b) {
        final String sql = "DELETE FROM amistad WHERE id_usuario = ? AND id_amigo = ?";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, a);
            ps.setLong(2, b);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("No se pudo borrar la relación amistad (" + a + ", " + b + ")", e);
        }
    }

    private Amistad mapear(ResultSet rs) throws SQLException {
        return new Amistad(rs.getLong("id_usuario"), rs.getLong("id_amigo"));
    }
}
