package com.mnrclara.api.management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mnrclara.api.management.model.matternote.MatterNote;

@Repository
@Transactional
public interface MatterNoteRepository extends JpaRepository<MatterNote,Long>,JpaSpecificationExecutor<MatterNote> {

	public List<MatterNote> findAll();
	public List<MatterNote> findByNotesNumber(String clientNotesNumber);
	public List<MatterNote> findByNotesNumberAndDeletionIndicator(String matterNoteNumber, Long l);
	public List<MatterNote> findByMatterNumberAndDeletionIndicator(String matterNoteNumber, long l);
}