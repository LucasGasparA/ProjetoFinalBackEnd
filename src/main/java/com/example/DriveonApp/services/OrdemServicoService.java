package com.example.DriveonApp.services;

import com.example.DriveonApp.DTOs.OrdemServicoDTO;
import com.example.DriveonApp.exceptions.RecursoNaoEncontrado;
import com.example.DriveonApp.models.Cliente;
import com.example.DriveonApp.models.OrdemServico;
import com.example.DriveonApp.models.StatusOrdemServico;
import com.example.DriveonApp.models.Veiculo;
import com.example.DriveonApp.repositorys.OrdemServicoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrdemServicoService {

    private final OrdemServicoRepository ordemServicoRepository;
    private final ClienteService clienteService;
    private final VeiculoService veiculoService;

    public OrdemServicoService(OrdemServicoRepository ordemServicoRepository, ClienteService clienteService, VeiculoService veiculoService) {
        this.ordemServicoRepository = ordemServicoRepository;
        this.clienteService = clienteService;
        this.veiculoService = veiculoService;
    }

    public OrdemServicoDTO create(OrdemServicoDTO ordemServicoDTO) {
        Cliente cliente = clienteService.getClienteEntity(ordemServicoDTO.getClienteId());
        Veiculo veiculo = veiculoService.getVeiculoEntity(ordemServicoDTO.getVeiculoId());

        OrdemServico ordemServico = new OrdemServico();
        BeanUtils.copyProperties(ordemServicoDTO, ordemServico, "clienteId", "veiculoId");

        ordemServico.setCliente(cliente);
        ordemServico.setVeiculo(veiculo);
        ordemServico.setDataAbertura(LocalDateTime.now());
        ordemServico.setStatus(StatusOrdemServico.ABERTA);

        ordemServico = ordemServicoRepository.save(ordemServico);
        ordemServicoDTO.setId(ordemServico.getId());
        ordemServicoDTO.setDataAbertura(ordemServico.getDataAbertura());
        ordemServicoDTO.setStatus(ordemServico.getStatus());
        return ordemServicoDTO;
    }

    public Page<OrdemServicoDTO> findAll(Pageable pageable) {
        return ordemServicoRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    public OrdemServicoDTO findById(Long id) {
        OrdemServico ordemServico = ordemServicoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontrado("Ordem de Serviço não encontrada com ID: " + id));
        return convertToDTO(ordemServico);
    }

    public OrdemServicoDTO update(Long id, OrdemServicoDTO ordemServicoDTO) {
        OrdemServico ordemServico = ordemServicoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontrado("Ordem de Serviço não encontrada com ID: " + id));

        Cliente cliente = clienteService.getClienteEntity(ordemServicoDTO.getClienteId());
        Veiculo veiculo = veiculoService.getVeiculoEntity(ordemServicoDTO.getVeiculoId());

        BeanUtils.copyProperties(ordemServicoDTO, ordemServico, "id", "dataAbertura", "status", "clienteId", "veiculoId");

        ordemServico.setCliente(cliente);
        ordemServico.setVeiculo(veiculo);

        // Lógica para fechar a OS
        if (ordemServicoDTO.getStatus() == StatusOrdemServico.CONCLUIDA && ordemServico.getDataFechamento() == null) {
            ordemServico.setDataFechamento(LocalDateTime.now());
        } else if (ordemServicoDTO.getStatus() != StatusOrdemServico.CONCLUIDA) {
            ordemServico.setDataFechamento(null);
        }
        ordemServico.setStatus(ordemServicoDTO.getStatus());

        ordemServico = ordemServicoRepository.save(ordemServico);
        return convertToDTO(ordemServico);
    }

    public void delete(Long id) {
        if (!ordemServicoRepository.existsById(id)) {
            throw new RecursoNaoEncontrado("Ordem de Serviço não encontrada com ID: " + id);
        }
        ordemServicoRepository.deleteById(id);
    }

    // Método auxiliar para converter Model em DTO
    private OrdemServicoDTO convertToDTO(OrdemServico ordemServico) {
        OrdemServicoDTO ordemServicoDTO = new OrdemServicoDTO();
        BeanUtils.copyProperties(ordemServico, ordemServicoDTO);
        ordemServicoDTO.setClienteId(ordemServico.getCliente().getId());
        ordemServicoDTO.setVeiculoId(ordemServico.getVeiculo().getId());
        return ordemServicoDTO;
    }
}
