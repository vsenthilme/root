package com.mnrclara.api.management.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.mnrclara.api.management.model.dto.MatterAssignmentImpl;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mnrclara.api.management.model.matterassignment.MatterAssignment;

@Repository
@Transactional
public interface MatterAssignmentRepository extends PagingAndSortingRepository<MatterAssignment,Long>, 
												JpaSpecificationExecutor<MatterAssignment> {

	public List<MatterAssignment> findAll();
	public Optional<MatterAssignment> findByMatterNumber(String matterNumber);
	public List<MatterAssignment> findByMatterNumberAndPartnerIn(String matterNumber, List<String> partner);
	public List<MatterAssignment> findByMatterNumberAndResponsibleTimeKeeperIn(String matterNumber, List<String> responsibleTimeKeeper);

	//Client Name
	@Query(value = "SELECT FIRST_LAST_NM FROM tblclientgeneralid WHERE CLIENT_ID = :clientId ", nativeQuery = true)
	public String findClientName(@Param("clientId") String clientId);

	//Streaming
	@Async
	@Query(value = "SELECT \n"
			+"MA.CASEINFO_NO caseInformationNo, \n"
			+"MA.LANG_ID languageId, \n"
			+"MA.CLASS_ID classId, \n"
			+"MA.MATTER_NO matterNumber, \n"
			+"MA.MATTER_TEXT matterDescription, \n"
			+"MA.CLIENT_ID clientId, \n"
			+"MA.CASE_CATEGORY_ID caseCategoryId, \n"
			+"MA.CASE_SUB_CATEGORY_ID caseSubCategoryId, \n"
			+"MA.CASE_OPEN_DATE caseOpenedDate, \n"
			+"MA.PARTNER partner, \n"
			+"MA.ORIGINATING_TK originatingTimeKeeper, \n"
			+"MA.RESPONSIBLE_TK responsibleTimeKeeper, \n"
			+"MA.ASSIGNED_TK assignedTimeKeeper, \n"
			+"MA.LEGAL_ASSIST legalAssistant, \n"
			+"MA.STATUS_ID statusId, \n"
			+"MA.IS_DELETED deletionIndicator, \n"
			+"MA.REF_FIELD_1 referenceField1, \n"
			+"MA.REF_FIELD_2 referenceField2, \n"
			+"MA.REF_FIELD_3 referenceField3, \n"
			+"MA.REF_FIELD_4 referenceField4, \n"
			+"MA.REF_FIELD_5 referenceField5, \n"
			+"MA.REF_FIELD_6 referenceField6, \n"
			+"MA.REF_FIELD_7 referenceField7, \n"
			+"MA.REF_FIELD_8 referenceField8, \n"
			+"MA.REF_FIELD_9 referenceField9, \n"
			+"MA.REF_FIELD_10 referenceField10, \n"
			+"MA.CTD_BY createdBy, \n"
			+"MA.CTD_ON createdOn, \n"
			+"MA.UTD_BY UpdatedBy, \n"
			+"MA.UTD_ON updatedOn, \n"
			+"CG.FIRST_LAST_NM clientName \n"
			+"from tblmatterassignmentid MA \n"
			+"left join tblclientgeneralid CG on CG.client_id = MA.client_id \n"
			+"where \n"
			+"(COALESCE(:matterNumber) IS NULL OR (MA.MATTER_NO IN (:matterNumber))) and \n"
			+"(COALESCE(:matterDescription) IS NULL OR (MA.MATTER_TEXT IN (:matterDescription))) and \n"
			+"(COALESCE(:clientId) IS NULL OR (MA.CLIENT_ID IN (:clientId))) and \n"
			+"(COALESCE(:caseCategoryId) IS NULL OR (MA.CASE_CATEGORY_ID IN (:caseCategoryId))) and \n"
			+"(COALESCE(:caseSubCategoryId) IS NULL OR (MA.CASE_SUB_CATEGORY_ID IN (:caseSubCategoryId))) and \n"
			+"(COALESCE(:partner) IS NULL OR (MA.PARTNER IN (:partner))) and \n"
			+"(COALESCE(:originatingTimeKeeper) IS NULL OR (MA.ORIGINATING_TK IN (:originatingTimeKeeper))) and \n"
			+"(COALESCE(:responsibleTimeKeeper) IS NULL OR (MA.RESPONSIBLE_TK IN (:responsibleTimeKeeper))) and \n"
			+"(COALESCE(:assignedTimeKeeper) IS NULL OR (MA.ASSIGNED_TK IN (:assignedTimeKeeper))) and \n"
			+"(COALESCE(:legalAssistant) IS NULL OR (MA.LEGAL_ASSIST IN (:legalAssistant))) and \n"
			+"(COALESCE(:statusId) IS NULL OR (MA.status_id IN (:statusId))) and \n"
			+ "MA.is_deleted=0 ", nativeQuery=true)
	public Stream<MatterAssignmentImpl> getMatterAssignmentListStreaming(@Param("matterNumber") List<String> matterNumber,
																		 @Param("matterDescription") List<String> matterDescription,
																		 @Param("clientId") List<String> clientId,
																		 @Param("caseCategoryId") List<Long> caseCategoryId,
																		 @Param("caseSubCategoryId") List<Long> caseSubCategoryId,
																		 @Param("partner") List<String> partner,
																		 @Param("originatingTimeKeeper") List<String> originatingTimeKeeper,
																		 @Param("responsibleTimeKeeper") List<String> responsibleTimeKeeper,
																		 @Param("assignedTimeKeeper") List<String> assignedTimeKeeper,
																		 @Param("legalAssistant") List<String> legalAssistant,
																		 @Param("statusId") List<Long> statusId);
}