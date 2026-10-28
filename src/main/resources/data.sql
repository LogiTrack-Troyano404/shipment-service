
SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE historial_de_estados;
TRUNCATE TABLE envios;
TRUNCATE TABLE clientes;

SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO clientes (id, nombre, email, telefono, fecha_creacion, fecha_modificacion) VALUES 
    ('11111111-1111-1111-1111-111111111111', 'Ana Perez', 'ana@example.com', '1122334455', CURRENT_TIMESTAMP, NULL),
    ('22222222-2222-2222-2222-222222222222', 'Juan Gomez', 'juan@example.com', '1166778899', CURRENT_TIMESTAMP, NULL);

INSERT INTO envios (id, codigo_de_seguimiento, peso, ancho, alto, estado, tarifa,origen_calle, origen_numero, origen_ciudad, origen_codigo_postal,destino_calle, destino_numero, destino_ciudad, destino_codigo_postal, repartidor_id, cliente_id, fecha_creacion, fecha_modificacion) VALUES
    ('55555555-5555-5555-5555-555555555555', 'ENVIO0000000001', 2.5, 20, 15, 'PENDIENTE', 1500.0, 'San Martin', '100', 'Cordoba', '5000', 'Belgrano', '250', 'Rosario', '2000', NULL, '11111111-1111-1111-1111-111111111111',CURRENT_TIMESTAMP, NULL),
    ('66666666-6666-6666-6666-666666666666', 'ENVIO0000000002', 5.0, 30, 25, 'EN_RUTA', 2800.0, 'Rivadavia', '500', 'Buenos Aires', '1002', 'Mitre', '300', 'La Plata', '1900', '33333333-3333-3333-3333-333333333333', '22222222-2222-2222-2222-222222222222', CURRENT_TIMESTAMP, NULL),
    ('77777777-7777-7777-7777-777777777777', 'ENVIO0000000003', 1.2, 15, 10, 'ENTREGADO', 950.0, 'Belgrano', '120', 'Mendoza', '5500', 'San Juan', '450', 'San Juan', '5400', '33333333-3333-3333-3333-333333333333', '11111111-1111-1111-1111-111111111111', CURRENT_TIMESTAMP, NULL);

INSERT INTO historial_de_estados (id, estado_anterior, fecha_cambio, envio_id, fecha_creacion, fecha_modificacion) VALUES
    ('88888888-8888-8888-8888-888888888888', 'PENDIENTE', CURRENT_TIMESTAMP, '55555555-5555-5555-5555-555555555555', CURRENT_TIMESTAMP, NULL),
    ('99999999-9999-9999-9999-999999999999', 'PENDIENTE', CURRENT_TIMESTAMP, '66666666-6666-6666-6666-666666666666', CURRENT_TIMESTAMP, NULL),
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'EN_RUTA', CURRENT_TIMESTAMP, '77777777-7777-7777-7777-777777777777', CURRENT_TIMESTAMP, NULL);