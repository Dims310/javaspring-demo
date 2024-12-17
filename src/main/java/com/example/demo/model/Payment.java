package com.example.demo.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "tb_m_payments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private String name;
  private String description;
  private Boolean status;

  @OneToMany(mappedBy = "payment")
  @JsonIgnore
  private List<Order> orders;
  
}
