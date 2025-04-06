package com.bidweb.desafio.config;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bidweb.desafio.model.Sale;
import com.bidweb.desafio.model.User;
import com.bidweb.desafio.repository.SaleRepository;
import com.bidweb.desafio.repository.UserRerpository;

@Configuration
public class DataInitializer implements CommandLineRunner {
  @Autowired
  private SaleRepository saleRepository;

  @Autowired
  private UserRerpository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  private final List<String> products = Arrays.asList(
      "Notebook", "Smartphone", "Tablet", "Smart TV", "Fone de Ouvido",
      "Mouse", "Teclado", "Monitor", "Câmera", "Console de Videogame");

  private final Random random = new Random();

  @Override
  public void run(String... args) throws Exception {
    // Verifica se já existe um usuário admin
    if (userRepository.findByEmail("admin@bidweb.com").isEmpty()) {
      User admin = new User();
      admin.setName("Administrador");
      admin.setEmail("admin@bidweb.com");
      admin.setPassword(passwordEncoder.encode("admin123"));
      admin.setCreatedAt(java.time.LocalDateTime.now());
      admin.setUpdatedAt(java.time.LocalDateTime.now());

      userRepository.save(admin);
      System.out.println("Usuário admin criado com sucesso!");
    }

    if (saleRepository.count() == 0) {
      for (int i = 0; i < 40; i++) {
        Sale sale = new Sale();

        // Seleciona um produto aleatório da lista
        String productName = products.get(random.nextInt(products.size()));
        sale.setProductName(productName);

        // Gera uma quantidade aleatória entre 1 e 10
        int quantity = random.nextInt(10) + 1;
        sale.setQuantity(quantity);

        // Gera um valor base aleatório entre 1000 e 5000
        double baseValue = 1000 + (random.nextDouble() * 4000);
        // Ajusta o valor base de acordo com o produto
        double adjustedValue = adjustValueByProduct(productName, baseValue);
        // Calcula o valor total
        BigDecimal totalValue = BigDecimal.valueOf(adjustedValue * quantity);
        sale.setTotalValue(totalValue);

        // Gera uma data aleatória nos últimos 30 dias
        sale.setSaleDate(LocalDateTime.now().minusDays(random.nextInt(30)));

        saleRepository.save(sale);
      }
      System.out.println("40 vendas simuladas criadas com sucesso!");
    }
  }

  private double adjustValueByProduct(String productName, double baseValue) {
    // Ajusta o valor base de acordo com o tipo de produto
    switch (productName) {
      case "Notebook":
        return baseValue * 1.5;
      case "Smart TV":
        return baseValue * 1.3;
      case "Console de Videogame":
        return baseValue * 1.2;
      case "Monitor":
        return baseValue * 0.8;
      case "Fone de Ouvido":
        return baseValue * 0.3;
      case "Mouse":
      case "Teclado":
        return baseValue * 0.2;
      default:
        return baseValue;
    }
  }
}
