package es.unizar.padel.vo;

import java.io.Serializable;

/**
 * Value Object de la tabla usuario: solo guarda datos, sin lógica.
 * id es null mientras el usuario no se ha guardado en la base de datos.
 * contrasena contiene el HASH, nunca la contraseña en claro.
 */
public final class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Long id;
    private final String rol;
    private final String email;
    private final String nombre;
    private final String contrasena;
    private final String estado;   // ACTIVO | BLOQUEADO (null = valor por defecto de la BD)
    private final String nivel;    // PRINCIPIANTE | INTERMEDIO | AVANZADO (null = por defecto)

    public Usuario(Long id, String rol, String email, String nombre, String contrasena, String estado, String nivel) {
        this.id = id;
        this.rol = rol;
        this.email = email;
        this.nombre = nombre;
        this.contrasena = contrasena;
        this.estado = estado;
        this.nivel = nivel;
    }

    public Long getId() { return id; }
    public String getRol(){return rol;}
    public String getEmail() { return email; }
    public String getNombre() { return nombre; }
    public String getContrasena() { return contrasena; }
    public String getEstado() { return estado; }
    public String getNivel() { return nivel; }
}
