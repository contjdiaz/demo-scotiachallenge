package com.scotiachallenge.demo.application.usecase.impl;

import com.scotiachallenge.demo.domain.model.Alumno;
import com.scotiachallenge.demo.domain.model.Estado;
import com.scotiachallenge.demo.domain.port.AlumnoRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

class ObtenerAlumnosActivosUseCaseImplTest {

    @Mock
    private AlumnoRepositoryPort repositoryPort;

    @InjectMocks
    private ObtenerAlumnosActivosUseCaseImpl obtenerAlumnosActivosUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void ejecutar_DebeRetornarAlumnosActivos() {
        Alumno alumno1 = new Alumno(1L, "Juan", "Perez", Estado.ACTIVO, 20);
        Alumno alumno2 = new Alumno(2L, "Maria", "Gomez", Estado.ACTIVO, 22);

        when(repositoryPort.findByEstado(Estado.ACTIVO)).thenReturn(Flux.just(alumno1, alumno2));

        StepVerifier.create(obtenerAlumnosActivosUseCase.ejecutar())
                .expectNext(alumno1)
                .expectNext(alumno2)
                .verifyComplete();
    }
}
