package com.scotiachallenge.demo.infraestructure.web.controller;

import com.scotiachallenge.demo.application.usecase.GuardarAlumnoUseCase;
import com.scotiachallenge.demo.application.usecase.ObtenerAlumnosActivosUseCase;
import com.scotiachallenge.demo.domain.model.Alumno;
import com.scotiachallenge.demo.domain.model.Estado;
import com.scotiachallenge.demo.infraestructure.web.dto.AlumnoRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = AlumnoController.class)
class AlumnoControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GuardarAlumnoUseCase guardarAlumnoUseCase;

    @MockBean
    private ObtenerAlumnosActivosUseCase obtenerAlumnosActivosUseCase;

    @Test
    void grabarAlumno_DebeRetornarCreated_CuandoDatosSonValidos() {
        AlumnoRequest request = new AlumnoRequest();
        request.setId(1L);
        request.setNombre("Juan");
        request.setApellido("Perez");
        request.setEstado(Estado.ACTIVO);
        request.setEdad(20);

        when(guardarAlumnoUseCase.ejecutar(any(Alumno.class))).thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/alumnos")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    void obtenerAlumnosActivos_DebeRetornarListaDeAlumnos() {
        Alumno alumno1 = new Alumno(1L, "Juan", "Perez", Estado.ACTIVO, 20);
        Alumno alumno2 = new Alumno(2L, "Maria", "Gomez", Estado.ACTIVO, 22);

        when(obtenerAlumnosActivosUseCase.ejecutar()).thenReturn(Flux.just(alumno1, alumno2));

        webTestClient.get()
                .uri("/alumnos/activos")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Alumno.class)
                .hasSize(2)
                .contains(alumno1, alumno2);
    }
}
