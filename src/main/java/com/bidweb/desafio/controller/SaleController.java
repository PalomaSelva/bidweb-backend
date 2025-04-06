package com.bidweb.desafio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bidweb.desafio.dto.PaginatedResponse;
import com.bidweb.desafio.dto.SaleResponse;
import com.bidweb.desafio.service.SaleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/sales")
public class SaleController {

  @Autowired
  private SaleService saleService;

  @GetMapping("/all")
  @Operation(summary = "Lista as vendas", description = "Retorna uma lista com todas as vendas cadastradas.")
  public ResponseEntity<List<SaleResponse>> getAllSales() {
    List<SaleResponse> sales = saleService.getAllSales();
    return ResponseEntity.ok(sales);
  }

  @GetMapping
  @Operation(summary = "Lista vendas paginadas", description = "Retorna uma lista paginada de vendas.")
  public ResponseEntity<PaginatedResponse<SaleResponse>> getAllSalesPaginated(
      @Parameter(description = "Número da página (começando em 1)") @RequestParam(defaultValue = "1") int page,
      @Parameter(description = "Quantidade de itens por página") @RequestParam(defaultValue = "10") int pageSize) {

    PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "id"));

    Page<SaleResponse> sales = saleService.getAllSales(pageRequest);
    PaginatedResponse<SaleResponse> response = new PaginatedResponse<>(
        sales.getContent(),
        sales.getTotalElements());
    return ResponseEntity.ok(response);
  }
}