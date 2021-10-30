// package com.webdashboard.dashboard.repository;

// import java.util.List;

// import com.webdashboard.dashboard.model.Prestamo;

// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.stereotype.Repository;

// @Repository
// public class PrestamoRepository extends JpaRepository<Prestamo, Integer>{
//     @Query(value = "SELECT o FROM Pedido o WHERE o.clienteId=?1")
//     List<Prestamo> findItemsByCliente(Integer clienteId);
// }
