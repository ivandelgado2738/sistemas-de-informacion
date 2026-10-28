-- =====================================================
--  DDL: reservas de pistas
--  Base de datos: padel   |   Esquema: sisinf_p3
-- =====================================================


CREATE SCHEMA IF NOT EXISTS sisinf_p3_grupo_11_07;

-- Activa PostGIS en el esquema del proyecto (lo exige el enunciado)
CREATE EXTENSION IF NOT EXISTS postgis WITH SCHEMA sisinf_p3_grupo_11_07;

-- A partir de aquí, las tablas se crean dentro de sisinf_p3
SET search_path TO sisinf_p3_grupo_11_07, public;

-- Borrado previo (orden inverso por las claves foráneas)
DROP TABLE IF EXISTS usuario_reserva;
DROP TABLE IF EXISTS juega;
DROP TABLE IF EXISTS favorito;
DROP TABLE IF EXISTS amistD;
DROP TABLE IF EXISTS mensaje;
DROP TABLE IF EXISTS partido;
DROP TABLE IF EXISTS horario;
DROP TABLE IF EXISTS reserva;
DROP TABLE IF EXISTS pista;
DROP TABLE IF EXISTS club;
DROP TABLE IF EXISTS usuario;

-- ---------- ENTIDADES ----------

CREATE TABLE usuario (
    id  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email       VARCHAR(255) NOT NULL UNIQUE,
    nombre      VARCHAR(100) NOT NULL,
    contrasena  VARCHAR(255) NOT NULL,              -- aquí va el HASH, nunca la contraseña en claro
    rol         VARCHAR(20)  NOT NULL DEFAULT 'USUARIO'
                CHECK (rol IN ('USUARIO', 'ADMIN')),
    estado      VARCHAR(20)  NOT NULL DEFAULT 'ACTIVO'
                CHECK (estado IN ('ACTIVO', 'BLOQUEADO')),
    Nivel       VARCHAR(12)   NOT NULL DEFAULT 'PRINCIPIANTE'
                CHECK(Nivel IN('PRINCIPIANTE','INTERMEDIO','AVANZADO'))
);

CREATE TABLE club (
    id_club     BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre      VARCHAR(100) NOT NULL UNIQUE,
    direccion   VARCHAR(255) NOT NULL,
    telefono    VARCHAR(20)  NOT NULL
);

CREATE TABLE pista (
    id_club     BIGINT,
    numero      INT,
    nombre      VARCHAR(50) NOT NULL,
    PRIMARY KEY(id_club,numero),
    FOREIGN KEY(id_club) REFERENCES club(id_club)
);

CREATE TABLE reserva (
    id_reserva  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_usuario  BIGINT NOT NULL,
    estado      VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE'
                CHECK (estado IN ('PENDIENTE', 'CONFIRMADA', 'CANCELADA')),
    FOREIGN KEY(id_usuario) REFERENCES usuario(id)
);

CREATE TABLE horario (
    id_club      BIGINT,
    numero_pista INT NOT NULL,
    fecha        DATE NOT NULL,
    hora_inicio  TIME NOT NULL,
    hora_fin     TIME NOT NULL,
    precio       INT  NOT NULL,
    id_reserva   BIGINT,
    PRIMARY KEY(id_club,numero_pista,fecha,hora_inicio),
    FOREIGN KEY(numero_pista,id_club) REFERENCES pista(numero,id_club),
    FOREIGN KEY(id_reserva) REFERENCES reserva(id_reserva),
    CONSTRAINT ck_horario_horas CHECK (hora_fin > hora_inicio)
);

CREATE TABLE partido (
    id_partido  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    resultado   VARCHAR(255),
    id_reserva  BIGINT NOT NULL,
    FOREIGN KEY(id_reserva) REFERENCES reserva(id_reserva)
);

CREATE TABLE mensaje (
    id_mensaje  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_usuario  BIGINT NOT NULL
    id_partido  BIGINT NOT NULL 
    contenido   TEXT NOT NULL,
    fecha_envio TIMESTAMPTZ NOT NULL DEFAULT now(),
    FOREIGN KEY(id_partido) REFERENCES partido(id_partido),
    FOREIGN KEY(id_usuario) REFERENCES usuario(id)
);

-- ---------- RELACIONES N:M ----------

-- es amigo de (reflexiva sobre usuario)
CREATE TABLE amistad (
    id_usuario  BIGINT NOT NULL,
    id_amigo    BIGINT NOT NULL,
    PRIMARY KEY (id_usuario, id_amigo),
    CONSTRAINT ck_amigo_distinto CHECK (id_usuario <> id_amigo),
    FOREIGN KEY(id_usuario) REFERENCES usuario(id),
    FOREIGN KEY(id_amigo) REFERENCES usuario(id)
);

-- tiene como favorito
CREATE TABLE favorito (
    id_usuario  BIGINT NOT NULL,
    id_club     BIGINT NOT NULL,
    PRIMARY KEY (id_usuario, id_club),
    FOREIGN KEY(id_usuario) REFERENCES usuario(id),
    FOREIGN KEY(id_club) REFERENCES club(id_club)
);

-- juega
CREATE TABLE juega (
    id_usuario  BIGINT NOT NULL,
    id_partido  BIGINT NOT NULL,
    PRIMARY KEY (id_usuario, id_partido),
    FOREIGN KEY(id_usuario) REFERENCES usuario(id),
    FOREIGN KEY(id_partido) REFERENCES partido(id_partido)
);


-- ---------- ÍNDICES en claves foráneas ----------
-- (pista, horario, partido ya quedan cubiertas por sus UNIQUE;
--  las PK compuestas cubren la primera columna de cada N:M)
CREATE INDEX idx_mensaje_usuario        ON mensaje (id_usuario);
CREATE INDEX idx_favorito_club          ON favorito (id_club);
CREATE INDEX idx_juega_partido          ON juega (id_partido);

-- ---------- USUARIO DE LA APLICACIÓN (permisos mínimos) ----------
-- Solo puede leer y escribir datos; no puede crear ni borrar tablas.
DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'padel_app') THEN
        CREATE ROLE padel_app LOGIN PASSWORD 'padel_app';
    END IF;
END
$$;

GRANT USAGE ON SCHEMA sisinf_p3_grupo_11_07 TO padel_app;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA sisinf_p3_grupo_11_07 TO padel_app;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA sisinf_p3_grupo_11_07 TO padel_app;
ALTER ROLE padel_app SET search_path TO sisinf_p3_grupo_11_07, public;
