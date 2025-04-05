package com.bidweb.desafio.controller;

import com.bidweb.desafio.dto.UserRequest;
import com.bidweb.desafio.dto.UserResponse;
import com.bidweb.desafio.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;

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
                    .body(createMessageResponse(e.getMessage()));
        }
    }

    private Map<String, String> createMessageResponse(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        return response;
    }
}
