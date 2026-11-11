package es.unizar.padel.funcionalidades;

import java.util.List;

import es.unizar.padel.dao.UsuarioDao;
import es.unizar.padel.vo.Usuario;

/**
 * Caso de uso del administrador: listar, editar, bloquear y activar usuarios.
 * Corresponde a las pantallas gestionUsuarios y editarUsuario.
 */
public class GestionUsuarios {
    public static final String ESTADO_ACTIVO = "ACTIVO";
    public static final String ESTADO_BLOQUEADO = "BLOQUEADO";

    private UsuarioDao u;

    public GestionUsuarios(UsuarioDao u){
        this.u=u;
    }

    public List<Usuario> Listar(){
        return u.findAll();
    }

    public Usuario Ver(Long id_usuario){
        return u.findById(id_usuario).orElseThrow(()-> new ModeloException("El usuario no existe"));
    }

    /** Cambia nombre y email de un usuario. El resto de campos se conserva. */
    public boolean Editar(Long id_usuario, String nombre, String email){
        Usuario user = Ver(id_usuario);
        if(nombre==null || nombre.isBlank() || email==null || email.isBlank()){
            throw new ModeloException("El nombre y el email no pueden estar vacíos");
        }
        return u.update(new Usuario(user.getId(), user.getRol(), email, nombre,
                user.getContrasena(), user.getEstado(), user.getNivel()));
    }

    public boolean Bloquear(Long id_usuario){
        return cambiarEstado(id_usuario, ESTADO_BLOQUEADO);
    }

    public boolean Activar(Long id_usuario){
        return cambiarEstado(id_usuario, ESTADO_ACTIVO);
    }

    private boolean cambiarEstado(Long id_usuario, String estado){
        Usuario user = Ver(id_usuario);
        if(estado.equals(user.getEstado())){
            throw new ModeloException("El usuario ya estaba " + estado.toLowerCase());
        }
        return u.update(new Usuario(user.getId(), user.getRol(), user.getEmail(), user.getNombre(),
                user.getContrasena(), estado, user.getNivel()));
    }
}
