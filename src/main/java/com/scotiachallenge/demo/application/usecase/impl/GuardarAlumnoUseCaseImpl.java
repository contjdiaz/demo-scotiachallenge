package main.java.com.scotiachallenge.demo.application.usecase.impl;
import main.java.com.scotiachallenge.demo.application.usecase.GuardarAlumnoUseCase;
import main.java.com.scotiachallenge.demo.domain.exception.DuplicateIdException;
import main.java.com.scotiachallenge.demo.domain.model.Alumno;
import main.java.com.scotiachallenge.demo.domain.port.AlumnoRepositoryPort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GuardarAlumnoUseCaseImpl implements GuardarAlumnoUseCase {
    private final AlumnoRepositoryPort repositoryPort;

    public GuardarAlumnoUseCaseImpl(AlumnoRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public Mono<Void> ejecutar(Alumno alumno) {
        return repositoryPort.existsById(alumno.getId())
                .flatMap(existe -> {
                    if (existe) {
                        return Mono.error(new DuplicateIdException("No se pudo hacer la grabación: ID repetido"));
                    }
                    return repositoryPort.save(alumno);
                });
    }
}
