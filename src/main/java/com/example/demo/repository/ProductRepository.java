package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Product;
import com.example.demo.model.dto.ProductDTO;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
  // Anotasi @Query untuk membuat custom JPA
  // parameter value berisi string query SQL
  // nativeQuery berfungsi untuk menandakan bahwa kueri yang digunakan kueri SQL bukan JPQL (menggunakan nama tabel/kolom)
  // Untuk TEST
  @Query(value = "SELECT stock FROM tb_products", nativeQuery = true)
  public List<Integer> getStock();

  // Untuk TEST
  // @Query("SELECT new com.example.dto.ProductDTO(p.id, p.name, p.price, p.category) FROM Product p WHERE p.id = 1")
  // public ProductDTO getProductByDTO();

  @Query("SELECT new com.example.demo.model.dto.ProductDTO(p.id, p.name, p.price, p.description, p.stock, p.status, c.name) "
  + " FROM Product p JOIN p.category c ORDER BY p.id ASC")
  public List<ProductDTO> getAllProductByDTO();
}
