package es.unizar.padel.db;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Comprueba que Java se conecta a la base de datos
 * (la base de datos de Docker debe estar encendida).
 */
class ConexionTest {

    @Test
    void seConectaALaBaseDeDatos() throws SQLException {
        try (Connection c = Database.getDataSource().getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT 1")) {
            assertTrue(rs.next());
            assertEquals(1, rs.getInt(1));
        }
    }

    @Test
    void veLasTablasDelEsquema() throws SQLException {
        // padel_app tiene sisinf_p3 en su search_path, no hace falta poner el esquema
        try (Connection c = Database.getDataSource().getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT count(*) FROM usuario")) {
            assertTrue(rs.next());
            assertTrue(rs.getInt(1) >= 0);
        }
    }
}
