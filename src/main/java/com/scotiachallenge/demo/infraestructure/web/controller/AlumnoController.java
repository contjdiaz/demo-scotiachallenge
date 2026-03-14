package main.java.com.scotiachallenge.demo.infraestructure.web.controller;

import main.java.com.scotiachallenge.demo.application.usecase.GuardarAlumnoUseCase;
import main.java.com.scotiachallenge.demo.application.usecase.ObtenerAlumnosActivosUseCase;
import main.java.com.scotiachallenge.demo.domain.model.Alumno;
import main.java.com.scotiachallenge.demo.infraestructure.web.dto.AlumnoRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/alumnos")
public class AlumnoController {
    private final GuardarAlumnoUseCase guardarUseCase;
    private final ObtenerAlumnosActivosUseCase obtenerUseCase;

    public AlumnoController(GuardarAlumnoUseCase guardarUseCase,
                            ObtenerAlumnosActivosUseCase obtenerUseCase) {
        this.guardarUseCase = guardarUseCase;
        this.obtenerUseCase = obtenerUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> grabarAlumno(@Valid @RequestBody AlumnoRequest request) {
        Alumno alumno = new Alumno(request.getId(), request.getNombre(),
                                   request.getApellido(), request.getEstado(), request.getEdad());
        return guardarUseCase.ejecutar(alumno);
    }

    @GetMapping("/activos")
    public Flux<Alumno> obtenerAlumnosActivos() {
        return obtenerUseCase.ejecutar();
    }
}
