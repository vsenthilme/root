package com.mnrclara.api.cg.setup.repository;

import com.mnrclara.api.cg.setup.model.relationshipid.RelationShipId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface RelationShipIdRepository extends JpaRepository<RelationShipId,Long>, JpaSpecificationExecutor<RelationShipId> {

    Optional<RelationShipId> findByCompanyIdAndLanguageIdAndRelationShipIdAndDeletionIndicator(
            String companyId, String languageId, Long relationShipId, Long deletionIndicator);
}
