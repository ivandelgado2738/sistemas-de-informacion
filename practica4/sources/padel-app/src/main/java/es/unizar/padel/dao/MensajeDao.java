package es.unizar.padel.dao;

import es.unizar.padel.vo.Mensaje;

import java.util.List;
import java.util.Optional;

/** Operaciones sobre la tabla mensaje (CRUD). */
public interface MensajeDao {

    /** Guarda un mensaje nuevo y lo devuelve con su id y fecha de envío ya asignados. */
    Mensaje create(Mensaje mensaje);

    Optional<Mensaje> findById(long idMensaje);

    /** Mensajes de un partido, del más antiguo al más reciente. */
    List<Mensaje> findByPartido(long idPartido);

    List<Mensaje> findAll();

    /** Modifica solo el contenido. @return true si el mensaje existía */
    boolean update(Mensaje mensaje);

    /** @return true si el mensaje existía y se borró */
    boolean delete(long idMensaje);
}
