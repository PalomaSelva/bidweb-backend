package com.bidweb.desafio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bidweb.desafio.dto.ProductRequest;
import com.bidweb.desafio.dto.ProductResponse;
import com.bidweb.desafio.service.ProductService;
import com.bidweb.desafio.util.ResponseUtils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {
  @Autowired
  private ProductService productService;

  @PostMapping
  @Operation(summary = "Cadastra um novo produto", description = "Adiciona um novo produto ao sistema.")
  public ResponseEntity<Object> createProduct(@Valid @RequestBody ProductRequest request) {
    try {
      ProductResponse response = productService.createProduct(request);
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(ResponseUtils.createMessageResponse(e.getMessage()));
    }
  }

  @GetMapping("/{id}")
  @Operation(summary = "Lista todos os produtos", description = "Retorna uma lista com todos os produtos cadastrados.")
  public ResponseEntity<List<ProductResponse>> getProductById(@PathVariable Long id) {
    try {
      List<ProductResponse> response = productService.getAllProducts();
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(List.of());
    }
  }
}