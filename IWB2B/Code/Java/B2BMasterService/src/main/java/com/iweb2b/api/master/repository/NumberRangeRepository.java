package com.iweb2b.api.master.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iweb2b.api.master.model.numberrange.NumberRange;

@Repository
public interface NumberRangeRepository extends JpaRepository<NumberRange, Long> {
	
	List<NumberRange> findAll();
	Optional<NumberRange> findById(Long id);
	
	public Optional<NumberRange> 
		findByNumberRangeCodeAndFiscalYearAndWarehouseId(Long numberRangeCode, Long fiscalYear, String warehouseId);
	
	//`LANG_ID`, `C_ID`, `NUM_RAN_CODE`, `NUM_RAN_OBJ`, `IS_DELETED`
	public Optional<NumberRange> 
		findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndNumberRangeCodeAndNumberRangeObjectAndFiscalYearAndDeletionIndicator 
		(String languageId, String companyCodeId, String plantId, String warehouseId, Long numberRangeCode, 
				String numberRangeObject, Long fiscalYear, Long deletionIndicator);
}


