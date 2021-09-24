package com.proyectoprogra.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyectoprogra.biblioteca.model.Material;

@Repository
public interface MaterialRepository extends JpaRepository<Material, String>{

}
