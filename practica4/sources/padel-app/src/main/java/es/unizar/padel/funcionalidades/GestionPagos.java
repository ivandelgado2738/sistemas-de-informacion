package es.unizar.padel.funcionalidades;

import java.util.List;

import es.unizar.padel.dao.JuegaDao;
import es.unizar.padel.dao.PartidoDao;
import es.unizar.padel.vo.Juega;

/**
 * Caso de uso del administrador: ver y marcar quién ha pagado en un partido.
 */
public class GestionPagos {
    private PartidoDao p;
    private JuegaDao j;
    private Long id_partido;

    public GestionPagos(Long id_partido, PartidoDao p, JuegaDao j){
        this.id_partido=id_partido;
        this.p=p;
        this.j=j;
        p.findById(id_partido).orElseThrow(()-> new ModeloException("El partido no existe"));
    }

    /** Jugadores del partido con su estado de pago (isHaPagado) y si llevan bolas. */
    public List<Juega> VerPagos(){
        return j.findByPartido(id_partido);
    }

    /** Marca a un jugador como pagado. */
    public boolean MarcarPagado(Long id_usuario){
        return cambiarPago(id_usuario, true);
    }

    /** Marca a un jugador como no pagado (para deshacer un error). */
    public boolean MarcarNoPagado(Long id_usuario){
        return cambiarPago(id_usuario, false);
    }

    /** @return true si todos los jugadores del partido han pagado */
    public boolean TodosHanPagado(){
        List<Juega> jugadores = VerPagos();
        return !jugadores.isEmpty() && jugadores.stream().allMatch(Juega::isHaPagado);
    }

    private boolean cambiarPago(Long id_usuario, boolean pagado){
        Juega jugador = j.findById(id_usuario, id_partido)
                .orElseThrow(()-> new ModeloException("El usuario no juega este partido"));
        if(jugador.isHaPagado()==pagado){
            throw new ModeloException(pagado ? "El jugador ya estaba marcado como pagado"
                                             : "El jugador ya estaba marcado como no pagado");
        }
        return j.update(new Juega(jugador.getIdUsuario(), jugador.getIdPartido(), pagado, jugador.isLlevaBolas()));
    }
}
