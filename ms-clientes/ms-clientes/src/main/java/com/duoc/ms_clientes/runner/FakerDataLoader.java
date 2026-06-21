package com.duoc.ms_clientes.runner;

import com.duoc.ms_clientes.model.Cliente;
import com.duoc.ms_clientes.model.Direccion;
import com.duoc.ms_clientes.repository.ClienteRepository;
import com.duoc.ms_clientes.repository.DireccionRepository;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Locale;

// Usa DataFaker para generar datos de prueba realistas al iniciar la aplicacion.
// Solo se activa en perfiles dev/test y cuando app.data-faker.enabled=true.
@Component
@Profile({"dev", "test"})
@ConditionalOnProperty(name = "app.data-faker.enabled", havingValue = "true")
@RequiredArgsConstructor
@Order(10)
public class FakerDataLoader implements CommandLineRunner {

    private final ClienteRepository clienteRepository;
    private final DireccionRepository direccionRepository;

    @Value("${app.data-faker.records:10}")
    private int targetRecords;

    // Completa la base hasta el total configurado sin duplicar registros en cada inicio.
    @Override
    public void run(String... args) {
        Faker faker = new Faker(new Locale("es"));
        long existing = clienteRepository.count();
        int recordsToCreate = Math.max(0, targetRecords - (int) existing);

        for (int i = 0; i < recordsToCreate; i++) {
            long sequence = existing + i + 1;
            Cliente cliente = new Cliente();
            cliente.setRut(String.format("%08d-%d", 20_000_000 + sequence, sequence % 10));
            cliente.setNombre(faker.name().firstName());
            cliente.setApellido(faker.name().lastName());
            cliente.setEmail("cliente" + sequence + "@faker.test");
            cliente.setTelefono(faker.number().numberBetween(9_000_000, 99_999_999));
            cliente.setActivo(faker.bool().bool());
            cliente.setFechaRegistro(LocalDate.now().minusDays(faker.number().numberBetween(0, 365)));
            Cliente guardado = clienteRepository.save(cliente);

            Direccion direccion = new Direccion();
            direccion.setCalle(faker.address().streetName());
            direccion.setNumero(faker.number().numberBetween(1, 5000));
            direccion.setComuna(faker.address().cityName());
            direccion.setCiudad(faker.address().city());
            direccion.setReferencia("Referencia faker " + sequence);
            direccion.setPrincipal(true);
            direccion.setFechaRegistro(cliente.getFechaRegistro());
            direccion.setCliente(guardado);
            direccionRepository.save(direccion);
        }
    }
}