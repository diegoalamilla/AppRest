package com.example.apprest.controllers;


import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.apprest.interfaces.ProfesorInterface;
import com.example.apprest.models.Profesor;
//import com.example.apprest.services.ProfesorService;

@RestController
@RequestMapping("/profesores")
public class ProfesorController {

    //@Autowired
   // private ProfesorService profesorService;

    @Autowired
    private ProfesorInterface profesorInterface;

    @GetMapping
    public ResponseEntity<List<Profesor>> getProfesores() {
        List<Profesor> profesores = profesorInterface.findAll();
        return ResponseEntity.ok(profesores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Profesor> getProfesorById(@PathVariable String id) {
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
             if (profesor.getId() == null || profesor.getId().isEmpty()) {
            profesor.setId(UUID.randomUUID().toString());
        }
            Profesor nuevoProfesor = profesorInterface.saveAndFlush(profesor);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProfesor);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); 
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Profesor> updateProfesor(@PathVariable String id, @RequestBody Profesor profesor) {
        try {
            profesor.setId(id);
           Profesor profesorActualizado = profesorInterface.saveAndFlush(profesor);
            return ResponseEntity.ok(profesorActualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Profesor> deleteProfesor(@PathVariable String id) {
        profesorInterface.deleteById(id);
        boolean isDeleted = profesorInterface.existsById(id);
        if (!isDeleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); 
        }
    }
    
    
}
