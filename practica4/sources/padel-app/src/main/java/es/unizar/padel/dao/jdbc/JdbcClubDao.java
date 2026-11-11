package es.unizar.padel.dao.jdbc;

import es.unizar.padel.dao.ClubDao;
import es.unizar.padel.dao.DaoException;
import es.unizar.padel.vo.Club;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación de ClubDao con JDBC.
 * Cada método pide una conexión al pool, ejecuta una consulta con parámetros (?)
 * y la devuelve al pool automáticamente gracias al try-with-resources.
 */
public final class JdbcClubDao implements ClubDao {

    private final DataSource dataSource;

    public JdbcClubDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Club create(Club club) {
        final String sql =
                "INSERT INTO club (nombre, direccion, telefono) VALUES (?, ?, ?) RETURNING id_club";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, club.getNombre());
            ps.setString(2, club.getDireccion());
            ps.setString(3, club.getTelefono());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return new Club(rs.getLong(1), club.getNombre(), club.getDireccion(), club.getTelefono());
            }
        } catch (SQLException e) {
            throw new DaoException("No se pudo crear el club '" + club.getNombre() + "'", e);
        }
    }

    @Override
    public Optional<Club> findById(long idClub) {
        final String sql = "SELECT id_club, nombre, direccion, telefono FROM club WHERE id_club = ?";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, idClub);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(mapear(rs));
            }
        } catch (SQLException e) {
            throw new DaoException("No se pudo leer el club con id " + idClub, e);
        }
    }

    @Override
    public List<Club> findAll() {
        final String sql = "SELECT id_club, nombre, direccion, telefono FROM club ORDER BY id_club";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Club> clubes = new ArrayList<>();
            while (rs.next()) {
                clubes.add(mapear(rs));
            }
            return clubes;
        } catch (SQLException e) {
            throw new DaoException("No se pudo listar los clubes", e);
        }
    }

    @Override
    public boolean update(Club club) {
        final String sql = "UPDATE club SET nombre = ?, direccion = ?, telefono = ? WHERE id_club = ?";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, club.getNombre());
            ps.setString(2, club.getDireccion());
            ps.setString(3, club.getTelefono());
            ps.setLong(4, club.getIdClub());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("No se pudo actualizar el club con id " + club.getIdClub(), e);
        }
    }

    @Override
    public boolean delete(long idClub) {
        final String sql = "DELETE FROM club WHERE id_club = ?";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, idClub);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("No se pudo borrar el club con id " + idClub, e);
        }
    }

    /** Convierte una fila de la tabla en un objeto Club (columna -> campo). */
    private Club mapear(ResultSet rs) throws SQLException {
        return new Club(
                rs.getLong("id_club"),
                rs.getString("nombre"),
                rs.getString("direccion"),
                rs.getString("telefono"));
    }
}
