package com.proyectoprogra.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;
import java.util.Optional;

import com.proyectoprogra.biblioteca.model.Documento;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, String>{
    @Query(value = "SELECT o FROM Libro o WHERE o.id=?1")
    Optional <Documento> getDocumentofindById(String id);
}
