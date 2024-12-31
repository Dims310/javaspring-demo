package com.example.demo.model;

import javax.persistence.Column;
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

@Entity
@Table(name = "tb_tr_order_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private Integer quantity;

  @Column(name = "price_at_order")
  private Integer priceAtOrder;

  private Boolean isReturned;

  @ManyToOne
  @JoinColumn(name = "tb_products_id", referencedColumnName = "id")
  private Product product;

  @ManyToOne
  @JoinColumn(name = "tb_tr_orders_id", referencedColumnName = "id")
  private Order order;
}
