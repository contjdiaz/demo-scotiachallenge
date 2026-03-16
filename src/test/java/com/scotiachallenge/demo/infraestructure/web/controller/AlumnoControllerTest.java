package com.scotiachallenge.demo.infraestructure.web.controller;

import com.scotiachallenge.demo.application.usecase.GuardarAlumnoUseCase;
import com.scotiachallenge.demo.application.usecase.ObtenerAlumnosActivosUseCase;
import com.scotiachallenge.demo.domain.exception.DuplicateIdException;
import com.scotiachallenge.demo.domain.model.Alumno;
import com.scotiachallenge.demo.domain.model.Estado;
import com.scotiachallenge.demo.infraestructure.web.dto.AlumnoRequest;
import com.scotiachallenge.demo.infraestructure.web.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.r2dbc.R2dbcDataAutoConfiguration;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

@WebFluxTest(excludeAutoConfiguration = {R2dbcAutoConfiguration.class, R2dbcDataAutoConfiguration.class})
@Import({AlumnoController.class, GlobalExceptionHandler.class, AlumnoControllerTest.TestConfig.class})
class AlumnoControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private GuardarAlumnoUseCase guardarAlumnoUseCase;

    @Autowired
    private ObtenerAlumnosActivosUseCase obtenerAlumnosActivosUseCase;

    @Configuration
    static class TestConfig {
        @Bean
        GuardarAlumnoUseCase guardarAlumnoUseCase() {
            return mock(GuardarAlumnoUseCase.class);
        }

        @Bean
        ObtenerAlumnosActivosUseCase obtenerAlumnosActivosUseCase() {
            return mock(ObtenerAlumnosActivosUseCase.class);
        }
    }

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
    void grabarAlumno_DebeRetornarBadRequest_CuandoIdEsRepetido() {
        AlumnoRequest request = new AlumnoRequest();
        request.setId(1L);
        request.setNombre("Juan");
        request.setApellido("Perez");
        request.setEstado(Estado.ACTIVO);
        request.setEdad(20);

        when(guardarAlumnoUseCase.ejecutar(any(Alumno.class)))
                .thenReturn(Mono.error(new DuplicateIdException("No se pudo hacer la grabacion: ID repetido")));

        webTestClient.post()
                .uri("/alumnos")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .isEqualTo("No se pudo hacer la grabacion: ID repetido");
    }

    @Test
    void grabarAlumno_DebeRetornarBadRequest_CuandoIdEsNulo() {
        AlumnoRequest request = new AlumnoRequest();
        request.setId(null);
        request.setNombre("Juan");
        request.setApellido("Perez");
        request.setEstado(Estado.ACTIVO);
        request.setEdad(20);

        webTestClient.post()
                .uri("/alumnos")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .isEqualTo("El id es obligatorio");
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
