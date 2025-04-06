package com.bidweb.desafio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bidweb.desafio.model.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long> {
}