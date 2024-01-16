package com.mnrclara.api.setup.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.setup.model.billingformat.BillingFormat;

@Repository
public interface BillingFormatRepository extends JpaRepository<BillingFormat, Long>{

	public List<BillingFormat> findAll();
	Optional<BillingFormat> findByBillingFormatId (Long billingFormatId);
}