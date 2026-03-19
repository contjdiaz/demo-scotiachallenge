package com.scotiachallenge.demo.application.usecase.impl;

import com.scotiachallenge.demo.domain.model.Alumno;
import com.scotiachallenge.demo.domain.model.Estado;
import com.scotiachallenge.demo.domain.port.AlumnoRepositoryPort;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ObtenerAlumnosActivosUseCaseImplTest {

    @Mock
    private AlumnoRepositoryPort repositoryPort;

    private MeterRegistry meterRegistry;
    private ObtenerAlumnosActivosUseCaseImpl obtenerAlumnosActivosUseCase;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        obtenerAlumnosActivosUseCase = new ObtenerAlumnosActivosUseCaseImpl(repositoryPort, meterRegistry);
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

    @Test
    void ejecutar_DebeRetornarVacio_CuandoNoHayActivos() {
        when(repositoryPort.findByEstado(Estado.ACTIVO)).thenReturn(Flux.empty());

        StepVerifier.create(obtenerAlumnosActivosUseCase.ejecutar())
                .verifyComplete();
    }

    @Test
    void ejecutar_DebeIncrementarContadorDeConsultas() {
        when(repositoryPort.findByEstado(Estado.ACTIVO)).thenReturn(Flux.empty());

        StepVerifier.create(obtenerAlumnosActivosUseCase.ejecutar())
                .verifyComplete();

        double count = meterRegistry.counter("alumnos.activos.consultas", "type", "query").count();
        assertEquals(1.0, count);
    }
}
