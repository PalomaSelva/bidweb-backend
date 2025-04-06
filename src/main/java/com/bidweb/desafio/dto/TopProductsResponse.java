package com.bidweb.desafio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopProductsResponse {
  private String produto;
  private Long quantidadeTotal;
}