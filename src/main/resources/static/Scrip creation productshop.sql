-- Creación de la base de datos
CREATE DATABASE IF NOT EXISTS productsshop CHARSET utf8mb4 COLLATE utf8mb4_spanish2_ci;

-- Selección de la base de datos
USE productsshop;

-- Creación de la tabla Users
CREATE TABLE IF NOT EXISTS Users (
    id_user BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- Creación de la tabla Products
CREATE TABLE IF NOT EXISTS Products (
    id_product BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    brand VARCHAR(255),
    price DOUBLE NOT NULL
);

-- Creación de la tabla Clients
CREATE TABLE IF NOT EXISTS clients (
    id_client BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    age VARCHAR(2)
);

-- Creación de la tabla Orders
CREATE TABLE orders (
    id_order BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_client BIGINT NOT NULL,
    total DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (id_client) REFERENCES clients(id_client)
);

-- Creación de la tabla OrderItems
CREATE TABLE IF NOT EXISTS order_items (
    id_order_items BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_product BIGINT NOT NULL,
    id_order BIGINT NOT NULL,
    quantity INT NOT NULL,
    price DOUBLE NOT NULL,
    FOREIGN KEY (id_product) REFERENCES products(id_product),
    FOREIGN KEY (id_order) REFERENCES orders(id_order)
);

-- Crear usuario
CREATE USER 'userProducts'@'localhost' IDENTIFIED BY 'userProducts';

-- Dar permisos de datos sobre la BD
GRANT SELECT, UPDATE, DELETE, INSERT ON productsshop.* TO 'userProducts'@'localhost';
FLUSH PRIVILEGES;
SHOW GRANTS FOR 'userProducts'@'localhost';