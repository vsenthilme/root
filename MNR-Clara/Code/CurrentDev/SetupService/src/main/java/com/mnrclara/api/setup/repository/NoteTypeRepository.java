package com.mnrclara.api.setup.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.setup.model.notetype.NoteType;

@Repository
public interface NoteTypeRepository extends JpaRepository<NoteType, Long>{

	public List<NoteType> findAll();
	
	// `LANG_ID`, `CLASS_ID`, `NOTE_TYP_ID`, `IS_DELETED`
	public Optional<NoteType> findByLanguageIdAndClassIdAndNoteTypeIdAndDeletionIndicator 
	(String languageId, Long classId, Long noteTypeId, Long deletionIndicator);

	public NoteType findByNoteTypeId(Long noteTypeId);
}