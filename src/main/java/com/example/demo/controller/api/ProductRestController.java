package com.example.demo.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.utils.CustomResponse;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("api")
public class ProductRestController {
  private ProductRepository productRepository;

  public ProductRestController(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @GetMapping("product")
  public ResponseEntity<Object> getProducts() {
    return CustomResponse.generate(
      HttpStatus.OK,
      "Success get all products", 
      productRepository.findAll()
    );
  }
  
  @GetMapping("product/{id}")
  public ResponseEntity<Object> geProductById(@PathVariable(required = false) Integer id) {
    Product product = productRepository.findById(id).orElse(null);

    if (product != null) {
      return CustomResponse.generate(
        HttpStatus.OK,
        "Success get a product", 
        product
      );
    } else {
      return CustomResponse.generate(
        HttpStatus.OK,
        "Product with id " + id + " not found"
      );
    }
  }

  @PostMapping("product")
  public ResponseEntity<Object> postProducts(@RequestBody Product product) {
    return CustomResponse.generate(
      HttpStatus.OK,
      "Success adding a product",
      productRepository.save(product)
    );
  }
  
  @DeleteMapping("product/{id}")
  public ResponseEntity<Object> deleteProducts(@PathVariable(required = false) Integer id) {
    try {
      productRepository.deleteById(id);
    } catch (Exception e) {
      return CustomResponse.generate(
        HttpStatus.OK,
        "Failed removing a product, id " + id + " not found"
      );
    }
    Boolean isProductDeleted = productRepository.findById(id).isEmpty();
    if (isProductDeleted) {
      return CustomResponse.generate(
        HttpStatus.OK,
        "Success remove a product"
      );
    } else {
      return CustomResponse.generate(
        HttpStatus.OK,
        "Failed removing a product"
      );
    }
  }
}
