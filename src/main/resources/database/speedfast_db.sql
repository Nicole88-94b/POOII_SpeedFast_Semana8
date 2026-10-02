CREATE DATABASE IF NOT EXISTS speedfast_db;
USE speedfast_db;

CREATE TABLE IF NOT EXISTS repartidor (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    tiene_mochila_termica BOOLEAN NOT NULL DEFAULT FALSE,
    disponible BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    direccion VARCHAR(150) NOT NULL,
    tipo VARCHAR(30) NOT NULL,
    distancia_km INT NOT NULL,
    estado VARCHAR(20) NOT NULL,
    id_repartidor INT NULL,

    CONSTRAINT fk_pedido_repartidor
    FOREIGN KEY (id_repartidor)
    REFERENCES repartidor(id)
    ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS entrega (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT NOT NULL,
    id_repartidor INT NOT NULL,
    fecha DATE NOT NULL,
    hora TIME NOT NULL,

    CONSTRAINT fk_entrega_pedido
        FOREIGN KEY (id_pedido) REFERENCES pedido(id),

    CONSTRAINT fk_entrega_repartidor
        FOREIGN KEY (id_repartidor) REFERENCES repartidor(id)
);
