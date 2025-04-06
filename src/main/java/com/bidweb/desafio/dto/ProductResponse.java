package com.bidweb.desafio.dto;

import com.bidweb.desafio.model.Product;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductResponse {
  private Long id;
  private String name;
  private BigDecimal price;

  public ProductResponse(Product product) {
    this.id = product.getId();
    this.name = product.getName();
    this.price = product.getPrice();
  }
}