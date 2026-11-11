package es.unizar.padel.dao;

import es.unizar.padel.vo.Favorito;

import java.util.List;
import java.util.Optional;

/** Operaciones sobre la tabla favorito. No hay update: la fila solo tiene la clave. */
public interface FavoritoDao {

    Favorito create(Favorito favorito);

    Optional<Favorito> findById(long idUsuario, long idClub);

    /** Clubes favoritos de un usuario. */
    List<Favorito> findByUsuario(long idUsuario);

    List<Favorito> findAll();

    /** @return true si el favorito existía y se borró */
    boolean delete(long idUsuario, long idClub);
}
