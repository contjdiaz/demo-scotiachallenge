package com.scotiachallenge.demo.domain.model;

public class Alumno {
    private final Long id;
    private final String nombre;
    private final String apellido;
    private final Estado estado;
    private final int edad;

    public Alumno(Long id, String nombre, String apellido, Estado estado, int edad) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.estado = estado;
        this.edad = edad;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public Estado getEstado() { return estado; }
    public int getEdad() { return edad; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alumno alumno = (Alumno) o;
        return edad == alumno.edad &&
               java.util.Objects.equals(id, alumno.id) &&
               java.util.Objects.equals(nombre, alumno.nombre) &&
               java.util.Objects.equals(apellido, alumno.apellido) &&
               estado == alumno.estado;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, nombre, apellido, estado, edad);
    }
}
