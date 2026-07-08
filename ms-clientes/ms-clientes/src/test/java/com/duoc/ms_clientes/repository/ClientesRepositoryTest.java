package com.duoc.ms_clientes.repository;
import com.duoc.ms_clientes.model.Cliente;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
public class ClientesRepositoryTest {
    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    void findByEmail_deberiaRetornarClienteCuandoExiste() {
        Cliente cliente = crearCliente("11111111-1", "Ana", "Perez", "ana.perez@test.cl");
        clienteRepository.save(cliente);

        Optional<Cliente> resultado = clienteRepository.findByEmail("ana.perez@test.cl");

        assertTrue(resultado.isPresent());
        assertEquals("Ana", resultado.get().getNombre());
    }

    @Test
    void findByEmailContainingIgnoreCase_deberiaFiltrarPorTexto() {
        clienteRepository.save(crearCliente("11111111-1", "Ana", "Perez", "ana.perez@test.cl"));
        clienteRepository.save(crearCliente("22222222-2", "Luis", "Soto", "luis.soto@test.cl"));

        List<Cliente> resultado = clienteRepository.findByEmailContainingIgnoreCase("ANA");

        assertEquals(1, resultado.size());
        assertEquals("ana.perez@test.cl", resultado.get(0).getEmail());
    }

    private Cliente crearCliente(String rut, String nombre, String apellido, String email) {
        Cliente cliente = new Cliente();
        cliente.setRut(rut);
        cliente.setNombre(nombre);
        cliente.setApellido(apellido);
        cliente.setEmail(email);
        cliente.setTelefono(987654321);
        cliente.setActivo(true);
        cliente.setFechaRegistro(LocalDate.now());
        return cliente;
    }

}
