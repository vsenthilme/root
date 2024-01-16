package com.mnrclara.api.management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mnrclara.api.management.model.clientnote.ClientNote;

@Repository
@Transactional
public interface ClientNoteRepository extends JpaRepository<ClientNote, Long>, JpaSpecificationExecutor<ClientNote> {

	public List<ClientNote> findAll();

	public Optional<ClientNote> findByNotesNumber(String clientNotesNumber);
}