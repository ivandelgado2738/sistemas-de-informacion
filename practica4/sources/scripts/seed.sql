-- =====================================================
--  Datos iniciales y de prueba
--  Se ejecuta después de ddl.sql (orden alfabético: d < s)
-- =====================================================
SET search_path TO sisinf_p3_grupo_11_07, public;

-- Administrador. Email: admin@padel.es   Contraseña: admin123
-- La contraseña va como hash SHA-256 (lo que calcula Seguridad.hash("admin123")).
INSERT INTO usuario (rol, email, nombre, contrasena, estado, nivel)
VALUES ('ADMIN', 'admin@padel.es', 'Administrador',
        '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'ACTIVO', 'PRINCIPIANTE');

-- Un club de ejemplo con dos pistas
INSERT INTO club (nombre, direccion, telefono)
VALUES ('Club Pádel Zaragoza', 'Paseo de los Puentes 2, Zaragoza', '976000000');

INSERT INTO pista (id_club, numero, cubierta) VALUES (1, 1, TRUE), (1, 2, FALSE);

-- Algunos horarios libres (fechas futuras) para probar verReservas
INSERT INTO horario (id_club, numero_pista, fecha, hora_inicio, hora_fin, precio, id_reserva) VALUES
    (1, 1, '2026-12-15', '18:00', '19:30', 24, NULL),
    (1, 1, '2026-12-15', '19:30', '21:00', 24, NULL),
    (1, 2, '2026-12-16', '10:00', '11:30', 18, NULL);
