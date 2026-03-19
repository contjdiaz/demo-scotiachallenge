package com.scotiachallenge.demo.application.usecase.impl;

import com.scotiachallenge.demo.application.usecase.GuardarAlumnoUseCase;
import com.scotiachallenge.demo.domain.exception.DuplicateIdException;
import com.scotiachallenge.demo.domain.model.Alumno;
import com.scotiachallenge.demo.domain.port.AlumnoRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class GuardarAlumnoUseCaseImpl implements GuardarAlumnoUseCase {

    private final AlumnoRepositoryPort repositoryPort;

    public GuardarAlumnoUseCaseImpl(AlumnoRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public Mono<Void> ejecutar(Alumno alumno) {
        log.debug("Verificando existencia del alumno con ID: {}", alumno.getId());
        return repositoryPort.existsById(alumno.getId())
                .flatMap(existe -> {
                    if (existe) {
                        log.warn("Intento de grabar alumno con ID duplicado: {}", alumno.getId());
                        return Mono.error(new DuplicateIdException("No se pudo hacer la grabacion: ID repetido"));
                    }
                    log.debug("Procediendo a grabar alumno: {} {}", alumno.getNombre(), alumno.getApellido());
                    return repositoryPort.save(alumno);
                });
    }
}


