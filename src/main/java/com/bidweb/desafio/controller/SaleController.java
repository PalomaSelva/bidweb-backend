package com.bidweb.desafio.controller;

import java.util.List;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bidweb.desafio.dto.PaginatedResponse;
import com.bidweb.desafio.dto.SaleResponse;
import com.bidweb.desafio.dto.TopProductsResponse;
import com.bidweb.desafio.dto.MonthProductsResponse;
import com.bidweb.desafio.dto.MonthReceiptResponse;
import com.bidweb.desafio.dto.DailyReceiptResponse;
import com.bidweb.desafio.service.SaleService;
import com.bidweb.desafio.util.ResponseUtils;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/sales")
public class SaleController {

  @Autowired
  private SaleService saleService;

  @GetMapping("/all")
  @Operation(summary = "Lista as vendas", description = "Retorna uma lista com todas as vendas cadastradas.")
  public ResponseEntity<List<SaleResponse>> getAllSales() {
    try {
      List<SaleResponse> sales = saleService.getAllSales();
      return ResponseEntity.ok(sales);
    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(List.of());
    }
  }

  @GetMapping
  @Operation(summary = "Lista vendas paginadas", description = "Retorna uma lista paginada de vendas.")
  public ResponseEntity<PaginatedResponse<SaleResponse>> getAllSalesPaginated(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int pageSize,
      @RequestParam(required = false) String productName

  ) {

    try {
      PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "id"));
      PaginatedResponse<SaleResponse> response = saleService.getAllSalesPaginated(pageRequest, productName);
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(new PaginatedResponse<>(List.of(), 0));
    }
  }

  @GetMapping("/top-products")
  @Operation(summary = "Lista os 5 produtos mais vendidos do mês", description = "Retorna uma lista com os 5 produtos mais vendidos no mês atual.")
  public ResponseEntity<Object> getTopProductsByMonth() {
    try {
      List<TopProductsResponse> response = saleService.getTopProductsByMonth();
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(ResponseUtils.createMessageResponse(e.getMessage()));
    }
  }

  @GetMapping("/month-receipt")
  @Operation(summary = "Receita do mês", description = "Retorna a receita do mês atual e a diferença percentual em relação ao mês anterior")
  public ResponseEntity<Object> getMonthReceipt() {
    try {
      MonthReceiptResponse response = saleService.getCurrentMonthTotalReceipt();
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(ResponseUtils.createMessageResponse(e.getMessage()));
    }
  }

  @GetMapping("/month-products")
  @Operation(summary = "Produtos vendidos no mês", description = "Retorna a quantidade de produtos vendidos no mês atual e a diferença percentual em relação ao mês anterior")
  public ResponseEntity<Object> getMonthProductsSold() {
    try {
      MonthProductsResponse response = saleService.getMonthProductsSold();
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(ResponseUtils.createMessageResponse(e.getMessage()));
    }
  }

  @GetMapping("/daily-receipt")
  @Operation(summary = "Receita diária", description = "Retorna a receita diária em um período de até 7 dias")
  public ResponseEntity<?> getDailyReceiptInPeriod(
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
    try {
      List<DailyReceiptResponse> response = saleService.getDailyReceiptInPeriod(from, to);
      return ResponseEntity.ok(response);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest()
          .body(ResponseUtils.createMessageResponse(e.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(ResponseUtils.createMessageResponse(e.getMessage()));
    }
  }

}