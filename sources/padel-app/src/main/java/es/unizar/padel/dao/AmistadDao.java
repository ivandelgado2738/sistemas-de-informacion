package es.unizar.padel.dao;

import es.unizar.padel.vo.Amistad;

import java.util.List;
import java.util.Optional;

/** Operaciones sobre la tabla amistad. No hay update: la fila solo tiene la clave. */
public interface AmistadDao {

    Amistad create(Amistad amistad);

    Optional<Amistad> findById(long idUsuario, long idAmigo);

    /** Amistades de un usuario (las filas donde él es idUsuario). */
    List<Amistad> findByUsuario(long idUsuario);

    List<Amistad> findAll();

    /** @return true si la amistad existía y se borró */
    boolean delete(long idUsuario, long idAmigo);
}
