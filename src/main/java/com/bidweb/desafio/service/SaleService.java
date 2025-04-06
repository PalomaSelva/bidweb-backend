package com.bidweb.desafio.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bidweb.desafio.dto.PaginatedResponse;
import com.bidweb.desafio.dto.SaleResponse;
import com.bidweb.desafio.dto.TopProductsResponse;
import com.bidweb.desafio.model.Sale;
import com.bidweb.desafio.repository.SaleRepository;

@Service
public class SaleService {

  @Autowired
  private SaleRepository saleRepository;

  public List<SaleResponse> getAllSales() {
    try {
      List<Sale> sales = saleRepository.findAll();
      return sales.stream()
          .map(SaleResponse::new)
          .collect(Collectors.toList());
    } catch (Exception e) {
      throw new RuntimeException("Erro ao buscar vendas: " + e.getMessage());
    }
  }

  public PaginatedResponse<SaleResponse> getAllSalesPaginated(Pageable pageable) {
    try {
      Page<Sale> sales = saleRepository.findAll(pageable);
      return new PaginatedResponse<>(
          sales.getContent().stream()
              .map(SaleResponse::new)
              .collect(Collectors.toList()),
          sales.getTotalElements());
    } catch (Exception e) {
      throw new RuntimeException("Erro ao buscar vendas paginadas: " + e.getMessage());
    }
  }

  public List<TopProductsResponse> getTopProductsByMonth() {
    try {
      LocalDateTime now = LocalDateTime.now();
      LocalDateTime startDate = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
      LocalDateTime endDate = startDate.plusMonths(1);

      List<Object[]> results = saleRepository.findTopProductsByMonth(startDate, endDate);

      return results.stream()
          .map(result -> new TopProductsResponse(
              (String) result[0],
              (Long) result[1]))
          .collect(Collectors.toList());
    } catch (Exception e) {
      throw new RuntimeException("Erro ao buscar produtos mais vendidos: " + e.getMessage());
    }
  }
}