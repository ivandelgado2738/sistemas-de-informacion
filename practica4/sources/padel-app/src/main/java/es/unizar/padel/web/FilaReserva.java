package es.unizar.padel.web;

import java.time.format.DateTimeFormatter;

import es.unizar.padel.vo.Horario;

/**
 * Una fila de la tabla de verReservas: el horario más el número de jugadores.
 * Solo existe para pasarle al JSP los datos ya preparados (fechas formateadas, etc.).
 */
public class FilaReserva {
    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm");

    private final Horario horario;
    private final int jugadores;

    public FilaReserva(Horario horario, int jugadores) {
        this.horario = horario;
        this.jugadores = jugadores;
    }

    public int getNumeroPista() { return horario.getNumeroPista(); }
    public String getFecha() { return horario.getFecha().format(FECHA); }
    /** Fecha en formato ISO (2026-11-15), para mandarla de vuelta en el formulario de cancelar. */
    public String getFechaIso() { return horario.getFecha().toString(); }
    public String getHoraInicio() { return horario.getHoraInicio().format(HORA); }
    public String getHoraFin() { return horario.getHoraFin().format(HORA); }
    public int getPrecio() { return horario.getPrecio(); }
    public int getJugadores() { return jugadores; }
    /** Id de la reserva del horario (null si está libre). */
    public Long getIdReserva() { return horario.getIdReserva(); }
    public boolean isReservada() { return horario.getIdReserva() != null; }
}
