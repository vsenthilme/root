package com.mnrclara.api.management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mnrclara.api.management.model.clientdocument.ClientDocument;

@Repository
@Transactional
public interface ClientDocumentRepository extends JpaRepository<ClientDocument, Long>, JpaSpecificationExecutor<ClientDocument> {

	public List<ClientDocument> findAll();
	public ClientDocument findByClientDocumentId(Long clientDocumentId);
	public Optional<ClientDocument> findByDocumentNo(String documentNo);

	public List<ClientDocument> findByLanguageIdAndClassIdAndClientIdAndDocumentNo (
				String languageId,
				Long classId,
				String clientId,
				String documentNo);
	
	public List<ClientDocument> findTopByLanguageIdAndClassIdAndClientIdAndDocumentNoOrderByDocumentUrlVersionDesc(String languageId,
			Long classId,
			String clientId,
			String documentNo);
}