package com.scotiachallenge.demo.application.usecase.impl;

import com.scotiachallenge.demo.application.usecase.GuardarAlumnoUseCase;
import com.scotiachallenge.demo.domain.exception.DuplicateIdException;
import com.scotiachallenge.demo.domain.model.Alumno;
import com.scotiachallenge.demo.domain.port.AlumnoRepositoryPort;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class GuardarAlumnoUseCaseImpl implements GuardarAlumnoUseCase {

    private final AlumnoRepositoryPort repositoryPort;
    private final Counter alumnosGrabadosCounter;
    private final Counter alumnosDuplicadosCounter;
    private final Timer guardarAlumnoTimer;

    public GuardarAlumnoUseCaseImpl(AlumnoRepositoryPort repositoryPort,
                                    MeterRegistry registry) {
        this.repositoryPort = repositoryPort;
        this.alumnosGrabadosCounter = Counter.builder("alumnos.grabados.total")
                .description("Total de alumnos grabados exitosamente")
                .tag("type", "success")
                .register(registry);
        this.alumnosDuplicadosCounter = Counter.builder("alumnos.grabados.duplicados")
                .description("Total de intentos de grabacion con ID duplicado")
                .tag("type", "error")
                .register(registry);
        this.guardarAlumnoTimer = Timer.builder("alumnos.guardar.tiempo")
                .description("Tiempo de grabacion de alumno")
                .register(registry);
    }

    @Override
    public Mono<Void> ejecutar(Alumno alumno) {
        return Mono.defer(() -> {
            Timer.Sample sample = Timer.start();
            log.debug("Verificando existencia del alumno con ID: {}", alumno.getId());

            return repositoryPort.existsById(alumno.getId())
                    .flatMap(existe -> {
                        if (Boolean.TRUE.equals(existe)) {
                            log.warn("Intento de grabar alumno con ID duplicado: {}", alumno.getId());
                            alumnosDuplicadosCounter.increment();
                            return Mono.<Void>error(new DuplicateIdException(
                                    "No se pudo hacer la grabacion: ID repetido"));
                        }
                        log.debug("Procediendo a grabar alumno: {} {}", alumno.getNombre(), alumno.getApellido());
                        return repositoryPort.save(alumno)
                                .doOnSuccess(v -> alumnosGrabadosCounter.increment());
                    })
                    .doFinally(signal -> sample.stop(guardarAlumnoTimer));
        });
    }
}


