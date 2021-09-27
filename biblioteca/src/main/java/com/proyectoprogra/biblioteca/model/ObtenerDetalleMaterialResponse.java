package com.proyectoprogra.biblioteca.model;
import java.util.List;
public class ObtenerDetalleMaterialResponse extends GlobalResponse{
    public int id_material;
    public String codigo_material;
    public String isbn;
    public String titulo;
    public String editorial;
    public String descripcion_material;
    public String anio;
    public String autor;
    public int num_paginas;
    public String tipo_material;
    public String categoria;
    public List<DataTema> lista_temas;
}
