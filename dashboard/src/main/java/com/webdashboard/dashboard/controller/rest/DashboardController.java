package com.webdashboard.dashboard.controller.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
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
        String strRequest = "{'fecha_fin':'','fecha_inicio':''}";
        
        try {
            var url = "https://programacionii.herokuapp.com/api/material/obtener_reporte_indicaciones";
            HttpRequest request = HttpRequest.newBuilder().uri(new URI(url)).POST(HttpRequest.BodyPublishers.ofString(strRequest)).build();

            HttpClient httpClient = HttpClient.newHttpClient();
            var response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            String resprueba = response.toString();
            respuesta.codigo = 1;
            respuesta.descripcion = resprueba;
        } catch (Exception e) {
            respuesta.codigo = -1;
            respuesta.descripcion = "Error interno al intentar obtener los indicadores";
        }        
        
        return new ResponseEntity<IndicadoresResponse>(respuesta, HttpStatus.OK);     
    }
}

