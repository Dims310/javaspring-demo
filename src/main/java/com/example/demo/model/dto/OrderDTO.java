package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
  private Integer id;
  private String locationLogs;
  private Long totalAmount;
  private String status;
  private String createdAt;
  private String username;
}
