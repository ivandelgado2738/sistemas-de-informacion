package es.unizar.padel.dao.jdbc;

import es.unizar.padel.dao.JuegaDao;
import es.unizar.padel.vo.Juega;
import es.unizar.padel.dao.DaoException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Implementación de JuegaDao con JDBC. */
public final class JdbcJuegaDao implements JuegaDao {

    private static final String COLUMNAS = "id_usuario, id_partido, ha_pagado, lleva_bolas";

    private final DataSource dataSource;

    public JdbcJuegaDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Juega create(Juega j) {
        final String sql = "INSERT INTO juega (" + COLUMNAS + ") VALUES (?, ?, ?, ?)";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, j.getIdUsuario());
            ps.setLong(2, j.getIdPartido());
            ps.setBoolean(3, j.isHaPagado());
            ps.setBoolean(4, j.isLlevaBolas());
            ps.executeUpdate();
            return j;
        } catch (SQLException e) {
            throw new DaoException("No se pudo crear juega (" + j.getIdUsuario() + ", " + j.getIdPartido() + ")", e);
        }
    }

    @Override
    public Optional<Juega> findById(long idUsuario, long idPartido) {
        final String sql = "SELECT " + COLUMNAS + " FROM juega WHERE id_usuario = ? AND id_partido = ?";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, idUsuario);
            ps.setLong(2, idPartido);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DaoException("No se pudo leer juega (" + idUsuario + ", " + idPartido + ")", e);
        }
    }

    @Override
    public List<Juega> findByPartido(long idPartido) {
        final String sql = "SELECT " + COLUMNAS + " FROM juega WHERE id_partido = ? ORDER BY id_usuario";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, idPartido);
            try (ResultSet rs = ps.executeQuery()) {
                List<Juega> lista = new ArrayList<>();
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
                return lista;
            }
        } catch (SQLException e) {
            throw new DaoException("No se pudo listar los jugadores del partido " + idPartido, e);
        }
    }

    @Override
    public List<Juega> findAll() {
        final String sql = "SELECT " + COLUMNAS + " FROM juega ORDER BY id_partido, id_usuario";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Juega> lista = new ArrayList<>();
            while (rs.next()) {
                lista.add(mapear(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new DaoException("No se pudo listar la tabla juega", e);
        }
    }

    @Override
    public boolean update(Juega j) {
        final String sql = "UPDATE juega SET ha_pagado = ?, lleva_bolas = ? WHERE id_usuario = ? AND id_partido = ?";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setBoolean(1, j.isHaPagado());
            ps.setBoolean(2, j.isLlevaBolas());
            ps.setLong(3, j.getIdUsuario());
            ps.setLong(4, j.getIdPartido());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("No se pudo actualizar juega (" + j.getIdUsuario() + ", " + j.getIdPartido() + ")", e);
        }
    }

    @Override
    public boolean delete(long idUsuario, long idPartido) {
        final String sql = "DELETE FROM juega WHERE id_usuario = ? AND id_partido = ?";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, idUsuario);
            ps.setLong(2, idPartido);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("No se pudo borrar juega (" + idUsuario + ", " + idPartido + ")", e);
        }
    }

    private Juega mapear(ResultSet rs) throws SQLException {
        return new Juega(
                rs.getLong("id_usuario"),
                rs.getLong("id_partido"),
                rs.getBoolean("ha_pagado"),
                rs.getBoolean("lleva_bolas"));
    }
}
