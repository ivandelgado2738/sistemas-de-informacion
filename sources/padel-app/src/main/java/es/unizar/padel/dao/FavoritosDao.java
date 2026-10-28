package es.unizar.padel.dao;

import es.unizar.padel.vo.Favoritos;

import java.util.List;

/**
 * Operaciones sobre la tabla club (CRUD).
 * El resto de la aplicación solo conoce esta interfaz, no el SQL.
 */
public interface FavoritosDao {


    Favoritos create(Favoritos rel);

    Boolean findById(long idClub,Long idUsuario);

    List<Favoritos> findAll();

    /** @return true si la relacion existía y se borró */
    boolean delete(long idClub, Long idUsuario);
}