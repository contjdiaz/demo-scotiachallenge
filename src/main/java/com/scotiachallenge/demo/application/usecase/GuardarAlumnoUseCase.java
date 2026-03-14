package com.scotiachallenge.demo.application.usecase;

import com.scotiachallenge.demo.domain.model.Alumno;
import reactor.core.publisher.Mono;

public interface GuardarAlumnoUseCase {
    Mono<Void> ejecutar(Alumno alumno);
}
