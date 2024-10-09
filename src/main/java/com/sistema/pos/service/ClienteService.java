package com.sistema.pos.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sistema.pos.entity.Cliente;
import com.sistema.pos.repository.ClienteRepository;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	public List<Cliente> listCliente() {
		return clienteRepository.findAll();
	}
	
	public Cliente obtenerClientePorId(Long id) {
		Optional<Cliente> user = clienteRepository.findById(id);
		if (user.isPresent()) {
			return user.get();
		}else {
			throw new UsernameNotFoundException("El usuario no se encuentra");
		}
	}
	
	public Cliente registrarCliente(Cliente cliente) {
		cliente.setActivo(true);
		return clienteRepository.save(cliente);
	}
	
	public Cliente modificarCliente(Long id, Cliente cliente) {
		Cliente clienteModificado = obtenerClientePorId(id);
		clienteModificado.setNombre(cliente.getNombre());
		clienteModificado.setApellido(cliente.getApellido());
		clienteModificado.setEmail(cliente.getEmail());
		clienteModificado.setNit(cliente.getNit());
		return clienteRepository.save(clienteModificado);
	}
	
	public void eliminarCliente(Long id) {
		Cliente cliente = obtenerClientePorId(id);
		cliente.setActivo(false);
		clienteRepository.save(cliente);
	}
	
	public List<Cliente> buscarClientes(String searchTerm) {
        return clienteRepository.buscarClientes(searchTerm);
    }

}