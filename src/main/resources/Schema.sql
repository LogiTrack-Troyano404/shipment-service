
CREATE TABLE clientes
(
    id                 CHAR(36) PRIMARY KEY,
    nombre             VARCHAR(150) NOT NULL,
    email              VARCHAR(100) NOT NULL UNIQUE,
    telefono           VARCHAR(20),
    fecha_creacion     TIMESTAMP    NOT NULL,
    fecha_modificacion TIMESTAMP
);

CREATE TABLE repartidores (
    id CHAR(36) PRIMARY KEY,
    nombre_repartidor VARCHAR(150) NOT NULL,
    tipo_de_vehiculo VARCHAR(200) NOT NULL,
    estado VARCHAR(20) NOT NULL,
    fecha_creacion TIMESTAMP NOT NULL,
    fecha_modificacion TIMESTAMP
);

CREATE TABLE repartidores (
    id CHAR(36) PRIMARY KEY,
    nombre_repartidor VARCHAR(150) NOT NULL,
    tipo_de_vehiculo VARCHAR(200) NOT NULL,
    estado VARCHAR(20) NOT NULL,
    fecha_creacion TIMESTAMP NOT NULL,
    fecha_modificacion TIMESTAMP
);

CREATE TABLE envios (
    id CHAR(36) PRIMARY KEY,
    codigo_de_seguimiento VARCHAR(15) NOT NULL UNIQUE,
    peso DOUBLE PRECISION NOT NULL,
    ancho DOUBLE PRECISION NOT NULL,
    alto DOUBLE PRECISION NOT NULL,
    estado VARCHAR(20) NOT NULL,
    tarifa DOUBLE PRECISION NOT NULL,

    origen_calle VARCHAR(200) NOT NULL,
    origen_numero VARCHAR(15) NOT NULL,
    origen_ciudad VARCHAR(200) NOT NULL,
    origen_codigo_postal VARCHAR(15) NOT NULL,

    destino_calle VARCHAR(200) NOT NULL,
    destino_numero VARCHAR(15) NOT NULL,
    destino_ciudad VARCHAR(200) NOT NULL,
    destino_codigo_postal VARCHAR(15) NOT NULL,

    repartidor_id UUID,
    cliente_id UUID NOT NULL,

    fecha_creacion TIMESTAMP NOT NULL,
    fecha_modificacion TIMESTAMP,

    CONSTRAINT fk_envio_cliente
        FOREIGN KEY (cliente_id) REFERENCES clientes(id),

    CONSTRAINT fk_envio_repartidor
        FOREIGN KEY (repartidor_id) REFERENCES repartidores(id)
);

CREATE TABLE historial_de_estados (
    id CHAR(36) PRIMARY KEY,
    estado_anterior VARCHAR(20) NOT NULL,
    fecha_cambio TIMESTAMP NOT NULL,
    envio_id UUID NOT NULL,

    fecha_creacion TIMESTAMP NOT NULL,
    fecha_modificacion TIMESTAMP,

    CONSTRAINT fk_historial_envio
        FOREIGN KEY (envio_id) REFERENCES envios(id)
);