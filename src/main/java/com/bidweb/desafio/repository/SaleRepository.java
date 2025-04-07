package com.bidweb.desafio.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

  @Query("SELECT s FROM Sale s " +
      "WHERE (:productName IS NULL OR LOWER(s.product.name) LIKE LOWER(CONCAT('%', :productName, '%')))")
  Page<Sale> findByProductName(@Param("productName") String productName, Pageable pageable);

  @Query(value = """
      WITH monthly_totals AS (
          SELECT
              TO_CHAR(sale_date, 'YYYY-MM') AS month_year,
              SUM(total_value) as total
          FROM sales
          WHERE sale_date >= :startDate
          GROUP BY TO_CHAR(sale_date, 'YYYY-MM')
      )
      SELECT
          month_year,
          total as receipt,
          LAG(total) OVER (ORDER BY month_year) as previousMonthReceipt
      FROM monthly_totals
      ORDER BY month_year DESC
      """, nativeQuery = true)
  List<Object[]> getMonthReceipts(@Param("startDate") LocalDateTime startDate);

  @Query(value = """
      SELECT
          TO_CHAR(s.sale_date, 'YYYY-MM') AS monthWithYear,
          COUNT(DISTINCT s.id) AS totalSales,
          SUM(s.quantity) AS totalProducts
      FROM sales s
      WHERE s.sale_date >= :startDate
      GROUP BY s.sale_date, TO_CHAR(s.sale_date, 'YYYY-MM')
      ORDER BY monthWithYear DESC
      """, nativeQuery = true)
  List<Object[]> getTotalProductsSoldByMonth(@Param("startDate") LocalDateTime startDate);

}