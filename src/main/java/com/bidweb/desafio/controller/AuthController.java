package com.bidweb.desafio.controller;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bidweb.desafio.dto.AuthRequest;
import com.bidweb.desafio.service.AuthService;
import com.bidweb.desafio.util.ResponseUtils;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  private AuthService authService;

  @PostMapping("/login")
  @Operation(summary = "Login", description = "Login de usuário")
  public ResponseEntity<?> login(@RequestBody AuthRequest request) throws AuthenticationException {
    try {
      String token = this.authService.login(request);
      return ResponseEntity.ok(ResponseUtils.createMessageResponse(token));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(ResponseUtils.createMessageResponse("Credenciais inválidas"));
    }
  }
}
