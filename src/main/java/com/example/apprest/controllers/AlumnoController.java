package com.example.apprest.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.apprest.models.Alumno;
import com.example.apprest.services.AlumnoService;
import com.example.apprest.services.S3Service;

@RestController
@RequestMapping("/alumnos")
public class AlumnoController {

    @Autowired
    private AlumnoService alumnoService;

    @Autowired
    private S3Service s3Service;

    @GetMapping
    public ResponseEntity<List<Alumno>> getAlumnos() {
        List<Alumno> alumnos = alumnoService.getAlumnos();
        return ResponseEntity.ok(alumnos); 
    }
    @GetMapping("/{id}")
    public ResponseEntity<Alumno> getAlumnoById(@PathVariable String id) {
        Alumno alumno = alumnoService.getAlumnoById(id);
        System.out.println(alumno);
        if (alumno != null) {
            return ResponseEntity.ok(alumno);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    

    @PostMapping
    public ResponseEntity<Alumno> addAlumno(@RequestBody Alumno alumno) {
        try {
        if (alumno.getId() == null || alumno.getId().isEmpty()) {
            alumno.setId(UUID.randomUUID().toString());
            System.out.println(alumno.getId()); // Genera el ID si no está presente
        }
            Alumno nuevoAlumno = alumnoService.addAlumno(alumno);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoAlumno); 
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); 
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Alumno> updateAlumno(@PathVariable String id, @RequestBody Alumno alumno) {
        try {
           Alumno alumnoActualizado = alumnoService.updateAlumno(alumno);
            return ResponseEntity.ok(alumnoActualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(alumno); 
        }
    }
   
    @DeleteMapping("/{id}")
    public ResponseEntity<Alumno> deleteAlumno(@PathVariable String id) {
        boolean isDeleted = alumnoService.deleteAlumno(id);
       if (isDeleted) {
           return ResponseEntity.ok(null);
       } else {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
       }
    }
   
    @PostMapping("/{id}/fotoPerfil")
    public ResponseEntity<Alumno> uploadFotoPerfil(@PathVariable String id, @RequestParam("fotoPerfil") MultipartFile fotoPerfilUrl) {
        Alumno alumno = alumnoService.getAlumnoById(id);
        if (alumno != null) {
            String fotoPerfilUrlS3 = s3Service.uploadFile(fotoPerfilUrl, alumno.getMatricula()+"fotodeperfil" );
            alumno.setFotoPerfilUrl(fotoPerfilUrlS3);
            return ResponseEntity.ok(alumno);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
