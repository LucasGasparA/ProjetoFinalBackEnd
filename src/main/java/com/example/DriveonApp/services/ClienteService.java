package com.example.DriveonApp.services;

import com.example.DriveonApp.DTOs.ClienteDTO;
import com.example.DriveonApp.exceptions.RecursoNaoEncontrado;
import com.example.DriveonApp.models.Cliente;
import com.example.DriveonApp.repositorys.ClienteRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public ClienteDTO create(ClienteDTO clienteDTO) {
        Cliente cliente = new Cliente();
        BeanUtils.copyProperties(clienteDTO, cliente);
        cliente = clienteRepository.save(cliente);
        clienteDTO.setId(cliente.getId());
        return clienteDTO;
    }

    public Page<ClienteDTO> findAll(Pageable pageable) {
        return clienteRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    public ClienteDTO findById(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontrado("Cliente não encontrado com ID: " + id));
        return convertToDTO(cliente);
    }

    public ClienteDTO update(Long id, ClienteDTO clienteDTO) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontrado("Cliente não encontrado com ID: " + id));

        BeanUtils.copyProperties(clienteDTO, cliente, "id");
        cliente = clienteRepository.save(cliente);
        return convertToDTO(cliente);
    }

    public void delete(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new RecursoNaoEncontrado("Cliente não encontrado com ID: " + id);
        }
        clienteRepository.deleteById(id);
    }

    // Método auxiliar para converter Model em DTO
    private ClienteDTO convertToDTO(Cliente cliente) {
        ClienteDTO clienteDTO = new ClienteDTO();
        BeanUtils.copyProperties(cliente, clienteDTO);
        return clienteDTO;
    }

    // Método auxiliar para obter a entidade Cliente
    public Cliente getClienteEntity(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontrado("Cliente não encontrado com ID: " + id));
    }
}
