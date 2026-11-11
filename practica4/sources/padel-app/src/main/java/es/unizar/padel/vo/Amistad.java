package es.unizar.padel.vo;

import java.io.Serializable;

/** Value Object de la tabla amistad (relación reflexiva usuario - usuario). */
public final class Amistad implements Serializable {

    private static final long serialVersionUID = 1L;

    private final long idUsuario;
    private final long idAmigo;

    public Amistad(long idUsuario, long idAmigo) {
        this.idUsuario = idUsuario;
        this.idAmigo = idAmigo;
    }

    public long getIdUsuario() { return idUsuario; }
    public long getIdAmigo() { return idAmigo; }
}
