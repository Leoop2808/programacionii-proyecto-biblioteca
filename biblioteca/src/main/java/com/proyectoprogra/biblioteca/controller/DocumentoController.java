package com.proyectoprogra.biblioteca.controller;

import com.proyectoprogra.biblioteca.model.Documento;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/documento")
public class DocumentoController{

    @PostMapping("/registrar")
    public ResponseEntity<String> registrarDocumento(@RequestBody Documento request){
        
        return new ResponseEntity<String>(request.getId(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Documento> findById(@PathVariable String id){
        return new ResponseEntity<Documento>(HttpStatus.NOT_FOUND);
    }

}
