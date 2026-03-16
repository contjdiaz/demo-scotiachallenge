package com.scotiachallenge.demo.infraestructure.web.dto;

import com.scotiachallenge.demo.domain.model.Estado;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AlumnoRequest {
    @NotNull(message = "El id es obligatorio")
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @NotNull(message = "El estado es obligatorio")
    private Estado estado;

    @Min(value = 0, message = "La edad no puede ser negativa")
    private int edad;
}
