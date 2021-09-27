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
    " values (:id_tipo_material,:id_categoria_material,:isbn,:autor,:titulo,:anio,:descripcion,:editorial,:num_paginas,true,true,false, current_timestamp, current_timestamp) returning id_material;", nativeQuery = true)
    @Transactional
    Integer RegistrarMaterial(@Param("id_tipo_material") Integer id_tipo_material, @Param("id_categoria_material") Integer id_categoria_material, 
    @Param("isbn") String isbn, @Param("autor") String autor, @Param("titulo") String titulo, @Param("anio") Integer anio, @Param("descripcion") String descripcion,
    @Param("editorial") String editorial, @Param("num_paginas") Integer num_paginas);

    @Modifying
    @Query(value = "insert into trs_material_tema (id_material, id_tema,activo, eliminado, fecha_registro, fecha_modificacion) values (:id_material,:id_tema,true,false, current_timestamp, current_timestamp)", nativeQuery = true)
    @Transactional
    Integer RegistrarTemaMaterial(@Param("id_material") Integer id_material, @Param("id_tema") Integer id_tema);

    @Modifying
    @Query(value = "update trs_material set cod_material  = :cod_material , fecha_modificacion = current_timestamp where id_material = :id_material", nativeQuery = true)
    @Transactional
    Integer RegistrarCodigoMaterial(@Param("cod_material") String cod_material, @Param("id_material") Integer id_material);

    @Modifying
    @Query(value = "update trs_material set autor = :autor , titulo = :titulo, anio = :anio , descripcion = :descripcion, editorial = :editorial, num_paginas = :num_paginas, fecha_modificacion = current_timestamp where id_material = :id_material", nativeQuery = true)
    @Transactional
    Integer ModificarMaterial(@Param("id_material") Integer id_material, @Param("autor") String autor, @Param("titulo") String titulo,
    @Param("anio") Integer anio, @Param("descripcion") String descripcion, @Param("editorial") String editorial, @Param("num_paginas") Integer num_paginas);

    @Query(value = "select distinct mt.id_material, mt.titulo, mt.autor, mt.editorial, mt.anio, cm.descripcion categoria, tipma.descripcion tipo_material, mt.fecha_registro " +
    "from trs_material mt inner join mst_categoria_material cm on cm.id_categoria_material = mt.id_categoria_material " +
    "inner join mst_tipo_material tipma on tipma.id_tipo_material = mt.id_tipo_material where mt.activo = true and mt.eliminado = false " +
    "and cm.activo = true and cm.eliminado = false and tipma.activo = true and tipma.eliminado = false ", nativeQuery = true)  
    List<Map<String, Object>> ListarMaterialPorFiltros();

    @Query(value = "select distinct mt.id_material,mt.cod_material,mt.isbn,mt.titulo, mt.autor, mt.editorial,mt.anio,mt.descripcion descripcion_material,mt.num_paginas,cm.descripcion categoria,tipma.descripcion tipo_material " +
    "from trs_material mt inner join mst_categoria_material cm on cm.id_categoria_material = mt.id_categoria_material inner join mst_tipo_material tipma on tipma.id_tipo_material = mt.id_tipo_material " +
    "where mt.id_material = :id_material and mt.activo = true and mt.eliminado = false and cm.activo = true and cm.eliminado = false and tipma.activo = true and tipma.eliminado = false", nativeQuery = true)
    Map<String, Object> ObtenerDetalleMaterial(@Param("id_material") Integer id_material);

    @Query(value = "select mt.cod_tema,mt.descripcion desc_tema from trs_material_tema tmt inner join trs_material tm on tm.id_material = tmt.id_material " +
    "inner join mst_tema mt on mt.id_tema = tmt .id_tema where tm.id_material = :id_material and tmt.activo = true and tmt.eliminado = false " +
    "and tm.activo = true and tm.eliminado = false and mt.activo = true and mt.eliminado = false ", nativeQuery = true)  
    List<Map<String, Object>> ObtenerTemasPorMaterial(@Param("id_material") Integer id_material);

    @Query(value = "select tm.cod_tipo_material, tm.descripcion as desc_tipo_material from mst_tipo_material tm where activo  = true and eliminado = false", nativeQuery = true)
    List<Map<String, Object>> ListarTipoMaterial();

    @Query(value = "select cod_categoria_material, descripcion as desc_categoria_material from mst_categoria_material where activo = true and eliminado = false", nativeQuery = true)
    List<Map<String, Object>> ListarCategoriasMaterial();

    @Query(value = "select cod_tema, descripcion  as desc_tema from mst_tema where activo = true and eliminado = false", nativeQuery = true)
    List<Map<String, Object>> ListarTemas();

    @Modifying
    @Query(value = "insert into mst_tema (cod_tema, descripcion, activo, eliminado) values (:codTema,:descTema,true,false)", nativeQuery = true)
    @Transactional
    Integer RegistrarTema(@Param("codTema") String codTema, @Param("descTema") String descTema);

    @Query(value = "select id_material from trs_material where cod_material = :cod_material and activo = true and eliminado = false", nativeQuery = true)
    Integer ObtenerIdMaterialPorCodigo(@Param("cod_material") String cod_material);

    @Query(value = "select flg_disponibilidad from trs_material where id_material = :id_material and activo = true and eliminado = false", nativeQuery = true)
    Boolean ObtenerDisponbilidadMaterial(@Param("id_material") Integer id_material);

    @Modifying
    @Query(value = "update trs_material set flg_disponibilidad = :flg_disponibilidad, fecha_modificacion  = current_timestamp where id_material = :id_material", nativeQuery = true)
    @Transactional
    Integer ActualizarDisponibilidadMaterial(@Param("id_material") Integer id_material, @Param("flg_disponibilidad") Boolean flg_disponibilidad);

    @Query(value = "insert into trs_prestamo_material(id_material, nombre_prestador, nombre_solicitante, dni_solicitante, " +
    "dias_prestamo, telefono_solicitante, correo_solicitante, fecha_pactada_devolucion, activo, eliminado, fecha_registro, fecha_modificacion) " +
    "values (:id_material,:nombre_prestador,:nombre_solicitante,:dni_solicitante,:dias_prestamo,:telefono_solicitante,:correo_solicitante, " +
    ":fecha_pactada_devolucion, true, false, current_timestamp, current_timestamp) returning id_prestamo_material", nativeQuery = true)
    @Transactional
    Integer RegistrarPrestamoMaterial(@Param("id_material") Integer id_material,@Param("nombre_solicitante") String nombre_solicitante,
    @Param("dni_solicitante") String dni_solicitante, @Param("correo_solicitante") String correo_solicitante, 
    @Param("telefono_solicitante") String telefono_solicitante,@Param("nombre_prestador") String nombre_prestador,
    @Param("dias_prestamo") Integer dias_prestamo, @Param("fecha_pactada_devolucion") Date fecha_pactada_devolucion);

    @Modifying
    @Query(value = "update trs_prestamo_material set cod_prestamo_material = :codigo_prestamo , fecha_modificacion = current_timestamp where id_prestamo_material = :id_prestamo ", nativeQuery = true)
    @Transactional
    Integer RegistrarCodigoPrestamoMaterial(@Param("codigo_prestamo") String codigoPrestamo,@Param("id_prestamo") Integer id_prestamo);
}
