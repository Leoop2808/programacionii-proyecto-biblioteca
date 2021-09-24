package com.proyectoprogra.biblioteca.controller;

import com.proyectoprogra.biblioteca.model.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @GetMapping("/lista_materiales")
    public ResponseEntity<ListarMaterialPorFiltrosResponse> ListarMaterialPorFiltros(@RequestBody ListarMaterialPorFiltrosRequest request){
        return new ResponseEntity<ListarMaterialPorFiltrosResponse>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ObtenerDetalleMaterialResponse> ObtenerDetalleMaterial(@PathVariable int id){
        return new ResponseEntity<ObtenerDetalleMaterialResponse>(HttpStatus.OK);
    }

    @PostMapping("/registrar_prestamo_material")
    public ResponseEntity<RegistrarPretamoMaterialResponse> RegistrarPretamoMaterial(@RequestBody RegistrarPretamoMaterialRequest request){
        return new ResponseEntity<RegistrarPretamoMaterialResponse>(HttpStatus.OK);
    }

    @GetMapping("/obtener_reporte_indicaciones")
    public ResponseEntity<ObtenerReporteIndicadoresResponse> ObtenerReporteIndicadores(@RequestBody ObtenerReporteIndicadoresRequest request){
        return new ResponseEntity<ObtenerReporteIndicadoresResponse>(HttpStatus.OK);
    }

    @GetMapping("/obtener_reporte_prestamos")
    public ResponseEntity<ObtenerReportePrestamosResponse> ObtenerReportePrestamos(@RequestBody ObtenerReportePrestamosRequest request){
        return new ResponseEntity<ObtenerReportePrestamosResponse>(HttpStatus.OK);
    }

    @PutMapping("/registrar_devolucion_material")
    public ResponseEntity<RegistrarDevolucionMaterialResponse> RegistrarDevolucionMaterial(@PathVariable String codigo_prestamo){
        return new ResponseEntity<RegistrarDevolucionMaterialResponse>(HttpStatus.OK);
    }
}