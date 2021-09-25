package com.proyectoprogra.biblioteca.model;

import java.util.List;

public class RegistrarMaterialRequest {
    public String isbn;
    public String autor;
    public String titulo;
    public Integer anio;
    public String cod_categoria_material;
    public String descripcion;
    public String editorial;
    public Integer num_paginas;
    public List<String> lista_temas;
    public String cod_tipo_material; 
}
