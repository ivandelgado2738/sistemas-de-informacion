package es.unizar.padel.vo;

import java.io.Serializable;

/** Value Object de la tabla favorito (usuario - club). */
public final class Favorito implements Serializable {

    private static final long serialVersionUID = 1L;

    private final long idUsuario;
    private final long idClub;

    public Favorito(long idUsuario, long idClub) {
        this.idUsuario = idUsuario;
        this.idClub = idClub;
    }

    public long getIdUsuario() { return idUsuario; }
    public long getIdClub() { return idClub; }
}
