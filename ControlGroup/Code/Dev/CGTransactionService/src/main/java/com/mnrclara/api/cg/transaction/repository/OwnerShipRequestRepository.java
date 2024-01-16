package com.mnrclara.api.cg.transaction.repository;

import com.mnrclara.api.cg.transaction.model.ownershiprequest.OwnerShipRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface OwnerShipRequestRepository extends JpaRepository<OwnerShipRequest, Long>, JpaSpecificationExecutor<OwnerShipRequest> {

    Optional<OwnerShipRequest> findByCompanyIdAndLanguageIdAndRequestIdAndDeletionIndicator(
            String companyId, String languageId, Long requestId, Long deletionIndicator);

}
