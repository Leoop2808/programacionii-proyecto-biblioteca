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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
            request.autor, request.titulo, request.anio, request.descripcion, request.editorial, request.num_paginas);       
            if (idNuevoMaterial == null || idNuevoMaterial <= 0) {
                 response.codigo = 0;
                 response.descripcion = "No se pudo completar el registro del material";
            }else{
                DecimalFormat df = new DecimalFormat("00000000");
                String codigoMaterial = df.format(idNuevoMaterial);
                Integer resUpdMat = _materialRepository.RegistrarCodigoMaterial(codigoMaterial, idNuevoMaterial);
                for (Integer idTema : listaTemas) {
                    Integer regTemaMat = _materialRepository.RegistrarTemaMaterial(idNuevoMaterial, idTema);  
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

    @PutMapping("/modificar_material/{id}")
    public ResponseEntity<ModificarMaterialResponse> ModificarMaterial(@PathVariable int id, @RequestBody ModificarMaterialRequest request){
        ModificarMaterialResponse response = new ModificarMaterialResponse();
        try {
            ValidarGeneral resVal = ValidarParametrosObligatoriosEdicionMaterial(request); 
            if (resVal.codigo != 1) {
                response.codigo = resVal.codigo;
                response.descripcion = resVal.descripcion;
                return  new ResponseEntity<ModificarMaterialResponse>(response, HttpStatus.OK);
            }

            Integer resUpdMat = _materialRepository.ModificarMaterial(id, request.autor, request.titulo, 
            request.anio, request.descripcion, request.editorial, request.num_paginas); 
            if (resUpdMat == null || resUpdMat <= 0) {
                response.codigo = 0;
                response.descripcion = "No se pudo completar la edición del material";
           }else{
               response.codigo = 1;   
               response.descripcion = "Material editado correctamente";
           }  
        } catch (Exception e) {
            response.codigo = -1;
            response.descripcion = "Error interno al modificar el material";
        }
        return  new ResponseEntity<ModificarMaterialResponse>(response, HttpStatus.OK);
    }

    @GetMapping("/lista_materiales")
    public ResponseEntity<ListarMaterialPorFiltrosResponse> ListarMaterialPorFiltros(@RequestBody ListarMaterialPorFiltrosRequest request){
        ListarMaterialPorFiltrosResponse response = new ListarMaterialPorFiltrosResponse();
        try {
            String listaFiltros = "";
            String ordenListado = "";
            if (!IsNullOrEmpty(request.autor)) {
                    listaFiltros = " ma.autor like '%" + request.autor.trim() + "%' ";
            } 

            if (!IsNullOrEmpty(request.titulo)) {
                if (IsNullOrEmpty(listaFiltros)) {
                    listaFiltros = " ma.titulo like '%" + request.titulo + "%' ";
                }else{
                    listaFiltros = listaFiltros + " and ma.titulo like '%" + request.titulo + "%' ";
                }
            } 

            if ((request.anio_inicio != null && request.anio_inicio > 0) &&
                (request.anio_fin == null && request.anio_fin <= 0)) {
                if (IsNullOrEmpty(listaFiltros)) {
                    listaFiltros = " anio :: int >= " + request.anio_inicio.toString() + " ";
                }else{
                    listaFiltros = listaFiltros + " and anio :: int >= " + request.anio_inicio.toString() + " ";
                }
            }

            if ((request.anio_fin != null && request.anio_fin > 0) &&
                (request.anio_inicio == null && request.anio_inicio <= 0)) {
                if (IsNullOrEmpty(listaFiltros)) {
                    listaFiltros = " anio :: int <= " + request.anio_fin.toString() + " ";
                }else{
                    listaFiltros = listaFiltros + " and anio :: int <= " + request.anio_fin.toString() + " ";
                }
            }

            if ((request.anio_fin != null && request.anio_fin > 0) &&
                (request.anio_inicio !=  null && request.anio_inicio > 0)) {
                if (IsNullOrEmpty(listaFiltros)) {
                    listaFiltros = " anio :: int between " + request.anio_inicio.toString() + " and " + request.anio_fin.toString() + " ";
                }else{
                    listaFiltros = listaFiltros + " and anio :: int between " + request.anio_inicio.toString() + " and " + request.anio_fin.toString() + " ";
                }
            }

            if (!IsNullOrEmpty(request.cod_tipo_material)) {
                if (IsNullOrEmpty(listaFiltros)) {
                    listaFiltros = " tipma.cod_tipo_material = '" + request.cod_tipo_material + "' ";
                }else{
                    listaFiltros = listaFiltros + " and tipma.cod_tipo_material = '" + request.cod_tipo_material + "' ";
                }
            } 

            if (!IsNullOrEmpty(request.cod_categoria_material)) {
                if (IsNullOrEmpty(listaFiltros)) {
                    listaFiltros = " cm.cod_categoria_material = '" + request.cod_categoria_material + "' ";
                }else{
                    listaFiltros = listaFiltros + " and cm.cod_categoria_material = '" + request.cod_categoria_material + "' ";
                }
            } 

            if (!IsNullOrEmpty(request.editorial)) {
                if (IsNullOrEmpty(listaFiltros)) {
                    listaFiltros = " ma.editorial like '%" + request.editorial + "%' ";
                }else{
                    listaFiltros = listaFiltros + " and ma.editorial like '%" + request.editorial + "%' ";
                }
            } 

            if (request.flg_orden_alfabetico) {
                ordenListado = " mt.titulo,";
            }   

            List<Map<String, Object>> listaMateriales = _materialRepository.ListarMaterialPorFiltros();         
            if (listaMateriales == null || listaMateriales.size() <= 0) {
                response.codigo = 0;
                response.descripcion = "No se encontraron materiales";
            } else {
                response.lista_materiales = new ArrayList<DataMaterial>();
                for (Map<String,Object> itemMaterial : listaMateriales) {
                    DataMaterial material = new DataMaterial();
                    material.id_material = Integer.parseInt(itemMaterial.get("id_material").toString());
                    material.titulo = itemMaterial.get("titulo").toString();
                    material.autor = itemMaterial.get("autor").toString();
                    material.editorial = itemMaterial.get("editorial").toString();
                    material.anio = Integer.parseInt(itemMaterial.get("anio").toString());
                    material.categoria = itemMaterial.get("categoria").toString();
                    material.tipo_material = itemMaterial.get("tipo_material").toString();
                    material.fecha_registro = itemMaterial.get("fecha_registro").toString();
                    response.lista_materiales.add(material);
                } 
                response.codigo = 1;
                response.descripcion = "Materiales obtenidos correctamente";               
            }  
        } catch (Exception e) {
            response.codigo = -1;
            response.descripcion = "Error interno al listar los materiales";
        }        

        return  new ResponseEntity<ListarMaterialPorFiltrosResponse>(response, HttpStatus.OK);
    }

    @GetMapping("/obtener_detalle_material/{id}")
    public ResponseEntity<ObtenerDetalleMaterialResponse> ObtenerDetalleMaterial(@PathVariable int id){
        ObtenerDetalleMaterialResponse response = new ObtenerDetalleMaterialResponse();
        try {
            Map<String, Object> DetalleMaterial = _materialRepository.ObtenerDetalleMaterial(id);   
            if (DetalleMaterial == null) {
                response.codigo = 0;
                response.descripcion = "No se encontraron datos del material";
                return  new ResponseEntity<ObtenerDetalleMaterialResponse>(response, HttpStatus.OK);
            }  
            response.id_material = Integer.parseInt(DetalleMaterial.get("id_material").toString());
            response.codigo_material = DetalleMaterial.get("cod_material").toString();
            response.isbn = DetalleMaterial.get("isbn").toString();
            response.titulo = DetalleMaterial.get("titulo").toString();
            response.autor = DetalleMaterial.get("autor").toString();
            response.editorial = DetalleMaterial.get("editorial").toString();
            response.anio = DetalleMaterial.get("anio").toString();
            response.descripcion_material = DetalleMaterial.get("descripcion_material").toString();
            response.num_paginas = Integer.parseInt(DetalleMaterial.get("num_paginas").toString());
            response.categoria = DetalleMaterial.get("categoria").toString();
            response.tipo_material = DetalleMaterial.get("tipo_material").toString();   

            List<Map<String, Object>> listaTemas = _materialRepository.ObtenerTemasPorMaterial(id);  
            if (listaTemas == null || listaTemas.size() <= 0) {
                response.codigo = 1;
                response.descripcion = "No se encontraron temas";
            } else {
                response.lista_temas = new ArrayList<DataTema>();
                for (Map<String,Object> itemTema : listaTemas) {
                    DataTema tema = new DataTema();
                    tema.cod_tema = itemTema.get("cod_tema").toString();
                    tema.desc_tema = itemTema.get("desc_tema").toString();
                    response.lista_temas.add(tema);
                } 
                
                response.codigo = 1;
                response.descripcion = "Detalle del material obtenido correctamente";
            }  
        } catch (Exception e) {
            response.codigo = -1;
            response.descripcion = "Error interno al obtener el detalle del material";
        }

        return  new ResponseEntity<ObtenerDetalleMaterialResponse>(response, HttpStatus.OK);
    }

    @PostMapping("/registrar_prestamo_material")
    public ResponseEntity<RegistrarPretamoMaterialResponse> RegistrarPretamoMaterial(@RequestBody RegistrarPretamoMaterialRequest request){
        RegistrarPretamoMaterialResponse response = new RegistrarPretamoMaterialResponse();
        try {
            ValidarGeneral resVal = ValidarParametrosObligatoriosRegistroPrestamo(request); 
            request.cod_material = request.cod_material.trim();
            if (resVal.codigo != 1) {
                response.codigo = resVal.codigo;
                response.descripcion = resVal.descripcion;
                return  new ResponseEntity<RegistrarPretamoMaterialResponse>(response, HttpStatus.OK);
            }

            Integer id_material = _materialRepository.ObtenerIdMaterialPorCodigo(request.cod_material);
            if (id_material == null || id_material <= 0) {
                response.codigo = 0;
                response.descripcion = "No se pudo identificar el material. No existe o no se encuentra disponible.";
                return  new ResponseEntity<RegistrarPretamoMaterialResponse>(response, HttpStatus.OK);
            } 

            Boolean flg_disponibilidad = _materialRepository.ObtenerDisponbilidadMaterial(id_material);
            if (!flg_disponibilidad) {
                response.codigo = 0;
                response.descripcion = "El material ya fue prestado y aun no se encuentra disponible para prestamos.";
                return  new ResponseEntity<RegistrarPretamoMaterialResponse>(response, HttpStatus.OK);
            }

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, request.dias_prestamo);
            Date fecha_pactada_devolucion = calendar.getTime();

            Integer idNuevoPrestamo = _materialRepository.RegistrarPrestamoMaterial(id_material,request.nombre_solicitante, 
            request.dni_solicitante, request.correo_solicitante, request.telefono_solicitante, request.nombre_prestador, 
            request.dias_prestamo, fecha_pactada_devolucion);  

            if (idNuevoPrestamo == null || idNuevoPrestamo <= 0) {
                 response.codigo = 0;
                 response.descripcion = "No se pudo completar el registro del pretamo";
            }else{
                DecimalFormat df = new DecimalFormat("00000000");
                String codigoPrestamo = df.format(idNuevoPrestamo);
                Integer resUpdPre = _materialRepository.RegistrarCodigoPrestamoMaterial(codigoPrestamo, idNuevoPrestamo);
                Integer resUpdMat = _materialRepository.ActualizarDisponibilidadMaterial(id_material, false);
                response.codigo = 1;   
                response.descripcion = "Prestamo de material registrado correctamente";
                response.codigo_prestamo = codigoPrestamo;
                SimpleDateFormat objSDF = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss");
                response.fecha_devolucion = objSDF.format(fecha_pactada_devolucion).toString();
            }  
        } catch (Exception e) {
            response.codigo = -1;
            response.descripcion = "Error interno al registrar el prestamo de un material" + e.getMessage();
        }
        return  new ResponseEntity<RegistrarPretamoMaterialResponse>(response, HttpStatus.OK);
    }

    @PutMapping("/registrar_devolucion_material/{codigo_prestamo}")
    public ResponseEntity<RegistrarDevolucionMaterialResponse> RegistrarDevolucionMaterial(@PathVariable String codigo_prestamo){
        RegistrarDevolucionMaterialResponse response = new RegistrarDevolucionMaterialResponse();
        try {
            Integer valExis = _materialRepository.ValidarExistenciaPrestamo(codigo_prestamo);
            if (valExis == null || valExis != 1) {
                response.codigo = 0;
                response.descripcion = "No se pudo identificar el prestamo. No existe o ya se registró la devolución";
                return  new ResponseEntity<RegistrarDevolucionMaterialResponse>(response, HttpStatus.OK);
            }

            Integer id_material = _materialRepository.ObtenerMaterialPrestado(codigo_prestamo);
            if (id_material == null || id_material <= 0) {
                response.codigo = 0;
                response.descripcion = "No se pudo identificar el material prestado";
                return  new ResponseEntity<RegistrarDevolucionMaterialResponse>(response, HttpStatus.OK);
            }

            Integer resRegDev = _materialRepository.RegistrarDevolucionMaterial(codigo_prestamo);
            if (resRegDev == null || resRegDev <= 0) {
                response.codigo = 0;
                response.descripcion = "No se pudo registrar la devolución del material";
                return  new ResponseEntity<RegistrarDevolucionMaterialResponse>(response, HttpStatus.OK);
            }

            Integer resUpdMat = _materialRepository.ActualizarDisponibilidadMaterial(id_material, true);

            response.codigo = 1;
            response.descripcion = "Devolución de material registrada correctamente";
        } catch (Exception e) {
            response.codigo = -1;
            response.descripcion = "Error interno al registrar la devolución del material";
        }
        return  new ResponseEntity<RegistrarDevolucionMaterialResponse>(response, HttpStatus.OK);
    }

    @GetMapping("/obtener_reporte_indicaciones")
    public ResponseEntity<ObtenerReporteIndicadoresResponse> ObtenerReporteIndicadores(@RequestBody ObtenerReporteIndicadoresRequest request){
        ObtenerReporteIndicadoresResponse response = new ObtenerReporteIndicadoresResponse();
        try {
            Integer cantidad_prestamos = _materialRepository.ObtenerCantidadPrestamos();
            if (cantidad_prestamos == null || cantidad_prestamos < 0) {
                response.codigo = 0;
                response.descripcion = "No se pudo obtener la cantidad de prestamos";
                return  new ResponseEntity<ObtenerReporteIndicadoresResponse>(response, HttpStatus.OK);
            }

            Integer cantidad_no_devueltos = _materialRepository.ObtenerCantidadNoDevueltos();
            if (cantidad_no_devueltos == null || cantidad_no_devueltos < 0) {
                response.codigo = 0;
                response.descripcion = "No se pudo obtener la cantidad de no devueltos";
                return  new ResponseEntity<ObtenerReporteIndicadoresResponse>(response, HttpStatus.OK);
            }

            Integer cantidad_solicitantes = _materialRepository.ObtenerCantidadSolicitantes();
            if (cantidad_solicitantes == null || cantidad_solicitantes < 0) {
                response.codigo = 0;
                response.descripcion = "No se pudo obtener la cantidad de solicitantes";
                return  new ResponseEntity<ObtenerReporteIndicadoresResponse>(response, HttpStatus.OK);
            }

            response.cantidad_prestamos = cantidad_prestamos;
            response.cantidad_no_devueltos = cantidad_no_devueltos;
            response.cantidad_solicitantes = cantidad_solicitantes;
            response.codigo = 1;
            response.descripcion = "Indicadores obtenidos correctamente";

        } catch (Exception e) {
            response.codigo = -1;
            response.descripcion = "Error interno al obtener reporte de indicadores";
        }
        return  new ResponseEntity<ObtenerReporteIndicadoresResponse>(response, HttpStatus.OK);
    }

    @GetMapping("/obtener_reporte_prestamos")
    public ResponseEntity<ObtenerReportePrestamosResponse> ObtenerReportePrestamos(@RequestBody ObtenerReportePrestamosRequest request){
        ObtenerReportePrestamosResponse response = new ObtenerReportePrestamosResponse();
        try {
            List<Map<String, Object>> listaReportePrestamos = _materialRepository.ObtenerReportePrestamos();         
            if (listaReportePrestamos == null || listaReportePrestamos.size() <= 0) {
                response.codigo = 0;
                response.descripcion = "No se encontraron registros de prestamos";
            } else {
                response.lista_prestamos = new ArrayList<DataPrestamo>();
                for (Map<String,Object> itemMaterial : listaReportePrestamos) {
                    DataPrestamo prestamo = new DataPrestamo();
                    prestamo.nombre_solicitante = itemMaterial.get("nombre_solicitante").toString();
                    prestamo.nombre_prestador = itemMaterial.get("nombre_prestador").toString();
                    prestamo.cod_material = itemMaterial.get("cod_material").toString();
                    prestamo.isbn = itemMaterial.get("isbn").toString();
                    prestamo.titulo_material = itemMaterial.get("titulo_material").toString();
                    prestamo.fecha_prestamo = itemMaterial.get("fecha_prestamo").toString();
                    prestamo.fecha_pactada_devolucion = itemMaterial.get("fecha_pactada_devolucion").toString();
                    prestamo.fecha_devolucion = itemMaterial.get("fecha_devolucion") == null ? "" : itemMaterial.get("fecha_devolucion").toString();
                    prestamo.correo_solicitante = itemMaterial.get("correo_solicitante") == null ? "" : itemMaterial.get("correo_solicitante").toString();
                    prestamo.telefono_solicitante = itemMaterial.get("telefono_solicitante") == null ? "" : itemMaterial.get("telefono_solicitante").toString();
                    response.lista_prestamos.add(prestamo);
                } 
                response.codigo = 1;
                response.descripcion = "Reporte de prestamos obtenido correctamente";               
            }  
        } catch (Exception e) {
            response.codigo = -1;
            response.descripcion = "Error interno al obtener reporte de prestamos";
        }
        return  new ResponseEntity<ObtenerReportePrestamosResponse>(response, HttpStatus.OK);
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
               Integer regTem = _materialRepository.RegistrarTema(request.cod_tema, request.desc_tema);       
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

    //#region Utilidades
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
    //#endregion

    //#region Validacion parametros obligatorios para el registro de prestamo
    public ValidarGeneral ValidarParametrosObligatoriosRegistroPrestamo(RegistrarPretamoMaterialRequest request){
        ValidarGeneral response = new ValidarGeneral();
        if (IsNullOrEmpty(request.cod_material)) {
            response.codigo = 0;
            response.descripcion = "Debe enviar el codigo del material";
            return response;
        } 

        if (IsNullOrEmpty(request.nombre_solicitante)) {
            response.codigo = 0;
            response.descripcion = "Debe enviar el nombre del solicitante";
            return response;
        }

        if (IsNullOrEmpty(request.dni_solicitante)) {
            response.codigo = 0;
            response.descripcion = "Debe enviar el dni del solicitante";
            return response;
        }

        if (IsNullOrEmpty(request.correo_solicitante)) {
            response.codigo = 0;
            response.descripcion = "Debe enviar el correo del solicitante";
            return response;
        }

        if (IsNullOrEmpty(request.telefono_solicitante)) {
            response.codigo = 0;
            response.descripcion = "Debe enviar el nombre del solicitante";
            return response;
        }

        if (IsNullOrEmpty(request.nombre_prestador)) {
            response.codigo = 0;
            response.descripcion = "Debe enviar el nombre del prestador";
            return response;
        }

        if (request.dias_prestamo == null || request.dias_prestamo <= 0) {
            response.codigo = 0;
            response.descripcion = "Debe enviar la cantidad de dias del prestamo";
            return response;
        }

        response.codigo = 1;
        response.descripcion = "Datos validados correctamente";
        return response;
    }
    //#endregion

    //#region Validacion parametros obligatorios para la edicion de un material
    private ValidarGeneral ValidarParametrosObligatoriosEdicionMaterial(ModificarMaterialRequest request)
    {
        ValidarGeneral response = new ValidarGeneral();
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

        response.codigo = 1;
        response.descripcion = "Datos validados correctamente";
        return response;
    }
    //#endregion

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