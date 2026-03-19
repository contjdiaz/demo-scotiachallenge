package com.scotiachallenge.demo.integration;

import com.scotiachallenge.demo.domain.model.Estado;
import com.scotiachallenge.demo.infraestructure.web.dto.AlumnoRequest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AlumnoIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @Order(1)
    void debeGrabarAlumnoExitosamente() {
        AlumnoRequest request = new AlumnoRequest();
        request.setId(100L);
        request.setNombre("Carlos");
        request.setApellido("Lopez");
        request.setEstado(Estado.ACTIVO);
        request.setEdad(25);

        webTestClient.post()
                .uri("/alumnos")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    @Order(2)
    void debeRetornarErrorAlGrabarAlumnoConIdDuplicado() {
        AlumnoRequest request = new AlumnoRequest();
        request.setId(100L);
        request.setNombre("Otro");
        request.setApellido("Alumno");
        request.setEstado(Estado.ACTIVO);
        request.setEdad(30);

        webTestClient.post()
                .uri("/alumnos")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isEqualTo("No se pudo hacer la grabacion: ID repetido")
                .jsonPath("$.status").isEqualTo(400)
                .jsonPath("$.timestamp").exists();
    }

    @Test
    @Order(3)
    void debeObtenerSoloAlumnosActivos() {
        // Grabar un alumno inactivo
        AlumnoRequest inactivo = new AlumnoRequest();
        inactivo.setId(200L);
        inactivo.setNombre("Inactivo");
        inactivo.setApellido("Test");
        inactivo.setEstado(Estado.INACTIVO);
        inactivo.setEdad(19);

        webTestClient.post()
                .uri("/alumnos")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(inactivo)
                .exchange()
                .expectStatus().isCreated();

        // Grabar otro alumno activo
        AlumnoRequest activo = new AlumnoRequest();
        activo.setId(201L);
        activo.setNombre("Activo");
        activo.setApellido("Test");
        activo.setEstado(Estado.ACTIVO);
        activo.setEdad(21);

        webTestClient.post()
                .uri("/alumnos")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(activo)
                .exchange()
                .expectStatus().isCreated();

        // Obtener activos - no debe contener alumnos inactivos
        webTestClient.get()
                .uri("/alumnos/activos")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$[?(@.id == 200)]").doesNotExist()
                .jsonPath("$[?(@.estado == 'INACTIVO')]").doesNotExist();
    }

    @Test
    @Order(4)
    void debeRechazarAlumnoConDatosInvalidos() {
        AlumnoRequest request = new AlumnoRequest();
        request.setId(null);
        request.setNombre("");
        request.setApellido("");
        request.setEstado(null);
        request.setEdad(-1);

        webTestClient.post()
                .uri("/alumnos")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo(400)
                .jsonPath("$.message").exists()
                .jsonPath("$.timestamp").exists();
    }
}
