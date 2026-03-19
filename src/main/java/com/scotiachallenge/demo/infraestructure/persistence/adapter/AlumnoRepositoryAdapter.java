package com.scotiachallenge.demo.infraestructure.persistence.adapter;

import com.scotiachallenge.demo.domain.model.Alumno;
import com.scotiachallenge.demo.domain.model.Estado;
import com.scotiachallenge.demo.domain.port.AlumnoRepositoryPort;
import com.scotiachallenge.demo.infraestructure.persistence.entity.R2dbcAlumno;
import com.scotiachallenge.demo.infraestructure.persistence.repository.R2dbcAlumnoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class AlumnoRepositoryAdapter implements AlumnoRepositoryPort {
    private final R2dbcAlumnoRepository jpaRepository;

    public AlumnoRepositoryAdapter(R2dbcAlumnoRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Mono<Void> save(Alumno alumno) {
        log.debug("Guardando alumno en base de datos con ID: {}", alumno.getId());
        R2dbcAlumno jpa = mapToJpa(alumno);
        // Force insert for creates when an ID is provided.
        jpa.markNew();
        return jpaRepository.save(jpa)
                .doOnSuccess(saved -> log.debug("Alumno guardado exitosamente con ID: {}", saved.getId()))
                .then();
    }

    @Override
    public Flux<Alumno> findByEstado(Estado estado) {
        log.debug("Buscando alumnos por estado: {}", estado);
        return jpaRepository.findByEstado(estado.name())
                .map(this::mapToDomain);
    }

    @Override
    public Mono<Boolean> existsById(Long id) {
        log.debug("Verificando existencia de alumno con ID: {}", id);
        return jpaRepository.existsById(id);
    }

    private R2dbcAlumno mapToJpa(Alumno alumno) {
        R2dbcAlumno jpa = new R2dbcAlumno();
        jpa.setId(alumno.getId());
        jpa.setNombre(alumno.getNombre());
        jpa.setApellido(alumno.getApellido());
        jpa.setEstadoEnum(alumno.getEstado());
        jpa.setEdad(alumno.getEdad());
        return jpa;
    }

    private Alumno mapToDomain(R2dbcAlumno jpa) {
        return new Alumno(jpa.getId(), jpa.getNombre(), jpa.getApellido(),
                          jpa.getEstadoEnum(), jpa.getEdad());
    }
}
