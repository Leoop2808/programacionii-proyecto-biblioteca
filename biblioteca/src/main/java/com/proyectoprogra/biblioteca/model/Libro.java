package com.proyectoprogra.biblioteca.model;

import javax.persistence.Id;


import javax.persistence.Entity;

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
@Table(name="t-libro")
public class Libro {
    @Id
    private Integer id;
    private String titulo;
}
