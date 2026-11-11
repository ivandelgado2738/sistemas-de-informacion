package es.unizar.padel.funcionalidades;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import es.unizar.padel.dao.HorarioDao;
import es.unizar.padel.dao.JuegaDao;
import es.unizar.padel.dao.PartidoDao;
import es.unizar.padel.dao.ReservaDao;
import es.unizar.padel.dao.UsuarioDao;
import es.unizar.padel.vo.Horario;
import es.unizar.padel.vo.Juega;
import es.unizar.padel.vo.Partido;
import es.unizar.padel.vo.Reserva;

/**
 * Caso de uso del cliente: reservar un horario libre.
 * Es una operación de varios pasos (reserva + horario + partido + jugador). Si alguno falla,
 * se deshacen los anteriores para no dejar la base de datos a medias (ver el catch de Reservar).
 */
public class reservar {
    public static final String ESTADO_CONFIRMADA = "CONFIRMADA";

    private UsuarioDao u;
    private HorarioDao h;
    private ReservaDao r;
    private PartidoDao p;
    private JuegaDao j;
    private Long id_usuario;
    private Long id_club;
    private int num_pista;
    private LocalDate d;
    private LocalTime t;
    private Partido match;
    private Reserva reservada;

    public reservar(UsuarioDao u, HorarioDao h, ReservaDao r,PartidoDao p,JuegaDao j,Long id_usuario,Long id_club, int pista,LocalDate fecha,LocalTime t){
        this.u=u;
        this.h=h;
        this.r=r;
        this.p=p;
        this.j=j;
        this.id_usuario=id_usuario;
        this.id_club=id_club;
        num_pista=pista;
        d=fecha;
        this.t=t;
        reservada=null;
    }

    public Reserva Reservar(boolean privado){
        u.findById(id_usuario).orElseThrow(()->new ModeloException("El usuario no existe"));
        Horario horario=h.findById(id_club,num_pista,d,t).orElseThrow(()-> new ModeloException("El horario no existe"));
        if(LocalDateTime.of(horario.getFecha(), horario.getHoraInicio()).isBefore(LocalDateTime.now())){
            throw new ModeloException("El horario no puede ser anterior a la hora actual");
        }
        if(horario.getIdReserva()!=null){
            throw new ModeloException("El horario ya esta reservado");
        }

        // Pasos de escritura, en orden. Cada uno se apunta en una variable para poder deshacerlo.
        Reserva reserva=null;
        boolean horarioOcupado=false;
        Partido partido=null;
        try{
            // 1. Crear la reserva
            reserva= r.create(new Reserva(null, id_usuario, ESTADO_CONFIRMADA));

            // 2. Ocupar el horario con esa reserva
            Horario ocupado=new Horario(horario.getIdClub(), horario.getNumeroPista(), horario.getFecha(), horario.getHoraInicio(),
                    horario.getHoraFin(), horario.getPrecio(), reserva.getIdReserva());
            if(!h.update(ocupado)){
                throw new ModeloException("No se ha podido reservar el horario");
            }
            horarioOcupado=true;

            // 3. Crear el partido asociado
            partido=p.create(new Partido(null, null, reserva.getIdReserva(), privado));

            // 4. Apuntar al usuario que reserva como primer jugador
            j.create(new Juega(id_usuario, partido.getIdPartido(), false, false));

            reservada=reserva;
            match=partido;
            return reserva;

        }catch (RuntimeException e){
            // Deshacer en orden inverso lo que ya se había guardado
            if(partido!=null){
                p.delete(partido.getIdPartido());
            }
            if(horarioOcupado){
                h.update(new Horario(horario.getIdClub(), horario.getNumeroPista(), horario.getFecha(), horario.getHoraInicio(),
                        horario.getHoraFin(), horario.getPrecio(), null));
            }
            if(reserva!=null){
                r.delete(reserva.getIdReserva());
            }
            reservada=null;
            throw e;
        }
    }

    public Reserva Reservar(){
        return Reservar(false);
    }

    public Reserva cancelar(){
        if(reservada==null){
            throw new ModeloException("Esta reserva no existe");
        }
        Horario horario= h.findById(id_club, num_pista, d, t).orElseThrow(()-> new ModeloException("el horario no existe"));
        if(!j.delete(id_usuario, match.getIdPartido())){
            throw new ModeloException("No se ha podido borrar el jugador del partido");
        }
        if(!p.delete(match.getIdPartido())){
            throw new ModeloException("No se ha podido borrar el partido");
        }
        if(!h.update(new Horario(horario.getIdClub(),horario.getNumeroPista(),horario.getFecha(),horario.getHoraInicio(),horario.getHoraFin(),
            horario.getPrecio(),null))){
            throw new ModeloException("No se ha podido cancelar la reserva");
        }
        r.update(new Reserva(reservada.getIdReserva(), reservada.getIdUsuario(), CancelarReserva.ESTADO_CANCELADA));
        reservada=null;
        return reservada;
    }
}
