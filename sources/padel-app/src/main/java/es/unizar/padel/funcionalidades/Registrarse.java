package es.unizar.padel.funcionalidades;

import es.unizar.padel.dao.UsuarioDao;
import es.unizar.padel.vo.Usuario;

public class Registrarse {
    private UsuarioDao u;
    private String nombre;
    private String email;
    private String contraseña;
    private Usuario user;

    Registrarse(UsuarioDao u,String nombre, String email, String contraseña){
        this.u=u;
        this.nombre=nombre;
        this.email=email;
        this.contraseña=contraseña;
    }

    public Usuario Registro(){
        Usuario user=u.create(new Usuario(null, null, email, nombre, contraseña, null, null));
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
