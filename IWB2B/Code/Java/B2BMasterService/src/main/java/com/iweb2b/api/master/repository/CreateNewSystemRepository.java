package com.iweb2b.api.master.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iweb2b.api.master.model.createnewsystems.CreateNewSystem;


@Repository
public interface CreateNewSystemRepository extends JpaRepository<CreateNewSystem, Long>{

	public List<CreateNewSystem> findAll();

	public Optional<CreateNewSystem> findByPartnerIdAndDeletionIndicator(String partnerId, long l);
}