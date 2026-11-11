package es.unizar.padel.funcionalidades;

import java.util.List;
import java.util.stream.Collectors;

import es.unizar.padel.dao.ClubDao;
import es.unizar.padel.dao.FavoritoDao;
import es.unizar.padel.dao.UsuarioDao;
import es.unizar.padel.vo.Club;
import es.unizar.padel.vo.Favorito;

/**
 * Caso de uso del cliente: marcar y quitar clubes favoritos, y listarlos.
 */
public class GestionFavoritos {
    private FavoritoDao f;
    private UsuarioDao u;
    private ClubDao c;
    private Long id_usuario;

    public GestionFavoritos(Long id_usuario, FavoritoDao f, UsuarioDao u, ClubDao c){
        this.id_usuario=id_usuario;
        this.f=f;
        this.u=u;
        this.c=c;
        u.findById(id_usuario).orElseThrow(()-> new ModeloException("El usuario no existe"));
    }

    public Favorito Marcar(Long id_club){
        c.findById(id_club).orElseThrow(()-> new ModeloException("El club no existe"));
        if(f.findById(id_usuario, id_club).isPresent()){
            throw new ModeloException("El club ya está en favoritos");
        }
        Favorito fav = f.create(new Favorito(id_usuario, id_club));
        if(fav==null){
            throw new ModeloException("No se ha podido añadir el favorito");
        }
        return fav;
    }

    public boolean Quitar(Long id_club){
        f.findById(id_usuario, id_club).orElseThrow(()-> new ModeloException("El club no está en favoritos"));
        return f.delete(id_usuario, id_club);
    }

    /** Clubes favoritos del usuario, como objetos Club (nombre, dirección, teléfono). */
    public List<Club> Listar(){
        return f.findByUsuario(id_usuario).stream()
                .map(x -> c.findById(x.getIdClub()).orElse(null))
                .filter(x -> x!=null)
                .collect(Collectors.toList());
    }
}
