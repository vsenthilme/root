package com.mnrclara.api.setup.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.setup.model.admincost.AdminCost;

@Repository
public interface AdminCostRepository extends JpaRepository<AdminCost, Long>{

	public List<AdminCost> findAll();
	Optional<AdminCost> findByAdminCostId (Long adminCostId);
}