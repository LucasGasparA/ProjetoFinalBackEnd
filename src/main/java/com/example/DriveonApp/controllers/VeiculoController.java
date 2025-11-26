package com.example.DriveonApp.controllers;

import com.example.DriveonApp.DTOs.VeiculoDTO;
import com.example.DriveonApp.services.VeiculoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/veiculos")
@CrossOrigin(origins = "*")
public class VeiculoController {

    private final VeiculoService veiculoService;

    public VeiculoController(VeiculoService veiculoService) {
        this.veiculoService = veiculoService;
    }

    @PostMapping
    public ResponseEntity<VeiculoDTO> create(@RequestBody @Valid VeiculoDTO veiculoDTO) {
        VeiculoDTO novoVeiculo = veiculoService.create(veiculoDTO);
        return new ResponseEntity<>(novoVeiculo, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<VeiculoDTO>> findAll(
            @RequestParam(required = false) String placa,
            @PageableDefault(page = 0, size = 10, sort = "modelo", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return ResponseEntity.ok(veiculoService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VeiculoDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(veiculoService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VeiculoDTO> update(@PathVariable Long id, @RequestBody @Valid VeiculoDTO veiculoDTO) {
        return ResponseEntity.ok(veiculoService.update(id, veiculoDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        veiculoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}