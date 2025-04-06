package com.bidweb.desafio.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bidweb.desafio.dto.ProductRequest;
import com.bidweb.desafio.dto.ProductResponse;
import com.bidweb.desafio.model.Product;
import com.bidweb.desafio.repository.ProductRepository;

@Service
public class ProductService {
  @Autowired
  private ProductRepository productRepository;

  public ProductResponse createProduct(ProductRequest request) {
    try {
      Product product = new Product();
      product.setName(request.getName());

      product = productRepository.save(product);
      return new ProductResponse(product);
    } catch (Exception e) {
      throw new RuntimeException("Erro ao criar produto: " + e.getMessage());
    }
  }

  public List<ProductResponse> getAllProducts() {
    try {
      List<ProductResponse> product = productRepository.findAllByOrderByIdAsc().stream()
          .map(ProductResponse::new)
          .collect(Collectors.toList());
      if (product.isEmpty()) {
        return new ArrayList<>();
      }
      return product;
    } catch (Exception e) {
      throw new RuntimeException("Erro ao buscar produto: " + e.getMessage());
    }
  }
}