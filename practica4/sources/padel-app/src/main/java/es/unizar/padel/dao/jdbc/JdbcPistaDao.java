package es.unizar.padel.dao.jdbc;

import es.unizar.padel.dao.PistaDao;
import es.unizar.padel.vo.Pista;
import es.unizar.padel.dao.DaoException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Implementación de PistaDao con JDBC. */
public final class JdbcPistaDao implements PistaDao {

    private static final String COLUMNAS = "id_club, numero, cubierta";

    private final DataSource dataSource;

    public JdbcPistaDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Pista create(Pista p) {
        final String sql = "INSERT INTO pista (id_club, numero, cubierta) VALUES (?, ?, ?)";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, p.getIdClub());
            ps.setInt(2, p.getNumero());
            ps.setBoolean(3, p.isCubierta());
            ps.executeUpdate();
            return p;
        } catch (SQLException e) {
            throw new DaoException("No se pudo crear la pista " + p.getNumero()
                    + " del club " + p.getIdClub(), e);
        }
    }

    @Override
    public Optional<Pista> findById(long idClub, int numero) {
        final String sql = "SELECT " + COLUMNAS + " FROM pista WHERE id_club = ? AND numero = ?";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, idClub);
            ps.setInt(2, numero);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DaoException("No se pudo leer la pista " + numero + " del club " + idClub, e);
        }
    }

    @Override
    public List<Pista> findByClub(long idClub) {
        final String sql = "SELECT " + COLUMNAS + " FROM pista WHERE id_club = ? ORDER BY numero";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, idClub);
            try (ResultSet rs = ps.executeQuery()) {
                List<Pista> lista = new ArrayList<>();
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
                return lista;
            }
        } catch (SQLException e) {
            throw new DaoException("No se pudo listar las pistas del club " + idClub, e);
        }
    }

    @Override
    public List<Pista> findAll() {
        final String sql = "SELECT " + COLUMNAS + " FROM pista ORDER BY id_club, numero";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Pista> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new DaoException("No se pudo listar las pistas", e);
        }
    }

    @Override
    public boolean update(Pista p) {
        final String sql = "UPDATE pista SET cubierta = ? WHERE id_club = ? AND numero = ?";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setBoolean(1, p.isCubierta());
            ps.setLong(2, p.getIdClub());
            ps.setInt(3, p.getNumero());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("No se pudo actualizar la pista " + p.getNumero()
                    + " del club " + p.getIdClub(), e);
        }
    }

    @Override
    public boolean delete(long idClub, int numero) {
        final String sql = "DELETE FROM pista WHERE id_club = ? AND numero = ?";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, idClub);
            ps.setInt(2, numero);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("No se pudo borrar la pista " + numero + " del club " + idClub, e);
        }
    }

    private Pista mapear(ResultSet rs) throws SQLException {
        return new Pista(rs.getLong("id_club"), rs.getInt("numero"), rs.getBoolean("cubierta"));
    }
}
