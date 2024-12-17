package com.example.demo.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import javax.persistence.CascadeType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
  @Id
  private Integer id;
  private String password;
  
  @ManyToOne
  @JoinColumn(name = "tb_m_roles_id", referencedColumnName = "id")
  private Role role;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "id", referencedColumnName = "id")
  private Person person;

  // @OneToOne
  // Penggunaan @JoinColumn untuk OneToOne pada kasus id yang sama pada dua tabel berbeda, menjadi seperti @JoinColumn(name = "id", referenced, referencedColumnName = "id")

  // @OneToMany(mappedBy = "user")
  // @JsonIgnore
  // private List<Order> orders;
}
