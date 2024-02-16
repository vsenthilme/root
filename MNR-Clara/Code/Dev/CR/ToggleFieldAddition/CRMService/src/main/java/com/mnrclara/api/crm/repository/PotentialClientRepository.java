package com.mnrclara.api.crm.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mnrclara.api.crm.model.potentialclient.PotentialClient;

@Repository
@Transactional
public interface PotentialClientRepository extends JpaRepository<PotentialClient, Long>, JpaSpecificationExecutor<PotentialClient> {
	
	public Optional<PotentialClient> findByPotentialClientId(@Param("potentialClientId") String potentialClientId);
	public List<PotentialClient> findByClassId(Long classId);
	
	@Query (value = "SELECT * FROM MNRCLARA.tblpotentialclientid WHERE \r\n"
			+ "MATCH (FIRST_NM, LAST_NM, FIRST_LAST_NM, LAST_FIRST_NM, EMAIL_ID, \r\n"
			+ "ADDRESS_LINE1, ADDRESS_LINE2, ADDRESS_LINE3, \r\n"
			+ "STATE, CITY, COUNTRY, MAIL_ADDRESS, OCCUPATION)\r\n"
			+ "AGAINST (:searchText)", nativeQuery = true)
	List<PotentialClient> findRecords(@Param(value = "searchText") String searchText);
	
	public List<PotentialClient> findByInquiryNumberAndDeletionIndicator(String inquiryNumber, Long deletionIndicator);
}
