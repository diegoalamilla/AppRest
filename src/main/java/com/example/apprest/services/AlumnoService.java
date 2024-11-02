package com.example.apprest.services;

import org.springframework.stereotype.Service;
import java.util.List;
import com.example.apprest.models.Alumno;
import java.util.ArrayList;

@Service
public class AlumnoService {
    private List<Alumno> alumnos = new ArrayList<Alumno>();

    public List<Alumno> getAlumnos() {
        return alumnos;
    }

    public Alumno getAlumnoById(int id) {
        return alumnos.stream()
                .filter(alumno -> alumno.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public Alumno addAlumno(Alumno alumno) {
        alumnos.add(alumno);
        return alumno;
    }

    public Alumno updateAlumno(Alumno alumno) {
        for (Alumno a : alumnos) {
            if (a.getId() == alumno.getId()) {
                a.setNombres(alumno.getNombres());
                a.setApellidos(alumno.getApellidos());
                a.setMatricula(alumno.getMatricula());
                a.setPromedio(alumno.getPromedio());
            }
        }
        return alumno;
    }

    public boolean deleteAlumno(int id) {
       return alumnos.removeIf(a -> a.getId() == id);
    }

}
