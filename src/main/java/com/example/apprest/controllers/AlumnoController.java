package com.example.apprest.controllers;


import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.password4j.Password;

import ch.qos.logback.core.subst.Token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.apprest.models.Alumno;
import com.example.apprest.models.SesionesAlumnos;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.example.apprest.interfaces.AlumnoInterface;
import com.example.apprest.services.DynamoService;
import com.example.apprest.services.S3Service;
import com.example.apprest.services.SNSService;

@RestController
@RequestMapping("/alumnos")
public class AlumnoController {


    @Autowired
    private AlumnoInterface alumnoInterface;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private SNSService snsService;

    @Autowired 
    private DynamoService dynamoService;

    @GetMapping
    public ResponseEntity<List<Alumno>> getAlumnos() {
        List<Alumno> alumnos = alumnoInterface.findAll();
        return ResponseEntity.ok(alumnos); 
    }
    @GetMapping("/{id}")
    public ResponseEntity<Alumno> getAlumnoById(@PathVariable int id) {
        Alumno alumno = alumnoInterface.findById(id).orElse(null);
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
        alumnoInterface.deleteById(id);
        boolean isDeleted = alumnoInterface.existsById(id);
       if (!isDeleted) {
           return ResponseEntity.ok(null);
       } else {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
       }
    }
   
    @PostMapping("/{id}/fotoPerfil")
    public ResponseEntity<Alumno> uploadFotoPerfil(@PathVariable int id, @RequestParam("fotoPerfil") MultipartFile fotoPerfilUrl) {
        Alumno alumno = alumnoInterface.findById(id).orElse(null);
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
        Alumno alumno = alumnoInterface.findById(id).orElse(null);
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

    @PostMapping("/{id}/session/login")
    public ResponseEntity<SesionesAlumnos> login(@PathVariable int id, @RequestBody Map<String, String> passwordJSON){
        Alumno alumnoDB = alumnoInterface.findById(id).orElse(null);
        if (alumnoDB == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        String password = passwordJSON.get("password");
            boolean isValidPassword = Password.check(password, alumnoDB.getPassword()).withArgon2();
            if (!isValidPassword) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }

            SesionesAlumnos sessionData = new SesionesAlumnos();
            sessionData.setId(UUID.randomUUID().toString());
            sessionData.setFecha(System.currentTimeMillis());
            sessionData.setAlumnoId(id);
            sessionData.setActive(true);
            sessionData.setSessionString(UUID.randomUUID().toString());


            boolean response = dynamoService.loginSession(sessionData);
            if(response){
                return ResponseEntity.ok(sessionData);
            }else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }

    
    }



    @PostMapping("/{id}/session/verify")
        public ResponseEntity<Alumno> verifySession(@PathVariable int id, @RequestBody Map<String, String> session){
            Alumno alumnoDB = alumnoInterface.findById(id).orElse(null);
            if (alumnoDB == null) {
               return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        String sessionString = session.get("sessionString");
    
        boolean response = dynamoService.verifySession(sessionString);
        if(response){
            return ResponseEntity.ok(alumnoDB);
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    
    }

    @PostMapping("/{id}/session/logout")
    public ResponseEntity<Alumno> logout(@PathVariable int id, @RequestBody Map<String, String> session){
        Alumno alumnoDB = alumnoInterface.findById(id).orElse(null);
        if (alumnoDB == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        String sessionString = session.get("sessionString");


        dynamoService.logoutSession(sessionString);
        return ResponseEntity.ok(alumnoDB);
    }

}

