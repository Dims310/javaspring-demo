package com.example.demo.controller.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.dto.OrderDTO;
import com.example.demo.repository.OrderRepository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("api")
public class OrderRestController {
  private OrderRepository orderRepository;

  public OrderRestController(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  @GetMapping("order")
  public Map<String, Object> getOrders(
    @RequestParam Integer draw,
    @RequestParam Integer start,
    @RequestParam Integer length,
    @RequestParam(value = "search") String search
  ) {

    Integer page = start/length;

    Pageable pageable = PageRequest.of(page, length);
    Page<OrderDTO> orders = orderRepository.findOrders(search, pageable);

    Long totalRecords = getTotalRecords();
    Long filteredRecords = getFilteredRecordsCount(search);

    Map<String, Object> response = new HashMap<>();
    response.put("draw", draw);
    response.put("recordsTotal", totalRecords);
    response.put("recordsFiltered", filteredRecords);
    response.put("data", orders.getContent());

    return response;
  };

  // TESTER
  // @GetMapping("order")
  // public List<Object[]> getOrder(@RequestParam(required = false) String prefix) {
  //   return orderRepository.findOrders(prefix);
  // }

  private Long getTotalRecords() {
    return orderRepository.count();
  }

  private Long getFilteredRecordsCount(String prefix) {
    return orderRepository.countFiltered(prefix);
  }
}
