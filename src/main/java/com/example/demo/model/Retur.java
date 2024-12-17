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

@Entity(name = "tb_tr_retur")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Retur {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private String reason;
  private String createdAt;
  private String updatedAt;

  @ManyToOne
  @JoinColumn(name = "tb_tr_order_details_id", referencedColumnName = "id")
  private OrderDetail orderDetail;

  @ManyToOne
  @JoinColumn(name = "tb_m_retur_status_id", referencedColumnName = "id")
  private ReturStatus returStatus;
}
