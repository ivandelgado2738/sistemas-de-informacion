package es.unizar.padel.funcionalidades;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import es.unizar.padel.dao.HorarioDao;
import es.unizar.padel.dao.JuegaDao;
import es.unizar.padel.dao.PartidoDao;
import es.unizar.padel.dao.ReservaDao;
import es.unizar.padel.vo.Horario;
import es.unizar.padel.vo.Juega;
import es.unizar.padel.vo.Partido;
import es.unizar.padel.vo.Reserva;

/**
 * Caso de uso del cliente: cancelar una reserva.
 * Según los requisitos, solo se cancela si TODOS los jugadores del partido están de acuerdo.
 * Cada jugador llama a Aceptar(); cuando todos han aceptado, Ejecutar() cancela la reserva.
 */
public class CancelarReserva {
    public static final String ESTADO_CANCELADA = "CANCELADA";

    private ReservaDao r;
    private HorarioDao h;
    private PartidoDao p;
    private JuegaDao j;
    private Long id_reserva;
    private Set<Long> deAcuerdo;

    public CancelarReserva(Long id_reserva, ReservaDao r, HorarioDao h, PartidoDao p, JuegaDao j){
        this.id_reserva=id_reserva;
        this.r=r;
        this.h=h;
        this.p=p;
        this.j=j;
        this.deAcuerdo=new HashSet<>();
        r.findById(id_reserva).orElseThrow(()-> new ModeloException("La reserva no existe"));
    }

    /** Un jugador del partido da su conformidad para cancelar. */
    public void Aceptar(Long id_usuario){
        Partido partido = partido();
        j.findById(id_usuario, partido.getIdPartido())
                .orElseThrow(()-> new ModeloException("El usuario no juega este partido"));
        deAcuerdo.add(id_usuario);
    }

    /** @return cuántos jugadores han aceptado ya la cancelación */
    public int NumeroAceptados(){
        return deAcuerdo.size();
    }

    /** @return true si ese usuario ya ha aceptado la cancelación */
    public boolean HaAceptado(Long id_usuario){
        return deAcuerdo.contains(id_usuario);
    }

    /** @return true si todos los jugadores han aceptado */
    public boolean TodosDeAcuerdo(){
        List<Juega> jugadores = j.findByPartido(partido().getIdPartido());
        return jugadores.stream().allMatch(x -> deAcuerdo.contains(x.getIdUsuario()));
    }

    /**
     * Cancela la reserva: la marca como CANCELADA, libera el horario y borra el partido
     * (la base de datos borra en cascada las filas de juega y los mensajes).
     */
    public Reserva Ejecutar(){
        if(!TodosDeAcuerdo()){
            throw new ModeloException("No todos los jugadores han aceptado la cancelación");
        }
        Reserva reserva = r.findById(id_reserva).orElseThrow(()-> new ModeloException("La reserva no existe"));
        if(ESTADO_CANCELADA.equals(reserva.getEstado())){
            throw new ModeloException("La reserva ya estaba cancelada");
        }
        Partido partido = partido();

        // 1. Liberar el horario (HorarioDao no busca por reserva: se recorre la lista)
        Horario horario = h.findAll().stream()
                .filter(x -> id_reserva.equals(x.getIdReserva()))
                .findFirst()
                .orElseThrow(()-> new ModeloException("La reserva no tiene horario asociado"));
        h.update(new Horario(horario.getIdClub(), horario.getNumeroPista(), horario.getFecha(),
                horario.getHoraInicio(), horario.getHoraFin(), horario.getPrecio(), null));

        // 2. Borrar el partido (cascada: juega y mensajes)
        p.delete(partido.getIdPartido());

        // 3. Marcar la reserva como cancelada (se conserva para el historial)
        Reserva cancelada = new Reserva(reserva.getIdReserva(), reserva.getIdUsuario(), ESTADO_CANCELADA);
        if(!r.update(cancelada)){
            throw new ModeloException("No se ha podido cancelar la reserva");
        }
        return cancelada;
    }

    private Partido partido(){
        return p.findAll().stream()
                .filter(x -> x.getIdReserva()==id_reserva)
                .findFirst()
                .orElseThrow(()-> new ModeloException("La reserva no tiene partido"));
    }
}
