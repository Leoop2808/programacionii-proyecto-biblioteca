package com.proyectoprogra.biblioteca.model;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Id;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "mst_categoria_material")
public class CategoriaMaterial {
    @Id
    @Column(name = "id_categoria_material")
    private Integer id;
    @Column(name = "cod_categoria_material")
    private String codTipoMaterial;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "activo")
    private Boolean activo;
    @Column(name = "eliminado")
    private Boolean eliminado;
    @Column(name = "fecha_registro")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date fecha_registro;
    @Column(name = "fecha_modificacion")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date fecha_modificacion;
}
