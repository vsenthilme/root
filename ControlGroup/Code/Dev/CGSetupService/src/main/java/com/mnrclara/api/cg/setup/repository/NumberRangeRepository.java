package com.mnrclara.api.cg.setup.repository;


import com.mnrclara.api.cg.setup.model.numberange.NumberRange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NumberRangeRepository extends JpaRepository<NumberRange, Long>, JpaSpecificationExecutor<NumberRange> {


    Optional<NumberRange>
    findByLanguageIdAndCompanyIdAndNumberRangeCodeAndNumberRangeObjectAndDeletionIndicator
            (String languageId, String companyId, Long numberRangeCode, String numberRangeObject, Long deletionIndicator);

    Optional<NumberRange> findByNumberRangeCode(Long numberRangeCode);
    Optional<NumberRange> findByNumberRangeCodeAndNumberRangeObjectAndLanguageIdAndCompanyId(
            Long numberRangeCode,String numberRangeObject,String languageId,String companyId);
}
