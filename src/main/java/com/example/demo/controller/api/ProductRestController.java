package com.example.demo.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Asset;
import com.example.demo.model.Product;
import com.example.demo.model.dto.ProductAPIDTO;
import com.example.demo.repository.AssetRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.utils.CustomResponse;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("api")
@CrossOrigin(origins = "*")
public class ProductRestController {
  private ProductRepository productRepository;
  private CategoryRepository categoryRepository;
  private AssetRepository assetRepository;

  public ProductRestController(ProductRepository productRepository, CategoryRepository categoryRepository, AssetRepository assetRepository) {
    this.productRepository = productRepository;
    this.categoryRepository = categoryRepository;
    this.assetRepository = assetRepository;
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
  public ResponseEntity<Object> postProducts(@RequestBody ProductAPIDTO productAPIDTO) {

    Product product = new Product();
    product.setName(productAPIDTO.getName());
    product.setPrice(productAPIDTO.getPrice());
    product.setDescription(productAPIDTO.getDescription());
    product.setStock(productAPIDTO.getStock());
    product.setStatus(productAPIDTO.getStatus());
    product.setCategory(categoryRepository.findById(productAPIDTO.getCategoryId()).get());

    Asset asset = new Asset();
    asset.setPath("https://res.cloudinary.com/dvaqmwb6c/image/upload/f_auto,q_auto/question-sing-flat-icon-vector-illustration-isolated-on-white-background_jmuois");
    assetRepository.save(asset);

    product.setAsset(assetRepository.findById(asset.getId()).get());

    return CustomResponse.generate(
      HttpStatus.OK,
      "Success adding a product",
      productRepository.save(product)
    );
  }

  @PutMapping("product/{id}")
  public ResponseEntity<Object> updateProducts(@PathVariable(required = true) Integer id, @RequestBody ProductAPIDTO productAPIDTO) {
    Product targetProduct = productRepository.findById(id).get();

    if (productAPIDTO.getName() != null) {
      targetProduct.setName(productAPIDTO.getName());
    }

    if (productAPIDTO.getPrice() != null) {
      targetProduct.setPrice(productAPIDTO.getPrice());
    }

    targetProduct.setStatus(productAPIDTO.getStatus());

    if (productAPIDTO.getStock() != null) {
      targetProduct.setStock(productAPIDTO.getStock());
    }

    if (productAPIDTO.getCategoryId() != null) {
      targetProduct.setCategory(categoryRepository.findById(productAPIDTO.getCategoryId()).get());
    }

    return CustomResponse.generate(
      HttpStatus.OK,
      "Success update a product",
      productRepository.save(targetProduct)
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
