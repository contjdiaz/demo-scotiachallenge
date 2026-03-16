package com.scotiachallenge.demo.infraestructure.web.dto;

import com.scotiachallenge.demo.domain.model.Estado;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AlumnoRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void validar_DebeAceptarRequestValido() {
        AlumnoRequest request = buildValidRequest();

        Set<ConstraintViolation<AlumnoRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void validar_DebeRechazarIdNulo() {
        AlumnoRequest request = buildValidRequest();
        request.setId(null);

        Set<ConstraintViolation<AlumnoRequest>> violations = validator.validate(request);

        assertTrue(violations.stream().anyMatch(v -> "El id es obligatorio".equals(v.getMessage())));
    }

    @Test
    void validar_DebeRechazarNombreVacio() {
        AlumnoRequest request = buildValidRequest();
        request.setNombre("  ");

        Set<ConstraintViolation<AlumnoRequest>> violations = validator.validate(request);

        assertTrue(violations.stream().anyMatch(v -> "El nombre es obligatorio".equals(v.getMessage())));
    }

    @Test
    void validar_DebeRechazarApellidoVacio() {
        AlumnoRequest request = buildValidRequest();
        request.setApellido("");

        Set<ConstraintViolation<AlumnoRequest>> violations = validator.validate(request);

        assertTrue(violations.stream().anyMatch(v -> "El apellido es obligatorio".equals(v.getMessage())));
    }

    @Test
    void validar_DebeRechazarEstadoNulo() {
        AlumnoRequest request = buildValidRequest();
        request.setEstado(null);

        Set<ConstraintViolation<AlumnoRequest>> violations = validator.validate(request);

        assertTrue(violations.stream().anyMatch(v -> "El estado es obligatorio".equals(v.getMessage())));
    }

    @Test
    void validar_DebeRechazarEdadNegativa() {
        AlumnoRequest request = buildValidRequest();
        request.setEdad(-1);

        Set<ConstraintViolation<AlumnoRequest>> violations = validator.validate(request);

        assertTrue(violations.stream().anyMatch(v -> "La edad no puede ser negativa".equals(v.getMessage())));
    }

    private AlumnoRequest buildValidRequest() {
        AlumnoRequest request = new AlumnoRequest();
        request.setId(1L);
        request.setNombre("PEPE");
        request.setApellido("PEREZ");
        request.setEstado(Estado.ACTIVO);
        request.setEdad(18);
        return request;
    }
}
