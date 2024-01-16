package com.mnrclara.api.management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mnrclara.api.management.model.matterdocument.MatterDocument;

@Repository
@Transactional
public interface MatterDocumentRepository extends JpaRepository<MatterDocument,Long>,JpaSpecificationExecutor<MatterDocument> {

	public Optional<MatterDocument> findByDocumentNo(String mattertDocumentId);
	public Optional<MatterDocument> findByMatterDocumentId(Long matterDocumentId);
	
	public List<MatterDocument> findByLanguageIdAndClassIdAndClientIdAndMatterNumberAndDocumentNo (
			String languageId,
			Long classId,
			String clientId,
			String matterNumber,
			String documentNo);
	
	public List<MatterDocument> findTopByLanguageIdAndClassIdAndMatterNumberAndClientIdAndDocumentNoOrderByDocumentUrlVersionDesc (
			String languageId,
			Long classId,
			String matterNumber,
			String clientId,
			String documentNo);

	@Query(value = "SELECT matter_text FROM tblmattergenaccid WHERE \r\n"
			+ "matter_no = :matterNumber and is_deleted = 0", nativeQuery = true)
	public String getDescription(@Param(value = "matterNumber") String matterNumber);
}