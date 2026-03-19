package com.scotiachallenge.demo.application.usecase.impl;

import com.scotiachallenge.demo.application.usecase.ObtenerAlumnosActivosUseCase;
import com.scotiachallenge.demo.domain.model.Alumno;
import com.scotiachallenge.demo.domain.model.Estado;
import com.scotiachallenge.demo.domain.port.AlumnoRepositoryPort;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Slf4j
@Service
public class ObtenerAlumnosActivosUseCaseImpl implements ObtenerAlumnosActivosUseCase {

    private final AlumnoRepositoryPort repositoryPort;
    private final Counter alumnosConsultadosCounter;

    public ObtenerAlumnosActivosUseCaseImpl(AlumnoRepositoryPort repositoryPort,
                                            MeterRegistry registry) {
        this.repositoryPort = repositoryPort;
        this.alumnosConsultadosCounter = Counter.builder("alumnos.activos.consultas")
                .description("Total de consultas de alumnos activos")
                .tag("type", "query")
                .register(registry);
    }

    @Override
    public Flux<Alumno> ejecutar() {
        log.debug("Consultando alumnos con estado: {}", Estado.ACTIVO);
        alumnosConsultadosCounter.increment();
        return repositoryPort.findByEstado(Estado.ACTIVO)
                .doOnNext(alumno -> log.debug("Alumno activo encontrado: {} {}", alumno.getNombre(), alumno.getApellido()));
    }
}
