package es.unizar.padel.dao.jdbc;

import es.unizar.padel.dao.InvitacionDao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import es.unizar.padel.dao.DaoException;
import es.unizar.padel.vo.Invitacion;

public class JbdcInvitacionDao implements InvitacionDao{
    private static final String COLUMNAS = "id_usuario, id_partido";

    private final DataSource dataSource;

    public JbdcInvitacionDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Invitacion create(Invitacion invitacion) {
        // COALESCE: si estado/nivel son null se usa el valor por defecto de la tabla
        final String sql = "INSERT INTO invitacion (id_usuario, id_partido) "
                + "VALUES (?, ?)"
                + "RETURNING " + COLUMNAS;
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, invitacion.getIdUsuario());
            ps.setLong(2, invitacion.getIdPartido());
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new DaoException("El INSERT no devolvió el usuario creado", null);
                }
                return mapear(rs);
            }
        } catch (SQLException e) {
            throw new DaoException("No se pudo crear la invitación '" + invitacion.getIdUsuario() + "," + invitacion.getIdPartido() + "'", e);
        }
    }

    @Override
    public Optional<Invitacion> findById(long id_usuario,Long id_partido) {
        final String sql = "SELECT " + COLUMNAS + " FROM usuario WHERE id_usuario = ? AND id_partido = ?";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id_usuario);
            ps.setLong(2, id_partido);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DaoException("No se pudo leer la invitación con id " + id_usuario + "," + id_partido, e);
        }
    }

    @Override
    public List<Invitacion> findAll() {
        final String sql = "SELECT " + COLUMNAS + " FROM usuario ORDER BY id";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Invitacion> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new DaoException("No se pudo listar las invitaciones", e);
        }
    }

    @Override
    public boolean update(Invitacion inv) {
        final String sql = "UPDATE usuario SET id_usuario = ?, id_partido = ?"
                + "WHERE id = ?";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, inv.getIdUsuario());
            ps.setLong(2, inv.getIdPartido());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("No se pudo actualizar la invitación con id " + inv.getIdUsuario() + "," + inv.getIdPartido(), e);
        }
    }

    @Override
    public boolean delete(long id_usuario, Long id_partido) {
        final String sql = "DELETE FROM usuario WHERE id_usuario = ? AND id_partido = ?";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id_usuario);
            ps.setLong(2, id_partido);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("No se pudo borrar la invitación con id " + id_usuario + "," + id_partido, e);
        }
    }

    /** Convierte una fila de la tabla en un objeto Usuario (columna -> campo). */
    private Invitacion mapear(ResultSet rs) throws SQLException {
        return new Invitacion(
                rs.getLong("id_usuario"),
                rs.getLong("id_partido"));
    }
}
