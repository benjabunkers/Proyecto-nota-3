-- Copias locales obtenidas mediante OpenFeign desde los microservicios de origen.
ALTER TABLE reserva
    ADD COLUMN correo_cliente VARCHAR(255) NOT NULL DEFAULT 'pendiente@sincronizacion.local',
    ADD COLUMN nombre_vehiculo VARCHAR(255) NOT NULL DEFAULT 'Pendiente de sincronizacion';
