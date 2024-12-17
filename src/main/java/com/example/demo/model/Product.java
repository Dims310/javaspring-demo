package com.example.demo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Mendefinisikan tabel yang ada di database
@Entity
@Table(name = "tb_products")
@Data // Anotasi data, ga perlu pake setter getter lagi
@AllArgsConstructor // Constructor untuk semua properti yang ada dikelas ini
@NoArgsConstructor // Constructor khusus yang tidak menggunakan parameter
public class Product {
  @Id // Pendefinisian primary key untuk id
  @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto Increment primary key
  private Integer id;
  private String name;
  private Integer price;
  private String description;
  private Integer stock;
  private Boolean status;

  // Cara penulisan FK
  @ManyToOne
  @JoinColumn(name = "tb_m_categories_id", referencedColumnName = "id")
  private Category category;

  // @OneToMany(mappedBy = "product") // Ini opsional
  // @JsonIgnore
  // private List<OrderDetail> orderDetails;
  
}
