package com.example.DriveonApp.services;

import com.example.DriveonApp.DTOs.VeiculoDTO;
import com.example.DriveonApp.exceptions.RecursoNaoEncontrado;
import com.example.DriveonApp.models.Cliente;
import com.example.DriveonApp.models.Veiculo;
import com.example.DriveonApp.repositorys.VeiculoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class VeiculoService {

    private final VeiculoRepository veiculoRepository;
    private final ClienteService clienteService;

    public VeiculoService(VeiculoRepository veiculoRepository, ClienteService clienteService) {
        this.veiculoRepository = veiculoRepository;
        this.clienteService = clienteService;
    }

    public VeiculoDTO create(VeiculoDTO veiculoDTO) {
        Cliente cliente = clienteService.getClienteEntity(veiculoDTO.getClienteId());

        Veiculo veiculo = new Veiculo();
        BeanUtils.copyProperties(veiculoDTO, veiculo, "clienteId");
        veiculo.setCliente(cliente);

        veiculo = veiculoRepository.save(veiculo);
        veiculoDTO.setId(veiculo.getId());
        return veiculoDTO;
    }

    public Page<VeiculoDTO> findAll(Pageable pageable) {
        return veiculoRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    public VeiculoDTO findById(Long id) {
        Veiculo veiculo = veiculoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontrado("Veículo não encontrado com ID: " + id));
        return convertToDTO(veiculo);
    }

    public VeiculoDTO update(Long id, VeiculoDTO veiculoDTO) {
        Veiculo veiculo = veiculoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontrado("Veículo não encontrado com ID: " + id));

        Cliente cliente = clienteService.getClienteEntity(veiculoDTO.getClienteId());

        BeanUtils.copyProperties(veiculoDTO, veiculo, "id", "clienteId");
        veiculo.setCliente(cliente);

        veiculo = veiculoRepository.save(veiculo);
        return convertToDTO(veiculo);
    }

    public void delete(Long id) {
        if (!veiculoRepository.existsById(id)) {
            throw new RecursoNaoEncontrado("Veículo não encontrado com ID: " + id);
        }
        veiculoRepository.deleteById(id);
    }

    private VeiculoDTO convertToDTO(Veiculo veiculo) {
        VeiculoDTO veiculoDTO = new VeiculoDTO();
        BeanUtils.copyProperties(veiculo, veiculoDTO);
        veiculoDTO.setClienteId(veiculo.getCliente().getId());
        return veiculoDTO;
    }

    public Veiculo getVeiculoEntity(Long id) {
        return veiculoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontrado("Veículo não encontrado com ID: " + id));
    }
}
