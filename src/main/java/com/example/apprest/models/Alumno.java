package com.example.apprest.models;

public class Alumno {
    private int id;
    private String nombres;
    private String apellidos;
    private String matricula;
    private Double promedio;

    
    public Alumno() {

    }
    public Alumno(int id, String nombres, String apellidos, String matricula, Double promedio) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.matricula = matricula;
        this.promedio = promedio;
    }
    public int getId() {
        
        return id;
    }
    public void setId(int id) {
        if (id <= 0) {
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

    
}
