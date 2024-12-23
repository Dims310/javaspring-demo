package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductAPIDTO {
  private Integer id;
  private String name;
  private Integer price;
  private String description;
  private Integer stock;
  private Boolean status;
  private Integer categoryId;
}
