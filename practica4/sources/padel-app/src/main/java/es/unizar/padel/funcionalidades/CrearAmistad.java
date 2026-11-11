package es.unizar.padel.funcionalidades;

import es.unizar.padel.dao.AmistadDao;
import es.unizar.padel.dao.UsuarioDao;
import es.unizar.padel.vo.Amistad;

public class CrearAmistad {
    private AmistadDao rel;
    private UsuarioDao u;
    private Long id_usuario;
    private Long id_amigo;
    private Amistad a;

    public CrearAmistad(AmistadDao rel, UsuarioDao u, Long id_usuario,Long id_amigo){
        this.rel=rel;
        this.u=u;
        this.id_usuario=id_usuario;
        this.id_amigo=id_amigo;
    }

    public Amistad Crear(){
        u.findById(id_usuario).orElseThrow(()-> new ModeloException("El usuario: " + id_usuario + "no existe"));
        u.findById(id_amigo).orElseThrow(()-> new ModeloException("El usuario: " + id_amigo + "no existe"));
        Amistad amistad=rel.create(new Amistad(id_usuario, id_amigo));
        if(amistad==null){
            throw new ModeloException("No se ha podido crear la amistad");
        }
        a=amistad;
        return amistad;
    }

    public Amistad Borrar(){
        if(a==null){
            throw new ModeloException("La amistada no existe");
        }
        rel.delete(id_usuario,id_amigo);
        a=null;
        return a;
    }
}