package com.example.demo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "tb_tr_order_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private Integer qty;
  private Integer priceAtOrder;
  private Boolean isReturned;

  @ManyToOne
  @JoinColumn(name = "tb_products_id", referencedColumnName = "id")
  private Product product;

  // Pending...
  private Integer orderId;
}
