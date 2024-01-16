package com.mnrclara.api.common.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.common.model.docusign.DocusignEnvelope;
import com.mnrclara.api.common.model.docusign.DocusignTransaction;

@Repository
public interface DocusignTransactionRepository extends JpaRepository<DocusignTransaction, Long> {

	public Optional<DocusignTransaction> findById(Long id);
	
	Optional<DocusignTransaction> findByClientId(String clientId);
	
	 @Query (value = "SELECT id, env_id AS envelopeId, sent_on FROM tbldocusigntrans  \r\n"
	 		+ " where sent_on=(select MAX(sent_on) AS sentOn \r\n"
	 		+ "	 		FROM tbldocusigntrans\r\n"
	 		+ "	 		WHERE client_id = :clientId \r\n"
	 		+ "	 		AND DOC_ID = :docId);", nativeQuery = true)
	 public DocusignEnvelope findLatestEnvId (@Param(value = "clientId") String clientId, @Param(value = "docId") String docId);
}