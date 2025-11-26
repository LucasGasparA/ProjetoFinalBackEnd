package com.example.DriveonApp.controllers;

import com.example.DriveonApp.DTOs.OrdemServicoDTO;
import com.example.DriveonApp.services.OrdemServicoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ordens-servico")
@CrossOrigin(origins = "*") // Libera acesso
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
    public ResponseEntity<Page<OrdemServicoDTO>> findAll(
            @PageableDefault(page = 0, size = 10, sort = "dataAbertura", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(ordemServicoService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdemServicoDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ordemServicoService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrdemServicoDTO> update(@PathVariable Long id, @RequestBody @Valid OrdemServicoDTO ordemServicoDTO) {
        return ResponseEntity.ok(ordemServicoService.update(id, ordemServicoDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ordemServicoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}