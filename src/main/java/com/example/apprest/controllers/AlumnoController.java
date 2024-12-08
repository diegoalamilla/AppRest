package com.example.apprest.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.apprest.models.Alumno;
//import com.example.apprest.services.AlumnoService;
import com.example.apprest.interfaces.AlumnoInterface;
import com.example.apprest.services.S3Service;
import com.example.apprest.services.SNSService;

@RestController
@RequestMapping("/alumnos")
public class AlumnoController {

  
  //  @Autowired
  //  private AlumnoService alumnoService;

    @Autowired
    private AlumnoInterface alumnoInterface;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private SNSService snsService;

    @GetMapping
    public ResponseEntity<List<Alumno>> getAlumnos() {
        List<Alumno> alumnos = alumnoInterface.findAll();
        return ResponseEntity.ok(alumnos); 
    }
    @GetMapping("/{id}")
    public ResponseEntity<Alumno> getAlumnoById(@PathVariable int id) {
        Alumno alumno = alumnoInterface.findById(String.valueOf(id)).orElse(null);
        if (alumno != null) {
            return ResponseEntity.ok(alumno);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<Alumno> addAlumno(@RequestBody Alumno alumno) {
        try {
        
            Alumno nuevoAlumno = alumnoInterface.saveAndFlush(alumno);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoAlumno); 
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); 
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Alumno> updateAlumno(@PathVariable int id, @RequestBody Alumno alumno) {
        try {
            alumno.setId(id);
           Alumno alumnoActualizado = alumnoInterface.saveAndFlush(alumno);
            return ResponseEntity.ok(alumnoActualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(alumno); 
        }
    }
   
    @DeleteMapping("/{id}")
    public ResponseEntity<Alumno> deleteAlumno(@PathVariable int id) {
        alumnoInterface.deleteById(String.valueOf(id));
        boolean isDeleted = alumnoInterface.existsById(String.valueOf(id));
       if (!isDeleted) {
           return ResponseEntity.ok(null);
       } else {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
       }
    }
   
    @PostMapping("/{id}/fotoPerfil")
    public ResponseEntity<Alumno> uploadFotoPerfil(@PathVariable int id, @RequestParam("fotoPerfil") MultipartFile fotoPerfilUrl) {
        Alumno alumno = alumnoInterface.findById(String.valueOf(id)).orElse(null);
        if (alumno != null) {
            String fotoPerfilUrlS3 = s3Service.uploadFile(fotoPerfilUrl, alumno.getMatricula()+"fotodeperfil" );
            alumno.setFotoPerfilUrl(fotoPerfilUrlS3);
            alumnoInterface.saveAndFlush(alumno);
            return ResponseEntity.ok(alumno);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/{id}/email")
    public ResponseEntity<Alumno> sendEmail(@PathVariable int id) {
        Alumno alumno = alumnoInterface.findById(String.valueOf(id)).orElse(null);
        if (alumno != null) {
            boolean emailSent = snsService.sendEmail("La calificación de " + alumno.getNombres() + " " + alumno.getApellidos() + " es "+ alumno.getPromedio(), "Calificación de Alumno");
            if (emailSent) {
                return ResponseEntity.ok(alumno);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }
}
