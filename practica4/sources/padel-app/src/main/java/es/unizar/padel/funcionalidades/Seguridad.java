package es.unizar.padel.funcionalidades;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utilidades de seguridad: resumen (hash) de contraseñas.
 * Nunca se guarda la contraseña en claro en la base de datos; se guarda su hash SHA-256,
 * y para comprobar una contraseña se calcula el hash de lo que escribe el usuario y se compara.
 */
public final class Seguridad {

    private Seguridad() {
    }

    /** Devuelve el hash SHA-256 de la contraseña, en hexadecimal (64 caracteres). */
    public static String hash(String contrasena) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(contrasena.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new ModeloException("No se pudo calcular el hash de la contraseña");
        }
    }

    /** @return true si la contraseña en claro se corresponde con el hash guardado */
    public static boolean verificar(String contrasena, String hashGuardado) {
        return hashGuardado != null && hashGuardado.equals(hash(contrasena));
    }
}
