package com.bidweb.desafio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductRequest {
  @NotBlank(message = "O nome do produto é obrigatório")
  private String name;

  @NotNull(message = "O preço do produto é obrigatório")
  @Positive(message = "O preço deve ser maior que zero")
  private BigDecimal price;
}