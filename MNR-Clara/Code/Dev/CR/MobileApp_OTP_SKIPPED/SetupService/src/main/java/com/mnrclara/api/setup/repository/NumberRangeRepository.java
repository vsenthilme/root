package com.mnrclara.api.setup.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.setup.model.numberange.NumberRange;

@Repository
public interface NumberRangeRepository extends JpaRepository<NumberRange, Long> {
	
	List<NumberRange> findAll();
	Optional<NumberRange> findById(Long id);
	Optional<NumberRange> findByNumberRangeCode(Long numberRangeCode);
	Optional<NumberRange> findByNumberRangeCodeAndClassId(Long numberRangeCode, Long classId);
	
	//`LANG_ID`, `CLASS_ID`, `NUM_RAN_CODE`, `NUM_RAN_OBJ`, `IS_DELETED`
	Optional<NumberRange> 
		findByLanguageIdAndClassIdAndNumberRangeCodeAndNumberRangeObjectAndDeletionIndicator 
		(String languageId, Long classId, Long numberRangeCode, String numberRangeObject, Long deletionIndicator);
}
