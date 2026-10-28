package es.unizar.padel.dao;

/**
 * Error de la capa de datos. Envuelve la SQLException original
 * (se puede consultar con getCause()) y añade un mensaje claro.
 */
public class DaoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DaoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
