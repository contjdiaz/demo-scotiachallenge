package com.scotiachallenge.demo.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.scotiachallenge.demo.Entity.Alumno;
import com.scotiachallenge.demo.Service.AlumnoService;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/alumnos")
public class AlumnoController {
    
    private final AlumnoService service;

    public AlumnoController(AlumnoService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)  // respuesta vacía como pide el reto
    public Mono<Void> grabarAlumno(@Valid @RequestBody Alumno alumno) {
        return service.guardar(alumno);
    }

    @GetMapping("/activos")
    public Flux<Alumno> obtenerAlumnosActivos() {
        return service.obtenerActivos();
    }
}
