package com.proyectoprogra.biblioteca.model;

import java.util.List;

public class RegistrarMaterialRequest {
    public String isbn;
    public String autor;
    public String titulo;
    public Integer anio;
    public Integer id_categoria_material;
    public String descripcion;
    public String editorial;
    public Integer num_paginas;
    public List<Integer> lista_temas;
    public Integer id_tipo_material; 
}
