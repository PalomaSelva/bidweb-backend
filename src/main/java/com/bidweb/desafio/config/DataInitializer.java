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
import com.bidweb.desafio.model.Product;
import com.bidweb.desafio.repository.SaleRepository;
import com.bidweb.desafio.repository.UserRerpository;
import com.bidweb.desafio.repository.ProductRepository;

@Configuration
public class DataInitializer implements CommandLineRunner {
  @Autowired
  private SaleRepository saleRepository;

  @Autowired
  private UserRerpository userRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  private final List<String> productNames = Arrays.asList(
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

    // Cria produtos se não existirem
    if (productRepository.count() == 0) {
      for (String productName : productNames) {
        Product product = new Product();
        product.setName(productName);
        product.setPrice(getDefaultPrice(productName));
        productRepository.save(product);
      }
      System.out.println("Produtos criados com sucesso!");
    }

    // Cria vendas se não existirem
    if (saleRepository.count() == 0) {
      List<Product> products = productRepository.findAll();

      for (int i = 0; i < 40; i++) {
        Sale sale = new Sale();

        // Seleciona um produto aleatório
        Product product = products.get(random.nextInt(products.size()));
        sale.setProduct(product);

        // Gera uma quantidade aleatória entre 1 e 10
        int quantity = random.nextInt(10) + 1;
        sale.setQuantity(quantity);

        // Calcula o valor total baseado no preço do produto
        BigDecimal totalValue = product.getPrice().multiply(BigDecimal.valueOf(quantity));
        sale.setTotalValue(totalValue);

        // Gera uma data aleatória nos últimos 30 dias
        sale.setSaleDate(LocalDateTime.now().minusDays(random.nextInt(30)));

        saleRepository.save(sale);
      }
      System.out.println("40 vendas simuladas criadas com sucesso!");
    }
  }

  private BigDecimal getDefaultPrice(String productName) {
    switch (productName) {
      case "Notebook":
        return new BigDecimal("5000.00");
      case "Smart TV":
        return new BigDecimal("3000.00");
      case "Console de Videogame":
        return new BigDecimal("2500.00");
      case "Monitor":
        return new BigDecimal("1500.00");
      case "Smartphone":
        return new BigDecimal("2000.00");
      case "Tablet":
        return new BigDecimal("1200.00");
      case "Fone de Ouvido":
        return new BigDecimal("300.00");
      case "Mouse":
        return new BigDecimal("100.00");
      case "Teclado":
        return new BigDecimal("150.00");
      case "Câmera":
        return new BigDecimal("800.00");
      default:
        return new BigDecimal("1000.00");
    }
  }
}
