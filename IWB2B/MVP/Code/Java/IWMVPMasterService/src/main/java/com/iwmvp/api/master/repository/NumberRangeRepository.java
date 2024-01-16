package com.iwmvp.api.master.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.iwmvp.api.master.model.numberrange.NumberRange;
import org.springframework.transaction.annotation.Transactional;
@Repository
@Transactional
public interface NumberRangeRepository extends JpaRepository<NumberRange, Long>, JpaSpecificationExecutor<NumberRange> {
	Optional<NumberRange> findByCompanyIdAndNumberRangeCodeAndNumberRangeObjectAndLanguageIdAndDeletionIndicator(String companyId,Long numberRangeCode, String numberRangeObject, String languageId,Long deletionIndicator);
}


