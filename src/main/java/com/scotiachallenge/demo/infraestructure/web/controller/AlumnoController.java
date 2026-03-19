package com.scotiachallenge.demo.infraestructure.web.controller;

import com.scotiachallenge.demo.application.usecase.GuardarAlumnoUseCase;
import com.scotiachallenge.demo.application.usecase.ObtenerAlumnosActivosUseCase;
import com.scotiachallenge.demo.domain.model.Alumno;
import com.scotiachallenge.demo.infraestructure.web.dto.AlumnoRequest;
import com.scotiachallenge.demo.infraestructure.web.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/alumnos")
@Tag(name = "Alumnos", description = "API para la gestión de alumnos")
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
    @Operation(summary = "Grabar un alumno", description = "Registra un nuevo alumno validando la consistencia de los campos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Alumno grabado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error de validación o ID duplicado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public Mono<Void> grabarAlumno(@Valid @RequestBody AlumnoRequest request) {
        log.info("Solicitud para grabar alumno con ID: {}", request.getId());
        Alumno alumno = new Alumno(request.getId(), request.getNombre(),
                                   request.getApellido(), request.getEstado(), request.getEdad());
        return guardarUseCase.ejecutar(alumno)
                .doOnSuccess(v -> log.info("Alumno grabado exitosamente con ID: {}", request.getId()));
    }

    @GetMapping("/activos")
    @Operation(summary = "Obtener alumnos activos", description = "Retorna todos los alumnos en estado activo")
    @ApiResponse(responseCode = "200", description = "Lista de alumnos activos obtenida exitosamente")
    public Flux<Alumno> obtenerAlumnosActivos() {
        log.info("Solicitud para obtener alumnos activos");
        return obtenerUseCase.ejecutar()
                .doOnComplete(() -> log.info("Consulta de alumnos activos completada"));
    }
}
