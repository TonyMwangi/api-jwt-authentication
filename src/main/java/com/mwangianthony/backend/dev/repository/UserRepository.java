package com.mwangianthony.backend.dev.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mwangianthony.backend.dev.entities.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	Optional<User> findByEmail(String email);

}
