package com.bidweb.desafio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bidweb.desafio.dto.AuthRequest;
import com.bidweb.desafio.repository.UserRerpository;

@Service
public class AuthService {
  @Autowired
  private UserRerpository userRepository;

  public void login(AuthRequest request) {
    var user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

  }
}
