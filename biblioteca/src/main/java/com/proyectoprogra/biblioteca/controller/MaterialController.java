package com.proyectoprogra.biblioteca.controller;

import com.proyectoprogra.biblioteca.model.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.*;

import com.proyectoprogra.biblioteca.repository.MaterialRepository;

@RestController
@RequestMapping(value = "api/material", produces = "application/json")
public class MaterialController {
    private MaterialRepository _materialRepository;

    public MaterialController(MaterialRepository materialRepository)
    {
        _materialRepository = materialRepository;
    }

    @PostMapping("/registrar_material")
    public ResponseEntity<RegistrarMaterialResponse> RegistrarMaterial(@RequestBody RegistrarMaterialRequest request){
        
        return new ResponseEntity<RegistrarMaterialResponse>(HttpStatus.OK);
    }

    @PostMapping("/modificar_material")
    public ResponseEntity<ModificarMaterialResponse> ModificarMaterial(@RequestBody ModificarMaterialRequest request){
        return new ResponseEntity<ModificarMaterialResponse>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListarMaterialPorFiltrosResponse> ListarMaterialPorFiltros(@RequestBody ListarMaterialPorFiltrosRequest request){
        return new ResponseEntity<ListarMaterialPorFiltrosResponse>(HttpStatus.OK);
    }
}