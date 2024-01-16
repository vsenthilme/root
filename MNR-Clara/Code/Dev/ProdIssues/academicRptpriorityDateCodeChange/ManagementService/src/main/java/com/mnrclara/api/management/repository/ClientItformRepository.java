package com.mnrclara.api.management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.management.model.clientitform.ClientItform;

@Repository
public interface ClientItformRepository extends JpaRepository<ClientItform, Long> {

	public List<ClientItform> findAll();

	Optional<ClientItform> findByIntakeFormId(Long clientItformId);
}