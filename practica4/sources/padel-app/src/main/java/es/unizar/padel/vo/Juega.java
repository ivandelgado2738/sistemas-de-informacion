package es.unizar.padel.vo;

import java.io.Serializable;

/** Value Object de la tabla juega (usuario - partido) con sus atributos propios. */
public final class Juega implements Serializable {

    private static final long serialVersionUID = 1L;

    private final long idUsuario;
    private final long idPartido;
    private final boolean haPagado;
    private final boolean llevaBolas;

    public Juega(long idUsuario, long idPartido, boolean haPagado, boolean llevaBolas) {
        this.idUsuario = idUsuario;
        this.idPartido = idPartido;
        this.haPagado = haPagado;
        this.llevaBolas = llevaBolas;
    }

    public long getIdUsuario() { return idUsuario; }
    public long getIdPartido() { return idPartido; }
    public boolean isHaPagado() { return haPagado; }
    public boolean isLlevaBolas() { return llevaBolas; }
}
