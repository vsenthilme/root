package com.iweb2b.api.master.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iweb2b.api.master.model.usercreation.UserCreation;

@Repository
public interface UserCreationRepository extends JpaRepository<UserCreation, Long>{

	public List<UserCreation> findAll();

	public Optional<UserCreation> findByUserIdAndDeletionIndicator(String userCreationId, long l);
}