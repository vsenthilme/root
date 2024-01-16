package com.mnrclara.api.crm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.crm.model.notes.Notes;

@Repository
public interface NotesRepository extends JpaRepository<Notes, String> {
	
	Optional<Notes> findByNotesNumber(@Param("notesNumber") String notesNumber);
	
}
