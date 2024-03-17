package com.mnrclara.api.setup.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.setup.model.referral.Referral;

@Repository
public interface ReferralRepository extends JpaRepository<Referral, Long>{

	public List<Referral> findAll();
	public Referral findByReferralId(Long referralId);
}