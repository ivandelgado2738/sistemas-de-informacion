package es.unizar.padel.dao;

import es.unizar.padel.vo.Partido;

import java.util.List;
import java.util.Optional;

/** Operaciones sobre la tabla partido (CRUD). */
public interface PartidoDao {

    /** Guarda un partido nuevo y lo devuelve con su id ya asignado. */
    Partido create(Partido partido);

    Optional<Partido> findById(long idPartido);

    List<Partido> findAll();

    /** @return true si el partido existía y se modificó */
    boolean update(Partido partido);

    /** @return true si el partido existía y se borró */
    boolean delete(long idPartido);
}
