package es.unizar.padel.funcionalidades;

import java.util.ArrayList;
import java.util.List;

import es.unizar.padel.dao.InvitacionDao;
import es.unizar.padel.dao.JuegaDao;
import es.unizar.padel.dao.MensajeDao;
import es.unizar.padel.dao.PartidoDao;
import es.unizar.padel.dao.UsuarioDao;
import es.unizar.padel.vo.Invitacion;
import es.unizar.padel.vo.Juega;
import es.unizar.padel.vo.Partido;
import es.unizar.padel.vo.Usuario;

public class GestionPartido {
    private Long id_partido;
    private PartidoDao p;
    private MensajeDao m;
    private JuegaDao j;
    private UsuarioDao u;
    private InvitacionDao i;
    private Chat c;
    private Long creador;
    private List<Juega> jugadores;
    private boolean alguien_bolas;
    private boolean pagado[];

    public GestionPartido(Long id_partido,Long creador, PartidoDao p,MensajeDao m, JuegaDao j, UsuarioDao u, InvitacionDao i){
        this.id_partido=id_partido;
        this.m=m;
        this.j=j;
        this.creador=creador;
        this.p=p;
        this.i=i;
        c=new Chat(id_partido,m,j);
        pagado=new boolean[4];
        jugadores=new ArrayList<>();
    }

    public Juega AñadirJugador(Long id_usuario){
        u.findById(id_usuario).orElseThrow(()-> new ModeloException("El usuario no existe"));
        Juega jugador=j.create(new Juega(id_usuario,id_partido, false, false));
        jugadores.add(jugador);
        return jugador;
    }

    public boolean QuitarJugador(Long id_usuario){
        Juega jugador=j.findById(id_usuario, id_partido).orElseThrow(()-> new ModeloException("No existe el usuario"));
        if(jugadores.contains(jugador)){
            if(jugador.isLlevaBolas()){
                alguien_bolas=false;
            }
            jugadores.remove(jugador);
            j.delete(id_usuario, id_partido);
            return true;
        }else{
            return false;
        }
    }

    public Invitacion InvitarUsuario(Long id_usuario){
        u.findById(id_usuario).orElseThrow(() -> new ModeloException("El usuario no existe"));
        Invitacion inv=i.create(new Invitacion(id_usuario, id_partido));
        if(inv==null){
            throw new ModeloException("No se ha podido crear la invitación");
        }
        return inv;
    }

    public boolean EliminarInvitacion(Long id_usuario){
        u.findById(id_usuario).orElseThrow(() -> new ModeloException("El usuario no existe"));
        i.findById(id_usuario, id_partido).orElseThrow(() -> new ModeloException("La invitacion no existe"));
        if(i.delete(id_usuario, id_partido)){
            return true;
        }else{
            return false;
        }
    }

    public boolean ModificarPrivacidad(){
        Partido partido=p.findById(id_partido).orElseThrow(()-> new ModeloException("El partido no existe"));
        boolean invert=!partido.getPrivado();
        return p.update(new Partido(partido.getIdPartido(), partido.getResultado(), partido.getIdReserva(), invert));
    }

    public boolean LLevarBolas(Long id_usuario){
        if(!alguien_bolas){
            u.findById(id_usuario).orElseThrow(()-> new ModeloException("El usuario no existe"));
            Juega jugador=j.findById(id_usuario, id_partido).orElseThrow(()-> new ModeloException("El jugador no juega este partido"));
            if(jugador.isLlevaBolas()){
                throw new ModeloException("El jugador ya lleva bolas");
            }
            alguien_bolas=true;
            return j.update(new Juega(jugador.getIdUsuario(),jugador.getIdPartido(), jugador.isHaPagado(), true));
        }else{
            throw new ModeloException("En este partido ya se llevan bolas");
        }
    }

    public boolean CancelarBolas(Long id_usuario){
        if(!alguien_bolas){
            throw new ModeloException("En este partido nadie lleva bolas");
        }
        u.findById(id_usuario).orElseThrow(()-> new ModeloException("El usuario no existe"));
        Juega jugador=j.findById(id_usuario, id_partido).orElseThrow(()-> new ModeloException("El jugador no juega este partido"));
        if(!jugador.isLlevaBolas()){
            throw new ModeloException("El jugador no lleva bolas");
        }
        alguien_bolas=false;
        return j.update(new Juega(jugador.getIdUsuario(),jugador.getIdPartido(), jugador.isHaPagado(), false));
    }

    public boolean Pagar(Long id_usuario){
        Usuario user=u.findById(id_usuario).orElseThrow(() -> new ModeloException("El usuario no existe"));
        if(!jugadores.contains(user) && id_usuario!=creador){
            throw new ModeloException("El usuario no juega este partido");
        }
        Juega jugador=j.findById(id_usuario, id_partido).orElseThrow(()-> new ModeloException("El jugador no juega este partido"));
        if(!jugadores.contains(user)){
            pagado[0]=true;
        }else{
            pagado[jugadores.indexOf(user)]=true;
        }
        return j.update(new Juega(jugador.getIdUsuario(),jugador.getIdPartido(),true,jugador.isLlevaBolas()));
    }

    public boolean EscribirEnChat(Long id_usuario, String mensaje){
        return c.EscribirMensaje(mensaje, id_usuario);
    }

    public boolean BorrarMensajeChat(Long id_usuario, Long id_mensaje){
        return c.BorrarMensaje(id_usuario, id_mensaje);
    }

    public boolean IntroducirResultado(Long id_usuario, String resultado){
        if(id_usuario!=creador){
            throw new ModeloException("El resultado lo debe meter el creador del partido");
        }
        Partido partido=p.findById(id_partido).orElseThrow(()-> new ModeloException("El partido no existe"));
        return p.update(new Partido(partido.getIdPartido(), resultado, partido.getIdReserva(), partido.getPrivado()));
    }
}
