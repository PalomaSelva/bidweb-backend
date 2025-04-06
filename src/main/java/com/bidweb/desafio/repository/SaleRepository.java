package com.bidweb.desafio.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.bidweb.desafio.model.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long> {
  @Query("SELECT s.product.name as produto, SUM(s.quantity) as vendas " +
      "FROM Sale s " +
      "WHERE s.saleDate >= :startDate AND s.saleDate < :endDate " +
      "GROUP BY s.product.name " +
      "ORDER BY vendas DESC " +
      "LIMIT 5")
  List<Object[]> findTopProductsByMonth(
      @Param("startDate") LocalDateTime startDate,
      @Param("endDate") LocalDateTime endDate);
}