package com.scotiachallenge.demo.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlumnoTest {

    @Test
    void constructor_DebeCrearAlumnoConTodosLosCampos() {
        Alumno alumno = new Alumno(1L, "Juan", "Perez", Estado.ACTIVO, 20);

        assertEquals(1L, alumno.getId());
        assertEquals("Juan", alumno.getNombre());
        assertEquals("Perez", alumno.getApellido());
        assertEquals(Estado.ACTIVO, alumno.getEstado());
        assertEquals(20, alumno.getEdad());
    }

    @Test
    void equals_DebeRetornarTrue_CuandoMismosAtributos() {
        Alumno alumno1 = new Alumno(1L, "Juan", "Perez", Estado.ACTIVO, 20);
        Alumno alumno2 = new Alumno(1L, "Juan", "Perez", Estado.ACTIVO, 20);

        assertEquals(alumno1, alumno2);
    }

    @Test
    void equals_DebeRetornarTrue_CuandoMismaInstancia() {
        Alumno alumno = new Alumno(1L, "Juan", "Perez", Estado.ACTIVO, 20);

        assertEquals(alumno, alumno);
    }

    @Test
    void equals_DebeRetornarFalse_CuandoIdDiferente() {
        Alumno alumno1 = new Alumno(1L, "Juan", "Perez", Estado.ACTIVO, 20);
        Alumno alumno2 = new Alumno(2L, "Juan", "Perez", Estado.ACTIVO, 20);

        assertNotEquals(alumno1, alumno2);
    }

    @Test
    void equals_DebeRetornarFalse_CuandoNombreDiferente() {
        Alumno alumno1 = new Alumno(1L, "Juan", "Perez", Estado.ACTIVO, 20);
        Alumno alumno2 = new Alumno(1L, "Carlos", "Perez", Estado.ACTIVO, 20);

        assertNotEquals(alumno1, alumno2);
    }

    @Test
    void equals_DebeRetornarFalse_CuandoApellidoDiferente() {
        Alumno alumno1 = new Alumno(1L, "Juan", "Perez", Estado.ACTIVO, 20);
        Alumno alumno2 = new Alumno(1L, "Juan", "Lopez", Estado.ACTIVO, 20);

        assertNotEquals(alumno1, alumno2);
    }

    @Test
    void equals_DebeRetornarFalse_CuandoEstadoDiferente() {
        Alumno alumno1 = new Alumno(1L, "Juan", "Perez", Estado.ACTIVO, 20);
        Alumno alumno2 = new Alumno(1L, "Juan", "Perez", Estado.INACTIVO, 20);

        assertNotEquals(alumno1, alumno2);
    }

    @Test
    void equals_DebeRetornarFalse_CuandoEdadDiferente() {
        Alumno alumno1 = new Alumno(1L, "Juan", "Perez", Estado.ACTIVO, 20);
        Alumno alumno2 = new Alumno(1L, "Juan", "Perez", Estado.ACTIVO, 25);

        assertNotEquals(alumno1, alumno2);
    }

    @Test
    void equals_DebeRetornarFalse_CuandoEsNull() {
        Alumno alumno = new Alumno(1L, "Juan", "Perez", Estado.ACTIVO, 20);

        assertNotEquals(null, alumno);
    }

    @Test
    void equals_DebeRetornarFalse_CuandoEsOtraClase() {
        Alumno alumno = new Alumno(1L, "Juan", "Perez", Estado.ACTIVO, 20);

        assertNotEquals("un string", alumno);
    }

    @Test
    void hashCode_DebeSerIgual_CuandoMismosAtributos() {
        Alumno alumno1 = new Alumno(1L, "Juan", "Perez", Estado.ACTIVO, 20);
        Alumno alumno2 = new Alumno(1L, "Juan", "Perez", Estado.ACTIVO, 20);

        assertEquals(alumno1.hashCode(), alumno2.hashCode());
    }

    @Test
    void hashCode_DebeSerDiferente_CuandoAtributosDiferentes() {
        Alumno alumno1 = new Alumno(1L, "Juan", "Perez", Estado.ACTIVO, 20);
        Alumno alumno2 = new Alumno(2L, "Maria", "Gomez", Estado.INACTIVO, 22);

        assertNotEquals(alumno1.hashCode(), alumno2.hashCode());
    }
}
