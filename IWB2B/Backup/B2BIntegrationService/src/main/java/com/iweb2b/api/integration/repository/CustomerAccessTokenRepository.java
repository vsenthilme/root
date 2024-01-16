package com.iweb2b.api.integration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.iweb2b.api.integration.model.auth.CustomerAccessToken;

@Repository
@Transactional
public interface CustomerAccessTokenRepository extends JpaRepository<CustomerAccessToken, Long> {

	public CustomerAccessToken findByToken(String token);
}