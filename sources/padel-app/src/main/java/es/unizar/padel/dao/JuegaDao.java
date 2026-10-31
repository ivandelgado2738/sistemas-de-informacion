package es.unizar.padel.dao;

import es.unizar.padel.vo.Juega;

import java.util.List;
import java.util.Optional;

/** Operaciones sobre la tabla juega (CRUD). La clave es (idUsuario, idPartido). */
public interface JuegaDao {

    Juega create(Juega juega);

    Optional<Juega> findById(long idUsuario, long idPartido);

    /** Jugadores de un partido. */
    List<Juega> findByPartido(long idPartido);

    List<Juega> findAll();

    /** Modifica haPagado y llevaBolas. @return true si la fila existía */
    boolean update(Juega juega);

    /** @return true si la fila existía y se borró */
    boolean delete(long idUsuario, long idPartido);
}
