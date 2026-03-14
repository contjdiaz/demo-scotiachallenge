package com.scotiachallenge.demo.infraestructure.persistence.entity;

import com.scotiachallenge.demo.domain.model.Estado;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "alumno")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JpaAlumno {
    @Id
    private Long id;
    private String nombre;
    private String apellido;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    private int edad;
}
