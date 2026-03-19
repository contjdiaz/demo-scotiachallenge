package com.scotiachallenge.demo.application.usecase.impl;

import com.scotiachallenge.demo.application.usecase.ObtenerAlumnosActivosUseCase;
import com.scotiachallenge.demo.domain.model.Alumno;
import com.scotiachallenge.demo.domain.model.Estado;
import com.scotiachallenge.demo.domain.port.AlumnoRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Slf4j
@Service
public class ObtenerAlumnosActivosUseCaseImpl implements ObtenerAlumnosActivosUseCase {
    
    private final AlumnoRepositoryPort repositoryPort;

    public ObtenerAlumnosActivosUseCaseImpl(AlumnoRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public Flux<Alumno> ejecutar() {
        log.debug("Consultando alumnos con estado: {}", Estado.ACTIVO);
        return repositoryPort.findByEstado(Estado.ACTIVO)
                .doOnNext(alumno -> log.debug("Alumno activo encontrado: {} {}", alumno.getNombre(), alumno.getApellido()));
    }
}
