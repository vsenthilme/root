package com.mnrclara.api.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.management.model.crm.Notes;

@Repository
public interface NotesRepository extends JpaRepository<Notes, String> {
	
	Notes findByNotesNumber(@Param("notesNumber") String notesNumber);
	
}
