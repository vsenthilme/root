package com.mnrclara.api.cg.transaction.repository;

import com.mnrclara.api.cg.transaction.model.bscontrollinginterest.BSControllingInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface BSControllingInterestRepository extends JpaRepository<BSControllingInterest, Long>, JpaSpecificationExecutor<BSControllingInterest> {


    Optional<BSControllingInterest> findByCompanyIdAndLanguageIdAndValidationIdAndDeletionIndicator(
            String companyId, String languageId, Long validationId, Long deletionIndicator);
}
