package com.bidweb.desafio.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bidweb.desafio.dto.UserRequest;
import com.bidweb.desafio.dto.UserResponse;
import com.bidweb.desafio.model.User;
import com.bidweb.desafio.repository.UserRerpository;

@Service
public class UserService {

  @Autowired
  private UserRerpository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public UserResponse createUser(UserRequest request) {
    try {
      Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
      if (existingUser.isPresent()) {
        throw new RuntimeException("Usuário já cadastrado");
      }
      var passwordEncoded = passwordEncoder.encode(request.getPassword());
      User newUser = new User();
      newUser.setName(request.getName());
      newUser.setEmail(request.getEmail());
      newUser.setPassword(passwordEncoded);
      newUser.setCreatedAt(LocalDateTime.now());
      newUser.setUpdatedAt(LocalDateTime.now());
      userRepository.save(newUser);
      return new UserResponse(newUser);
    } catch (Exception ex) {
      throw new RuntimeException(ex.getMessage());
    }
  }

  public UserResponse getUserById(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    return new UserResponse(user);
  }
}
