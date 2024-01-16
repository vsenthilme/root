package com.mnrclara.api.accounting.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mnrclara.api.accounting.model.quotation.QuotationLineEntity;

@Repository
@Transactional
public interface QuotationLineRepository extends JpaRepository<QuotationLineEntity,Long>, JpaSpecificationExecutor<QuotationLineEntity> {
	public List<QuotationLineEntity> findAll();
	public Optional<QuotationLineEntity> 
		findByQuotationNoAndQuotationRevisionNoAndSerialNumberAndDeletionIndicator(String quotationNo, 
																					Long quotationRevisionNo, 
																					Long serialNumber, 
																					Long deletionIndicator);
	
	public List<QuotationLineEntity> 
		findByQuotationNoAndQuotationRevisionNoAndDeletionIndicator(String quotationNo, 
																				Long quotationRevisionNo, 
																				Long deletionIndicator);
	
	public Optional<QuotationLineEntity> findByQuotationNo(String quotationRevisionNo);
	
	@Query(value="SELECT MAX(SERIAL_NO) FROM tblquotationline "
			+ "WHERE QUOTE_NO = :quotationNo AND QUTO_REV_NO = :quotationRevisionNo", nativeQuery=true)
    public Long getMaxSerialNumber(@Param ("quotationNo") String quotationNo, @Param ("quotationRevisionNo") Long quotationRevisionNo);
}