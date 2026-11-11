package es.unizar.padel.funcionalidades;

import es.unizar.padel.dao.UsuarioDao;
import es.unizar.padel.vo.Usuario;

/**
 * Caso de uso: registro de un nuevo cliente.
 * La contraseña se guarda como hash (nunca en claro) y el usuario nace con rol USUARIO y estado ACTIVO.
 */
public class Registrarse {
    public static final String ROL_USUARIO = "USUARIO";
    public static final String ESTADO_ACTIVO = "ACTIVO";
    public static final String NIVEL_POR_DEFECTO = "PRINCIPIANTE";

    private UsuarioDao u;
    private String nombre;
    private String email;
    private String contraseña;
    private Usuario user;

    public Registrarse(UsuarioDao u,String nombre, String email, String contraseña){
        this.u=u;
        this.nombre=nombre;
        this.email=email;
        this.contraseña=contraseña;
    }

    public Usuario Registro(){
        if(nombre==null || nombre.isBlank()){
            throw new ModeloException("El nombre no puede estar vacío");
        }
        if(email==null || !email.contains("@")){
            throw new ModeloException("El email no es válido");
        }
        if(contraseña==null || contraseña.length()<6){
            throw new ModeloException("La contraseña debe tener al menos 6 caracteres");
        }
        boolean emailRepetido = u.findAll().stream().anyMatch(x -> email.equalsIgnoreCase(x.getEmail()));
        if(emailRepetido){
            throw new ModeloException("Ya existe una cuenta con ese email");
        }
        Usuario user=u.create(new Usuario(null, ROL_USUARIO, email, nombre,
                Seguridad.hash(contraseña), ESTADO_ACTIVO, NIVEL_POR_DEFECTO));
        this.user=user;
        return user;
    }

    public Usuario Baja(){
        if(user==null){
            throw new ModeloException("El usuario no existe");
        }
        if(u.delete(this.user.getId())){
            return null;
        }else{
            return user;
        }
    }
}
