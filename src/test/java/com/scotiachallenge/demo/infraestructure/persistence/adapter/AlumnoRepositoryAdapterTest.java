package com.scotiachallenge.demo.infraestructure.persistence.adapter;

import com.scotiachallenge.demo.domain.model.Alumno;
import com.scotiachallenge.demo.domain.model.Estado;
import com.scotiachallenge.demo.infraestructure.persistence.entity.R2dbcAlumno;
import com.scotiachallenge.demo.infraestructure.persistence.repository.R2dbcAlumnoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AlumnoRepositoryAdapterTest {

    @Mock
    private R2dbcAlumnoRepository jpaRepository;

    @InjectMocks
    private AlumnoRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_DebeGuardarAlumno() {
        Alumno alumno = new Alumno(1L, "Juan", "Perez", Estado.ACTIVO, 20);
        R2dbcAlumno jpaAlumno = new R2dbcAlumno(1L, "Juan", "Perez", "ACTIVO", 20, false);
        when(jpaRepository.save(any(R2dbcAlumno.class))).thenReturn(Mono.just(jpaAlumno));

        StepVerifier.create(adapter.save(alumno))
                .verifyComplete();
    }

    @Test
    void findByEstado_DebeRetornarAlumnos() {
        R2dbcAlumno jpaAlumno1 = new R2dbcAlumno(1L, "Juan", "Perez", "ACTIVO", 20, false);
        R2dbcAlumno jpaAlumno2 = new R2dbcAlumno(2L, "Maria", "Gomez", "ACTIVO", 22, false);

        when(jpaRepository.findByEstado("ACTIVO")).thenReturn(Flux.just(jpaAlumno1, jpaAlumno2));

        StepVerifier.create(adapter.findByEstado(Estado.ACTIVO))
                .expectNextMatches(alumno -> alumno.getId().equals(1L))
                .expectNextMatches(alumno -> alumno.getId().equals(2L))
                .verifyComplete();
    }

    @Test
    void existsById_DebeRetornarTrue() {
        when(jpaRepository.existsById(1L)).thenReturn(Mono.just(true));

        StepVerifier.create(adapter.existsById(1L))
                .expectNext(true)
                .verifyComplete();
    }
}
