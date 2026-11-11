package es.unizar.padel.web;

import java.time.format.DateTimeFormatter;

import es.unizar.padel.vo.Horario;
import es.unizar.padel.vo.Partido;

/** Una tarjeta de la pantalla "Mis partidos": datos del partido, de su horario y de la cancelación en curso. */
public class FilaPartido {
    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm");

    private final Partido partido;
    private final Horario horario;
    private final String club;
    private final int jugadores;
    private final int aceptados;
    private final boolean yoHeAceptado;

    public FilaPartido(Partido partido, Horario horario, String club, int jugadores,
                       int aceptados, boolean yoHeAceptado) {
        this.partido = partido;
        this.horario = horario;
        this.club = club;
        this.jugadores = jugadores;
        this.aceptados = aceptados;
        this.yoHeAceptado = yoHeAceptado;
    }

    public long getIdReserva() { return partido.getIdReserva(); }
    public boolean isPrivado() { return partido.getPrivado(); }
    public String getClub() { return club; }
    public int getNumeroPista() { return horario.getNumeroPista(); }
    public String getFecha() { return horario.getFecha().format(FECHA); }
    public String getHoraInicio() { return horario.getHoraInicio().format(HORA); }
    public String getHoraFin() { return horario.getHoraFin().format(HORA); }
    public int getPrecio() { return horario.getPrecio(); }
    public int getJugadores() { return jugadores; }
    /** Cuántos jugadores han aceptado ya cancelar (0 si no hay ninguna cancelación en curso). */
    public int getAceptados() { return aceptados; }
    public boolean isYoHeAceptado() { return yoHeAceptado; }
}
