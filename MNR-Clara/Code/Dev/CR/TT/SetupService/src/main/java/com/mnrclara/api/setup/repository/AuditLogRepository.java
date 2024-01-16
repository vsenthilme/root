package com.mnrclara.api.setup.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.setup.model.auditlog.AuditLog;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long>{

	public List<AuditLog> findAll();
	Optional<AuditLog> findByAuditLogNumber (String auditlogId);
	
	// `LANG_ID`, `CLASS_ID`, `AUD_LOG_NO`, `TRANS_ID`,  `TRANS_NO`, `IS_DELETED`
		Optional<AuditLog>
			findByLanguageIdAndClassIdAndAuditLogNumberAndTransactionIdAndTransactionNo 
			(String languageId, 
					Long classId, 
					String auditLogNumber, 
					Long transactionId, 
					String transactionNo);
}