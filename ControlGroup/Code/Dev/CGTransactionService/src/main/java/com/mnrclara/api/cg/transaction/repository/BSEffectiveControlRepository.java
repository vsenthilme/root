package com.mnrclara.api.cg.transaction.repository;


import com.mnrclara.api.cg.transaction.model.bseffectivecontrol.BSEffectiveControl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface BSEffectiveControlRepository extends JpaRepository<BSEffectiveControl, String>, JpaSpecificationExecutor<BSEffectiveControl> {


    Optional<BSEffectiveControl> findByCompanyIdAndLanguageIdAndValidationIdAndDeletionIndicator(String companyId, String languageId, Long validationId, Long deletionIndicator);
}
