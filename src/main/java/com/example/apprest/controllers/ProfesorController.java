package com.example.apprest.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.apprest.interfaces.ProfesorInterface;
import com.example.apprest.models.Profesor;

@RestController
@RequestMapping("/profesores")
public class ProfesorController {



    @Autowired
    private ProfesorInterface profesorInterface;

    @GetMapping
    public ResponseEntity<List<Profesor>> getProfesores() {
        List<Profesor> profesores = profesorInterface.findAll();
        return ResponseEntity.ok(profesores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Profesor> getProfesorById(@PathVariable int id) {
        Profesor profesor = profesorInterface.findById(id).orElse(null);
        if (profesor != null) {
            return ResponseEntity.ok(profesor);
        } else {
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<Profesor> addProfesor(@RequestBody Profesor profesor) {
        try {
            Profesor nuevoProfesor = profesorInterface.saveAndFlush(profesor);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProfesor);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); 
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Profesor> updateProfesor(@PathVariable int id, @RequestBody Profesor profesor) {
        try {
            profesor.setId(id);
           Profesor profesorActualizado = profesorInterface.saveAndFlush(profesor);
            return ResponseEntity.ok(profesorActualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Profesor> deleteProfesor(@PathVariable int id) {
       boolean exists = profesorInterface.existsById(id);
       if (!exists) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
       }
         profesorInterface.deleteById(id);
         profesorInterface.flush();
        return ResponseEntity.ok(null);
    }
    
    
}
