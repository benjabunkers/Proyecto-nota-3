package com.duoc.ms_reservas.runner;

import com.duoc.ms_reservas.model.EstadoReserva;
import com.duoc.ms_reservas.model.Reserva;
import com.duoc.ms_reservas.repository.EstadoReservaRepository;
import com.duoc.ms_reservas.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
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

@Component
@Profile({"dev", "test"})
@ConditionalOnProperty(name = "app.data-faker.enabled", havingValue = "true")
@RequiredArgsConstructor
public class FakerDataLoader implements CommandLineRunner {

    private final EstadoReservaRepository estadoReservaRepository;
    private final ReservaRepository reservaRepository;

    @Value("${app.data-faker.records:10}")
    private int targetRecords;

    @Value("${app.data-faker.cliente-id-max:10}")
    private int clienteIdMax;

    @Value("${app.data-faker.vehiculo-id-max:10}")
    private int vehiculoIdMax;

    @Override
    public void run(String... args) {
        Faker faker = new Faker(new Locale("es"));
        List<EstadoReserva> estados = ensureEstados(faker);
        long existing = reservaRepository.count();
        int recordsToCreate = Math.max(0, targetRecords - (int) existing);

        for (int i = 0; i < recordsToCreate; i++) {
            int dias = faker.number().numberBetween(1, 15);
            LocalDate inicio = LocalDate.now().plusDays(faker.number().numberBetween(0, 60));
            BigDecimal tarifa = BigDecimal.valueOf(faker.number().numberBetween(25_000, 100_001));

            Reserva reserva = new Reserva();
            reserva.setClienteId(faker.number().numberBetween(1, clienteIdMax + 1));
            reserva.setNombreCliente(faker.name().fullName());
            reserva.setVehiculoId(faker.number().numberBetween(1, vehiculoIdMax + 1));
            reserva.setFechaInicio(inicio);
            reserva.setFechaFin(inicio.plusDays(dias));
            reserva.setCantidadDias(dias);
            reserva.setMontoTotal(tarifa.multiply(BigDecimal.valueOf(dias)));
            reserva.setObservacion("Reserva faker: " + faker.lorem().sentence(5));
            reserva.setActiva(faker.bool().bool());
            reserva.setEstadoReserva(faker.options().option(estados.toArray(new EstadoReserva[0])));
            reservaRepository.save(reserva);
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