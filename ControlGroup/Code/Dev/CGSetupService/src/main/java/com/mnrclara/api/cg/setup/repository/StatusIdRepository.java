package com.mnrclara.api.cg.setup.repository;

import com.mnrclara.api.cg.setup.model.status.StatusId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface StatusIdRepository extends JpaRepository<StatusId, Long>, JpaSpecificationExecutor<StatusId> {

	Optional<StatusId> findByLanguageIdAndStatusIdAndDeletionIndicator (String languageId, Long statusId, Long deletionIndicator);
}