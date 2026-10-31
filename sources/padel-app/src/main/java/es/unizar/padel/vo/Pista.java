package es.unizar.padel.vo;

import java.io.Serializable;

/** Value Object de la tabla pista. Clave primaria compuesta: (idClub, numero). */
public final class Pista implements Serializable {

    private static final long serialVersionUID = 1L;

    private final long idClub;
    private final int numero;
    private final boolean cubierta;

    public Pista(long idClub, int numero, boolean cubierta) {
        this.idClub = idClub;
        this.numero = numero;
        this.cubierta = cubierta;
    }

    public long getIdClub() { return idClub; }
    public int getNumero() { return numero; }
    public boolean isCubierta() { return cubierta; }
}
