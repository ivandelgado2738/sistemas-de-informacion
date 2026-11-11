package es.unizar.padel.funcionalidades;

import java.util.ArrayList;
import java.util.List;

import es.unizar.padel.dao.JuegaDao;
import es.unizar.padel.dao.MensajeDao;
import es.unizar.padel.vo.Mensaje;

public class Chat {
    private MensajeDao m;
    private JuegaDao j;
    private Long id_partido;
    private List<Mensaje> chat;

    public Chat(Long id_partido,MensajeDao m,JuegaDao j){
        this.m=m;
        this.j=j;
        this.id_partido=id_partido;
        chat=new ArrayList<>();
    }

    public List<String> MostrarChat(){
        List<String> contenidos=new ArrayList<>();
        for(Mensaje d:chat){
            contenidos.add(d.getContenido());
        }
        return contenidos;
    }

    public boolean EscribirMensaje(String mensaje,Long id_usuario){
        j.findById(id_usuario, id_partido).orElseThrow(()->new ModeloException("El usuario no pertenece a este partido"));
        Mensaje men= m.create(new Mensaje(null, id_usuario, id_partido, mensaje, null));
        return chat.add(men);
    }

    public boolean BorrarMensaje(Long id_usuario, Long id_mensaje){
        Mensaje men=m.findById(id_mensaje).orElseThrow(()-> new ModeloException("El mensaje no existe"));
        if(men.getIdUsuario()!=id_usuario.longValue()){
            throw new ModeloException("No puedes borrar un mensaje que no sea tuyo");
        }
        return chat.remove(men) && m.delete(men.getIdMensaje());
    }
}
