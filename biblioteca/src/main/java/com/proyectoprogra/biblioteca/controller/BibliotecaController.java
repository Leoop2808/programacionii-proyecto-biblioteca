package com.proyectoprogra.biblioteca.controller;

import com.proyectoprogra.biblioteca.model.*;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/alumno")
public class BibliotecaController {
    
    @PostMapping("/registrar")
    public ResponseEntity<String> registrarLibro(){

        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Libro> findById(@PathVariable String id){
        return null;
    }

}