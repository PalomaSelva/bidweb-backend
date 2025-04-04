package com.bidweb.desafio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bidweb.desafio.model.User;

public interface UserRerpository extends JpaRepository<User,Long>{
  
}
