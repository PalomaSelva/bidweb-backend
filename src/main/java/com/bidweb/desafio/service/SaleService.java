package com.bidweb.desafio.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

  public PaginatedResponse<SaleResponse> getAllSalesPaginated(Pageable pageable, String productName) {
    try {
      Page<Sale> sales = saleRepository.findByProductName(productName, pageable);

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

  public Map<String, Object> getMonthProductsSold() {
    try {
      LocalDateTime now = LocalDateTime.now();
      LocalDateTime startDate = now.minusMonths(2).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);

      List<Object[]> results = saleRepository.getTotalProductsSoldByMonth(startDate);

      if (results.isEmpty()) {
        Map<String, Object> response = new HashMap<>();
        response.put("amount", 0L);
        response.put("diffFromLastMonth", BigDecimal.ZERO);
        return response;
      }

      Object[] currentMonthData = results.get(0);
      Long currentMonthQuantity = ((Number) currentMonthData[2]).longValue();
      Long previousMonthQuantity = currentMonthData[3] != null ? ((Number) currentMonthData[3]).longValue() : 0L;

      BigDecimal diffPercentage = BigDecimal.ZERO;
      if (previousMonthQuantity > 0) {
        diffPercentage = BigDecimal.valueOf(currentMonthQuantity)
            .multiply(BigDecimal.valueOf(100))
            .divide(BigDecimal.valueOf(previousMonthQuantity), 2, RoundingMode.HALF_UP)
            .subtract(BigDecimal.valueOf(100));
      }

      Map<String, Object> response = new HashMap<>();
      response.put("amount", currentMonthQuantity);
      response.put("diffFromLastMonth", diffPercentage);

      return response;
    } catch (Exception e) {
      throw new RuntimeException("Erro ao calcular produtos vendidos: " + e.getMessage());
    }
  }

  public Map<String, Object> getCurrentMonthTotalReceipt() {
    try {
      LocalDateTime now = LocalDateTime.now();
      LocalDateTime startDate = now.minusMonths(2).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);

      List<Object[]> results = saleRepository.getMonthReceipts(startDate);

      if (results.isEmpty()) {
        Map<String, Object> response = new HashMap<>();
        response.put("receipt", BigDecimal.ZERO);
        response.put("diffFromLastMonth", BigDecimal.ZERO);
        return response;
      }

      Object[] currentMonthData = results.get(0);
      BigDecimal currentMonthReceipt = (BigDecimal) currentMonthData[1];
      BigDecimal previousMonthReceipt = (BigDecimal) currentMonthData[2];

      BigDecimal diffPercentage = BigDecimal.ZERO;
      if (previousMonthReceipt != null && previousMonthReceipt.compareTo(BigDecimal.ZERO) > 0) {
        diffPercentage = currentMonthReceipt
            .multiply(BigDecimal.valueOf(100))
            .divide(previousMonthReceipt, 2, RoundingMode.HALF_UP)
            .subtract(BigDecimal.valueOf(100));
      }

      Map<String, Object> response = new HashMap<>();
      response.put("receipt", currentMonthReceipt);
      response.put("diffFromLastMonth", diffPercentage);

      return response;
    } catch (Exception e) {
      throw new RuntimeException("Erro ao calcular receita total do mês atual: " + e.getMessage());
    }
  }

  public List<Map<String, Object>> getDailyReceiptInPeriod(LocalDateTime startDate, LocalDateTime endDate) {
    try {
      if (startDate == null) {
        startDate = LocalDateTime.now().minusDays(7).withHour(0).withMinute(0).withSecond(0);
      } else {
        startDate = startDate.withHour(0).withMinute(0).withSecond(0);
      }

      if (endDate == null) {
        endDate = startDate != null ? startDate.plusDays(7) : LocalDateTime.now();
      }
      endDate = endDate.withHour(23).withMinute(59).withSecond(59);

      // Verificar se o período é maior que 7 dias
      if (endDate.toLocalDate().toEpochDay() - startDate.toLocalDate().toEpochDay() > 7) {
        throw new IllegalArgumentException("O intervalo das datas não pode ser superior a 7 dias.");
      }

      List<Object[]> results = saleRepository.getDailyReceiptInPeriod(startDate, endDate);

      return results.stream()
          .map(result -> {
            Map<String, Object> dayData = new HashMap<>();
            dayData.put("date", result[0]);
            dayData.put("receipt", result[1]);
            return dayData;
          })
          .collect(Collectors.toList());
    } catch (Exception e) {
      throw new RuntimeException("Erro ao buscar receita diária: " + e.getMessage());
    }
  }

}