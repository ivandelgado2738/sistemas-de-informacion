package es.unizar.padel.funcionalidades;

import es.unizar.padel.dao.UsuarioDao;
import es.unizar.padel.vo.Usuario;

/**
 * Caso de uso del cliente sobre su propia cuenta: ver y cambiar sus datos y darse de baja.
 * Corresponde a las pantallas perfil y confirmarBaja.
 */
public class GestionPerfil {
    private UsuarioDao u;
    private Long id_usuario;

    public GestionPerfil(Long id_usuario, UsuarioDao u){
        this.id_usuario=id_usuario;
        this.u=u;
        u.findById(id_usuario).orElseThrow(()-> new ModeloException("El usuario no existe"));
    }

    public Usuario Ver(){
        return u.findById(id_usuario).orElseThrow(()-> new ModeloException("El usuario no existe"));
    }

    public boolean CambiarNombre(String nombre){
        if(nombre==null || nombre.isBlank()){
            throw new ModeloException("El nombre no puede estar vacío");
        }
        Usuario user = Ver();
        return u.update(new Usuario(user.getId(), user.getRol(), user.getEmail(), nombre,
                user.getContrasena(), user.getEstado(), user.getNivel()));
    }

    public boolean CambiarEmail(String email){
        if(email==null || !email.contains("@")){
            throw new ModeloException("El email no es válido");
        }
        Usuario user = Ver();
        return u.update(new Usuario(user.getId(), user.getRol(), email, user.getNombre(),
                user.getContrasena(), user.getEstado(), user.getNivel()));
    }

    /** Cambia la contraseña. Pide la actual para evitar que otra persona la cambie desde una sesión abierta. */
    public boolean CambiarContrasena(String actual, String nueva){
        Usuario user = Ver();
        if(!Seguridad.verificar(actual, user.getContrasena())){
            throw new ModeloException("La contraseña actual no es correcta");
        }
        if(nueva==null || nueva.length()<6){
            throw new ModeloException("La nueva contraseña debe tener al menos 6 caracteres");
        }
        return u.update(new Usuario(user.getId(), user.getRol(), user.getEmail(), user.getNombre(),
                Seguridad.hash(nueva), user.getEstado(), user.getNivel()));
    }

    /** Baja de la cuenta. Se confirma con la contraseña, como en la pantalla confirmarBaja. */
    public boolean DarseDeBaja(String contrasena){
        Usuario user = Ver();
        if(!Seguridad.verificar(contrasena, user.getContrasena())){
            throw new ModeloException("La contraseña no es correcta");
        }
        return u.delete(id_usuario);
    }
}
