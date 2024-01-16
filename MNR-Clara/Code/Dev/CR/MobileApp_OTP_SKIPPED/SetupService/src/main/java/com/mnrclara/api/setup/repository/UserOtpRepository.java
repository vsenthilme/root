package com.mnrclara.api.setup.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.setup.model.userprofile.UserOTP;

@Repository
public interface UserOtpRepository extends JpaRepository<UserOTP, Long>{

	Optional<UserOTP> findByUserId(String id);
}