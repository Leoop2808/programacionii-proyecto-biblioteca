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
import java.util.List;
import com.proyectoprogra.biblioteca.repository.MaterialRepository;

@RestController
@RequestMapping(value = "api/material", produces = "application/json")
public class MaterialController {
    private final MaterialRepository _materialRepository;

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

    @GetMapping("/listar_tipo_material")
    public ResponseEntity<ListarTipoMaterialResponse> ListarTipoMaterial(){
        ListarTipoMaterialResponse response = new ListarTipoMaterialResponse();
        try {
            List<Map<String, Object>> listaTiposMaterial = _materialRepository.ListarTipoMaterial();         
           
            if (listaTiposMaterial == null || listaTiposMaterial.size() <= 0) {
                response.codigo = 0;
                response.descripcion = "No se encontraron tipos de materiales";
            } else {
                response.listaTiposMaterial = new ArrayList<DetalleTipoMaterial>();
                for (Map<String,Object> itemIipoMaterial : listaTiposMaterial) {
                    DetalleTipoMaterial tipoMaterial = new DetalleTipoMaterial();
                    tipoMaterial.cod_tipo_material = itemIipoMaterial.get("cod_tipo_material").toString();
                    tipoMaterial.desc_tipo_material = itemIipoMaterial.get("desc_tipo_material").toString();
                    response.listaTiposMaterial.add(tipoMaterial);
                }
                response.codigo = 1;
                response.descripcion = "Los tipos de materiales se obtuvieron correctamente";
            }  
        } catch (Exception e) {
            response.codigo = -1;
            response.descripcion = "Error interno al obtener los tipos de materiales";
        }
        
        return  new ResponseEntity<ListarTipoMaterialResponse>(response, HttpStatus.OK);
    }

    @PostMapping("/registrar_tema")
    public ResponseEntity<RegistrarTemaResponse> RegistrarTema(@RequestBody RegistrarTemaRequest request){
        RegistrarTemaResponse response = new RegistrarTemaResponse();
        try {
            if (request == null) {
                response.codigo = 0;
                response.descripcion = "No se recibieron datos para registrar un nuevo tema";
            } else {
               Integer regTem = _materialRepository.RegistrarTema(request.cod_tema, request.desc_tema , true, false);       
               if (regTem == null || regTem <= 0) {
                    response.codigo = 0;
                    response.descripcion = "No se pudo completar el registro del nuevo tema";
               }else{
                    response.codigo = 1;
                    response.descripcion = "Tema registrado correctamente";
               }    
            } 
        } catch (Exception e) {
            response.codigo = -1;
            response.descripcion = "Error interno al registrar el tema";
        }
        
        return  new ResponseEntity<RegistrarTemaResponse>(response, HttpStatus.OK);
    }
}