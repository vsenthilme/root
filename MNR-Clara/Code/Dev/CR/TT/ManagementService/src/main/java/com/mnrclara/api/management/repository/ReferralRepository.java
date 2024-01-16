package com.mnrclara.api.management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.management.model.setup.Referral;

@Repository
public interface ReferralRepository extends JpaRepository<Referral, Long>{

	public List<Referral> findAll();
	public Referral findByReferralId(Long referralId);
}