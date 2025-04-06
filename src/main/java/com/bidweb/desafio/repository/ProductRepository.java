package com.bidweb.desafio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bidweb.desafio.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
  List<Product> findAllByOrderByIdAsc();
}