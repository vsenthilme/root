package com.mnrclara.api.setup.repository;

import java.util.List;
import java.util.Optional;

import com.mnrclara.api.setup.model.status.StatusImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.setup.model.status.Status;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {

	public List<Status> findAll();
	public Status findByStatusId(Long statusId);
	
	// `LANG_ID`, `STATUS_ID`, `IS_DELETED`
	Optional<Status> findByLanguageIdAndStatusIdAndDeletionIndicator (String languageId, Long statusId, Long deletionIndicator);

	@Query(value = "select \n" +
			"ts.STATUS_ID AS statusId,\n" +
			"ts.STATUS_TEXT AS statusDesc,\n" +
			"concat(ts.STATUS_ID,'-',ts.STATUS_TEXT) AS statusIdDesc\n" +
			" from tblstatusid ts \n" +
			"where \n" +
			"ts.IS_DELETED=0 and ts.status_id in (33,34,35,46,51,56)", nativeQuery = true)
	public List<StatusImpl> getStatusForMobile();
}