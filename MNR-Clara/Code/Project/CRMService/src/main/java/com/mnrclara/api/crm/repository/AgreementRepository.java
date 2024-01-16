package com.mnrclara.api.crm.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mnrclara.api.crm.model.agreement.Agreement;

@Repository
@Transactional
public interface AgreementRepository extends JpaRepository<Agreement, Long>, JpaSpecificationExecutor<Agreement> {
	
	public Optional<Agreement> findByAgreementCode(@Param("agreementCode") String agreementCode);
	public List<Agreement> findByClassId(Long classId);
	public Optional<Agreement> findByPotentialClientId(String potentialClientId);
	
	// `EMAIL_ID`, `AGREEMENT_URL_VER`, `AGREEMENT_URL`, `CLIENT_ID`, `AGREEMENT_CODE`
	@Query (value = "SELECT * FROM MNRCLARA.tblagreementid WHERE \r\n"
			+ "MATCH (EMAIL_ID, AGREEMENT_URL_VER, AGREEMENT_URL, CLIENT_ID, AGREEMENT_CODE)\r\n"
			+ "AGAINST (:searchText)", nativeQuery = true)
	List<Agreement> findRecords(@Param(value = "searchText") String searchText);
}
