package main.java.com.scotiachallenge.demo.application.usecase.impl;

import main.java.com.scotiachallenge.demo.application.usecase.ObtenerAlumnosActivosUseCase;
import main.java.com.scotiachallenge.demo.domain.model.Alumno;
import main.java.com.scotiachallenge.demo.domain.model.Estado;
import main.java.com.scotiachallenge.demo.domain.port.AlumnoRepositoryPort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ObtenerAlumnosActivosUseCaseImpl implements ObtenerAlumnosActivosUseCase {
    
    private final AlumnoRepositoryPort repositoryPort;

    public ObtenerAlumnosActivosUseCaseImpl(AlumnoRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public Flux<Alumno> ejecutar() {
        return repositoryPort.findByEstado(Estado.ACTIVO);
    }
}
