package main.java.com.scotiachallenge.demo.domain.port;

import main.java.com.scotiachallenge.demo.domain.model.Alumno;
import main.java.com.scotiachallenge.demo.domain.model.Estado;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AlumnoRepositoryPort {
    Mono<Void> save(Alumno alumno);
    Flux<Alumno> findByEstado(Estado estado);
    Mono<Boolean> existsById(Long id);
}
