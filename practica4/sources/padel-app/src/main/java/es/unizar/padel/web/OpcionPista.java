package es.unizar.padel.web;

/**
 * Una opción del desplegable de pistas de ofrecerReserva: valor "idClub-numero" y texto visible.
 */
public class OpcionPista {
    private final String valor;
    private final String texto;

    public OpcionPista(long idClub, int numero, String nombreClub) {
        this.valor = idClub + "-" + numero;
        this.texto = nombreClub + " - Pista " + numero;
    }

    public String getValor() { return valor; }
    public String getTexto() { return texto; }
}
