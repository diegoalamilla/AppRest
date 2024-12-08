package com.example.apprest.models;

import java.util.UUID;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Profesor {
    @Id
    private String id;
    private int numeroEmpleado;
    private String nombres;
    private String apellidos;
    private int horasClase;


    
    public Profesor() {
    }
    public Profesor(String id, int numeroEmpleado, String nombres, String apellidos, int horasClase) {
        this.id = UUID.randomUUID().toString(); 
        this.numeroEmpleado = numeroEmpleado;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.horasClase = horasClase;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Id inválido");
        }
        this.id = id;
    }
    public int getNumeroEmpleado() {
        return numeroEmpleado;
    }
    public void setNumeroEmpleado(int numeroEmpleado) {
        if (numeroEmpleado <= 0) {
            throw new IllegalArgumentException("Número de empleado inválido");
        }
        this.numeroEmpleado = numeroEmpleado;
    }
    public String getNombres() {
        return nombres;
    }
    public void setNombres(String nombres) {
        if (nombres == null || nombres.isEmpty()) {
            throw new IllegalArgumentException("Nombre inválido");
        }
        this.nombres = nombres;
    }
    public String getApellidos() {
        return apellidos;
    }
    public void setApellidos(String apellidos) {
        if (apellidos == null || apellidos.isEmpty()) {
            throw new IllegalArgumentException("Apellido inválido");
        }
        this.apellidos = apellidos;
    }
    public int getHorasClase() {
        return horasClase;
    }
    public void setHorasClase(int horasClase) {
        if (horasClase <= 0) {
            throw new IllegalArgumentException("Horas de clase inválidas");
        }
        this.horasClase = horasClase;
    }

    
}
