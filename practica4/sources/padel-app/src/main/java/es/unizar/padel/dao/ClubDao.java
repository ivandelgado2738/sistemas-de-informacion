package es.unizar.padel.dao;

import es.unizar.padel.vo.Club;

import java.util.List;
import java.util.Optional;

/**
 * Operaciones sobre la tabla club (CRUD).
 * El resto de la aplicación solo conoce esta interfaz, no el SQL.
 */
public interface ClubDao {

    /** Guarda un club nuevo y lo devuelve con su id ya asignado. */
    Club create(Club club);

    Optional<Club> findById(long idClub);

    List<Club> findAll();

    /** @return true si el club existía y se modificó */
    boolean update(Club club);

    /** @return true si el club existía y se borró */
    boolean delete(long idClub);
}
