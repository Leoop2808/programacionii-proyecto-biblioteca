package com.proyectoprogra.biblioteca.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import com.proyectoprogra.biblioteca.model.*;

@Repository
public interface MaterialRepository extends JpaRepository<Material, String>{

    @Query(value = "select id_categoria_material from mst_categoria_material where cod_categoria_material = :codCatMat and activo = true and eliminado = false", nativeQuery = true)
    Integer ObtenerIdCategoriaMaterialPorCodigo(@Param("codCatMat") String cod_categoria_material);

    @Query(value = "select id_tipo_material from mst_tipo_material mtm where cod_tipo_material = :codTipoMaterial and activo = true and eliminado = false", nativeQuery = true)
    Integer ObtenerIdTipoMaterialPorCodigo(@Param("codTipoMaterial") String cod_tipo_material);

    @Query(value = "select id_tema from mst_tema where cod_tema in (:concatTemas) and activo = true and eliminado = false", nativeQuery = true)
    List<Integer> ObtenerListaIdentificadoresTemas(@Param("concatTemas") List<String> concatTemas);

    
    @Query(value = "insert into trs_material (id_tipo_material,id_categoria_material, isbn, autor, titulo, anio, descripcion,editorial, num_paginas,flg_disponibilidad,activo,eliminado, fecha_registro, fecha_modificacion) "+
    " values (:id_tipo_material,:id_categoria_material,:isbn,:autor,:titulo,:anio,:descripcion,:editorial,:num_paginas,:flg_disponibilidad,:activo,:eliminado, :fecha_hoy, :fecha_hoy) returning id_material;", nativeQuery = true)
    @Transactional
    Integer RegistrarMaterial(@Param("id_tipo_material") Integer id_tipo_material, @Param("id_categoria_material") Integer id_categoria_material, 
    @Param("isbn") String isbn, @Param("autor") String autor, @Param("titulo") String titulo, @Param("anio") Integer anio, @Param("descripcion") String descripcion,
    @Param("editorial") String editorial, @Param("num_paginas") Integer num_paginas, @Param("flg_disponibilidad") Boolean flg_disponibilidad, 
    @Param("activo") Boolean activo, @Param("eliminado") Boolean eliminado, @Param("fecha_hoy") Date fecha_hoy);

    @Modifying
    @Query(value = "insert into trs_material_tema (id_material, id_tema,activo, eliminado, fecha_registro, fecha_modificacion) values (:id_material,:id_tema,:activo,:eliminado, :fecha_hoy, :fecha_hoy)", nativeQuery = true)
    @Transactional
    Integer RegistrarTemaMaterial(@Param("id_material") Integer id_material, @Param("id_tema") Integer id_tema, @Param("activo") Boolean activo, @Param("eliminado") Boolean eliminado, @Param("fecha_hoy") Date fecha_hoy);

    @Modifying
    @Query(value = "update trs_material set cod_material  = :cod_material, fecha_modificacion = :fecha_hoy where id_material = :id_material", nativeQuery = true)
    @Transactional
    Integer RegistrarCodigoMaterial(@Param("cod_material") String cod_material, @Param("id_material") Integer id_material, @Param("fecha_hoy") Date fecha_hoy);

    @Query(value = "select tm.cod_tipo_material, tm.descripcion as desc_tipo_material from mst_tipo_material tm where activo  = true and eliminado = false", nativeQuery = true)
    List<Map<String, Object>> ListarTipoMaterial();

    @Query(value = "select cod_categoria_material, descripcion as desc_categoria_material from mst_categoria_material where activo = true and eliminado = false", nativeQuery = true)
    List<Map<String, Object>> ListarCategoriasMaterial();

    @Query(value = "select cod_tema, descripcion  as desc_tema from mst_tema where activo = true and eliminado = false", nativeQuery = true)
    List<Map<String, Object>> ListarTemas();

    @Modifying
    @Query(value = "insert into mst_tema (cod_tema, descripcion, activo, eliminado) values (:codTema,:descTema,:activo,:eliminado)", nativeQuery = true)
    @Transactional
    Integer RegistrarTema(@Param("codTema") String codTema, @Param("descTema") String descTema, @Param("activo") Boolean activo, @Param("eliminado") Boolean eliminado);
}
