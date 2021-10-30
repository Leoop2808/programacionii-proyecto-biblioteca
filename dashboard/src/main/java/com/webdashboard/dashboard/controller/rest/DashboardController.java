package com.webdashboard.dashboard.controller.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Version;
import java.net.http.HttpResponse.BodyHandlers;

import com.diduweiwu.processor.param.BodyProcessor;
import com.webdashboard.dashboard.model.IndicadoresResponse;

@RestController
@RequestMapping(value = "api/dashboard", produces = "application/json")
public class DashboardController {
    
    public DashboardController(){
        
    } 

    @GetMapping(value = "/indicadores", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IndicadoresResponse> ObtenerIndicadores(){
        IndicadoresResponse respuesta = new IndicadoresResponse();        
        try {
            var url = "https://programacionii.herokuapp.com/api/material/obtener_reporte_indicaciones";
            HttpRequest request = HttpRequest.newBuilder().uri(new URI(url)).GET().build();

            HttpClient httpClient = HttpClient.newHttpClient();
            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonObj = new JSONObject(response.body());

            if (jsonObj.isNull("codigo") || jsonObj.length() <= 0) {
                respuesta.codigo = 0;
                respuesta.descripcion = "Error al intentar obtener los indicadores";
            }
            else
            {
                respuesta.codigo = jsonObj.getInt("codigo");
                respuesta.descripcion = jsonObj.getString("descripcion");
                respuesta.cantidad_no_devueltos = jsonObj.getInt("cantidad_no_devueltos");
                respuesta.cantidad_prestamos = jsonObj.getInt("cantidad_prestamos");
                respuesta.cantidad_solicitantes = jsonObj.getInt("cantidad_solicitantes");
            }   
           
        } catch (Exception e) {
            respuesta.codigo = -1;
            respuesta.descripcion = "Error interno al intentar obtener los indicadores";
        }        
        
        return new ResponseEntity<IndicadoresResponse>(respuesta, HttpStatus.OK);     
    }
}

