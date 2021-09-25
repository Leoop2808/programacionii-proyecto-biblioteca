package com.proyectoprogra.biblioteca.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.proyectoprogra.biblioteca.model.*;

@Repository
public interface MaterialRepository extends JpaRepository<Material, String>{
    @Query(value = "select tm.cod_tipo_material, tm.descripcion as desc_tipo_material from mst_tipo_material tm where activo  = true and eliminado = false", nativeQuery = true)
    List<Map<String, Object>> ListarTipoMaterial();

    @Modifying
    @Query(value = "insert into mst_tema (cod_tema, descripcion, activo, eliminado) values (:codTema,:descTema,:activo,:eliminado)", nativeQuery = true)
    @Transactional
    Integer RegistrarTema(@Param("codTema") String codTema, @Param("descTema") String descTema, @Param("activo") Boolean activo, @Param("eliminado") Boolean eliminado);
}
