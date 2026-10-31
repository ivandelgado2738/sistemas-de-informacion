package es.unizar.padel.dao;

import es.unizar.padel.vo.Reserva;

import java.util.List;
import java.util.Optional;

/** Operaciones sobre la tabla reserva (CRUD). */
public interface ReservaDao {

    /** Guarda una reserva nueva y la devuelve con su id ya asignado. */
    Reserva create(Reserva reserva);

    Optional<Reserva> findById(long idReserva);

    List<Reserva> findAll();

    /** @return true si la reserva existía y se modificó */
    boolean update(Reserva reserva);

    /** @return true si la reserva existía y se borró */
    boolean delete(long idReserva);
}
