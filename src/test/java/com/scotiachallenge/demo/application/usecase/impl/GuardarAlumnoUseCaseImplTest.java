package com.scotiachallenge.demo.application.usecase.impl;

import com.scotiachallenge.demo.domain.exception.DuplicateIdException;
import com.scotiachallenge.demo.domain.model.Alumno;
import com.scotiachallenge.demo.domain.model.Estado;
import com.scotiachallenge.demo.domain.port.AlumnoRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class GuardarAlumnoUseCaseImplTest {

    @Mock
    private AlumnoRepositoryPort repositoryPort;

    @InjectMocks
    private GuardarAlumnoUseCaseImpl guardarAlumnoUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void ejecutar_DebeGuardarAlumno_CuandoIdNoExiste() {
        Alumno alumno = new Alumno(1L, "Juan", "Perez", Estado.ACTIVO, 20);
        when(repositoryPort.existsById(1L)).thenReturn(Mono.just(false));
        when(repositoryPort.save(any(Alumno.class))).thenReturn(Mono.empty());

        StepVerifier.create(guardarAlumnoUseCase.ejecutar(alumno))
                .verifyComplete();
    }

    @Test
    void ejecutar_DebeRetornarError_CuandoIdYaExiste() {
        Alumno alumno = new Alumno(1L, "Juan", "Perez", Estado.ACTIVO, 20);
        when(repositoryPort.existsById(1L)).thenReturn(Mono.just(true));

        StepVerifier.create(guardarAlumnoUseCase.ejecutar(alumno))
                .expectError(DuplicateIdException.class)
                .verify();
    }
}
