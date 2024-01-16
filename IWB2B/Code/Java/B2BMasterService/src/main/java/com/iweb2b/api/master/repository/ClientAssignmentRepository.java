package com.iweb2b.api.master.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iweb2b.api.master.model.clientassignment.ClientAssignment;

@Repository
public interface ClientAssignmentRepository extends JpaRepository<ClientAssignment, Long>{

	public List<ClientAssignment> findAll();

	public Optional<ClientAssignment> findByPartnerIdAndDeletionIndicator(String partnerId, long l);
}