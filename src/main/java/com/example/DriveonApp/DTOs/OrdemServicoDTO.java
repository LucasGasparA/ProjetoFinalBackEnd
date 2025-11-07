package com.example.DriveonApp.DTOs;

import com.example.DriveonApp.models.StatusOrdemServico;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrdemServicoDTO {

    private Long id;

    @NotNull(message = "O ID do cliente é obrigatório")
    private Long clienteId;

    @NotNull(message = "O ID do veículo é obrigatório")
    private Long veiculoId;

    @NotBlank(message = "A descrição do serviço é obrigatória")
    private String descricaoServico;

    private LocalDateTime dataAbertura;

    private LocalDateTime dataFechamento;

    private StatusOrdemServico status;

    @PositiveOrZero(message = "O valor total deve ser positivo ou zero")
    private BigDecimal valorTotal;
}
