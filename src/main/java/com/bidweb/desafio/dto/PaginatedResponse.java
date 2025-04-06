package com.bidweb.desafio.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaginatedResponse<T> {
  private List<T> items;
  private long totalCount;
}