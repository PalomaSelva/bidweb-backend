package com.bidweb.desafio.service;

import java.time.Duration;
import java.time.Instant;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.bidweb.desafio.dto.AuthRequest;
import com.bidweb.desafio.repository.UserRerpository;

@Service
public class AuthService {

  @Value("${security.jwt.secret}")
  private String secretKey;

  @Autowired
  private UserRerpository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public String login(AuthRequest request) throws AuthenticationException {
    var user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    var passwordMatches = passwordEncoder.matches(request.getPassword(),
        user.getPassword());
    if (!passwordMatches) {
      throw new AuthenticationException();
    }
    Algorithm algorithm = Algorithm.HMAC256(secretKey);

    var token = JWT.create().withIssuer("vendas")
        .withExpiresAt(Instant.now().plus(Duration.ofHours(2)))
        .withSubject(user.getId().toString())
        .withClaim("userId", user.getId().toString())
        .sign(algorithm);

    return token;
  }
}
