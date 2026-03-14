package com.scotiachallenge.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scotiachallenge.demo.Entity.Alumno;
import com.scotiachallenge.demo.Entity.Estado;

public interface AlumnoRepository extends JpaRepository<Alumno, Long> {
    List<Alumno> findByEstado(Estado estado);
}
