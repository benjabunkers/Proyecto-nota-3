package com.duoc.msvehiculos.runner;

import com.duoc.msvehiculos.model.Categoria;
import com.duoc.msvehiculos.model.Vehiculo;
import com.duoc.msvehiculos.repository.CategoriaRepository;
import com.duoc.msvehiculos.repository.VehiculoRepository;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

//Usa DataFaker para generar datos de prueba realistas al iniciar la aplicacion.
// Solo se activa en perfiles dev/test y cuando app.data-faker.enabled=true.
@Component
@Profile({"dev", "test"})
@ConditionalOnProperty(name = "app.data-faker.enabled", havingValue = "true")
@RequiredArgsConstructor
public class FakerDataLoader implements CommandLineRunner {

    private final CategoriaRepository categoriaRepository;
    private final VehiculoRepository vehiculoRepository;

    @Value("${app.data-faker.records:10}")
    private int targetRecords;

    //Completa la base hasta el total configurado sin duplicar registros en cada inicio
    @Override
    public void run(String... args) {
        Faker faker = new Faker(new Locale("es"));
        List<Categoria> categorias = ensureCategorias(faker);
        long existing = vehiculoRepository.count();
        int recordsToCreate = Math.max(0, targetRecords - (int) existing);
        String[] marcas = {"Toyota", "Hyundai", "Kia", "Nissan", "Chevrolet", "Mazda"};
        String[] modelos = {"Yaris", "Tucson", "Rio", "Versa", "Tracker", "CX-5"};

        for (int i = 0; i < recordsToCreate; i++) {
            long sequence = existing + i + 1;
            Vehiculo vehiculo = Vehiculo.builder()
                    .patente(String.format("FK%04d", sequence))
                    .marca(faker.options().option(marcas))
                    .modelo(faker.options().option(modelos))
                    .anio(faker.number().numberBetween(2015, LocalDate.now().getYear() + 1))
                    .color(faker.color().name())
                    .precioArriendoDiario(BigDecimal.valueOf(faker.number().numberBetween(25_000, 100_001)))
                    .kilometraje(faker.number().numberBetween(0, 150_001))
                    .disponible(faker.bool().bool())
                    .fechaIngreso(LocalDate.now().minusDays(faker.number().numberBetween(0, 730)))
                    .categoria(faker.options().option(categorias.toArray(new Categoria[0])))
                    .build();
            vehiculoRepository.save(vehiculo);
        }
    }

    private List<Categoria> ensureCategorias(Faker faker) {
        if (categoriaRepository.count() == 0) {
            categoriaRepository.save(createCategoria("Economico", 25_000, faker));
            categoriaRepository.save(createCategoria("SUV", 45_000, faker));
            categoriaRepository.save(createCategoria("Premium", 70_000, faker));
        }
        return categoriaRepository.findAll();
    }

    private Categoria createCategoria(String nombre, int tarifa, Faker faker) {
        return Categoria.builder()
                .nombre(nombre)
                .descripcion("Categoria " + nombre + " generada con " + faker.company().name())
                .tarifaBase(BigDecimal.valueOf(tarifa))
                .capacidadPasajeros(5)
                .activa(true)
                .fechaCreacion(LocalDate.now())
                .build();
    }
}