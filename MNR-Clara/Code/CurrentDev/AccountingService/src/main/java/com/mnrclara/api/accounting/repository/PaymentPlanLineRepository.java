package com.mnrclara.api.accounting.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mnrclara.api.accounting.model.paymentplan.PaymentPlanLineEntity;

@Repository
@Transactional
public interface PaymentPlanLineRepository extends JpaRepository<PaymentPlanLineEntity,Long>, JpaSpecificationExecutor<PaymentPlanLineEntity> {
	
	public List<PaymentPlanLineEntity> findAll();
	
	public Optional<PaymentPlanLineEntity> 
		findByPaymentPlanNumberAndPaymentPlanRevisionNoAndItemNumberAndDeletionIndicator(
				String paymentPlanNumber, Long paymentPlanRevisionNo, Long itemNumber, Long deletionIndicator);
	
	public List<PaymentPlanLineEntity> 
		findByPaymentPlanNumberAndPaymentPlanRevisionNoAndDeletionIndicator(
			String paymentPlanNumber, Long paymentPlanRevisionNo, Long deletionIndicator);
	
	public List<PaymentPlanLineEntity> findByReminderDateBetweenAndDeletionIndicator(Date dueDate1, Date dueDate2, Long deletionIndicator);

	public List<PaymentPlanLineEntity> findByReminderDateBetweenAndDeletionIndicatorAndReferenceField10IsNull(Date date,
			Date date2, long l);
}