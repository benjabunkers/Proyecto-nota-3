package com.duoc.ms_clientes.repository;
import com.duoc.ms_clientes.model.Cliente;
import com.duoc.ms_clientes.model.Direccion;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
public class DireccionRepositoryTest {

    @Autowired
    private DireccionRepository direccionRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    void save_deberiaGuardarDireccionConCliente() {
        Cliente cliente = clienteRepository.save(crearCliente());
        Direccion direccion = crearDireccion(cliente);

        Direccion guardada = direccionRepository.save(direccion);
        Optional<Direccion> resultado = direccionRepository.findById(guardada.getId());

        assertTrue(resultado.isPresent());
        assertEquals("Santiago", resultado.get().getCiudad());
        assertEquals(cliente.getId(), resultado.get().getCliente().getId());
    }

    private Cliente crearCliente() {
        Cliente cliente = new Cliente();
        cliente.setRut("11111111-1");
        cliente.setNombre("Ana");
        cliente.setApellido("Perez");
        cliente.setEmail("ana.perez@test.cl");
        cliente.setTelefono(987654321);
        cliente.setActivo(true);
        cliente.setFechaRegistro(LocalDate.now());
        return cliente;
    }

    private Direccion crearDireccion(Cliente cliente) {
        Direccion direccion = new Direccion();
        direccion.setCalle("Av. Siempre Viva");
        direccion.setNumero(742);
        direccion.setComuna("Santiago");
        direccion.setCiudad("Santiago");
        direccion.setReferencia("Casa azul");
        direccion.setPrincipal(true);
        direccion.setFechaRegistro(LocalDate.now());
        direccion.setCliente(cliente);
        return direccion;
    }
}
