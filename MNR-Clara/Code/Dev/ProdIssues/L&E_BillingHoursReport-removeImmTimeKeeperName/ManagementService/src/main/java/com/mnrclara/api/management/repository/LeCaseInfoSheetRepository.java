package com.mnrclara.api.management.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.management.model.caseinfosheet.LeCaseInfoSheet;

@Repository
public interface LeCaseInfoSheetRepository extends MongoRepository<LeCaseInfoSheet, String> {

	Optional<LeCaseInfoSheet> findById(String id);

	@Query("{$or : [" + "{id : ?0}, " + "{firstNameLastName: ?1}, " + "{clientId : ?2}, " + "{matterNumber : ?3}, "
			+ "{statusId : ?4}, " + "{createdBy : ?5}, " + "{createdOn : { $gte: ?6, $lte: ?7 } } " + "]}")
	List<LeCaseInfoSheet> findByMultipleParams(String id, String firstNameLastName, String clientId,
			String matterNumber, Long statusId, String createdBy, Date startCreatedOn, Date endCreatedOn);
}
