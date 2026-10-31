package es.unizar.padel.dao;

import es.unizar.padel.vo.Usuario;

import java.util.List;
import java.util.Optional;

/** Operaciones sobre la tabla usuario (CRUD). */
public interface UsuarioDao {

    /** Guarda un usuario nuevo y lo devuelve con su id ya asignado. */
    Usuario create(Usuario usuario);

    Optional<Usuario> findById(long id);

    List<Usuario> findAll();

    /** @return true si el usuario existía y se modificó */
    boolean update(Usuario usuario);

    /** @return true si el usuario existía y se borró */
    boolean delete(long id);
}
