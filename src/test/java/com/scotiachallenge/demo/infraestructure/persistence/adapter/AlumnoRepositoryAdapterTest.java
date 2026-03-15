package com.scotiachallenge.demo.infraestructure.persistence.adapter;

import com.scotiachallenge.demo.domain.model.Alumno;
import com.scotiachallenge.demo.domain.model.Estado;
import com.scotiachallenge.demo.infraestructure.persistence.entity.JpaAlumno;
import com.scotiachallenge.demo.infraestructure.persistence.repository.JpaAlumnoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AlumnoRepositoryAdapterTest {

    @Mock
    private JpaAlumnoRepository jpaRepository;

    @InjectMocks
    private AlumnoRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_DebeGuardarAlumno() {
        Alumno alumno = new Alumno(1L, "Juan", "Perez", Estado.ACTIVO, 20);
        JpaAlumno jpaAlumno = new JpaAlumno(1L, "Juan", "Perez", Estado.ACTIVO, 20);
        when(jpaRepository.save(any(JpaAlumno.class))).thenReturn(jpaAlumno);

        StepVerifier.create(adapter.save(alumno))
                .verifyComplete();
    }

    @Test
    void findByEstado_DebeRetornarAlumnos() {
        JpaAlumno jpaAlumno1 = new JpaAlumno(1L, "Juan", "Perez", Estado.ACTIVO, 20);
        JpaAlumno jpaAlumno2 = new JpaAlumno(2L, "Maria", "Gomez", Estado.ACTIVO, 22);

        when(jpaRepository.findByEstado(Estado.ACTIVO)).thenReturn(List.of(jpaAlumno1, jpaAlumno2));

        StepVerifier.create(adapter.findByEstado(Estado.ACTIVO))
                .expectNextMatches(alumno -> alumno.getId().equals(1L))
                .expectNextMatches(alumno -> alumno.getId().equals(2L))
                .verifyComplete();
    }

    @Test
    void existsById_DebeRetornarTrue() {
        when(jpaRepository.existsById(1L)).thenReturn(true);

        StepVerifier.create(adapter.existsById(1L))
                .expectNext(true)
                .verifyComplete();
    }
}
