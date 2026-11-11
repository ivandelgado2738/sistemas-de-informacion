package es.unizar.padel.vo;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * Value Object de la tabla mensaje.
 * idMensaje y fechaEnvio son null hasta que la BD los genera.
 */
public final class Mensaje implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Long idMensaje;
    private final long idUsuario;
    private final long idPartido;
    private final String contenido;
    private final OffsetDateTime fechaEnvio;

    public Mensaje(Long idMensaje, long idUsuario, long idPartido, String contenido,
                   OffsetDateTime fechaEnvio) {
        this.idMensaje = idMensaje;
        this.idUsuario = idUsuario;
        this.idPartido = idPartido;
        this.contenido = contenido;
        this.fechaEnvio = fechaEnvio;
    }

    public Long getIdMensaje() { return idMensaje; }
    public long getIdUsuario() { return idUsuario; }
    public long getIdPartido() { return idPartido; }
    public String getContenido() { return contenido; }
    public OffsetDateTime getFechaEnvio() { return fechaEnvio; }
}
