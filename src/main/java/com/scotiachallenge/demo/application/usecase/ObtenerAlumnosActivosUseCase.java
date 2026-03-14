package main.java.com.scotiachallenge.demo.application.usecase;

import main.java.com.scotiachallenge.demo.domain.model.Alumno;
import reactor.core.publisher.Flux;

public interface ObtenerAlumnosActivosUseCase {
    Flux<Alumno> ejecutar();
}
