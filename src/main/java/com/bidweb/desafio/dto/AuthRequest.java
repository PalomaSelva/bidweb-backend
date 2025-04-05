package com.bidweb.desafio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthRequest {
  private String email;
  private String password;
}
