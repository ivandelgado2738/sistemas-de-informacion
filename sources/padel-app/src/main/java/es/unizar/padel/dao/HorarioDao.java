package es.unizar.padel.dao;

import es.unizar.padel.vo.Horario;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/** Operaciones sobre la tabla horario (CRUD). Clave: (idClub, numeroPista, fecha, horaInicio). */
public interface HorarioDao {

    /** Guarda un horario nuevo (la clave la aporta el llamante). */
    Horario create(Horario horario);

    Optional<Horario> findById(long idClub, int numeroPista, LocalDate fecha, LocalTime horaInicio);

    /** Horarios sin reserva de un club en una fecha. */
    List<Horario> findLibres(long idClub, LocalDate fecha);

    List<Horario> findAll();

    /** Modifica horaFin, precio e idReserva. @return true si el horario existía */
    boolean update(Horario horario);

    /** @return true si el horario existía y se borró */
    boolean delete(long idClub, int numeroPista, LocalDate fecha, LocalTime horaInicio);
}
