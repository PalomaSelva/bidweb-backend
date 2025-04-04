package com.bidweb.desafio.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRequest {
    @NotBlank(message = "O nome do usuário é obrigatório")
    private String nome;

    @Email(message = "O campo (email) deve conter um e-mail válido.")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    private String password;
}
