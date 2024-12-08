package com.example.apprest.models;


import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;



@Entity 

public class Alumno {
    @Id
    private String id;
    private String nombres;
    private String apellidos;
    private String matricula;
    private Double promedio;
    private String fotoPerfilUrl;
    private String password;

    
    public Alumno() {

    }
    public Alumno(String nombres, String apellidos, String matricula, Double promedio) {
        this.id = UUID.randomUUID().toString(); 
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.matricula = matricula;
        this.promedio = promedio;
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
    public String getMatricula() {
        return matricula;
    }
    public void setMatricula(String matricula) {
        if (matricula == null || matricula.isEmpty()) {
            throw new IllegalArgumentException("Matrícula inválida");
        }
        this.matricula = matricula;
    }
    public Double getPromedio() {
        return promedio;
    }
    public void setPromedio(Double promedio) {
        if (promedio < 0 || promedio > 10) {
            throw new IllegalArgumentException("Promedio inválido");
        }
        this.promedio = promedio;
    }

    public String getFotoPerfilUrl() {
        return fotoPerfilUrl;
    }

    public void setFotoPerfilUrl(String fotoPerfilUrl) {
        this.fotoPerfilUrl = fotoPerfilUrl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    
}
