package com.example.apprest.services;

import com.example.apprest.models.Profesor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProfesorService {
    private List<Profesor> profesores;

    public ProfesorService(){
        profesores = new ArrayList<>();
    }

    public List<Profesor> getProfesores() {
        return profesores;
    }

    public Profesor getProfesorById(String id) {
        return profesores.stream()
                .filter(profesor -> profesor.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Profesor addProfesor(Profesor profesor) {
        profesores.add(profesor);
        return profesor;
    }

    public Profesor updateProfesor(Profesor profesor) {
        for (Profesor p : profesores) {
            if (p.getId().equals(profesor.getId())) {
                p.setNombres(profesor.getNombres());
                p.setApellidos(profesor.getApellidos());
                p.setNumeroEmpleado(profesor.getNumeroEmpleado());
                p.setHorasClase(profesor.getHorasClase());
            }
        }
        return profesor;
    }

    public boolean deleteProfesor(String id) {
        return profesores.removeIf(profesor -> profesor.getId().equals(id));
    }
}