package com.proyectoprogra.biblioteca.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="t-articulo")
public class Documento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String titulo;
    private Date anioEdicion;
    private Integer idAutor;
    private String idioma;
    private String paisAutor;
    private String editorial;
    private Integer nroPaginas;
    private String sipnospi;
    private Integer idGenero; // ingenieria - arquitectura - economia - finanzas ...
    private Integer idTipo;     //libro - tesis pregtado - paper - revista - tesis postgrado - informes - articulo
}
