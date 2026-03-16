package com.scotiachallenge.demo.infraestructure.persistence.repository;

import com.scotiachallenge.demo.infraestructure.persistence.entity.R2dbcAlumno;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface R2dbcAlumnoRepository extends ReactiveCrudRepository<R2dbcAlumno, Long> {
    @Query("SELECT * FROM alumno WHERE estado = :estado")
    Flux<R2dbcAlumno> findByEstado(String estado);
}
