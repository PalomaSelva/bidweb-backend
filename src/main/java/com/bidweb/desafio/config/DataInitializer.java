package com.bidweb.desafio.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bidweb.desafio.model.User;
import com.bidweb.desafio.repository.UserRerpository;

@Configuration
public class DataInitializer implements CommandLineRunner {

  @Autowired
  private UserRerpository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

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
  }
}