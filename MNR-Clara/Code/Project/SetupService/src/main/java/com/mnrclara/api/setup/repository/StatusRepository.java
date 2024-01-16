package com.mnrclara.api.setup.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.setup.model.status.Status;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {

	public List<Status> findAll();
	public Status findByStatusId(Long statusId);
	
	// `LANG_ID`, `STATUS_ID`, `IS_DELETED`
	Optional<Status> findByLanguageIdAndStatusIdAndDeletionIndicator (String languageId, Long statusId, Long deletionIndicator);
}