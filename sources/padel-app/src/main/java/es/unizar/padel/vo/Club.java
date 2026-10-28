package es.unizar.padel.vo;

import java.io.Serializable;

/**
 * Value Object de la tabla club: solo guarda datos, sin lógica.
 * idClub es null mientras el club todavía no se ha guardado en la base de datos.
 */
public final class Club implements Serializable {

    private final Long idClub;
    private final String nombre;
    private final String direccion;
    private final String telefono;      // puede ser null

    public Club(Long idClub, String nombre, String direccion, String telefono) {
        this.idClub = idClub;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    public Long getIdClub() { return idClub; }
    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }
    public String getTelefono() { return telefono; }
}
