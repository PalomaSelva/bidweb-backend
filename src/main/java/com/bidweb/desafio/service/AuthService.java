package com.bidweb.desafio.service;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bidweb.desafio.dto.AuthRequest;
import com.bidweb.desafio.repository.UserRerpository;

@Service
public class AuthService {
  @Autowired
  private UserRerpository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public void login(AuthRequest request) throws AuthenticationException {
    var user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    var passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());
    if (!passwordMatches) {
      throw new AuthenticationException();
    }
  }
}
