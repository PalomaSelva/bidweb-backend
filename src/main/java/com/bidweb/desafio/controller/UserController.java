package com.bidweb.desafio.controller;

import com.bidweb.desafio.dto.UserRequest;
import com.bidweb.desafio.dto.UserResponse;
import com.bidweb.desafio.service.UserService;
import com.bidweb.desafio.util.ResponseUtils;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    @Operation(summary = "Cadastra um novo usuário", description = "Adiciona um novo usuário ao sistema.")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRequest request) {
        try {
            UserResponse userResponse = userService.createUser(request);
            return ResponseEntity.ok(userResponse);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ResponseUtils.createMessageResponse(e.getMessage()));
        }
    }

}
