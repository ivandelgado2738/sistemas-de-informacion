package es.unizar.padel.vo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Value Object de la tabla horario.
 * Clave primaria: (idClub, numeroPista, fecha, horaInicio).
 * idReserva es null mientras el horario está libre.
 */
public final class Horario implements Serializable {

    private static final long serialVersionUID = 1L;

    private final long idClub;
    private final int numeroPista;
    private final LocalDate fecha;
    private final LocalTime horaInicio;
    private final LocalTime horaFin;
    private final int precio;
    private final Long idReserva;      // null = libre

    public Horario(long idClub, int numeroPista, LocalDate fecha, LocalTime horaInicio,
                   LocalTime horaFin, int precio, Long idReserva) {
        this.idClub = idClub;
        this.numeroPista = numeroPista;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.precio = precio;
        this.idReserva = idReserva;
    }

    public long getIdClub() { return idClub; }
    public int getNumeroPista() { return numeroPista; }
    public LocalDate getFecha() { return fecha; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public LocalTime getHoraFin() { return horaFin; }
    public int getPrecio() { return precio; }
    public Long getIdReserva() { return idReserva; }
}
