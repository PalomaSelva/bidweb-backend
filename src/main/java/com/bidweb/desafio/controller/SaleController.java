package com.bidweb.desafio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bidweb.desafio.dto.SaleResponse;
import com.bidweb.desafio.service.SaleService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/sales")
public class SaleController {

  @Autowired
  private SaleService saleService;

  @GetMapping
  @Operation(summary = "Lista as vendas", description = "Retorna uma lista com todas as vendas cadastradas.")
  public ResponseEntity<List<SaleResponse>> getAllSales() {
    List<SaleResponse> sales = saleService.getAllSales();
    return ResponseEntity.ok(sales);
  }
}