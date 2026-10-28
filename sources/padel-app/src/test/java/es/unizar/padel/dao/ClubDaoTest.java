package es.unizar.padel.dao;

import es.unizar.padel.dao.jdbc.JdbcClubDao;
import es.unizar.padel.db.Database;
import es.unizar.padel.vo.Club;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas de ClubDao contra la base de datos real (la de Docker).
 * Todos los clubes de prueba empiezan por "TEST " y se borran al terminar cada prueba.
 */
class ClubDaoTest {

    private final ClubDao dao = new JdbcClubDao(Database.getDataSource());

    @AfterEach
    void limpiar() throws SQLException {
        try (Connection c = Database.getDataSource().getConnection();
             Statement st = c.createStatement()) {
            st.executeUpdate("DELETE FROM club WHERE nombre LIKE 'TEST %'");
        }
    }

    @Test
    void cicloCompletoCrud() {
        // CREATE
        Club creado = dao.create(new Club(null, "TEST Club Central", "Calle Mayor 1", "976000000"));
        assertNotNull(creado.getIdClub());

        // READ por id
        Optional<Club> leido = dao.findById(creado.getIdClub());
        assertTrue(leido.isPresent());
        assertEquals("TEST Club Central", leido.get().getNombre());
        assertEquals("Calle Mayor 1", leido.get().getDireccion());
        assertEquals("976000000", leido.get().getTelefono());

        // READ lista
        assertTrue(dao.findAll().stream()
                .anyMatch(club -> creado.getIdClub().equals(club.getIdClub())));

        // UPDATE
        boolean actualizado = dao.update(
                new Club(creado.getIdClub(), "TEST Club Norte", "Calle Mayor 1", "976111111"));
        assertTrue(actualizado);
        assertEquals("TEST Club Norte", dao.findById(creado.getIdClub()).get().getNombre());

        // DELETE
        assertTrue(dao.delete(creado.getIdClub()));
        assertTrue(dao.findById(creado.getIdClub()).isEmpty());
    }

    @Test
    void nombreDuplicadoLanzaExcepcion() {
        dao.create(new Club(null, "TEST Club Duplicado", "Calle A", null));

        DaoException error = assertThrows(DaoException.class,
                () -> dao.create(new Club(null, "TEST Club Duplicado", "Calle B", null)));

        // La causa original (la SQLException de PostgreSQL) se conserva
        assertInstanceOf(SQLException.class, error.getCause());
    }
}
