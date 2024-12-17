package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.web.bind.annotation.PostMapping;

@Controller // Anotasi wajib
// @RequestMapping("produk") // Perutean khusus untuk class
@RequestMapping("admin/product")
public class ProductController {

  private ProductRepository productRepository;
  private CategoryRepository categoryRepository;

  public ProductController(ProductRepository productRepository, CategoryRepository categoryRepository) {
    this.productRepository = productRepository;
    this.categoryRepository = categoryRepository;
  }

  @GetMapping() // Method yang menggunakan GetMapping merupakan endpoint
  public String index(Model model) {
    // Penggunaan Model
    model.addAttribute("products", productRepository.getAllProductByDTO());
    // List<Product> products = productRepository.findAll();
    return "product/index"; // Yang dikembalikan berupa path untuk View
  }

  // CARA 1 
  // @GetMapping("form")
  // public String form(@RequestParam(value = "id", required = false) Integer id, Model model) {
  //   Product productData = new Product();
  //   model.addAttribute("categories", categoryRepository.findAll());

  //   if (id != null) {
  //     productData = productRepository.findById(id).get();

  //     model.addAttribute("product", productData);
  //   } else {
  //     productData.setStatus(false);
  //     model.addAttribute("product", new Product());
  //   }

  //   return "product/form";
  // }

  // CARA 2
  @GetMapping(value = {"form", "form/{id}"})
  public String form(@PathVariable(required = false) Integer id, Model model) {
    Product productData = new Product();
    model.addAttribute("categories", categoryRepository.findAll());

    if (id != null) {
      productData = productRepository.findById(id).get();

      model.addAttribute("product", productData);
    } else {
      productData.setStatus(false);
      model.addAttribute("product", new Product());
    }

    return "product/form";
  }

  @PostMapping("save")
  public String save(Product product) {
    Category category = categoryRepository.findById(product.getCategory().getId()).orElse(null);
    product.setCategory(category);
    productRepository.save(product);
    return "redirect:/product";
  }  
  
  @PostMapping(value = {"delete", "delete/{id}"})
  public String delete(@PathVariable(required = false) Integer id) {
    productRepository.deleteById(id);
    return "redirect:/product";
  }

  // get untuk provide data
  // post untuk memodifikasi data termasuk menghapus
}