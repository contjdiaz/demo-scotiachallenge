package com.scotiachallenge.demo.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EstadoTest {

    @Test
    void debeContenerDosValores() {
        assertEquals(2, Estado.values().length);
    }

    @Test
    void debeContenerActivo() {
        assertEquals(Estado.ACTIVO, Estado.valueOf("ACTIVO"));
    }

    @Test
    void debeContenerInactivo() {
        assertEquals(Estado.INACTIVO, Estado.valueOf("INACTIVO"));
    }

    @Test
    void valueOfInvalido_DebeLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> Estado.valueOf("INVALIDO"));
    }
}
