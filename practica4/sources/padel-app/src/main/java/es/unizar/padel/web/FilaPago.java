package es.unizar.padel.web;

/** Una fila de la tabla de marcarPagos: un jugador del partido y su estado de pago. */
public class FilaPago {
    private final long idUsuario;
    private final String nombre;
    private final String email;
    private final String importe;
    private final boolean pagado;

    public FilaPago(long idUsuario, String nombre, String email, String importe, boolean pagado) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.email = email;
        this.importe = importe;
        this.pagado = pagado;
    }

    public long getIdUsuario() { return idUsuario; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    /** Parte del precio que le toca a este jugador, ya formateada (p. ej. "10,00 €"). */
    public String getImporte() { return importe; }
    public boolean isPagado() { return pagado; }
}
