package es.unizar.padel.vo;

import java.io.Serializable;

/**
 * Value Object de la tabla partido.
 * idPartido es null mientras no se ha guardado en la base de datos.
 */
public final class Partido implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Long idPartido;
    private final String resultado;   // puede ser null (partido sin jugar)
    private final long idReserva;
    private final boolean privado;

    public Partido(Long idPartido, String resultado, long idReserva,boolean privado) {
        this.idPartido = idPartido;
        this.resultado = resultado;
        this.idReserva = idReserva;
        this.privado=privado; 
    }

    public Long getIdPartido() { return idPartido; }
    public String getResultado() { return resultado; }
    public long getIdReserva() { return idReserva; }
    public boolean getPrivado() { return privado;}
}
