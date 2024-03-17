package com.mnrclara.wrapper.core.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.wrapper.core.model.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	public List<User> findAll();
	Optional<User> findByEmail(String email);
	Optional<User> findByUsername(String username);
}