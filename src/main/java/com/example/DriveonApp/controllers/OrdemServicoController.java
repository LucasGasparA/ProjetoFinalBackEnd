package com.example.DriveonApp.controllers;

import com.example.DriveonApp.DTOs.OrdemServicoDTO;
import com.example.DriveonApp.services.OrdemServicoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ordens-servico")
public class OrdemServicoController {

    private final OrdemServicoService ordemServicoService;

    public OrdemServicoController(OrdemServicoService ordemServicoService) {
        this.ordemServicoService = ordemServicoService;
    }

    @PostMapping
    public ResponseEntity<OrdemServicoDTO> create(@RequestBody @Valid OrdemServicoDTO ordemServicoDTO) {
        OrdemServicoDTO novaOrdemServico = ordemServicoService.create(ordemServicoDTO);
        return new ResponseEntity<>(novaOrdemServico, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<OrdemServicoDTO>> findAll(Pageable pageable) {
        Page<OrdemServicoDTO> ordensServico = ordemServicoService.findAll(pageable);
        return ResponseEntity.ok(ordensServico);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdemServicoDTO> findById(@PathVariable Long id) {
        OrdemServicoDTO ordemServico = ordemServicoService.findById(id);
        return ResponseEntity.ok(ordemServico);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrdemServicoDTO> update(@PathVariable Long id, @RequestBody @Valid OrdemServicoDTO ordemServicoDTO) {
        OrdemServicoDTO ordemServicoAtualizada = ordemServicoService.update(id, ordemServicoDTO);
        return ResponseEntity.ok(ordemServicoAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ordemServicoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
