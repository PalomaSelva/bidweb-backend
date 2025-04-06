package com.bidweb.desafio.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.bidweb.desafio.model.Sale;

import lombok.Data;

@Data
public class SaleResponse {
  private Long id;
  private String product;
  private Integer quantity;
  private LocalDateTime saleDate;
  private BigDecimal totalValue;

  public SaleResponse(Sale sale) {
    this.id = sale.getId();
    this.product = sale.getProduct().getName();
    this.quantity = sale.getQuantity();
    this.saleDate = sale.getSaleDate();
    this.totalValue = sale.getTotalValue();
  }
}