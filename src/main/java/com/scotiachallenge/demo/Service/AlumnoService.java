package com.scotiachallenge.demo.Service;

import org.springframework.stereotype.Service;

import com.scotiachallenge.demo.Entity.Alumno;
import com.scotiachallenge.demo.Entity.Estado;
import com.scotiachallenge.demo.Exception.DuplicateIdException;
import com.scotiachallenge.demo.Repository.AlumnoRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class AlumnoService {
    private final AlumnoRepository repository;

    public AlumnoService(AlumnoRepository repository) {
        this.repository = repository;
    }

    public Mono<Void> guardar(Alumno alumno) {
        return Mono.fromCallable(() -> {
            if (repository.existsById(alumno.getId())) {
                throw new DuplicateIdException("No se pudo hacer la grabación: ID repetido");
            }
            repository.save(alumno);
            return null;
        })
        .subscribeOn(Schedulers.boundedElastic())
        .then();
    }

    public Flux<Alumno> obtenerActivos() {
        return Mono.fromCallable(() -> repository.findByEstado(Estado.ACTIVO))
                .flatMapMany(Flux::fromIterable)
                .subscribeOn(Schedulers.boundedElastic());
    }
}
