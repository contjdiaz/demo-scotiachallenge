package com.scotiachallenge.demo.infraestructure.persistence.adapter;

import com.scotiachallenge.demo.domain.model.Alumno;
import com.scotiachallenge.demo.domain.model.Estado;
import com.scotiachallenge.demo.domain.port.AlumnoRepositoryPort;
import com.scotiachallenge.demo.infraestructure.persistence.entity.JpaAlumno;
import com.scotiachallenge.demo.infraestructure.persistence.repository.JpaAlumnoRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class AlumnoRepositoryAdapter implements AlumnoRepositoryPort {
    private final JpaAlumnoRepository jpaRepository;

    public AlumnoRepositoryAdapter(JpaAlumnoRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Mono<Void> save(Alumno alumno) {
        return jpaRepository.save(mapToJpa(alumno)).then();
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

    private JpaAlumno mapToJpa(Alumno alumno) {
        JpaAlumno jpa = new JpaAlumno();
        jpa.setId(alumno.getId());
        jpa.setNombre(alumno.getNombre());
        jpa.setApellido(alumno.getApellido());
        jpa.setEstadoEnum(alumno.getEstado());
        jpa.setEdad(alumno.getEdad());
        return jpa;
    }

    private Alumno mapToDomain(JpaAlumno jpa) {
        return new Alumno(jpa.getId(), jpa.getNombre(), jpa.getApellido(),
                          jpa.getEstadoEnum(), jpa.getEdad());
    }
}
