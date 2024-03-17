package com.mnrclara.api.accounting.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mnrclara.api.accounting.model.quotation.QuotationHeaderEntity;

@Repository
@Transactional
public interface QuotationHeaderRepository extends PagingAndSortingRepository<QuotationHeaderEntity,Long>, JpaSpecificationExecutor<QuotationHeaderEntity> {
	public List<QuotationHeaderEntity> findAll();
	public Optional<QuotationHeaderEntity> 
		findByQuotationNoAndQuotationRevisionNoAndDeletionIndicator(String quotationNo, 
																	Long quotationRevisionNo, 
																	Long deletionIndicator);
	public Optional<QuotationHeaderEntity> findByQuotationNoAndDeletionIndicator(String quotationNo, Long deletionIndicator);
	
	public Optional<QuotationHeaderEntity> findByMatterNumber(String matterNumber);
}