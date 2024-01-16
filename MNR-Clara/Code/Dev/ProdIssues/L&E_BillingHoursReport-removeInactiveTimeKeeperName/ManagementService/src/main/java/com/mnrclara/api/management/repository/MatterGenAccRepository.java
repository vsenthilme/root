package com.mnrclara.api.management.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.mnrclara.api.management.model.mattergeneral.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mnrclara.api.management.model.dto.IKeyValuePair;
import com.mnrclara.api.management.model.dto.IMatterDropDown;

@Repository
@Transactional
public interface MatterGenAccRepository extends PagingAndSortingRepository<MatterGenAcc, Long>,
        JpaSpecificationExecutor<MatterGenAcc>,
        DynamicNativeQuery {
    @QueryHints(@javax.persistence.QueryHint(name = "org.hibernate.fetchSize", value = "1000"))
    public List<MatterGenAcc> findAll();

    public List<MatterGenAcc> findBySentToQBAndDeletionIndicator(Long sentToQB, Long deleteFlag);

    /**
     * --------------------------------------------------------------------------------------------
     **/
    public MatterGenAcc findTopBySentToQBAndDeletionIndicatorOrderByMatterNumberDesc(Long sentToQB, Long deleteFlag);

    /**
     * --------------------------------------------------------------------------------------------
     **/

    public List<MatterGenAcc> findAllByDeletionIndicator(Long deleteFlag);

    public List<MatterGenAcc> findAllByStatusIdAndDeletionIndicator(Long statusId, Long deleteFlag);

    public Optional<MatterGenAcc> findByMatterNumber(String matterNumber);

    public List<MatterGenAcc> findByMatterNumberInAndAndDeletionIndicator(List<String> matterNumber, Long deleteFlag);

    public List<MatterGenAcc> findByMatterNumberIn(List<String> matterNumber);

    public List<MatterGenAcc> findByClientId(String clientId);

    public List<MatterGenAcc> findByClassIdAndDeletionIndicator(Long classId, Long deleteFlag);

    public MatterGenAcc findByClassIdAndClientIdAndMatterNumberAndDeletionIndicator(Long classId,
                                                                                    String clientId, String matterNumber, Long deleteFlag);

    public List<MatterGenAcc> findByClassIdAndStatusIdAndDeletionIndicator(Long classId, Long statusId, Long deleteFlag);

    // Accounting Service - PreBill Functionality
    public List<MatterGenAcc> findByClassIdAndBillingModeIdInAndStatusIdNotInAndMatterNumberInAndDeletionIndicator(
            Long classId, List<String> billingModeId, List<Long> statusId, List<String> matterNumber, Long deleteFlag);

    // Accounting Service - PreBill Functionality - BillByGroup
    public List<MatterGenAcc> findByClassIdAndBillingModeIdInAndStatusIdNotAndMatterNumberInAndDeletionIndicator(
            Long classId, List<String> billingModeId, Long statusId, List<String> matterNumber, Long deleteFlag);

    // Accounting Service - PreBill Functionality - BillGroup
    public List<MatterGenAcc> findByClassIdAndBillingModeIdInAndMatterNumberInAndDeletionIndicator(
            Long classId, List<String> billingModeIds, List<String> matterNumber, Long deleteFlag);

    public List<MatterGenAcc> findByClassIdAndBillingModeIdInAndStatusIdNotInAndDeletionIndicator(
            Long classId, List<String> billingModeId, List<Long> statusId, Long deleteFlag);

    public List<MatterGenAcc> findByClassIdAndBillingModeIdAndStatusIdAndMatterNumberInAndDeletionIndicator(
            Long classId, String billingModeId, Long statusId, List<String> matterNumbers, long deleteFlag);

    public MatterGenAcc findTopByOrderByCreatedOnDesc();

    @Query(value = "SELECT * FROM MNRCLARA.tblmattergenaccid WHERE \r\n"
            + " MATCH (CLIENT_ID, MATTER_NO, MATTER_TEXT) \r\n"
            + " AGAINST (:searchText)", nativeQuery = true)
    List<MatterGenAcc> findRecords(@Param(value = "searchText") String searchText);

    public MatterGenAcc findTopBySentToQBAndDeletionIndicatorOrderByCreatedOn(long l, long m);

    //----------------------------------------------------------------------------------------------------------
    @Query(value = "SELECT client.FIRST_LAST_NM AS CLIENT_NAME\r\n"
            + "FROM tblmattergenaccid matter \r\n"
            + "JOIN tblclientgeneralid client ON matter.CLIENT_ID = client.CLIENT_ID \r\n"
            + "WHERE matter.MATTER_NO = :matterNumber", nativeQuery = true)
    public String getClientName(@Param(value = "matterNumber") String matterNumber);

    @Query(value = "SELECT cc.CASE_CATEGORY AS caseCategory\r\n"
            + "FROM tblmattergenaccid matter \r\n"
            + "JOIN tblcasecategoryid cc ON matter.CASE_CATEGORY_ID = cc.CASE_CATEGORY_ID\r\n"
            + "WHERE matter.MATTER_NO = :matterNumber", nativeQuery = true)
    public String getCaseCategory(@Param(value = "matterNumber") String matterNumber);

    @Query(value = "SELECT cs.CASE_SUB_CATEGORY AS caseSubCategory\r\n"
            + "FROM tblmattergenaccid matter \r\n"
            + "JOIN tblcasesubcategoryid cs ON matter.CASE_SUB_CATEGORY_ID = cs.CASE_SUB_CATEGORY_ID\r\n"
            + "WHERE matter.MATTER_NO = :matterNumber", nativeQuery = true)
    public String getSubCaseCategory(@Param(value = "matterNumber") String matterNumber);

    @Query(value = "SELECT status.STATUS_TEXT AS statusText\r\n"
            + "FROM tblmattergenaccid matter \r\n"
            + "JOIN tblstatusid status ON matter.STATUS_ID = status.STATUS_ID\r\n"
            + "WHERE matter.MATTER_NO = :matterNumber", nativeQuery = true)
    public String getStatusId(@Param(value = "matterNumber") String matterNumber);

    @Query(value = "SELECT CLS.CLASS AS VALUE \r\n"
            + "FROM tblmattergenaccid MATTER \r\n"
            + "JOIN tblclassid CLS ON MATTER.CLASS_ID = CLS.CLASS_ID\r\n"
            + "WHERE MATTER.MATTER_NO = :matterNumber", nativeQuery = true)
    public String getClassName(@Param(value = "matterNumber") String matterNumber);

    //---------------Attorney Productivity Report------------------------------------------------------------------------------------
    @Query(value = "select \n"
            + "tc.class classId, \n"
            + "tcg.client_id clientId, \n"
            + "tcg.first_last_nm clientName, \n"
            + "tmg.matter_no matterNumber, \n"
            + "tmg.matter_text matterDescription, \n"
            + "tcc.case_category caseCategoryId, \n"
            + "tcs.case_sub_category caseSubCategoryId, \n"
            + "tmg.flat_fee flatFee, \n"
            + "tma.originating_tk originatingTimeKeeper, \n"
            + "tma.assigned_tk assignedTimeKeeper, \n"
            + "tma.responsible_tk responsibleTimeKeeper, \n"
            + "tma.LEGAL_ASSIST legalAssistant, \n"
            + "tma.REF_FIELD_2 paralegal, \n"
            + "ts.status_text statusDescription, \n"
            + "tmg.case_open_date caseOpenDate, \n"
            + "tmg.case_filed_date caseFiledDate \n"
            + "from tblmattergenaccid tmg \n"
            + "left join tblclassid tc on tc.class_id=tmg.class_id \n"
            + "left join tblcasecategoryid tcc on tcc.case_category_id=tmg.case_category_id \n"
            + "left join tblcasesubcategoryid tcs on tcs.case_sub_category_id=tmg.case_sub_category_id \n"
            + "left join tblclientgeneralid tcg on tcg.client_id=tmg.client_id \n"
            + "left join tblmatterassignmentid tma on tma.matter_no=tmg.matter_no \n"
            + "left join tblstatusid ts on ts.status_id = tmg.status_id \n"
            + "where \n"
            + "(COALESCE(:matterNumber,null) IS NULL OR (tmg.matter_no IN (:matterNumber))) and \n"
            + "(COALESCE(:classId,null) IS NULL OR (tmg.class_id IN (:classId))) and \n"
            + "(COALESCE(:responsibleTimeKeeper,null) IS NULL OR (tma.responsible_tk IN (:responsibleTimeKeeper))) and \n"
            + "(COALESCE(:assignedTimeKeeper,null) IS NULL OR (tma.assigned_tk IN (:assignedTimeKeeper))) and \n"
            + "(COALESCE(:originatingTimeKeeper,null) IS NULL OR (tma.originating_tk IN (:originatingTimeKeeper))) and \n"
            + "(COALESCE(:caseCategory,null) IS NULL OR (tcc.case_category_id IN (:caseCategory))) and \n"
            + "(COALESCE(:caseSubCategory,null) IS NULL OR (tcs.case_sub_category_id IN (:caseSubCategory))) and \n"
            + "(COALESCE(:startDate) IS NULL OR (tmg.case_open_date BETWEEN :startDate AND :endDate)) and \n"
            + "(COALESCE(:caseFiledStartDate) IS NULL OR (tmg.case_filed_date BETWEEN :caseFiledStartDate AND :caseFiledEndDate)) and \n"
            + "tmg.is_deleted = 0", nativeQuery = true)
    public List<AttorneyProductivityReportImpl> getAttorneyProductivityReport(
            @Param(value = "classId") List<Long> classId,
            @Param(value = "matterNumber") List<String> matterNumber,
            @Param(value = "responsibleTimeKeeper") List<String> responsibleTimeKeeper,
            @Param(value = "assignedTimeKeeper") List<String> assignedTimeKeeper,
            @Param(value = "originatingTimeKeeper") List<String> originatingTimeKeeper,
            @Param(value = "caseCategory") List<String> caseCategory,
            @Param(value = "caseSubCategory") List<String> caseSubCategory,
            @Param(value = "startDate") Date startDate,
            @Param(value = "endDate") Date endDate,
            @Param(value = "caseFiledStartDate") Date caseFiledStartDate,
            @Param(value = "caseFiledEndDate") Date caseFiledEndDate);

    //Attorney Productivity Report - FeeShare Calculation
    @Query(value = "select \n"
            + "tmf.tk_code timeKeeperCode, \n"
            + "tmf.fee_share_percentage feeSharePercentage \n"
            + "from tblmatterfeesharingid tmf \n"
            + "where \n"
            + "tmf.matter_no IN (:matterNumber) and \n"
            + "tmf.is_deleted = 0", nativeQuery = true)
    public List<AttorneyProductivityReportFeeShareImpl> getAttorneyFeeSharing(@Param(value = "matterNumber") String matterNumber);

    //Attorney Productivity Report - Billed Fees Calculation
    @Query(value = "select \n"
            + "sum(il.bill_amount) billedFees \n"
            + "from tblinvoiceline il \n"
            + "join tblinvoiceheader ih on ih.invoice_no = il.invoice_no \n"
            + "where il.item_no = 1 and ih.is_deleted = 0 and \n"
            + "ih.matter_no IN (:matterNumber) and \n"
            + "ih.posting_date between :startDate AND :endDate", nativeQuery = true)
    public Double getAttorneyBilledFees(@Param(value = "matterNumber") String matterNumber,
                                        @Param(value = "startDate") Date startDate,
                                        @Param(value = "endDate") Date endDate);

    //Matter Immigration Report
    @Query(value = "select *,\n"
            + "(case when x1.corporationClientId is not null and x1.clientCategoryId = 3 then \n"
            + "	(select first_last_nm from tblclientgeneralid where is_deleted = 0 and client_id=x1.corporationClientId)\n"
            + "	else x1.imPetitionerName end) as petitionerName,\n"
            + "(case when (clientCategoryId is not null and (clientCategoryId = 4 or clientCategoryId = 3)) then \n"
            + "	x1.clientName else null end) as firstNameLastName,\n"
            + "(case when x1.imPetitionerName is not null and  x1.clientCategoryId = 4 then \n"
            + "	(select cont_no from tblclientgeneralid where client_id=x1.imPetitionerName and is_deleted=0)\n"
            + "	when x1.corporationClientId is not null and  x1.clientCategoryId = 3 then \n"
            + "	(select cont_no from tblclientgeneralid where client_id=x1.corporationClientId and is_deleted=0)\n"
            + "	else null end) as petitionerContactNumber,\n"
            + "(case when x1.imPetitionerName is not null and  x1.clientCategoryId = 4 then \n"
            + "	(select email_id from tblclientgeneralid where client_id=x1.imPetitionerName and is_deleted=0)\n"
            + "	when x1.corporationClientId is not null and  x1.clientCategoryId = 3 then \n"
            + "	(select email_id from tblclientgeneralid where client_id=x1.corporationClientId and is_deleted=0)\n"
            + "	else null end) as petitionerEmailId,\n"
            + "(case when x1.clientName is not null then x1.contactNumber else null end) as beneficiaryContactNumber,\n"
            + "(case when x1.clientName is not null then x1.emailId else null end) as benerficiaryEmailId\n"
            + "from\n"
            + "(select \n"
            + "tcg.client_id clientId, \n"
            + "tcg.first_last_nm clientName,\n"
            + "tcg.client_cat_id clientCategoryId,\n"
            + "tcg.corp_client_id corporationClientId,\n"
            + "tcg.ref_field_2 imPetitionerName,\n"
            + "tcg.ctd_on clientOpenedDate,\n"
            + "tcg.cont_no contactNumber,\n"
            + "tcg.email_id emailId,\n"
            + "tcg.pot_client_id potentialClientId,\n"
            + "tmg.matter_no matterNumber, \n"
            + "tmg.matter_text matterDescription, \n"
            + "tmg.case_filed_date caseFiledDate,\n"
            + "tmg.ctd_by createdBy,\n"
            + "tmg.ref_field_12 referredBy,\n"
            + "(case when tmg.status_id = 30 then 'INACTIVE' else 'ACTIVE' end) status,\n"
            + "(case when RIGHT(tmg.matter_no,1)=1 then 'Y' else 'N4' end) newMatterExistingClient,\n"
            + "tbm.bill_mode_text billModeText,\n"
            + "tmg.case_open_date matterOpenedDate,\n"
            + "tmg.flat_fee flatFeeAmount,\n"
            + "tmg.admin_cost expensesFee,\n"
            + "tmg.case_closed_date matterClosedDate,\n"
            + "tmg.ref_field_14 retainerPaid,\n"
            + "tcc.case_category caseCategoryId, \n"
            + "tcs.case_sub_category caseSubCategoryId, \n"
            + "tma.originating_tk originatingTimeKeeper, \n"
            + "tma.assigned_tk assignedTimeKeeper, \n"
            + "tma.responsible_tk responsibleTimeKeeper, \n"
            + "tma.legal_assist legalAssistant,\n"
            + "tma.ref_field_1 lawClerk,\n"
            + "tma.ref_field_2 paralegal,\n"
            + "tma.partner partner,\n"
            + "tn.note_text attorneysNotes,\n"
            + "(select note_text from tblmatternoteid where is_deleted=0 and matter_no=tmg.matter_no order by note_no limit 1) matterNotes\n"
            + "from tblmattergenaccid tmg \n"
            + "left join tblcasecategoryid tcc on tcc.case_category_id=tmg.case_category_id \n"
            + "left join tblcasesubcategoryid tcs on tcs.case_sub_category_id=tmg.case_sub_category_id \n"
            + "left join tblclientgeneralid tcg on tcg.client_id=tmg.client_id \n"
            + "left join tblmatterassignmentid tma on tma.matter_no=tmg.matter_no\n"
            + "left join tblbillingmodeid tbm on tbm.bill_mode_id=tmg.bill_mode_id\n"
            + "left join tblpotentialclientid tpc on tpc.pot_client_id=tcg.pot_client_id\n"
            + "left join tblnotesid tn on tn.note_no=tpc.pc_note_no\n"
            + "where \n"
            + "(COALESCE(:responsibleTimeKeeper,null) IS NULL OR (tma.responsible_tk IN (:responsibleTimeKeeper))) and \n"
            + "(COALESCE(:assignedTimeKeeper,null) IS NULL OR (tma.assigned_tk IN (:assignedTimeKeeper))) and \n"
            + "(COALESCE(:originatingTimeKeeper,null) IS NULL OR (tma.originating_tk IN (:originatingTimeKeeper))) and \n"
            + "(COALESCE(:legalAssistant,null) IS NULL OR (tma.legal_assist IN (:legalAssistant))) and \n"
            + "(COALESCE(:lawClerks,null) IS NULL OR (tma.ref_field_1 IN (:lawClerks))) and \n"
            + "(COALESCE(:partner,null) IS NULL OR (tma.partner IN (:partner))) and \n"
            + "(COALESCE(:caseCategory,null) IS NULL OR (tcc.case_category_id IN (:caseCategory))) and \n"
            + "(COALESCE(:caseSubCategory,null) IS NULL OR (tcs.case_sub_category_id IN (:caseSubCategory))) and \n"
            + "(COALESCE(:billingModeId,null) IS NULL OR (tmg.bill_mode_id IN (:billingModeId))) and \n"
            + "(COALESCE(:statusId,null) IS NULL OR (tmg.status_id IN (:statusId))) and \n"
            + "(COALESCE(:refferedBy,null) IS NULL OR (tmg.ref_field_12 IN (:refferedBy))) and \n"
            + "(COALESCE(:classId,null) IS NULL OR (tmg.class_id IN (:classId))) and \n"
            + "(COALESCE(:startDate) IS NULL OR (tmg.case_open_date BETWEEN :startDate AND :endDate)) and \n"
            + "(COALESCE(:closedStartDate) IS NULL OR (tmg.case_closed_date BETWEEN :closedStartDate AND :closedEndDate)) and \n"
            + "tmg.is_deleted=0) As x1", nativeQuery = true)
    public List<ImmigrationReportImpl> getImmigrationReport(
            @Param(value = "responsibleTimeKeeper") List<String> responsibleTimeKeeper,
            @Param(value = "assignedTimeKeeper") List<String> assignedTimeKeeper,
            @Param(value = "originatingTimeKeeper") List<String> originatingTimeKeeper,
            @Param(value = "legalAssistant") List<String> legalAssistant,
            @Param(value = "lawClerks") List<String> lawClerks,
            @Param(value = "caseCategory") List<Long> caseCategory,
            @Param(value = "caseSubCategory") List<Long> caseSubCategory,
            @Param(value = "partner") List<String> partner,
            @Param(value = "billingModeId") List<String> billingModeId,
            @Param(value = "statusId") List<Long> statusId,
            @Param(value = "refferedBy") List<String> refferedBy,
            @Param(value = "classId") Long classId,
            @Param(value = "startDate") Date startDate,
            @Param(value = "endDate") Date endDate,
            @Param(value = "closedStartDate") Date closedStartDate,
            @Param(value = "closedEndDate") Date closedEndDate);

    //LNE Report
    @Query(value = "select *, \n"
            + "(case when x1.corporationClientId is not null then  \n"
            + "(select first_last_nm from tblclientgeneralid where client_id=x1.corporationClientId) else null end) corporateName \n"
            + "from \n"
            + "(select  \n"
            + "tcg.client_id clientId,  \n"
            + "tcg.corp_client_id corporationClientId, \n"
            + "tcg.pot_client_id potentialClientId, \n"
            + "tmg.matter_no matterNumber, \n"
            + "tmg.matter_text matterDescription, \n"
            + "tmg.caseinfo_no caseInfoNumber, \n"
            + "tmg.ctd_by createdBy, \n"
            + "tmg.utd_by updatedBy, \n"
            + "tmg.ref_field_12 referredBy, \n"
            + "(case when tmg.status_id = 30 then 'INACTIVE' else 'ACTIVE' end) status, \n"
            + "tbm.bill_mode_text billingmodeText, \n"
            + "tmg.case_open_date matterOpenedDate, \n"
            + "tmg.case_closed_date matterClosedDate,  \n"
            + "tcc.case_category caseCategory,  \n"
            + "tcs.case_sub_category caseSubCategory, \n"
            + "tn.note_text noteDescription, \n"
            + "tma.originating_tk originatingTimeKeeper, \n"
            + "tma.assigned_tk assignedTimeKeeper,  \n"
            + "tma.responsible_tk responsibleTimeKeeper, \n"
            + "tma.legal_assist legalAssistant, \n"
            + "tma.ref_field_1 lawclerk, \n"
            + "tma.ref_field_2 paralegal, \n"
            + "tma.partner partner  \n"
            + "from tblmattergenaccid tmg  \n"
            + "left join tblcasecategoryid tcc on tcc.case_category_id=tmg.case_category_id  \n"
            + "left join tblcasesubcategoryid tcs on tcs.case_sub_category_id=tmg.case_sub_category_id \n"
            + "left join tblclientgeneralid tcg on tcg.client_id=tmg.client_id  \n"
            + "left join tblmatterassignmentid tma on tma.matter_no=tmg.matter_no \n"
            + "left join tblbillingmodeid tbm on tbm.bill_mode_id=tmg.bill_mode_id  \n"
            + "left join tblpotentialclientid tpc on tpc.pot_client_id=tcg.pot_client_id \n"
            + "left join tblnotesid tn on tn.note_no=tpc.pc_note_no \n"
            + "where  \n"
            + "(COALESCE(:responsibleTimeKeeper,null) IS NULL OR (tma.responsible_tk IN (:responsibleTimeKeeper))) and \n"
            + "(COALESCE(:assignedTimeKeeper,null) IS NULL OR (tma.assigned_tk IN (:assignedTimeKeeper))) and \n"
            + "(COALESCE(:originatingTimeKeeper,null) IS NULL OR (tma.originating_tk IN (:originatingTimeKeeper))) and \n"
            + "(COALESCE(:legalAssistant,null) IS NULL OR (tma.legal_assist IN (:legalAssistant))) and \n"
            + "(COALESCE(:lawClerks,null) IS NULL OR (tma.ref_field_1 IN (:lawClerks))) and \n"
            + "(COALESCE(:partner,null) IS NULL OR (tma.partner IN (:partner))) and \n"
            + "(COALESCE(:caseCategory,null) IS NULL OR (tcc.case_category_id IN (:caseCategory))) and \n"
            + "(COALESCE(:caseSubCategory,null) IS NULL OR (tcs.case_sub_category_id IN (:caseSubCategory))) and \n"
            + "(COALESCE(:billingModeId,null) IS NULL OR (tmg.bill_mode_id IN (:billingModeId))) and \n"
            + "(COALESCE(:statusId,null) IS NULL OR (tmg.status_id IN (:statusId))) and \n"
            + "(COALESCE(:refferedBy,null) IS NULL OR (tmg.ref_field_12 IN (:refferedBy))) and \n"
            + "(COALESCE(:classId,null) IS NULL OR (tmg.class_id IN (:classId))) and \n"
            + "(COALESCE(:startDate) IS NULL OR (tmg.case_open_date BETWEEN :startDate AND :endDate)) and \n"
            + "(COALESCE(:closedStartDate) IS NULL OR (tmg.case_closed_date BETWEEN :closedStartDate AND :closedEndDate)) and \n"
            + "tmg.is_deleted=0) x1", nativeQuery = true)
    public List<LNEReportImpl> getLNEReport(
            @Param(value = "responsibleTimeKeeper") List<String> responsibleTimeKeeper,
            @Param(value = "assignedTimeKeeper") List<String> assignedTimeKeeper,
            @Param(value = "originatingTimeKeeper") List<String> originatingTimeKeeper,
            @Param(value = "legalAssistant") List<String> legalAssistant,
            @Param(value = "lawClerks") List<String> lawClerks,
            @Param(value = "caseCategory") List<Long> caseCategory,
            @Param(value = "caseSubCategory") List<Long> caseSubCategory,
            @Param(value = "partner") List<String> partner,
            @Param(value = "billingModeId") List<String> billingModeId,
            @Param(value = "statusId") List<Long> statusId,
            @Param(value = "refferedBy") List<String> refferedBy,
            @Param(value = "classId") Long classId,
            @Param(value = "startDate") Date startDate,
            @Param(value = "endDate") Date endDate,
            @Param(value = "closedStartDate") Date closedStartDate,
            @Param(value = "closedEndDate") Date closedEndDate);

    //---------------find matter------------------------------------------------------------------------------------
    @Query(value = "select tc.class as classId,\n"
            + "mg.Matter_no as matterNumber,\n"
            + "mg.Matter_text as matterDescription,\n"
            + "tcg.first_last_nm as clientId,\n"
            + "tcc.case_category as caseCategoryId,\n"
            + "tcs.case_sub_category as caseSubCategoryId,\n"
            + "mg.caseinfo_no as caseInformationNo,\n"
            + "mg.case_open_date as caseOpenedDate,\n"
            + "ts.status_text as statusId\n"
            + "from tblmattergenaccid mg \n"
            + "left join tblclassid tc on tc.class_id=mg.class_id \n"
            + "left join tblcasecategoryid tcc on tcc.case_category_id=mg.case_category_id \n"
            + "left join tblcasesubcategoryid tcs on tcs.case_sub_category_id=mg.case_sub_category_id \n"
            + "left join tblclientgeneralid tcg on tcg.client_id=mg.client_id \n"
            + "left join tblstatusid ts on ts.status_id=mg.status_id \n"
            + "WHERE \n"
            + "(COALESCE(:matterNumber) IS NULL OR (mg.matter_no IN (:matterNumber))) and \n"
            + "(COALESCE(:classId) IS NULL OR (tc.class_id IN (:classId))) and \n"
            + "(COALESCE(:clientId) IS NULL OR (tcg.client_id IN (:clientId))) and \n"
            + "(COALESCE(:caseCategoryId) IS NULL OR (tcc.case_category_id IN (:caseCategoryId))) and \n"
            + "(COALESCE(:caseInformationNo) IS NULL OR (mg.caseinfo_no IN (:caseInformationNo))) and \n"
            + "(COALESCE(:statusId) IS NULL OR (ts.status_id IN (:statusId))) and \n"
            + "(COALESCE(:fromDate) IS NULL OR (mg.case_open_date BETWEEN :fromDate AND :toDate)) and \n"
            + "mg.is_deleted=0 order by mg.case_open_date desc", nativeQuery = true)
    public List<FindMatterGenImpl> getMatterGenList(@Param("matterNumber") List<String> matterNumber,
                                                    @Param("clientId") List<String> clientId,
                                                    @Param("classId") List<Long> classId,
                                                    @Param("caseCategoryId") List<Long> caseCategoryId,
                                                    @Param("caseInformationNo") List<String> caseInformationNo,
                                                    @Param("statusId") List<Long> statusId,
                                                    @Param("fromDate") Date fromDate,
                                                    @Param("toDate") Date toDate);

    //---------------Dropdown------------------------------------------------------------------------------------

    @Query(value = "SELECT distinct MATTER_NO AS keyIndex, MATTER_TEXT AS value\r\n"
            + "FROM tblmattergenaccid where IS_DELETED = 0", nativeQuery = true)
    public List<IKeyValuePair> getMatterList();

    @Query(value = "SELECT DISTINCT MATTER.CLASS_ID AS KEYINDEX, CLS.CLASS AS VALUE \r\n"
            + "FROM tblmattergenaccid MATTER \r\n"
            + "JOIN tblclassid CLS ON \r\n "
            + "MATTER.CLASS_ID = CLS.CLASS_ID \r\n"
            + "WHERE MATTER.IS_DELETED = 0", nativeQuery = true)
    public List<IKeyValuePair> getClassList();

    @Query(value = "SELECT distinct matter.CLIENT_ID AS keyIndex, client.FIRST_LAST_NM AS value\r\n"
            + "FROM tblmattergenaccid matter \r\n "
            + "JOIN tblclientgeneralid client ON \r\n "
            + "matter.CLIENT_ID = client.CLIENT_ID WHERE client.IS_DELETED = 0", nativeQuery = true)
    public List<IKeyValuePair> getClientNameList();

    @Query(value = "SELECT distinct matter.CASE_CATEGORY_ID AS keyIndex, cc.CASE_CATEGORY AS value \r\n"
            + "FROM tblmattergenaccid matter \r\n "
            + "JOIN tblcasecategoryid cc ON \r\n "
            + "matter.CASE_CATEGORY_ID = cc.CASE_CATEGORY_ID WHERE matter.IS_DELETED = 0", nativeQuery = true)
    public List<IKeyValuePair> getCaseCategoryList();

    @Query(value = "SELECT distinct matter.CASE_SUB_CATEGORY_ID AS keyIndex, cs.CASE_SUB_CATEGORY AS value\r\n"
            + "FROM tblmattergenaccid matter \r\n "
            + "JOIN tblcasesubcategoryid cs ON \r\n "
            + "matter.CASE_SUB_CATEGORY_ID = cs.CASE_SUB_CATEGORY_ID \r\n"
            + "WHERE matter.IS_DELETED = 0 ", nativeQuery = true)
    public List<IKeyValuePair> getSubCaseCategoryList();

    @Query(value = "SELECT distinct matter.STATUS_ID AS keyIndex, status.STATUS_TEXT AS value \r\n"
            + "FROM tblmattergenaccid matter \r\n"
            + "JOIN tblstatusid status ON matter.STATUS_ID = status.STATUS_ID \r\n"
            + "WHERE matter.IS_DELETED = 0", nativeQuery = true)
    public List<IKeyValuePair> getStatusIdList();

    @Query(value = "SELECT distinct matter.MATTER_NO AS matterNumber, \r\n"
            + "matter.MATTER_TEXT AS matterDescription, \r\n"
            + "matter.CLIENT_ID AS clientId, \r\n"
            + "client.FIRST_LAST_NM AS clientName\r\n"
            + "FROM tblmattergenaccid matter \r\n"
            + "JOIN tblclientgeneralid client ON matter.CLIENT_ID = client.CLIENT_ID \r\n"
            + "WHERE matter.IS_DELETED = 0", nativeQuery = true)
    public List<IMatterDropDown> getMatterDropDownList();

    @Query(value = "SELECT distinct matter.MATTER_NO AS matterNumber, \r\n"
            + "matter.MATTER_TEXT AS matterDescription, \r\n"
            + "matter.CLIENT_ID AS clientId, \r\n"
            + "client.FIRST_LAST_NM AS clientName\r\n"
            + "FROM tblmattergenaccid matter \r\n"
            + "JOIN tblclientgeneralid client ON matter.CLIENT_ID = client.CLIENT_ID \r\n"
            + "WHERE matter.STATUS_ID <> 30 AND matter.IS_DELETED = 0", nativeQuery = true)
    public List<IMatterDropDown> getOpenMatterDropDownList();

    @Query(value = "SELECT CLIENT_ID AS clientId,\r\n"
            + "LANG_ID AS languageId,\r\n"
            + "CLASS_ID AS classId,"
            + "MATTER_NO AS matterNumber,\r\n"
            + "CASEINFO_NO AS caseInformationNo,\r\n"
            + "TRANS_ID AS transactionId,\r\n"
            + "CASE_CATEGORY_ID AS caseCategoryId,\r\n"
            + "CASE_SUB_CATEGORY_ID AS caseSubCategoryId,\r\n"
            + "MATTER_TEXT AS matterDescription,\r\n"
            + "FILE_NO AS fileNumber,\r\n"
            + "CASE_FILE_NO AS caseFileNumber,\r\n"
            + "CASE_OPEN_DATE AS caseOpenedDate,\r\n"
            + "CASE_CLOSED_DATE AS caseClosedDate,\r\n"
            + "CASE_FILED_DATE AS caseFiledDate,\r\n"
            + "PRIORITY_DATE AS priorityDate,\r\n"
            + "RECEIPT_NOT_NO AS receiptNoticeNo,\r\n"
            + "RECEIPT_DATE AS receiptDate,\r\n"
            + "EXPIRATION_DATE AS expirationDate,\r\n"
            + "COURT_DATE AS courtDate,\r\n"
            + "APPROVAL_DATE AS approvalDate,\r\n"
            + "BILL_MODE_ID AS billingModeId,\r\n"
            + "BILL_FREQ_ID AS billingFrequencyId,\r\n"
            + "BILL_FORMAT_ID AS billingFormatId,\r\n"
            + "BILL_REMARK AS billingRemarks,\r\n"
            + "AR_ACCOUNT_NO AS arAccountNumber,\r\n"
            + "TRUST_DEPOSIT_NO AS trustDepositNo,\r\n"
            + "FLAT_FEE AS flatFeeAmount,\r\n"
            + "ADMIN_COST AS administrativeCost,\r\n"
            + "CONTIG_FEE AS contigencyFeeAmount,\r\n"
            + "RATE_UNIT AS rateUnit,\r\n"
            + "DIR_PHONE_NO AS directPhoneNumber,\r\n"
            + "STATUS_ID AS statusId,"
            + "CTD_ON AS createdOn,\r\n"
            + "UTD_BY AS updatedBy,\r\n"
            + "UTD_ON AS updatedOn "
            + "FROM tblmattergenaccid \r\n"
            + "WHERE CLASS_ID in :classId and IS_DELETED = 0",
            countQuery = "SELECT * FROM tblmattergenaccid WHERE CLASS_ID in :classId and IS_DELETED = 0",
            nativeQuery = true)
    public Page<IMatterGenAcc> findAllMattersByClassId(@Param(value = "classId") List<Long> classId, Pageable pageable);

    //---------------------Docketwise-------------------------------------------------------------------------------------
    public MatterGenAcc findTopByClassIdAndSentToDocketwiseAndDeletionIndicatorOrderByCreatedOn(long l, long m, long n);

    //---------------------Getting-Latest-Matter-No-for-client-id---------------------------------------------------------
    //	select ctd_on,MATTER_NO from tblmattergenaccid where client_id=2656 order by ctd_on desc;
    public MatterGenAcc findTopByClientIdOrderByCreatedOnDesc(String clientId);

    @Query(value = "SELECT MATTER_TEXT FROM tblmattergenaccid WHERE MATTER_NO = :matterNumber ", nativeQuery = true)
    public String getMatterText(@Param("matterNumber") String matterNumber);
}