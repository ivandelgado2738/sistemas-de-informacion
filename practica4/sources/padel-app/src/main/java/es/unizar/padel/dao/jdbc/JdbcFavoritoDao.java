package es.unizar.padel.dao.jdbc;

import es.unizar.padel.dao.FavoritoDao;
import es.unizar.padel.vo.Favorito;
import es.unizar.padel.dao.DaoException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Implementación de FavoritoDao con JDBC. Tabla de relación: solo tiene la clave, no hay update. */
public final class JdbcFavoritoDao implements FavoritoDao {

    private final DataSource dataSource;

    public JdbcFavoritoDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Favorito create(Favorito rel) {
        final String sql = "INSERT INTO favorito (id_usuario, id_club) VALUES (?, ?)";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, rel.getIdUsuario());
            ps.setLong(2, rel.getIdClub());
            ps.executeUpdate();
            return rel;
        } catch (SQLException e) {
            throw new DaoException("No se pudo crear la relación favorito (" + rel.getIdUsuario() + ", " + rel.getIdClub() + ")", e);
        }
    }

    @Override
    public Optional<Favorito> findById(long a, long b) {
        final String sql = "SELECT id_usuario, id_club FROM favorito WHERE id_usuario = ? AND id_club = ?";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, a);
            ps.setLong(2, b);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DaoException("No se pudo leer la relación favorito (" + a + ", " + b + ")", e);
        }
    }

    @Override
    public List<Favorito> findByUsuario(long idUsuario) {
        final String sql = "SELECT id_usuario, id_club FROM favorito WHERE id_usuario = ? ORDER BY id_club";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                List<Favorito> lista = new ArrayList<>();
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
                return lista;
            }
        } catch (SQLException e) {
            throw new DaoException("No se pudo listar favorito del usuario " + idUsuario, e);
        }
    }

    @Override
    public List<Favorito> findAll() {
        final String sql = "SELECT id_usuario, id_club FROM favorito ORDER BY id_usuario, id_club";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Favorito> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new DaoException("No se pudo listar la tabla favorito", e);
        }
    }

    @Override
    public boolean delete(long a, long b) {
        final String sql = "DELETE FROM favorito WHERE id_usuario = ? AND id_club = ?";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, a);
            ps.setLong(2, b);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("No se pudo borrar la relación favorito (" + a + ", " + b + ")", e);
        }
    }

    private Favorito mapear(ResultSet rs) throws SQLException {
        return new Favorito(rs.getLong("id_usuario"), rs.getLong("id_club"));
    }
}
