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
        RegistrarMaterialResponse response = new RegistrarMaterialResponse();
        try {
            ValidarGeneral resVal = ValidarParametrosObligatoriosRegistroMaterial(request); 
            if (resVal.codigo != 1) {
                response.codigo = resVal.codigo;
                response.descripcion = resVal.descripcion;
                return  new ResponseEntity<RegistrarMaterialResponse>(response, HttpStatus.OK);
            }

            Integer id_categoria_material = _materialRepository.ObtenerIdCategoriaMaterialPorCodigo(request.cod_categoria_material);
            if (id_categoria_material == null || id_categoria_material <= 0) {
                response.codigo = 0;
                response.descripcion = "No se pudo identificar la categoria del material. La categoria recibida no existe o no se encuentra disponible.";
                return  new ResponseEntity<RegistrarMaterialResponse>(response, HttpStatus.OK);
            } 

            List<Integer> listaTemas = _materialRepository.ObtenerListaIdentificadoresTemas(request.lista_temas);  
            if (listaTemas == null || listaTemas.size() <= 0)
            {
                response.codigo = 0;
                response.descripcion = "No se pudo identificar los temas o no se encuentran disponibles.";
                return  new ResponseEntity<RegistrarMaterialResponse>(response, HttpStatus.OK);
            }

            Integer id_tipo_material = _materialRepository.ObtenerIdTipoMaterialPorCodigo(request.cod_tipo_material);
            if (id_tipo_material == null || id_tipo_material <= 0) {
                response.codigo = 0;
                response.descripcion = "No se pudo identificar el tipo de material. El tipo recibido no existe o no se encuentra disponible.";
                return  new ResponseEntity<RegistrarMaterialResponse>(response, HttpStatus.OK);
            } 
            
            Integer idNuevoMaterial = _materialRepository.RegistrarMaterial(id_tipo_material,id_categoria_material,request.isbn,
            request.autor, request.titulo, request.anio, request.descripcion, request.editorial, request.num_paginas,
            true, true, false);       
            if (idNuevoMaterial == null || idNuevoMaterial <= 0) {
                 response.codigo = 0;
                 response.descripcion = "No se pudo completar el registro del material";
            }else{
                String codigoMaterial = idNuevoMaterial.toString();
                Integer resUpdMat = _materialRepository.RegistrarCodigoMaterial(codigoMaterial, idNuevoMaterial);
                for (Integer idTema : listaTemas) {
                    Integer regTemaMat = _materialRepository.RegistrarTemaMaterial(idNuevoMaterial, idTema, true, false);  
                }
                response.codigo = 1;   
                response.descripcion = "Material registrado correctamente";
                response.codigo_material = codigoMaterial;
            }              
        } catch (Exception e) {
            response.codigo = -1;
            response.descripcion = "Error interno al registrar un nuevo material";
        }
        
        return  new ResponseEntity<RegistrarMaterialResponse>(response, HttpStatus.OK);
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

    @GetMapping("/listar_maestros")
    public ResponseEntity<ListarMaestrosResponse> ListarMaestros(){
        ListarMaestrosResponse response = new ListarMaestrosResponse();
        response.codigo = 2;
        try {
            List<Map<String, Object>> listaTiposMaterial = _materialRepository.ListarTipoMaterial();         
            List<Map<String, Object>> listaCategoriasMaterial = _materialRepository.ListarCategoriasMaterial();  
            List<Map<String, Object>> listaTemas = _materialRepository.ListarTemas(); 
             
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
            }  

            if (listaCategoriasMaterial == null || listaCategoriasMaterial.size() <= 0) {
                response.codigo = 0;
                if (IsNullOrEmpty(response.descripcion)) {
                    response.descripcion = "No se encontraron categorias de material";
                } else {
                    response.descripcion = response.descripcion + ". No se encontraron categorias de material";
                }
            } else {
                response.listaCategoriasMaterial = new ArrayList<DetalleCategoriaMaterial>();
                for (Map<String,Object> itemCategoriaMaterial : listaCategoriasMaterial) {
                    DetalleCategoriaMaterial categoriaMaterial = new DetalleCategoriaMaterial();
                    categoriaMaterial.cod_categoria_material = itemCategoriaMaterial.get("cod_categoria_material").toString();
                    categoriaMaterial.desc_categoria_material = itemCategoriaMaterial.get("desc_categoria_material").toString();
                    response.listaCategoriasMaterial.add(categoriaMaterial);
                }                
            }  

            if (listaTemas == null || listaTemas.size() <= 0) {
                response.codigo = 0;               
                if (IsNullOrEmpty(response.descripcion)) {
                    response.descripcion = "No se encontraron temas";
                } else {
                    response.descripcion = response.descripcion + ". No se encontraron temas";
                }
            } else {
                response.listaTemas = new ArrayList<DetalleTema>();
                for (Map<String,Object> itemTema : listaTemas) {
                    DetalleTema tema = new DetalleTema();
                    tema.cod_tema = itemTema.get("cod_tema").toString();
                    tema.desc_tema = itemTema.get("desc_tema").toString();
                    response.listaTemas.add(tema);
                }                
            }  

            if (response.codigo == 2) {
                response.codigo = 1;
                response.descripcion = "Los datos maestros se obtuvieron correctamente";
            }           
        } catch (Exception e) {
            response.codigo = -1;
            response.descripcion = "Error interno al obtener lo datos maestros";
        }
        
        return  new ResponseEntity<ListarMaestrosResponse>(response, HttpStatus.OK);
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

    private boolean IsNullOrEmpty(String descripcion) {
        return descripcion == null || descripcion == "";
    }

    private String JoinListaString(List<String> listaString, String caracterConcat)
    {
        String concat = "";
        Integer cant = listaString.size();
        Integer cont = 1;

        for (String cod_tema : listaString) {
            if (cont == cant) {
                concat = concat + cod_tema;
            }else{
                concat = concat + cod_tema + caracterConcat;
            }                
            cont++;
        }    
        return concat;
    }

    //#region Validacion parametros obligatorios para el registro de un nuevo material
    private ValidarGeneral ValidarParametrosObligatoriosRegistroMaterial(RegistrarMaterialRequest request)
    {
        ValidarGeneral response = new ValidarGeneral();

        if (IsNullOrEmpty(request.isbn)) {
            response.codigo = 0;
            response.descripcion = "Debe enviar el codigo ISBN";
            return response;
        } 

        if (IsNullOrEmpty(request.autor)) {
            response.codigo = 0;
            response.descripcion = "Debe enviar el nombre del autor";
            return response;
        } 

        if (IsNullOrEmpty(request.titulo)) {
            response.codigo = 0;
            response.descripcion = "Debe enviar el titulo del material";
            return response;
        }

        if (request.anio == null || request.anio <= 0) {
            response.codigo = 0;
            response.descripcion = "Debe enviar el año de publicación";
            return response;
        }

        if (IsNullOrEmpty(request.cod_categoria_material)) {
            response.codigo = 0;
            response.descripcion = "Debe enviar el codigo de la categoria del material";
            return response;
        }

        if (IsNullOrEmpty(request.descripcion)) {
            response.codigo = 0;
            response.descripcion = "Debe enviar la descripción para el material";
            return response;
        }

        if (IsNullOrEmpty(request.editorial)) {
            response.codigo = 0;
            response.descripcion = "Debe enviar el nombre de la editorial";
            return response;
        }

        if (request.num_paginas == null || request.num_paginas <= 0) {
            response.codigo = 0;
            response.descripcion = "Debe enviar el número de paginas";
            return response;
        }

        if (request.lista_temas == null || request.lista_temas.size() <= 0) {
            response.codigo = 0;
            response.descripcion = "Debe enviar los temas involucrados en el material";
            return response;
        }

        if (IsNullOrEmpty(request.cod_tipo_material)) {
            response.codigo = 0;
            response.descripcion = "Debe enviar el codigo del tipo de material";
            return response;
        }

        response.codigo = 1;
        response.descripcion = "Datos validados correctamente";
        return response;
    }
    //#endregion
}