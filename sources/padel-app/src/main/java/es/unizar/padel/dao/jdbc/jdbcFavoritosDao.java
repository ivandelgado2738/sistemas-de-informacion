package es.unizar.padel.dao.jdbc;

import es.unizar.padel.dao.FavoritosDao;
import es.unizar.padel.dao.DaoException;
import es.unizar.padel.vo.Favoritos;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de ClubDao con JDBC.
 * Cada método pide una conexión al pool, ejecuta una consulta con parámetros (?)
 * y la devuelve al pool automáticamente gracias al try-with-resources.
 */
public final class jdbcFavoritosDao implements FavoritosDao {

    private final DataSource dataSource;

    public jdbcFavoritosDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Favoritos create(Favoritos rel) {
        final String sql =
                "INSERT INTO Favoritos (idUsuario, idClub) VALUES (?, ?)";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, rel.getIdUsuario());
            ps.setLong(2, rel.getIdClub());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return new Favoritos(rel.getIdClub(),rel.getIdUsuario());
            }
        } catch (SQLException e) {
            throw new DaoException("No se pudo crear la relacion '" + rel.getIdClub() +", " + rel.getIdUsuario() + "'", e);
        }
    }

    @Override
    public Boolean findById(long idClub, Long idUsuario) {
        final String sql = "SELECT * FROM Favoritos WHERE id_club = ? AND idUsuario = ?";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, idClub);
            ps.setLong(2, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return false;
                }
                return true;
            }
        } catch (SQLException e) {
            throw new DaoException("No se pudo leer la relacion con id " + idClub + ", " + idUsuario, e);
        }
    }

    @Override
    public List<Favoritos> findAll() {
        final String sql = "SELECT * FROM club ORDER BY id_club";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Favoritos> favoritos = new ArrayList<>();
            while (rs.next()) {
                favoritos.add(mapear(rs));
            }
            return favoritos;
        } catch (SQLException e) {
            throw new DaoException("No se pudo listar las relaciones", e);
        }
    }

    @Override
    public boolean delete(long idClub, Long idUsuario) {
        final String sql = "DELETE FROM Favorito WHERE id_club = ?, idUsuario= ?";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, idClub);
            ps.setLong(2, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("No se pudo borrar la relacion con id " + idClub + ", " + idUsuario, e);
        }
    }

    /** Convierte una fila de la tabla en un objeto Club (columna -> campo). */
    private Favoritos mapear(ResultSet rs) throws SQLException {
        return new Favoritos(
                rs.getLong("id_club"),
                rs.getLong("IdUsuario"));
        
    }
}
