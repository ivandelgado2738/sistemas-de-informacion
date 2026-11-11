package es.unizar.padel.dao;

import es.unizar.padel.vo.Pista;

import java.util.List;
import java.util.Optional;

/** Operaciones sobre la tabla pista (CRUD). La clave es (idClub, numero). */
public interface PistaDao {

    /** Guarda una pista nueva (la clave la aporta el llamante). */
    Pista create(Pista pista);

    Optional<Pista> findById(long idClub, int numero);

    /** Pistas de un club, ordenadas por número. */
    List<Pista> findByClub(long idClub);

    List<Pista> findAll();

    /** @return true si la pista existía y se modificó */
    boolean update(Pista pista);

    /** @return true si la pista existía y se borró */
    boolean delete(long idClub, int numero);
}
