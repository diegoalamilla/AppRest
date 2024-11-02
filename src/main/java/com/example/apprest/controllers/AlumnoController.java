package com.example.apprest.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.example.apprest.models.Alumno;
import com.example.apprest.services.AlumnoService;

@RestController
@RequestMapping("/alumnos")
public class AlumnoController {

    @Autowired
    private AlumnoService alumnoService;

    @GetMapping
    public ResponseEntity<List<Alumno>> getAlumnos() {
        List<Alumno> alumnos = alumnoService.getAlumnos();
        return ResponseEntity.ok(alumnos); 
    }
    @GetMapping("/{id}")
    public ResponseEntity<Alumno> getAlumnoById(@PathVariable int id) {
        Alumno alumno = alumnoService.getAlumnoById(id);
        if (alumno != null) {
            return ResponseEntity.ok(alumno);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    

    @PostMapping
    public ResponseEntity<Alumno> addAlumno(@RequestBody Alumno alumno) {
        try {
            Alumno nuevoAlumno = alumnoService.addAlumno(alumno);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoAlumno); 
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); 
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Alumno> updateAlumno(@PathVariable int id, @RequestBody Alumno alumno) {
        try {
           alumno.setId(id);
           Alumno alumnoActualizado = alumnoService.updateAlumno(alumno);
            return ResponseEntity.ok(alumnoActualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(alumno); 
        }
    }
   
    @DeleteMapping("/{id}")
    public ResponseEntity<Alumno> deleteAlumno(@PathVariable int id) {
        boolean isDeleted = alumnoService.deleteAlumno(id);
       if (isDeleted) {
           return ResponseEntity.ok(null);
       } else {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
       }
    }
}
