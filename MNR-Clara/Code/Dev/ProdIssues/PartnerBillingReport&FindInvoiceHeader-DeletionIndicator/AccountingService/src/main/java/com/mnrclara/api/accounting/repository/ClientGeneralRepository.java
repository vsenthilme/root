package com.mnrclara.api.accounting.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mnrclara.api.accounting.model.management.ClientGeneral;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public interface ClientGeneralRepository extends PagingAndSortingRepository<ClientGeneral, Long>, 
	JpaSpecificationExecutor<ClientGeneral>,DynamicNativeQuery {

	public ClientGeneral findByClientId(String clientId);

	@Query(value = "select ti.ASSIGNED_USR_ID as assignedUserId, count(ti.ASSIGNED_USR_ID) as countAssignedUserId \n" +
			"from tblinquiryid ti \n" +
			"join tblclientgeneralid cg on ti.INQ_NO = cg.INQ_NO \n" +
			"where ti.ASSIGNED_USR_ID is not null and ti.ASSIGNED_USR_ID not in (:assignedUser) and ti.CLASS_ID = :classId and ti.IS_DELETED = 0 and \n" +
			"cg.CTD_ON BETWEEN :fromDate AND :toDate GROUP BY ti.ASSIGNED_USR_ID",nativeQuery = true)
	public List<String[]> getAssignedUserId(@Param("assignedUser") List<String> assignedUser,
											@Param("classId") Long classId,
											@Param("fromDate") Date fromDate,
											@Param("toDate") Date toDate);

	@Query(value="select count(cg.INQ_NO) from tblinquiryid i \r\n"
			+ "join tblclientgeneralid cg on i.INQ_NO = cg.INQ_NO \r\n"
			+ "WHERE i.ASSIGNED_USR_ID is not null and i.ASSIGNED_USR_ID in (:assignedUserId) AND i.CLASS_ID = :classId AND cg.CTD_ON BETWEEN :fromDate AND :toDate \r\n"
			+ "AND i.IS_DELETED = 0 and cg.IS_DELETED = 0 ", nativeQuery=true)
	public BigDecimal getClientCountForInquiryNo(@Param("assignedUserId") String assignedUserId,
												 @Param("classId") Long classId,
												 @Param("fromDate") Date fromDate,
												 @Param("toDate") Date toDate);
	
	@Query(value="select b.FIRST_LAST_NM AS FirstName\r\n"
			+ "from tblclientgeneralid a, tblclientgeneralid b \r\n"
			+ "where a.corp_client_id = b.client_id and a.client_id = :clientId", nativeQuery=true)
	public String getCorpClientIdFirstName(@Param("clientId") String clientId);
}