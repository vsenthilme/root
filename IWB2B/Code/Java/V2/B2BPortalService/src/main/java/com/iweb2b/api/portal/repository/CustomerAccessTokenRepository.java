package com.iweb2b.api.portal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.iweb2b.api.portal.model.auth.CustomerAccessToken;

@Repository
@Transactional
public interface CustomerAccessTokenRepository extends JpaRepository<CustomerAccessToken, Long> {

	public CustomerAccessToken findByToken(String token);
}