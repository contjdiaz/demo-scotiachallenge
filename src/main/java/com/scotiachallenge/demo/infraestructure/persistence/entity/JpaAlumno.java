package com.scotiachallenge.demo.infraestructure.persistence.entity;

import com.scotiachallenge.demo.domain.model.Estado;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("alumno")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JpaAlumno {
    @Id
    private Long id;
    private String nombre;
    private String apellido;
    private String estado;
    private int edad;

    public Estado getEstadoEnum() {
        return Estado.valueOf(this.estado);
    }

    public void setEstadoEnum(Estado estado) {
        this.estado = estado.name();
    }
}
