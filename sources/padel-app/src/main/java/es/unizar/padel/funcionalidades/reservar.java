package es.unizar.padel.funcionalidades;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import es.unizar.padel.dao.HorarioDao;
import es.unizar.padel.dao.JuegaDao;
import es.unizar.padel.dao.PartidoDao;
import es.unizar.padel.dao.ReservaDao;
import es.unizar.padel.dao.UsuarioDao;
import es.unizar.padel.vo.Horario;
import es.unizar.padel.vo.Juega;
import es.unizar.padel.vo.Partido;
import es.unizar.padel.vo.Reserva;

public class reservar {
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

    public Reserva Reservar(){
        u.findById(id_usuario).orElseThrow(()->new ModeloException("El usuario no existe"));
        Horario horario=h.findById(id_club,num_pista,d,t).orElseThrow(()-> new ModeloException("El horario no existe"));
        if(LocalDateTime.of(horario.getFecha(), horario.getHoraInicio()).isBefore(LocalDateTime.now())){
            throw new ModeloException("El horario no puede ser anterior a la hora actual");
        }        
        if(horario.getIdReserva()!=null){
            throw new ModeloException("El horario ya esta reservado");
        }
        try{
            Reserva reserva= r.create(new Reserva(null, id_usuario, "Confirmada"));
            List<Horario>libres=h.findLibres(id_club, d);
            if(!libres.contains(horario)){
                throw new ModeloException("El horario esta ocupado");
            }
            horario=new Horario(horario.getIdClub(), horario.getNumeroPista(), horario.getFecha(), horario.getHoraInicio(),
            horario.getHoraFin(), horario.getPrecio(), reserva.getIdReserva());
            if(!h.update(horario)){
                throw new ModeloException("No se ha podido reservar el horario");
            }
            reservada=reserva;
            Partido partido=p.create(new Partido(null, "0-0", reserva.getIdReserva()));
            j.create(new Juega(id_usuario, partido.getIdPartido(), false, false));
            match=partido;
            return reserva;
        }catch (RuntimeException e){
            reservada=null;
            throw e;
        }
    }

    public Reserva cancelar(){
        if(reservada==null){
            throw new ModeloException("Esta reserva no existe");
        }
        Horario horario= h.findById(id_club, num_pista, d, t).orElseThrow(()-> new ModeloException("el horario no existe"));
        if(!j.delete(id_usuario, match.getIdPartido())){
            throw new ModeloException("No se ha podido borrar el partido");
        }
        if(!p.delete(match.getIdPartido())){
            throw new ModeloException("No se ha podido borrar el partido");
        }
        if(!h.update(new Horario(horario.getIdClub(),horario.getNumeroPista(),horario.getFecha(),horario.getHoraInicio(),horario.getHoraFin(),
            horario.getPrecio(),null))){
            throw new ModeloException("No se ha podido cancelar la reserva");
        }
        reservada=null;
        return reservada;
    }
}
