package es.unizar.padel.funcionalidades;

import java.util.Optional;

import es.unizar.padel.dao.UsuarioDao;
import es.unizar.padel.vo.Usuario;

/**
 * Caso de uso: iniciar sesión.
 * Busca al usuario por su email, comprueba que esté activo y que la contraseña coincida.
 */
public class IniciarSesion {
    public static final String ROL_ADMIN = "ADMIN";
    public static final String ESTADO_ACTIVO = "ACTIVO";

    private UsuarioDao u;

    public IniciarSesion(UsuarioDao u){
        this.u=u;
    }

    /**
     * @return el usuario autenticado
     * @throws ModeloException si el email no existe, la cuenta está bloqueada o la contraseña es incorrecta
     */
    public Usuario Entrar(String email, String contrasena){
        Usuario user = buscarPorEmail(email)
                .orElseThrow(()-> new ModeloException("Email o contraseña incorrectos"));

        if(!ESTADO_ACTIVO.equals(user.getEstado())){
            throw new ModeloException("La cuenta está bloqueada");
        }
        if(!Seguridad.verificar(contrasena, user.getContrasena())){
            // El mismo mensaje que si no existe el email: así no se revela qué emails están registrados
            throw new ModeloException("Email o contraseña incorrectos");
        }
        return user;
    }

    /** @return true si el usuario tiene rol de administrador */
    public boolean EsAdministrador(Usuario user){
        return ROL_ADMIN.equals(user.getRol());
    }

    /** UsuarioDao no tiene búsqueda por email, así que se recorre la lista completa. */
    private Optional<Usuario> buscarPorEmail(String email){
        if(email==null){
            return Optional.empty();
        }
        return u.findAll().stream()
                .filter(x -> email.equalsIgnoreCase(x.getEmail()))
                .findFirst();
    }
}
