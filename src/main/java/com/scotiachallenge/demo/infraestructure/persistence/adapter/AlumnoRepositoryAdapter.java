package com.scotiachallenge.demo.infraestructure.persistence.adapter;

import com.scotiachallenge.demo.domain.model.Alumno;
import com.scotiachallenge.demo.domain.model.Estado;
import com.scotiachallenge.demo.domain.port.AlumnoRepositoryPort;
import com.scotiachallenge.demo.infraestructure.persistence.entity.R2dbcAlumno;
import com.scotiachallenge.demo.infraestructure.persistence.repository.R2dbcAlumnoRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class AlumnoRepositoryAdapter implements AlumnoRepositoryPort {
    private final R2dbcAlumnoRepository jpaRepository;

    public AlumnoRepositoryAdapter(R2dbcAlumnoRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Mono<Void> save(Alumno alumno) {
        R2dbcAlumno jpa = mapToJpa(alumno);
        // Force insert for creates when an ID is provided.
        jpa.markNew();
        return jpaRepository.save(jpa).then();
    }

    @Override
    public Flux<Alumno> findByEstado(Estado estado) {
        return jpaRepository.findByEstado(estado.name())
                .map(this::mapToDomain);
    }

    @Override
    public Mono<Boolean> existsById(Long id) {
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
