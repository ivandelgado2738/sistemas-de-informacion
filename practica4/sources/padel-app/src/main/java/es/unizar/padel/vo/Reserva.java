package es.unizar.padel.vo;

import java.io.Serializable;

/**
 * Value Object de la tabla reserva.
 * idReserva es null mientras no se ha guardado en la base de datos.
 */
public final class Reserva implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Long idReserva;
    private final long idUsuario;
    private final String estado;   // PENDIENTE | CONFIRMADA | CANCELADA (null = por defecto)

    public Reserva(Long idReserva, long idUsuario, String estado) {
        this.idReserva = idReserva;
        this.idUsuario = idUsuario;
        this.estado = estado;
    }

    public Long getIdReserva() { return idReserva; }
    public long getIdUsuario() { return idUsuario; }
    public String getEstado() { return estado; }
}
