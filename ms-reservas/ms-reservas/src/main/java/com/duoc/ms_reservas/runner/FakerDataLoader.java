package com.duoc.ms_reservas.runner;

import com.duoc.ms_reservas.dto.ClienteDTO;
import com.duoc.ms_reservas.dto.VehiculoDTO;
import com.duoc.ms_reservas.feign.ClienteClient;
import com.duoc.ms_reservas.feign.VehiculoClient;
import com.duoc.ms_reservas.model.EstadoReserva;
import com.duoc.ms_reservas.model.Reserva;
import com.duoc.ms_reservas.repository.EstadoReservaRepository;
import com.duoc.ms_reservas.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

// Genera reservas usando datos reales obtenidos desde clientes y vehiculos.
@Slf4j
@Component
@Profile("dev")
@ConditionalOnProperty(name = "app.data-faker.enabled", havingValue = "true")
@RequiredArgsConstructor
public class FakerDataLoader implements CommandLineRunner {

    private final EstadoReservaRepository estadoReservaRepository;
    private final ReservaRepository reservaRepository;
    private final ClienteClient clienteClient;
    private final VehiculoClient vehiculoClient;

    @Value("${app.data-faker.records:10}")
    private int targetRecords;

    @Value("${app.data-faker.cliente-id-max:10}")
    private int clienteIdMax;

    @Value("${app.data-faker.vehiculo-id-max:10}")
    private int vehiculoIdMax;

    // Sincroniza datos existentes y completa la cantidad configurada de reservas.
    @Override
    public void run(String... args) {
        Faker faker = new Faker(new Locale("es"));
        List<EstadoReserva> estados = ensureEstados(faker);

        sincronizarDatosRemotos();

        long existing = reservaRepository.count();
        int recordsToCreate = Math.max(0, targetRecords - (int) existing);
        int created = 0;
        int attempts = 0;
        int maxAttempts = Math.max(recordsToCreate * 4, (clienteIdMax + vehiculoIdMax) * 2);

        while (created < recordsToCreate && attempts < maxAttempts) {
            attempts++;
            ClienteDTO cliente = obtenerCliente(faker.number().numberBetween(1, clienteIdMax + 1));
            VehiculoDTO vehiculo = obtenerVehiculo(faker.number().numberBetween(1, vehiculoIdMax + 1));

            if (cliente == null || vehiculo == null) {
                continue;
            }

            int dias = faker.number().numberBetween(1, 15);
            LocalDate inicio = LocalDate.now().plusDays(faker.number().numberBetween(0, 60));
            BigDecimal tarifa = BigDecimal.valueOf(faker.number().numberBetween(25_000, 100_001));

            Reserva reserva = new Reserva();
            reserva.setClienteId(cliente.getId());
            reserva.setNombreCliente(cliente.getNombre());
            reserva.setCorreoCliente(cliente.getEmail());
            reserva.setVehiculoId(vehiculo.getId());
            reserva.setNombreVehiculo(vehiculo.getNombre());
            reserva.setFechaInicio(inicio);
            reserva.setFechaFin(inicio.plusDays(dias));
            reserva.setCantidadDias(dias);
            reserva.setMontoTotal(tarifa.multiply(BigDecimal.valueOf(dias)));
            reserva.setObservacion("Reserva faker: " + faker.lorem().sentence(5));
            reserva.setActiva(faker.bool().bool());
            reserva.setEstadoReserva(faker.options().option(estados.toArray(new EstadoReserva[0])));
            reservaRepository.save(reserva);
            created++;
        }

        if (created < recordsToCreate) {
            log.warn("Solo se crearon {} de {} reservas por falta de datos remotos validos", created, recordsToCreate);
        }
    }

    // Actualiza copias locales cuando cambian los datos en sus microservicios de origen.
    private void sincronizarDatosRemotos() {
        for (Reserva reserva : reservaRepository.findAll()) {
            ClienteDTO cliente = obtenerCliente(reserva.getClienteId());
            VehiculoDTO vehiculo = obtenerVehiculo(reserva.getVehiculoId());
            boolean changed = false;

            if (cliente != null) {
                if (!Objects.equals(reserva.getNombreCliente(), cliente.getNombre())) {
                    reserva.setNombreCliente(cliente.getNombre());
                    changed = true;
                }
                if (!Objects.equals(reserva.getCorreoCliente(), cliente.getEmail())) {
                    reserva.setCorreoCliente(cliente.getEmail());
                    changed = true;
                }
            }

            if (vehiculo != null && !Objects.equals(reserva.getNombreVehiculo(), vehiculo.getNombre())) {
                reserva.setNombreVehiculo(vehiculo.getNombre());
                changed = true;
            }

            if (changed) {
                reservaRepository.save(reserva);
            }
        }
    }

    private ClienteDTO obtenerCliente(Integer clienteId) {
        try {
            ClienteDTO cliente = clienteClient.obtenerClientePorId(clienteId);
            if (cliente == null || cliente.getId() == null
                    || cliente.getNombre() == null || cliente.getNombre().isBlank()
                    || cliente.getEmail() == null || cliente.getEmail().isBlank()) {
                log.warn("ms-clientes no devolvio datos validos para el cliente {}", clienteId);
                return null;
            }
            return cliente;
        } catch (Exception ex) {
            log.warn("No fue posible obtener el cliente {}: {}", clienteId, ex.getMessage());
            return null;
        }
    }

    private VehiculoDTO obtenerVehiculo(Integer vehiculoId) {
        try {
            VehiculoDTO vehiculo = vehiculoClient.obtenerVehiculoPorId(vehiculoId);
            if (vehiculo == null || vehiculo.getId() == null
                    || vehiculo.getNombre() == null || vehiculo.getNombre().isBlank()) {
                log.warn("ms-vehiculos no devolvio datos validos para el vehiculo {}", vehiculoId);
                return null;
            }
            return vehiculo;
        } catch (Exception ex) {
            log.warn("No fue posible obtener el vehiculo {}: {}", vehiculoId, ex.getMessage());
            return null;
        }
    }

    private List<EstadoReserva> ensureEstados(Faker faker) {
        if (estadoReservaRepository.count() == 0) {
            estadoReservaRepository.save(createEstado("Pendiente", 1, faker));
            estadoReservaRepository.save(createEstado("Confirmada", 2, faker));
            estadoReservaRepository.save(createEstado("Cancelada", 3, faker));
        }
        return estadoReservaRepository.findAll();
    }

    private EstadoReserva createEstado(String nombre, int prioridad, Faker faker) {
        EstadoReserva estado = new EstadoReserva();
        estado.setNombre(nombre);
        estado.setDescripcion("Estado " + nombre + ": " + faker.lorem().sentence(4));
        estado.setPrioridad(prioridad);
        estado.setActivo(true);
        estado.setFechaCreacion(LocalDateTime.now());
        return estado;
    }
}
