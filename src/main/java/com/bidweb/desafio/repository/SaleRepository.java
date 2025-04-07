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
      WITH monthly_totals AS (
          SELECT
              TO_CHAR(sale_date, 'YYYY-MM') AS month_year,
              COUNT(DISTINCT id) as total_sales,
              SUM(quantity) as total_products
          FROM sales
          WHERE sale_date >= :startDate
          GROUP BY TO_CHAR(sale_date, 'YYYY-MM')
      )
      SELECT
          month_year,
          total_sales,
          total_products,
          LAG(total_products) OVER (ORDER BY month_year) as previous_month_products
      FROM monthly_totals
      ORDER BY month_year DESC
      """, nativeQuery = true)
  List<Object[]> getTotalProductsSoldByMonth(@Param("startDate") LocalDateTime startDate);

  @Query(value = """
      WITH daily_totals AS (
          SELECT
              TO_CHAR(sale_date, 'DD/MM') as date,
              CAST(sale_date AS DATE) as full_date,
              SUM(total_value) as receipt
          FROM sales
          WHERE sale_date >= :startDate
          AND sale_date <= :endDate
          GROUP BY TO_CHAR(sale_date, 'DD/MM'), CAST(sale_date AS DATE)
      )
      SELECT
          date,
          receipt
      FROM daily_totals
      ORDER BY full_date
      """, nativeQuery = true)
  List<Object[]> getDailyReceiptInPeriod(
      @Param("startDate") LocalDateTime startDate,
      @Param("endDate") LocalDateTime endDate);

}