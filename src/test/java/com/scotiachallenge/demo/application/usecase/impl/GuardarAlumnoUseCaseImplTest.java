package com.scotiachallenge.demo.application.usecase.impl;

import com.scotiachallenge.demo.domain.exception.DuplicateIdException;
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
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GuardarAlumnoUseCaseImplTest {

    @Mock
    private AlumnoRepositoryPort repositoryPort;

    private MeterRegistry meterRegistry;
    private GuardarAlumnoUseCaseImpl guardarAlumnoUseCase;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        guardarAlumnoUseCase = new GuardarAlumnoUseCaseImpl(repositoryPort, meterRegistry);
    }

    @Test
    void ejecutar_DebeGuardarAlumno_CuandoIdNoExiste() {
        Alumno alumno = new Alumno(1L, "Juan", "Perez", Estado.ACTIVO, 20);
        when(repositoryPort.existsById(1L)).thenReturn(Mono.just(false));
        when(repositoryPort.save(any(Alumno.class))).thenReturn(Mono.empty());

        StepVerifier.create(guardarAlumnoUseCase.ejecutar(alumno))
                .verifyComplete();

        verify(repositoryPort).save(alumno);
    }

    @Test
    void ejecutar_DebeRetornarError_CuandoIdYaExiste() {
        Alumno alumno = new Alumno(1L, "Juan", "Perez", Estado.ACTIVO, 20);
        when(repositoryPort.existsById(1L)).thenReturn(Mono.just(true));

        StepVerifier.create(guardarAlumnoUseCase.ejecutar(alumno))
                .expectError(DuplicateIdException.class)
                .verify();

        verify(repositoryPort, never()).save(any());
    }

    @Test
    void ejecutar_DebeIncrementarContadorExito_CuandoGrabacionCorrecta() {
        Alumno alumno = new Alumno(1L, "Juan", "Perez", Estado.ACTIVO, 20);
        when(repositoryPort.existsById(1L)).thenReturn(Mono.just(false));
        when(repositoryPort.save(any(Alumno.class))).thenReturn(Mono.empty());

        StepVerifier.create(guardarAlumnoUseCase.ejecutar(alumno))
                .verifyComplete();

        double count = meterRegistry.counter("alumnos.grabados.total", "type", "success").count();
        assertEquals(1.0, count);
    }

    @Test
    void ejecutar_DebeIncrementarContadorDuplicado_CuandoIdRepetido() {
        Alumno alumno = new Alumno(1L, "Juan", "Perez", Estado.ACTIVO, 20);
        when(repositoryPort.existsById(1L)).thenReturn(Mono.just(true));

        StepVerifier.create(guardarAlumnoUseCase.ejecutar(alumno))
                .expectError(DuplicateIdException.class)
                .verify();

        double count = meterRegistry.counter("alumnos.grabados.duplicados", "type", "error").count();
        assertEquals(1.0, count);
    }
}
