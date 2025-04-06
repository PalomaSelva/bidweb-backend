package com.bidweb.desafio.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bidweb.desafio.dto.SaleResponse;
import com.bidweb.desafio.model.Sale;
import com.bidweb.desafio.repository.SaleRepository;

@Service
public class SaleService {

  @Autowired
  private SaleRepository saleRepository;

  public List<SaleResponse> getAllSales() {
    List<Sale> sales = saleRepository.findAll();
    return sales.stream()
        .map(SaleResponse::new)
        .collect(Collectors.toList());
  }

  public Page<SaleResponse> getAllSales(Pageable pageable) {
    Page<Sale> sales = saleRepository.findAll(pageable);
    return sales.map(SaleResponse::new);
  }
}