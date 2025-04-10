package com.bidweb.desafio.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthProductsResponse {
  private Long amount;
  private BigDecimal diffFromLastMonth;
}