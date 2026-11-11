package es.unizar.padel.funcionalidades;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import es.unizar.padel.dao.HorarioDao;
import es.unizar.padel.dao.JuegaDao;
import es.unizar.padel.dao.PartidoDao;
import es.unizar.padel.dao.PistaDao;
import es.unizar.padel.vo.Horario;
import es.unizar.padel.vo.Partido;

/**
 * Casos de uso del administrador sobre los horarios de un club:
 * ofrecer un horario, ver los horarios y las reservas, y cancelar un horario ofertado.
 * Un horario es "una reserva ofertada": cuando un cliente lo reserva, idReserva deja de ser null.
 */
public class GestionReserva {
    private PistaDao pi;
    private HorarioDao h;
    private PartidoDao p;
    private JuegaDao j;
    private Long id_club;

    public GestionReserva(Long id_club, PistaDao pi, HorarioDao h, PartidoDao p, JuegaDao j){
        this.id_club=id_club;
        this.pi=pi;
        this.h=h;
        this.p=p;
        this.j=j;
    }

    /** El trabajador oferta un horario de una pista con su hora de inicio, fin y precio. */
    public Horario OfrecerHorario(int numeroPista, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, int precio){
        pi.findById(id_club, numeroPista)
                .orElseThrow(()-> new ModeloException("La pista " + numeroPista + " no existe en este club"));
        if(horaFin==null || horaInicio==null || !horaFin.isAfter(horaInicio)){
            throw new ModeloException("La hora de fin debe ser posterior a la de inicio");
        }
        if(LocalDateTime.of(fecha, horaInicio).isBefore(LocalDateTime.now())){
            throw new ModeloException("No se puede ofrecer un horario que ya ha pasado");
        }
        if(precio<0){
            throw new ModeloException("El precio no puede ser negativo");
        }
        if(h.findById(id_club, numeroPista, fecha, horaInicio).isPresent()){
            throw new ModeloException("Ya existe un horario para esa pista, fecha y hora");
        }
        Horario horario = h.create(new Horario(id_club, numeroPista, fecha, horaInicio, horaFin, precio, null));
        if(horario==null){
            throw new ModeloException("No se ha podido crear el horario");
        }
        return horario;
    }

    /** Todos los horarios ofertados por el club (libres y reservados), ordenados por fecha y hora. */
    public List<Horario> VerHorarios(){
        return h.findAll().stream()
                .filter(x -> x.getIdClub()==id_club)
                .sorted((a,b) -> {
                    int c = a.getFecha().compareTo(b.getFecha());
                    return c!=0 ? c : a.getHoraInicio().compareTo(b.getHoraInicio());
                })
                .collect(Collectors.toList());
    }

    /** Solo los horarios que ya han sido reservados por un cliente. */
    public List<Horario> VerReservas(){
        return VerHorarios().stream()
                .filter(x -> x.getIdReserva()!=null)
                .collect(Collectors.toList());
    }

    /** Solo los horarios libres de una fecha. */
    public List<Horario> VerLibres(LocalDate fecha){
        return h.findLibres(id_club, fecha);
    }

    /** Partido asociado a una reserva (PartidoDao no tiene búsqueda por reserva). */
    public Partido PartidoDeReserva(Long id_reserva){
        return p.findAll().stream()
                .filter(x -> x.getIdReserva()==id_reserva)
                .findFirst()
                .orElseThrow(()-> new ModeloException("La reserva " + id_reserva + " no tiene partido"));
    }

    /** Cuántos jugadores tiene la reserva de un horario (0 si el horario está libre). */
    public int NumeroJugadores(Horario horario){
        if(horario.getIdReserva()==null){
            return 0;
        }
        Partido partido = PartidoDeReserva(horario.getIdReserva());
        return j.findByPartido(partido.getIdPartido()).size();
    }

    /**
     * El trabajador cancela un horario ofertado. Solo se permite si nadie lo ha reservado.
     * @return true si se borró
     */
    public boolean CancelarHorario(int numeroPista, LocalDate fecha, LocalTime horaInicio){
        Horario horario = h.findById(id_club, numeroPista, fecha, horaInicio)
                .orElseThrow(()-> new ModeloException("El horario no existe"));
        if(horario.getIdReserva()!=null){
            int jugadores = NumeroJugadores(horario);
            throw new ModeloException("No se puede cancelar: el horario ya está reservado"
                    + (jugadores>0 ? " y tiene " + jugadores + " jugador(es) inscritos" : ""));
        }
        return h.delete(id_club, numeroPista, fecha, horaInicio);
    }
}
