package com.mnrclara.api.accounting.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mnrclara.api.accounting.model.paymentplan.PaymentPlanHeaderEntity;

@Repository
@Transactional
public interface PaymentPlanHeaderRepository extends PagingAndSortingRepository<PaymentPlanHeaderEntity,Long>, 
													JpaSpecificationExecutor<PaymentPlanHeaderEntity> {
	
	public List<PaymentPlanHeaderEntity> findAll();
	public Optional<PaymentPlanHeaderEntity> 
		findByPaymentPlanNumberAndPaymentPlanRevisionNoAndDeletionIndicator(
				String paymentPlanNumber, Long paymentPlanRevisionNo, Long deletionIndicator);
	
	public PaymentPlanHeaderEntity findTopByPaymentPlanNumberOrderByPaymentPlanRevisionNoDesc (String paymentPlanNumber);
}