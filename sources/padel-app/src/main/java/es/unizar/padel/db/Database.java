package es.unizar.padel.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

/**
 * Punto único de acceso a la base de datos.
 * Crea un pool de conexiones (HikariCP) y lo expone como DataSource.
 * Los DAO pedirán las conexiones a este DataSource.
 */
public final class Database {

    private static final String URL = "jdbc:postgresql://localhost:5432/padel";
    private static final String USUARIO = "padel_app";      // usuario con permisos mínimos
    private static final String CONTRASENA = "padel_app";

    private static final HikariDataSource DATA_SOURCE = crear();

    private Database() {
    }

    private static HikariDataSource crear() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(URL);
        config.setUsername(USUARIO);
        config.setPassword(CONTRASENA);
        config.setMaximumPoolSize(5);
        return new HikariDataSource(config);
    }

    public static DataSource getDataSource() {
        return DATA_SOURCE;
    }
}
