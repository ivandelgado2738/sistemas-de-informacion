package es.unizar.padel.dao.jdbc;

import es.unizar.padel.dao.MensajeDao;
import es.unizar.padel.vo.Mensaje;
import es.unizar.padel.dao.DaoException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.OffsetDateTime;

/** Implementación de MensajeDao con JDBC. */
public final class JdbcMensajeDao implements MensajeDao {

    private static final String COLUMNAS = "id_mensaje, id_usuario, id_partido, contenido, fecha_envio";

    private final DataSource dataSource;

    public JdbcMensajeDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Mensaje create(Mensaje m) {
        // fecha_envio la pone la BD (DEFAULT now()) y se devuelve con RETURNING
        final String sql = "INSERT INTO mensaje (id_usuario, id_partido, contenido) VALUES (?, ?, ?) "
                + "RETURNING " + COLUMNAS;
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, m.getIdUsuario());
            ps.setLong(2, m.getIdPartido());
            ps.setString(3, m.getContenido());
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new DaoException("El INSERT no devolvió el mensaje creado", null);
                }
                return mapear(rs);
            }
        } catch (SQLException e) {
            throw new DaoException("No se pudo crear el mensaje del usuario " + m.getIdUsuario()
                    + " en el partido " + m.getIdPartido(), e);
        }
    }

    @Override
    public Optional<Mensaje> findById(long idMensaje) {
        final String sql = "SELECT " + COLUMNAS + " FROM mensaje WHERE id_mensaje = ?";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, idMensaje);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DaoException("No se pudo leer el mensaje con id " + idMensaje, e);
        }
    }

    @Override
    public List<Mensaje> findByPartido(long idPartido) {
        final String sql = "SELECT " + COLUMNAS + " FROM mensaje WHERE id_partido = ? "
                + "ORDER BY fecha_envio, id_mensaje";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, idPartido);
            try (ResultSet rs = ps.executeQuery()) {
                List<Mensaje> lista = new ArrayList<>();
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
                return lista;
            }
        } catch (SQLException e) {
            throw new DaoException("No se pudo listar los mensajes del partido " + idPartido, e);
        }
    }

    @Override
    public List<Mensaje> findAll() {
        final String sql = "SELECT " + COLUMNAS + " FROM mensaje ORDER BY id_mensaje";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Mensaje> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new DaoException("No se pudo listar los mensajes", e);
        }
    }

    @Override
    public boolean update(Mensaje m) {
        final String sql = "UPDATE mensaje SET contenido = ? WHERE id_mensaje = ?";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, m.getContenido());
            ps.setLong(2, m.getIdMensaje());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("No se pudo actualizar el mensaje con id " + m.getIdMensaje(), e);
        }
    }

    @Override
    public boolean delete(long idMensaje) {
        final String sql = "DELETE FROM mensaje WHERE id_mensaje = ?";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, idMensaje);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("No se pudo borrar el mensaje con id " + idMensaje, e);
        }
    }

    private Mensaje mapear(ResultSet rs) throws SQLException {
        return new Mensaje(
                rs.getLong("id_mensaje"),
                rs.getLong("id_usuario"),
                rs.getLong("id_partido"),
                rs.getString("contenido"),
                rs.getObject("fecha_envio", OffsetDateTime.class));
    }
}
