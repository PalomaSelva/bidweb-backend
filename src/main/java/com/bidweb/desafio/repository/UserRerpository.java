package com.bidweb.desafio.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bidweb.desafio.model.User;

public interface UserRerpository extends JpaRepository<User,Long>{
  Optional<User> findByEmail(String email);
}
