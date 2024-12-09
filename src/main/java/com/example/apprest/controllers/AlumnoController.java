package com.example.apprest.controllers;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.password4j.Password;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.apprest.models.Alumno;
import com.example.apprest.models.SesionesAlumnos;
import com.example.apprest.repositories.AlumnoRepository;
import com.example.apprest.services.DynamoService;
import com.example.apprest.services.S3Service;
import com.example.apprest.services.SNSService;

@RestController
@RequestMapping("/alumnos")
public class AlumnoController {


    @Autowired
    private AlumnoRepository alumnoRepository;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private SNSService snsService;

    @Autowired 
    private DynamoService dynamoService;

    @GetMapping
    public ResponseEntity<List<Alumno>> getAlumnos() {
        List<Alumno> alumnos = alumnoRepository.findAll();
        return ResponseEntity.ok(alumnos); 
    }
    @GetMapping("/{id}")
    public ResponseEntity<Alumno> getAlumnoById(@PathVariable int id) {
        Alumno alumno = alumnoRepository.findById(id).orElse(null);
        if (alumno != null) {
            return ResponseEntity.ok(alumno);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<Alumno> addAlumno(@RequestBody Alumno alumno) {
        try {
            Alumno nuevoAlumno = alumnoRepository.saveAndFlush(alumno);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoAlumno); 
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); 
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Alumno> updateAlumno(@PathVariable int id, @RequestBody Alumno alumno) {
        try {
            alumno.setId(id);
           Alumno alumnoActualizado = alumnoRepository.saveAndFlush(alumno);
            return ResponseEntity.ok(alumnoActualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(alumno); 
        }
    }
   
    @DeleteMapping("/{id}")
    public ResponseEntity<Alumno> deleteAlumno(@PathVariable int id) {
        boolean exists = alumnoRepository.existsById(id);
        if (!exists) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        alumnoRepository.deleteById(id);
        alumnoRepository.flush();
        return ResponseEntity.ok(null);
      
    }
   
    @PostMapping("/{id}/fotoPerfil")
    public ResponseEntity<Alumno> uploadFotoPerfil(@PathVariable int id, @RequestParam("foto") MultipartFile fotoPerfilUrl) {
        Alumno alumno = alumnoRepository.findById(id).orElse(null);
        if (alumno != null) {
            String fotoPerfilUrlS3 = s3Service.uploadFile(fotoPerfilUrl, alumno.getMatricula()+"fotodeperfil" );
            alumno.setFotoPerfilUrl(fotoPerfilUrlS3);
            alumnoRepository.saveAndFlush(alumno);
            return ResponseEntity.ok(alumno);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(alumno);
        }
    }

    @PostMapping("/{id}/email")
    public ResponseEntity<Alumno> sendEmail(@PathVariable int id) {
        Alumno alumno = alumnoRepository.findById(id).orElse(null);
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
        Alumno alumnoDB = alumnoRepository.findById(id).orElse(null);
        if (alumnoDB == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        String password = passwordJSON.get("password");
            boolean isValidPassword = Password.check(password, alumnoDB.getPassword()).withArgon2();
            if (!isValidPassword) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            SesionesAlumnos sessionData = new SesionesAlumnos();
            sessionData.setId(UUID.randomUUID().toString());
            sessionData.setFecha(System.currentTimeMillis());
            sessionData.setAlumnoId(id);
            sessionData.setActive(true);
            sessionData.setSessionString(RandomStringUtils.randomAlphanumeric(128));          
            boolean response = dynamoService.loginSession(sessionData);
            if(response){
                return ResponseEntity.ok(sessionData);
            }else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(sessionData);
            }

    
    }



    @PostMapping("/{id}/session/verify") 
        public ResponseEntity<?> verifySession(@PathVariable int id, @RequestBody Map<String, String> session){
            Alumno alumnoDB = alumnoRepository.findById(id).orElse(null);
            if (alumnoDB == null) {
               return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        String sessionString = session.get("sessionString");
       if (sessionString == null || sessionString.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", 400);
        errorResponse.put("error", "Bad Request");
        errorResponse.put("message", "sessionString no es válido");
        errorResponse.put("path", "/alumnos/"+id+"/session/verify");
                
        SesionesAlumnos foundSesion = dynamoService.getSession(sessionString);
        if (foundSesion == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        boolean response = dynamoService.verifySession(sessionString);
        SesionesAlumnos sessionData = dynamoService.getSession(sessionString);
        if (response) {
            return ResponseEntity.ok(sessionData);
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sessionData);
        }
    
    }

    @PostMapping("/{id}/session/logout")
    public ResponseEntity<SesionesAlumnos> logout(@PathVariable int id, @RequestBody Map<String, String> session){
        Alumno alumnoDB = alumnoRepository.findById(id).orElse(null);
        if (alumnoDB == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        String sessionString = session.get("sessionString");
        dynamoService.logoutSession(sessionString);
        SesionesAlumnos sessionData = dynamoService.getSession(sessionString);
        return ResponseEntity.ok(sessionData);
    }

}

