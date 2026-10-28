package es.unizar.padel.vo;

import java.io.Serializable;

/**
 * Value Object de la tabla club: solo guarda datos, sin lógica.
 * idClub es null mientras el club todavía no se ha guardado en la base de datos.
 */
public final class Favoritos implements Serializable {

    private final Long idClub;
    private final Long idUsuario;      // puede ser null

    public Favoritos(Long idClub, Long idUsuario) {
        this.idClub = idClub;
        this.idUsuario=idUsuario;
    }

    public Long getIdClub() { return idClub; }
    public Long getIdUsuario(){return idUsuario;}
}
