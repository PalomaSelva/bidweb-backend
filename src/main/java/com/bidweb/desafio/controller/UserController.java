package com.bidweb.desafio.controller;

import com.bidweb.desafio.dto.UserRequest;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @PostMapping
    @Operation(summary ="Cadastra um novo usuário",description = "Adiciona um novo usuário ao sistema.")
    public ResponseEntity<?> createUser(@RequestBody UserRequest request){
    return ResponseEntity.ok(request);
    }
}
