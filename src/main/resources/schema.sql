-- Script inicial de base de datos — shipment-service (Sprint 1)
-- DDL compatible con el esquema que genera Hibernate (ddl-auto).
-- IF NOT EXISTS lo hace idempotente: corre seguro aunque las tablas ya existan.

CREATE TABLE IF NOT EXISTS clientes (
    id                 varchar(36)  NOT NULL,
    fecha_creacion     datetime(6)  NOT NULL,
    fecha_modificacion datetime(6)  DEFAULT NULL,
    email              varchar(100) NOT NULL,
    nombre             varchar(150) NOT NULL,
    telefono           varchar(20)  DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_clientes_email (email)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;


CREATE TABLE IF NOT EXISTS envios (
    id                    varchar(36)  NOT NULL,
    fecha_creacion        datetime(6)  NOT NULL,
    fecha_modificacion    datetime(6)  DEFAULT NULL,
    alto                  double       NOT NULL,
    ancho                 double       NOT NULL,
    codigo_de_seguimiento varchar(15)  NOT NULL,
    destino_calle         varchar(200) NOT NULL,
    destino_ciudad        varchar(200) NOT NULL,
    destino_codigo_postal varchar(15)  NOT NULL,
    destino_numero        varchar(15)  NOT NULL,
    estado                enum('CANCELADO','ENTREGADO','EN_RUTA','PENDIENTE') NOT NULL,
    origen_calle          varchar(200) NOT NULL,
    origen_ciudad         varchar(200) NOT NULL,
    origen_codigo_postal  varchar(15)  NOT NULL,
    origen_numero         varchar(15)  NOT NULL,
    peso                  double       NOT NULL,
    tarifa                double       NOT NULL,
    cliente_id            varchar(36)  NOT NULL,
    repartidor_id         varchar(36)  DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_envios_codigo_seguimiento (codigo_de_seguimiento),
    KEY fk_envios_cliente (cliente_id),
    CONSTRAINT fk_envios_cliente FOREIGN KEY (cliente_id) REFERENCES clientes (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS historial_de_estados (
    id                 varchar(36) NOT NULL,
    fecha_creacion     datetime(6) NOT NULL,
    fecha_modificacion datetime(6) DEFAULT NULL,
    estado_anterior    enum('CANCELADO','ENTREGADO','EN_RUTA','PENDIENTE') NOT NULL,
    fecha_cambio       datetime(6) NOT NULL,
    envio_id           varchar(36) NOT NULL,
    PRIMARY KEY (id),
    KEY fk_historial_envio (envio_id),
    CONSTRAINT fk_historial_envio FOREIGN KEY (envio_id) REFERENCES envios (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
