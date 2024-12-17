package com.example.demo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "tb_m_categories")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private String name;
  private String description;
  private Boolean status;

  // @OneToMany(mappedBy = "category")
  // @JsonIgnore
  // private List<Product> products;
}
