package com.example.DriveonApp.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VeiculoDTO {

    private Long id;

    @NotBlank(message = "A marca é obrigatória")
    @Size(min = 2, max = 50, message = "A marca deve ter entre 2 e 50 caracteres")
    private String marca;

    @NotBlank(message = "O modelo é obrigatório")
    @Size(min = 2, max = 50, message = "O modelo deve ter entre 2 e 50 caracteres")
    private String modelo;

    @NotBlank(message = "A placa é obrigatória")
    @Pattern(regexp = "[A-Z]{3}[0-9][0-9A-Z][0-9]{2}", message = "A placa deve seguir o padrão (AAA-1234 ou AAA1A23)")
    private String placa;

    @NotNull(message = "O ID do cliente é obrigatório")
    private Long clienteId;
}
