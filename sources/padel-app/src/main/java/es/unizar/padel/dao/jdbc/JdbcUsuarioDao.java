package es.unizar.padel.dao.jdbc;

import es.unizar.padel.dao.UsuarioDao;
import es.unizar.padel.vo.Usuario;
import es.unizar.padel.dao.DaoException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Implementación de UsuarioDao con JDBC (pool + PreparedStatement + try-with-resources). */
public final class JdbcUsuarioDao implements UsuarioDao {

    private static final String COLUMNAS = "id, email, nombre, contrasena, estado, nivel";

    private final DataSource dataSource;

    public JdbcUsuarioDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Usuario create(Usuario u) {
        // COALESCE: si estado/nivel son null se usa el valor por defecto de la tabla
        final String sql = "INSERT INTO usuario (email, nombre, rol, contrasena, estado, nivel) "
                + "VALUES (?, ?, ?, ?, COALESCE(?, 'ACTIVO'), COALESCE(?, 'PRINCIPIANTE')) "
                + "RETURNING " + COLUMNAS;
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, u.getEmail());
            ps.setString(2, u.getNombre());
            ps.setString(3, u.getRol());
            ps.setString(4, u.getContrasena());
            ps.setString(5, u.getEstado());
            ps.setString(6, u.getNivel());
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new DaoException("El INSERT no devolvió el usuario creado", null);
                }
                return mapear(rs);
            }
        } catch (SQLException e) {
            throw new DaoException("No se pudo crear el usuario '" + u.getEmail() + "'", e);
        }
    }

    @Override
    public Optional<Usuario> findById(long id) {
        final String sql = "SELECT " + COLUMNAS + " FROM usuario WHERE id = ?";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DaoException("No se pudo leer el usuario con id " + id, e);
        }
    }

    @Override
    public List<Usuario> findAll() {
        final String sql = "SELECT " + COLUMNAS + " FROM usuario ORDER BY id";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Usuario> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new DaoException("No se pudo listar los usuarios", e);
        }
    }

    @Override
    public boolean update(Usuario u) {
        final String sql = "UPDATE usuario SET email = ?, nombre = ?, rol = ?, contrasena = ?, estado = ?, nivel = ? "
                + "WHERE id = ?";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, u.getEmail());
            ps.setString(2, u.getNombre());
            ps.setString(3, u.getRol());
            ps.setString(4, u.getContrasena());
            ps.setString(5, u.getEstado());
            ps.setString(6, u.getNivel());
            ps.setLong(7, u.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("No se pudo actualizar el usuario con id " + u.getId(), e);
        }
    }

    @Override
    public boolean delete(long id) {
        final String sql = "DELETE FROM usuario WHERE id = ?";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("No se pudo borrar el usuario con id " + id, e);
        }
    }

    /** Convierte una fila de la tabla en un objeto Usuario (columna -> campo). */
    private Usuario mapear(ResultSet rs) throws SQLException {
        return new Usuario(
                rs.getLong("id"),
                rs.getString("rol"),
                rs.getString("email"),
                rs.getString("nombre"),
                rs.getString("contrasena"),
                rs.getString("estado"),
                rs.getString("nivel"));
    }
}
