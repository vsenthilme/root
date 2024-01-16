package com.iweb2b.api.master.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iweb2b.api.master.model.usermanagement.UserManagement;

@Repository
public interface UserManagementRepository extends JpaRepository<UserManagement, Long>{

	public List<UserManagement> findAll();

	public Optional<UserManagement> findByUserIdAndDeletionIndicator(String userManagementId, long l);
}