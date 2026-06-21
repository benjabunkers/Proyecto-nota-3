-- Conserva una copia del nombre obtenido desde ms-clientes para mostrar la reserva.
-- El cargador de desarrollo reemplaza este valor por el nombre real del cliente.
ALTER TABLE reserva
    ADD COLUMN nombre_cliente VARCHAR(255) NOT NULL DEFAULT 'Pendiente de sincronizacion';
