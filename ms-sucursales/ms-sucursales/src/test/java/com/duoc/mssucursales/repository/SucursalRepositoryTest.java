package com.duoc.mssucursales.repository;
import com.duoc.mssucursales.model.Region;
import com.duoc.mssucursales.model.Sucursal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:sucursales_test;MODE=MySQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
public class SucursalRepositoryTest {

    @Autowired
    private SucursalRepository sucursalRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Test
    void listarSucursalesOperativasOrdenadas_deberiaRetornarSoloOperativasOrdenadasPorNombre() {
        Region region = regionRepository.save(crearRegion());
        sucursalRepository.save(crearSucursal("Zona Norte", true, region));
        sucursalRepository.save(crearSucursal("Alameda", true, region));
        sucursalRepository.save(crearSucursal("Cerrada", false, region));

        List<Sucursal> resultado = sucursalRepository.listarSucursalesOperativasOrdenadas();

        assertEquals(2, resultado.size());
        assertEquals("Alameda", resultado.get(0).getNombre());
        assertEquals("Zona Norte", resultado.get(1).getNombre());
    }

    private Region crearRegion() {
        Region region = new Region();
        region.setNombre("Metropolitana");
        region.setCodigo("RM");
        region.setNumeroRegion(13);
        region.setCapitalRegional("Santiago");
        region.setActiva(true);
        region.setFechaCreacion(LocalDate.now());
        return region;
    }

    private Sucursal crearSucursal(String nombre, boolean operativa, Region region) {
        Sucursal sucursal = new Sucursal();
        sucursal.setNombre(nombre);
        sucursal.setDireccion("Av. Principal 123");
        sucursal.setComuna("Santiago");
        sucursal.setTelefono(222222222);
        sucursal.setOperativa(operativa);
        sucursal.setFechaApertura(LocalDate.now());
        sucursal.setRegion(region);
        return sucursal;
    }

}
