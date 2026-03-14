package main.java.com.scotiachallenge.demo.infraestructure.persistence.adapter;

import main.java.com.scotiachallenge.demo.domain.model.Alumno;
import main.java.com.scotiachallenge.demo.domain.model.Estado;
import main.java.com.scotiachallenge.demo.domain.port.AlumnoRepositoryPort;
import main.java.com.scotiachallenge.demo.infraestructure.persistence.entity.JpaAlumno;
import main.java.com.scotiachallenge.demo.infraestructure.persistence.repository.JpaAlumnoRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class AlumnoRepositoryAdapter implements AlumnoRepositoryPort {
    private final JpaAlumnoRepository jpaRepository;

    public AlumnoRepositoryAdapter(JpaAlumnoRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Mono<Void> save(Alumno alumno) {
        return Mono.fromCallable(() -> {
            jpaRepository.save(mapToJpa(alumno));
            return null;
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }

    @Override
    public Flux<Alumno> findByEstado(Estado estado) {
        return Mono.fromCallable(() -> jpaRepository.findByEstado(estado))
                .flatMapMany(Flux::fromIterable)
                .map(this::mapToDomain)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Boolean> existsById(Long id) {
        return Mono.fromCallable(() -> jpaRepository.existsById(id))
                .subscribeOn(Schedulers.boundedElastic());
    }

    private JpaAlumno mapToJpa(Alumno alumno) {
        return new JpaAlumno(alumno.getId(), alumno.getNombre(), alumno.getApellido(),
                             alumno.getEstado(), alumno.getEdad());
    }

    private Alumno mapToDomain(JpaAlumno jpa) {
        return new Alumno(jpa.getId(), jpa.getNombre(), jpa.getApellido(),
                          jpa.getEstado(), jpa.getEdad());
    }
}
