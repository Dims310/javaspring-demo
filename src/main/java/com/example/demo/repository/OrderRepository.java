package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Order;
import com.example.demo.model.dto.OrderDTO;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>{
  // @Query(value = "SELECT" +
  //   " o.id, o.location_logs, SUM(od.price_at_order * od.quantity) AS total_amount," +
  //   " os.name AS status, o.created_at," +
  //   " per.fullname AS username" +
  //   " FROM tb_tr_order_details od" +
  //   " JOIN tb_products pro ON od.tb_products_id = pro.id" +
  //   " JOIN tb_tr_orders o ON od.tb_tr_orders_id = o.id" +
  //   " JOIN tb_m_order_status os ON o.tb_m_order_status_id = os.id" +
  //   " JOIN tb_users u ON o.tb_users_id = u.id" +
  //   " JOIN tb_person per ON u.id = per.id" +
  //   " WHERE pro.name LIKE LOWER(CONCAT('%', :search, '%'))" +
  //   " OR per.fullname LIKE LOWER(CONCAT('%', :search, '%'))" +
  //   " OR os.name LIKE LOWER(CONCAT('%', :search, '%'))" +
  //   " OR o.created_at LIKE LOWER(CONCAT('%', :search, '%'))" +
  //   " GROUP BY o.id"
  // , nativeQuery = true)
  // Page<Object[]> findOrders(@Param("search") String search, Pageable pageable);

  @Query("SELECT new com.example.demo.model.dto.OrderDTO(" + 
  " o.id, o.locationLogs, SUM(od.priceAtOrder * od.quantity)," +
  " os.name, o.createdAt, per.fullname)" +
  " FROM OrderDetail od" +
  " JOIN od.product pro JOIN od.order o JOIN o.orderStatus os" +
  " JOIN o.user u JOIN u.person per" +
  " WHERE pro.name LIKE LOWER(CONCAT('%', :search, '%'))" +
  " OR per.fullname LIKE LOWER(CONCAT('%', :search, '%'))" +
  " OR os.name LIKE LOWER(CONCAT('%', :search, '%'))" +
  " OR o.createdAt LIKE LOWER(CONCAT('%', :search, '%'))" +
  " GROUP BY o.id")
  Page<OrderDTO> findOrders(@Param("search") String search, Pageable pageable);

  @Query(value = "SELECT COUNT(DISTINCT o.id)" +
    " FROM tb_tr_order_details od" +
    " JOIN tb_products pro ON od.tb_products_id = pro.id" +
    " JOIN tb_tr_orders o ON od.tb_tr_orders_id = o.id" +
    " JOIN tb_m_order_status os ON o.tb_m_order_status_id = os.id" +
    " JOIN tb_users u ON o.tb_users_id = u.id" +
    " JOIN tb_person per ON u.id = per.id" +
    " WHERE pro.name LIKE LOWER(CONCAT('%', :search, '%'))" +
    " OR per.fullname LIKE LOWER(CONCAT('%', :search, '%'))" +
    " OR os.name LIKE LOWER(CONCAT('%', :search, '%'))" +
    " OR o.created_at LIKE LOWER(CONCAT('%', :search, '%'))",
    nativeQuery = true)
  Long countFiltered(String search);
}
