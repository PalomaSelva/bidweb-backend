package com.bidweb.desafio.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import com.bidweb.desafio.dto.MonthProductsResponse;
import com.bidweb.desafio.dto.MonthReceiptResponse;
import com.bidweb.desafio.dto.DailyReceiptResponse;

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

  public MonthProductsResponse getMonthProductsSold() {
    try {
      LocalDateTime now = LocalDateTime.now();
      LocalDateTime startDate = now.minusMonths(2).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);

      List<Object[]> results = saleRepository.getTotalProductsSoldByMonth(startDate);
      // [
      // ["2025-04", 150, 900, 850],
      // ["2025-03", 120, 850, 700],
      // ]
      if (results.isEmpty()) {
        return new MonthProductsResponse(0L, BigDecimal.ZERO);
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

      return new MonthProductsResponse(currentMonthQuantity, diffPercentage);
    } catch (Exception e) {
      throw new RuntimeException("Erro ao calcular produtos vendidos: " + e.getMessage());
    }
  }

  public MonthReceiptResponse getCurrentMonthTotalReceipt() {
    try {
      LocalDateTime now = LocalDateTime.now();
      LocalDateTime startDate = now.minusMonths(2).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);

      // Busca as receitas dos últimos meses no banco de dados
      List<Object[]> results = saleRepository.getMonthReceipts(startDate);

      // Se não houver dados, retorna valores zerados
      if (results.isEmpty()) {
        return new MonthReceiptResponse(BigDecimal.ZERO, BigDecimal.ZERO);
      }

      // Pega os dados do mês atual (primeiro item da lista)
      Object[] currentMonthData = results.get(0);

      BigDecimal currentMonthReceipt = (BigDecimal) currentMonthData[1];

      BigDecimal previousMonthReceipt = (BigDecimal) currentMonthData[2];

      BigDecimal diffPercentage = BigDecimal.ZERO;

      // Calcula a diferença percentual apenas se houver receita no mês anterior
      if (previousMonthReceipt != null && previousMonthReceipt.compareTo(BigDecimal.ZERO) > 0) {

        // Fórmula: ((receita_atual * 100) / receita_anterior) - 100
        // Exemplo: se anterior foi 1000 e atual é 1200
        // ((1200 * 100) / 1000) - 100 = 20% de aumento
        diffPercentage = currentMonthReceipt
            .multiply(BigDecimal.valueOf(100))
            .divide(previousMonthReceipt, 2, RoundingMode.HALF_UP)
            .subtract(BigDecimal.valueOf(100));
      }

      return new MonthReceiptResponse(currentMonthReceipt, diffPercentage);
    } catch (Exception e) {
      throw new RuntimeException("Erro ao calcular receita total do mês atual: " + e.getMessage());
    }
  }

  public List<DailyReceiptResponse> getDailyReceiptInPeriod(LocalDateTime startDate, LocalDateTime endDate) {
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
          .map(result -> new DailyReceiptResponse(
              (String) result[0],
              (BigDecimal) result[1]))
          .collect(Collectors.toList());
    } catch (Exception e) {
      throw new RuntimeException("Erro ao buscar receita diária: " + e.getMessage());
    }
  }

}