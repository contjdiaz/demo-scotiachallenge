package com.scotiachallenge.demo.infraestructure.persistence.repository;
import java.util.List;

import com.scotiachallenge.demo.domain.model.Estado;
import com.scotiachallenge.demo.infraestructure.persistence.entity.JpaAlumno;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JpaAlumnoRepository extends JpaRepository<JpaAlumno, Long>{
    List<JpaAlumno> findByEstado(Estado estado);
}
