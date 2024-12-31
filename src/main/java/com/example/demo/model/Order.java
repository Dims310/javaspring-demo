package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "tb_tr_orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "location_logs")
  private String locationLogs;

  private String expiredAt;

  @Column(name = "created_at")
  private String createdAt;

  private String updatedAt;

  @ManyToOne
  @JoinColumn(name = "tb_users_id", referencedColumnName = "id")
  private User user;
  
  @ManyToOne
  @JoinColumn(name = "tb_m_payments_id", referencedColumnName = "id")
  private Payment payment;

  @ManyToOne
  @JoinColumn(name = "tb_m_order_status_id", referencedColumnName = "id")
  private OrderStatus orderStatus;

  @ManyToOne
  @JoinColumn(name = "tb_couriers_id", referencedColumnName = "id")
  private Courier courier;
}
