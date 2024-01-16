package com.mnrclara.api.management.service;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import com.mnrclara.api.management.model.mattergeneral.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.mnrclara.api.management.controller.exception.BadRequestException;
import com.mnrclara.api.management.model.auth.AuthToken;
import com.mnrclara.api.management.model.caseinfosheet.LeCaseInfoSheet;
import com.mnrclara.api.management.model.clientgeneral.ClientGeneral;
import com.mnrclara.api.management.model.dto.BillingMode;
import com.mnrclara.api.management.model.dto.CaseCategory;
import com.mnrclara.api.management.model.dto.CaseSubcategory;
import com.mnrclara.api.management.model.dto.ClassKeyValuePair;
import com.mnrclara.api.management.model.dto.DashboardReport;
import com.mnrclara.api.management.model.dto.Dropdown;
import com.mnrclara.api.management.model.dto.IKeyValuePair;
import com.mnrclara.api.management.model.dto.IMatterDropDown;
import com.mnrclara.api.management.model.dto.ITimeTicket;
import com.mnrclara.api.management.model.dto.ImmigrationMatter;
import com.mnrclara.api.management.model.dto.KeyValuePair;
import com.mnrclara.api.management.model.dto.LNEMatter;
import com.mnrclara.api.management.model.dto.MatterDropDown;
import com.mnrclara.api.management.model.dto.MatterDropdownList;
import com.mnrclara.api.management.model.dto.Notes;
import com.mnrclara.api.management.model.dto.PotentialClient;
import com.mnrclara.api.management.model.dto.Referral;
import com.mnrclara.api.management.model.dto.UserProfile;
import com.mnrclara.api.management.model.dto.docketwise.Matter;
import com.mnrclara.api.management.model.matterassignment.MatterAssignment;
import com.mnrclara.api.management.model.matterassignment.SearchMatterAssignmentIMMReport;
import com.mnrclara.api.management.model.matterassignment.SearchMatterAssignmentLNEReport;
import com.mnrclara.api.management.model.mattergeneral.WIPAgedPBReport.Current;
import com.mnrclara.api.management.model.mattergeneral.WIPAgedPBReport.From61To90Days;
import com.mnrclara.api.management.model.matternote.MatterNote;
import com.mnrclara.api.management.repository.BillingModeRepository;
import com.mnrclara.api.management.repository.CaseCategoryRepository;
import com.mnrclara.api.management.repository.CaseSubcategoryRepository;
import com.mnrclara.api.management.repository.ClientGeneralRepository;
import com.mnrclara.api.management.repository.InvoiceHeaderRepository;
import com.mnrclara.api.management.repository.MatterAssignmentRepository;
import com.mnrclara.api.management.repository.MatterExpenseRepository;
import com.mnrclara.api.management.repository.MatterGenAccRepository;
import com.mnrclara.api.management.repository.MatterLNEReportRepository;
import com.mnrclara.api.management.repository.MatterTimeTicketRepository;
import com.mnrclara.api.management.repository.NotesRepository;
import com.mnrclara.api.management.repository.PotentialClientRepository;
import com.mnrclara.api.management.repository.PreBillDetailsRepository;
import com.mnrclara.api.management.repository.ReferralRepository;
import com.mnrclara.api.management.repository.specification.MatterAssignmentIMMSpecification;
import com.mnrclara.api.management.repository.specification.MatterAssignmentLNESpecification;
import com.mnrclara.api.management.repository.specification.MatterGenAccSpecification;
import com.mnrclara.api.management.repository.specification.MatterGeneralIMMSpecification;
import com.mnrclara.api.management.repository.specification.MatterGeneralLNESpecification;
import com.mnrclara.api.management.repository.specification.MatterGeneralWIPAgedPBReportSpecification;
import com.mnrclara.api.management.repository.specification.MatterLNEReportSpecification;
import com.mnrclara.api.management.util.CommonUtils;
import com.mnrclara.api.management.util.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MatterGenAccService {

    private static final String MATTERGENACC = "MATTERGENACC";

    @Autowired
    MatterGenAccRepository matterGenAccRepository;

    @Autowired
    MatterAssignmentRepository matterAssignmentRepository;

    @Autowired
    MatterTimeTicketRepository matterTimeTicketRepository;

    @Autowired
    MatterExpenseRepository matterExpenseRepository;

    @Autowired
    InvoiceHeaderRepository invoiceHeaderRepository;

    @Autowired
    PreBillDetailsRepository preBillDetailsRepository;

    @Autowired
    ClientGeneralRepository clientGeneralRepository;

    @Autowired
    ReferralRepository referralRepository;

    @Autowired
    CaseCategoryRepository caseCategoryRepository;

    @Autowired
    CaseSubcategoryRepository caseSubcategoryRepository;

    @Autowired
    BillingModeRepository billingModeRepository;

    @Autowired
    PotentialClientRepository potentialClientRepository;

    @Autowired
    NotesRepository notesRepository;

    @Autowired
    MatterLNEReportRepository matterLNEReportRepository;

    @Autowired
    LeCaseInfoSheetService leCaseInfoSheetService;

    @Autowired
    AuthTokenService authTokenService;

    @Autowired
    ClientGeneralService clientGeneralService;

    @Autowired
    MatterNoteService matterNoteService;

    @Autowired
    SetupService setupService;

    @Autowired
    CommonService commonService;

    @Autowired
    CRMService crmService;

    /**
     * getMatterGenAccs
     *
     * @return
     */
    public List<MatterGenAcc> getMatterGenAccs() {
        List<MatterGenAcc> matterGenAccList = matterGenAccRepository.findAll();
        matterGenAccList = matterGenAccList.stream()
                .filter(n -> n.getDeletionIndicator() != null && n.getDeletionIndicator() == 0)
                .collect(Collectors.toList());
        return matterGenAccList;
    }

    /**
     * @param pageNo
     * @param pageSize
     * @param sortBy
     * @return
     */
    public Page<MatterGenAcc> getAllMatterGenerals(Integer pageNo, Integer pageSize, String sortBy, List<Long> classId) {
        try {
            Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
            Page<IMatterGenAcc> pagedResult = matterGenAccRepository.findAllMattersByClassId(classId, pageable);
            List<IMatterGenAcc> iMatterGenAccList = pagedResult.getContent();
            List<MatterGenAcc> matterGenAccList = new ArrayList<>();
            for (IMatterGenAcc iMatterGenAcc : iMatterGenAccList) {
                MatterGenAcc matterGenAcc = new MatterGenAcc();
                matterGenAcc.setMatterNumber(iMatterGenAcc.getMatterNumber());
                matterGenAcc.setLanguageId(iMatterGenAcc.getLanguageId());
                matterGenAcc.setClassId(iMatterGenAcc.getClassId());
                matterGenAcc.setCaseInformationNo(iMatterGenAcc.getCaseInformationNo());
                matterGenAcc.setClientId(iMatterGenAcc.getClientId());
                matterGenAcc.setTransactionId(iMatterGenAcc.getTransactionId());
                matterGenAcc.setCaseCategoryId(iMatterGenAcc.getCaseCategoryId());
                matterGenAcc.setCaseSubCategoryId(iMatterGenAcc.getCaseSubCategoryId());
                matterGenAcc.setMatterDescription(iMatterGenAcc.getMatterDescription());
                matterGenAcc.setFileNumber(iMatterGenAcc.getFileNumber());
                matterGenAcc.setCaseFileNumber(iMatterGenAcc.getCaseFileNumber());
                matterGenAcc.setCaseOpenedDate(iMatterGenAcc.getCaseOpenedDate());
                matterGenAcc.setCaseClosedDate(iMatterGenAcc.getCaseClosedDate());
                matterGenAcc.setCaseFiledDate(iMatterGenAcc.getCaseFiledDate());
                matterGenAcc.setPriorityDate(iMatterGenAcc.getPriorityDate());
                matterGenAcc.setReceiptNoticeNo(iMatterGenAcc.getReceiptNoticeNo());
                matterGenAcc.setReceiptDate(iMatterGenAcc.getReceiptDate());
                matterGenAcc.setExpirationDate(iMatterGenAcc.getExpirationDate());
                matterGenAcc.setCourtDate(iMatterGenAcc.getCourtDate());
                matterGenAcc.setApprovalDate(iMatterGenAcc.getApprovalDate());
                matterGenAcc.setBillingModeId(iMatterGenAcc.getBillingModeId());
                matterGenAcc.setBillingFrequencyId(iMatterGenAcc.getBillingFrequencyId());
                matterGenAcc.setBillingFormatId(iMatterGenAcc.getBillingFormatId());
                matterGenAcc.setBillingRemarks(iMatterGenAcc.getBillingRemarks());
                matterGenAcc.setArAccountNumber(iMatterGenAcc.getArAccountNumber());
                matterGenAcc.setTrustDepositNo(iMatterGenAcc.getTrustDepositNo());
                matterGenAcc.setFlatFeeAmount(iMatterGenAcc.getFlatFeeAmount());
                matterGenAcc.setAdministrativeCost(iMatterGenAcc.getAdministrativeCost());
                matterGenAcc.setContigencyFeeAmount(iMatterGenAcc.getContigencyFeeAmount());
                matterGenAcc.setRateUnit(iMatterGenAcc.getRateUnit());
                matterGenAcc.setDirectPhoneNumber(iMatterGenAcc.getDirectPhoneNumber());
                matterGenAcc.setStatusId(iMatterGenAcc.getStatusId());
                matterGenAcc.setCreatedBy(iMatterGenAcc.getCreatedBy());
                matterGenAcc.setCreatedOn(iMatterGenAcc.getCreatedOn());
                matterGenAcc.setUpdatedBy(iMatterGenAcc.getUpdatedBy());
                matterGenAcc.setUpdatedOn(iMatterGenAcc.getUpdatedOn());

                // Status
                matterGenAcc.setReferenceField23(matterGenAccRepository.getStatusId(matterGenAcc.getMatterNumber()));

                // Sub CaseCategory
                matterGenAcc.setReferenceField24(matterGenAccRepository.getSubCaseCategory(matterGenAcc.getMatterNumber()));

                // Client Name
                matterGenAcc.setReferenceField26(matterGenAccRepository.getClientName(matterGenAcc.getMatterNumber()));

                // Class
                matterGenAcc.setReferenceField27(matterGenAccRepository.getClassName(matterGenAcc.getMatterNumber()));

                // CaseCategory
                matterGenAcc.setReferenceField28(matterGenAccRepository.getCaseCategory(matterGenAcc.getMatterNumber()));
                matterGenAccList.add(matterGenAcc);
            }

            final Page<MatterGenAcc> page = new PageImpl<>(matterGenAccList, pageable, pagedResult.getTotalElements());
            return page;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return
     */
    public MatterDropdownList getMatterDropdownList() {
        MatterDropdownList dropdown = new MatterDropdownList();

        List<IMatterDropDown> matterDropDownList = matterGenAccRepository.getMatterDropDownList();
        List<MatterDropDown> matterDropDowns = new ArrayList<>();
        for (IMatterDropDown iMatterDropDown : matterDropDownList) {
            MatterDropDown matterDropDown = new MatterDropDown();
            matterDropDown.setMatterNumber(iMatterDropDown.getMatterNumber());
            matterDropDown.setMatterDescription(iMatterDropDown.getMatterDescription());
            matterDropDown.setClientId(iMatterDropDown.getClientId());
            matterDropDown.setClientName(iMatterDropDown.getClientName());
            matterDropDowns.add(matterDropDown);
        }
        dropdown.setMatterDropDown(matterDropDowns);
        return dropdown;
    }

    /**
     * @return
     */
    public MatterDropdownList getOpenMatterDropdownList() {
        MatterDropdownList dropdown = new MatterDropdownList();
        List<IMatterDropDown> matterDropDownList = matterGenAccRepository.getOpenMatterDropDownList();
        List<MatterDropDown> matterDropDowns = new ArrayList<>();
        for (IMatterDropDown iMatterDropDown : matterDropDownList) {
            MatterDropDown matterDropDown = new MatterDropDown();
            matterDropDown.setMatterNumber(iMatterDropDown.getMatterNumber());
            matterDropDown.setMatterDescription(iMatterDropDown.getMatterDescription());
            matterDropDown.setClientId(iMatterDropDown.getClientId());
            matterDropDown.setClientName(iMatterDropDown.getClientName());
            matterDropDowns.add(matterDropDown);
        }
        dropdown.setMatterDropDown(matterDropDowns);
        return dropdown;
    }

    /**
     * @return
     */
    public Dropdown getDropdownList() {
        Dropdown dropdown = new Dropdown();

        List<IKeyValuePair> ikeyValues = matterGenAccRepository.getClientNameList();
        List<KeyValuePair> clientList = new ArrayList<>();
        for (IKeyValuePair iKeyValuePair : ikeyValues) {
            KeyValuePair keyValuePair = new KeyValuePair();
            keyValuePair.setKey(iKeyValuePair.getKeyIndex());
            keyValuePair.setValue(iKeyValuePair.getValue());
            clientList.add(keyValuePair);
        }
        dropdown.setClientNameList(clientList);

        ikeyValues = matterGenAccRepository.getCaseCategoryList();
        List<KeyValuePair> catList = new ArrayList<>();
        for (IKeyValuePair iKeyValuePair : ikeyValues) {
            KeyValuePair keyValuePair = new KeyValuePair();
            keyValuePair.setKey(iKeyValuePair.getKeyIndex());
            keyValuePair.setValue(iKeyValuePair.getValue());
            catList.add(keyValuePair);
        }
        dropdown.setCaseCategoryList(catList);

        ikeyValues = matterGenAccRepository.getSubCaseCategoryList();
        List<KeyValuePair> subcatList = new ArrayList<>();
        for (IKeyValuePair iKeyValuePair : ikeyValues) {
            KeyValuePair keyValuePair = new KeyValuePair();
            keyValuePair.setKey(iKeyValuePair.getKeyIndex());
            keyValuePair.setValue(iKeyValuePair.getValue());
            subcatList.add(keyValuePair);
        }
        dropdown.setSubCaseCategoryList(subcatList);

        ikeyValues = matterGenAccRepository.getStatusIdList();
        List<KeyValuePair> statusList = new ArrayList<>();
        for (IKeyValuePair iKeyValuePair : ikeyValues) {
            KeyValuePair keyValuePair = new KeyValuePair();
            keyValuePair.setKey(iKeyValuePair.getKeyIndex());
            keyValuePair.setValue(iKeyValuePair.getValue());
            statusList.add(keyValuePair);
        }
        dropdown.setStatusIdList(statusList);

        // Matter
        ikeyValues = matterGenAccRepository.getMatterList();
        List<KeyValuePair> matterList = new ArrayList<>();
        for (IKeyValuePair iKeyValuePair : ikeyValues) {
            KeyValuePair keyValuePair = new KeyValuePair();
            keyValuePair.setKey(iKeyValuePair.getKeyIndex());
            keyValuePair.setValue(iKeyValuePair.getValue());
            matterList.add(keyValuePair);
        }
        dropdown.setMatterList(matterList);

        // Class
        ikeyValues = matterGenAccRepository.getClassList();
        List<ClassKeyValuePair> classList = new ArrayList<>();
        for (IKeyValuePair iKeyValuePair : ikeyValues) {
            ClassKeyValuePair keyValuePair = new ClassKeyValuePair();
            keyValuePair.setKey(Long.valueOf(iKeyValuePair.getKeyIndex()));
            keyValuePair.setValue(iKeyValuePair.getValue());
            classList.add(keyValuePair);
        }
        dropdown.setClassList(classList);

        return dropdown;
    }

    /**
     * getMatterGenAcc
     *
     * @param matterNumber
     * @return
     */
    public MatterGenAcc getMatterGenAcc(String matterNumber) {
        log.info("matterNumber :  " + matterNumber);
        MatterGenAcc matterGenAcc = matterGenAccRepository.findByMatterNumber(matterNumber).orElse(null);
        if (matterGenAcc != null && matterGenAcc.getDeletionIndicator() != null
                && matterGenAcc.getDeletionIndicator().longValue() == 0) {
            return matterGenAcc;
        } else {
            throw new BadRequestException("The given MatterGenAcc ID : " + matterNumber + " doesn't exist.");
        }
    }

    /**
     * @param matterNumber
     * @return
     */
    public List<MatterGenAcc> getMatterGenAcc(List<String> matterNumber) {
        List<MatterGenAcc> matterGenAcc = matterGenAccRepository.findByMatterNumberInAndAndDeletionIndicator(matterNumber, 0L);
        if (!matterGenAcc.isEmpty()) {
            return matterGenAcc;
        }
        return null;
    }

    /**
     * @return
     */
    public MatterGenAcc getLatestMatterGeneral() {
        MatterGenAcc matterGenAcc = matterGenAccRepository.findTopByOrderByCreatedOnDesc();
        return matterGenAcc;
    }

    /**
     * @return
     */
    public MatterGenAcc getReceiptPaymentQueryNumber() {
        MatterGenAcc matterGenAcc = matterGenAccRepository.findTopByOrderByCreatedOnDesc();
        return matterGenAcc;
    }

    /**
     * @return
     */
    public MatterGenAcc getMatterGeneralByLimit() {
        MatterGenAcc matterGenAcc =
                matterGenAccRepository.findTopBySentToQBAndDeletionIndicatorOrderByCreatedOn(0L, 0L);
        return matterGenAcc;
    }

    /**
     * @param matterNumber
     * @param matterId
     * @return
     * @throws ParseException
     */
    public MatterGenAcc getMatterGenAccFromDocketwise(String matterNumber, String matterId) throws ParseException {
        // Get AuthToken for CommonService
        AuthToken authTokenForCommonService = authTokenService.getCommonServiceAuthToken();
        Matter matter = commonService.getDocketwiseMatter(matterId, authTokenForCommonService.getAccess_token());
        log.info("matter : " + matter);

        MatterGenAcc dbMatterGenAcc = matterGenAccRepository.findByMatterNumber(matterNumber).orElse(null);
        if (dbMatterGenAcc != null && dbMatterGenAcc.getDeletionIndicator() != null
                && dbMatterGenAcc.getDeletionIndicator().longValue() == 0) {
            //-----------------Update the Docketwise details in CLARA----------------------------------------
            // Id
            dbMatterGenAcc.setReferenceField9(String.valueOf(matter.getId()));

            // Number
            dbMatterGenAcc.setMatterNumber(matter.getNumber());

            // Title
            dbMatterGenAcc.setMatterDescription(matter.getTitle());

            // UTD_ON
            dbMatterGenAcc.setUpdatedOn(matter.getUpdated_at());

            // Receipt_number
            dbMatterGenAcc.setReceiptNoticeNo(matter.getReceipt_number());

            // Preference_category_id
            dbMatterGenAcc.setReferenceField29(matter.getPreference_category_id());

            // Priority_date
            String priDate = matter.getPriority_date(); //2021-12-30;
            if (priDate != null) {
                priDate += " 00:00";
                log.info("priDate-----------> : " + priDate);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime dateTime = LocalDateTime.parse(priDate, formatter);
                Date out = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
                dbMatterGenAcc.setPriorityDate(out);
                log.info("dbMatterGenAcc--PriorityDate-------> : " + dbMatterGenAcc.getPriorityDate());
            }
            // Priority_date_status
            dbMatterGenAcc.setReferenceField30(matter.getPriority_date_status());
            MatterGenAcc updatedMatterGenAcc = matterGenAccRepository.save(dbMatterGenAcc);
            log.info("updatedMatterGenAcc : " + updatedMatterGenAcc);
            return updatedMatterGenAcc;
        } else {
            throw new BadRequestException("The given MatterGenAcc ID : " + matterNumber + " doesn't exist.");
        }
    }

    /**
     * getMatterGenAccs
     *
     * @param classId
     * @param clientId
     * @param matterNumber
     * @return
     * @throws ParseException
     */
    public MatterGenAcc getMatterGenAcc(Long classId, String clientId, String matterNumber)
            throws ParseException {
        MatterGenAcc matterGenAccList = matterGenAccRepository.findByClassIdAndClientIdAndMatterNumberAndDeletionIndicator(classId,
                clientId, matterNumber, 0L);
        return matterGenAccList;
    }


    /**
     * findMatterGenAccs
     *
     * @param searchMatterGeneral
     * @return
     * @throws ParseException
     */
    public List<MatterGenAcc> findMatterGenAccs(SearchMatterGeneral searchMatterGeneral) throws ParseException {
        if (searchMatterGeneral.getCaseOpenedDate1() != null && searchMatterGeneral.getCaseOpenedDate2() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(searchMatterGeneral.getCaseOpenedDate1(), searchMatterGeneral.getCaseOpenedDate2());
            searchMatterGeneral.setCaseOpenedDate1(dates[0]);
            searchMatterGeneral.setCaseOpenedDate2(dates[1]);
        }

        MatterGenAccSpecification spec = new MatterGenAccSpecification(searchMatterGeneral);
        List<MatterGenAcc> searchResults = matterGenAccRepository.findAll(spec);
        for (MatterGenAcc matterGenAcc : searchResults) {
            try {
                // Status - ReferenceField23
                matterGenAcc.setReferenceField23(matterGenAccRepository.getStatusId(matterGenAcc.getMatterNumber()));

                // Sub CaseCategory - ReferenceField24
                matterGenAcc.setReferenceField24(matterGenAccRepository.getSubCaseCategory(matterGenAcc.getMatterNumber()));

                // Client Name
                matterGenAcc.setReferenceField26(matterGenAccRepository.getClientName(matterGenAcc.getMatterNumber()));

                // Class
                matterGenAcc.setReferenceField27(matterGenAccRepository.getClassName(matterGenAcc.getMatterNumber()));

                // CaseCategory
                matterGenAcc.setReferenceField28(matterGenAccRepository.getCaseCategory(matterGenAcc.getMatterNumber()));
            } catch (Exception e) {
                log.info("Find Api:" + e.toString());
                e.printStackTrace();
            }
        }
        return searchResults;
    }

    public List<FindMatterGenImpl> findMatterGeneral(FindMatterGeneral searchMatterGeneral) throws ParseException {
        if (searchMatterGeneral.getFromDate() != null && searchMatterGeneral.getEndDate() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(searchMatterGeneral.getFromDate(), searchMatterGeneral.getEndDate());
            searchMatterGeneral.setFromDate(dates[0]);
            searchMatterGeneral.setEndDate(dates[1]);
        }

        List<FindMatterGenImpl> data = matterGenAccRepository.getMatterGenList(searchMatterGeneral.getMatterNumber(),
                searchMatterGeneral.getClientId(),
                searchMatterGeneral.getClassId(),
                searchMatterGeneral.getCaseCategoryId(),
                searchMatterGeneral.getCaseInformationNo(),
                searchMatterGeneral.getStatusId(),
                searchMatterGeneral.getFromDate(),
                searchMatterGeneral.getEndDate());

        return data;
    }

    /**
     * @param searchMatterGeneral
     * @return
     * @throws ParseException
     */
    public List<MatterGenAcc> findMatterGenAccs(SearchWIPAgedPBReport searchMatterGeneral) throws ParseException {
        MatterGeneralWIPAgedPBReportSpecification spec = new MatterGeneralWIPAgedPBReportSpecification(searchMatterGeneral);
        List<MatterGenAcc> searchResults = matterGenAccRepository.findAll(spec);
        log.info("searchResults: " + searchResults);
        return searchResults;
    }

    /**
     * @param matterNumber
     * @param loginUserID
     * @return
     * @throws Exception
     */
    public MatterGenAcc updateMatterGenAccByQBSync(String matterNumber, String loginUserID)
            throws Exception {
        MatterGenAcc dbMatterGenAcc = getMatterGenAcc(matterNumber);
        dbMatterGenAcc.setSentToQB(1L);
        dbMatterGenAcc.setUpdatedBy(loginUserID);
        dbMatterGenAcc = matterGenAccRepository.save(dbMatterGenAcc);
        log.info("QbSync Updated : " + dbMatterGenAcc);
        return dbMatterGenAcc;
    }

    /**
     * updateMatterGenAcc
     *
     * @param matterNumber
     * @param updateMatterGenAcc
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ParseException
     */
    public MatterGenAcc updateMatterGenAcc(String matterNumber, MatterGenAcc updateMatterGenAcc,
                                           String loginUserID) throws IllegalAccessException, InvocationTargetException, ParseException {
        MatterGenAcc dbMatterGenAcc = getMatterGenAcc(matterNumber);
        log.info("dbMatterGenAcc : " + dbMatterGenAcc);

        // Get AuthToken for SetupService
        AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();

        // MATTER_TEXT
        if (updateMatterGenAcc.getMatterDescription() != null
                && !updateMatterGenAcc.getMatterDescription().equalsIgnoreCase(dbMatterGenAcc.getMatterDescription())) {
            log.info("Inserting Audit log for MATTER_TEXT");
            setupService.createAuditLogRecord(loginUserID, matterNumber, 3L, MATTERGENACC,
                    "MATTER_TEXT", dbMatterGenAcc.getMatterDescription(), updateMatterGenAcc.getMatterDescription(),
                    authTokenForSetupService.getAccess_token());
        }

        // CASE_CATEGORY_ID/CASE_CATEGORY
        Long CASE_CATEGORY_ID = updateMatterGenAcc.getCaseCategoryId();
        Long DB_CASE_CATEGORY_ID = dbMatterGenAcc.getCaseCategoryId();
        if (CASE_CATEGORY_ID != null && DB_CASE_CATEGORY_ID != null &&
                CASE_CATEGORY_ID.longValue() != DB_CASE_CATEGORY_ID.longValue()) {
            log.info("Inserting Audit log for CASE_CATEGORY_ID");
            setupService.createAuditLogRecord(loginUserID, String.valueOf(CASE_CATEGORY_ID), 3L, MATTERGENACC,
                    "CASE_CATEGORY_ID", String.valueOf(dbMatterGenAcc.getCaseCategoryId()),
                    String.valueOf(CASE_CATEGORY_ID), authTokenForSetupService.getAccess_token());
        }

        // CASE_SUB_CATEGORY_ID/CASE_SUB_CATEGORY
        Long CASE_SUB_CATEGORY_ID = updateMatterGenAcc.getCaseSubCategoryId();
        Long DB_CASE_SUB_CATEGORY_ID = dbMatterGenAcc.getCaseSubCategoryId();
        if (CASE_SUB_CATEGORY_ID != null && DB_CASE_SUB_CATEGORY_ID != null &&
                CASE_SUB_CATEGORY_ID.longValue() != DB_CASE_SUB_CATEGORY_ID.longValue()) {
            log.info("Inserting Audit log for CASE_SUB_CATEGORY_ID");
            setupService.createAuditLogRecord(loginUserID, String.valueOf(CASE_SUB_CATEGORY_ID), 3L, MATTERGENACC,
                    "CASE_SUB_CATEGORY_ID", String.valueOf(dbMatterGenAcc.getCaseSubCategoryId()),
                    String.valueOf(CASE_SUB_CATEGORY_ID), authTokenForSetupService.getAccess_token());
        }

        // DIR_PHONE_NO
        String DIR_PHONE_NO = updateMatterGenAcc.getDirectPhoneNumber();
        if (DIR_PHONE_NO != null && !DIR_PHONE_NO.equalsIgnoreCase(dbMatterGenAcc.getDirectPhoneNumber())) {
            log.info("Inserting Audit log for DIR_PHONE_NO");
            setupService.createAuditLogRecord(loginUserID, DIR_PHONE_NO, 3L, MATTERGENACC, "DIR_PHONE_NO",
                    dbMatterGenAcc.getDirectPhoneNumber(), DIR_PHONE_NO, authTokenForSetupService.getAccess_token());
        }

        // RECEIPT_NOT_NO
        String RECEIPT_NOT_NO = updateMatterGenAcc.getReceiptNoticeNo();
        if (RECEIPT_NOT_NO != null && !RECEIPT_NOT_NO.equalsIgnoreCase(dbMatterGenAcc.getReceiptNoticeNo())) {
            log.info("Inserting Audit log for RECEIPT_NOT_NO");
            setupService.createAuditLogRecord(loginUserID, RECEIPT_NOT_NO, 3L, MATTERGENACC, "RECEIPT_NOT_NO",
                    dbMatterGenAcc.getReceiptNoticeNo(), RECEIPT_NOT_NO, authTokenForSetupService.getAccess_token());
        }

        BeanUtils.copyProperties(updateMatterGenAcc, dbMatterGenAcc, CommonUtils.getNullPropertyNames(updateMatterGenAcc));
        
        if (updateMatterGenAcc.getCaseOpenedDate() == null) {
            dbMatterGenAcc.setCaseOpenedDate(null);
        }
        
        if (updateMatterGenAcc.getCaseClosedDate() == null) {
            dbMatterGenAcc.setCaseClosedDate(null);
        }
        
        if (updateMatterGenAcc.getCaseFiledDate() == null) {
            dbMatterGenAcc.setCaseFiledDate(null);
        }
        
        if (updateMatterGenAcc.getPriorityDate() == null) {
            dbMatterGenAcc.setPriorityDate(null);
        }
        
        if (updateMatterGenAcc.getReceiptDate() == null) {
            dbMatterGenAcc.setReceiptDate(null);
        }
        
        if (updateMatterGenAcc.getExpirationDate() == null) {
            dbMatterGenAcc.setExpirationDate(null);
        }
        
        if (updateMatterGenAcc.getCourtDate() == null) {
            dbMatterGenAcc.setCourtDate(null);
        }
        
        if (updateMatterGenAcc.getApprovalDate() == null) {
            dbMatterGenAcc.setApprovalDate(null);
        }

        if (!loginUserID.equalsIgnoreCase("QBUpdate")) {
            dbMatterGenAcc.setUpdatedBy(loginUserID);
        }
        
        dbMatterGenAcc.setUpdatedOn(new Date());
        log.info("=============>dbMatterGenAcc : " + dbMatterGenAcc);
        MatterGenAcc updatedMatterGenAcc = matterGenAccRepository.save(dbMatterGenAcc);

        //----------------------Docketwise Integration------------------------------------------------------------
        /*
         * Only if classId == 2 then we are allowing Docketwise Update
         */
        if (updatedMatterGenAcc.getClassId().longValue() == 2) {
            try {
                Matter docketwiseMatter = populateDocketwiseMatter(updatedMatterGenAcc);

                // Get AuthToken for CommonService
                AuthToken authTokenForCommonService = authTokenService.getCommonServiceAuthToken();
                Matter createdMatter = commonService.updateMatter(updatedMatterGenAcc.getReferenceField9(),
                        docketwiseMatter, authTokenForCommonService.getAccess_token());
                log.info("createdMatter : " + createdMatter);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("Docketwise Error:" + e.getLocalizedMessage());
            }
        }
        return updatedMatterGenAcc;
    }

    /**
     * populateDocketwiseMatter
     *
     * @param updatedMatterGenAcc
     * @return
     */
    private Matter populateDocketwiseMatter(MatterGenAcc updatedMatterGenAcc) {
        Matter matter = new Matter();

        // Number
        matter.setNumber(updatedMatterGenAcc.getMatterNumber());

        // Title
        matter.setTitle(updatedMatterGenAcc.getMatterDescription());

        // Description
        matter.setDescription(updatedMatterGenAcc.getMatterDescription());

        // Client_id
        log.info("client_id : " + updatedMatterGenAcc.getClientId());
        ClientGeneral clientGeneral = clientGeneralService.getClientGeneral(updatedMatterGenAcc.getClientId());
        log.info("clientGeneral Ref_9:: " + clientGeneral.getReferenceField9());

        matter.setClient_id(Long.valueOf(clientGeneral.getReferenceField9()));

        return matter;
    }

    /**
     * @param newMatterGenAccs
     * @param loginUserID
     */
    public void createBulkMatters(AddMatterGenAcc[] newMatterGenAccs, String loginUserID) {
        List<MatterGenAcc> createMatterGenAccs = new ArrayList<>();
        for (AddMatterGenAcc newMatterGenAcc : newMatterGenAccs) {
            MatterGenAcc matterGeneralAccount = new MatterGenAcc();
            BeanUtils.copyProperties(newMatterGenAcc, matterGeneralAccount, CommonUtils.getNullPropertyNames(newMatterGenAcc));

            // TRANS_ID
            matterGeneralAccount.setTransactionId(8L); // Hard Coded Value "08"

            // STATUS_ID
            matterGeneralAccount.setStatusId(26L); // Hard coded Value "25"
            matterGeneralAccount.setDeletionIndicator(0L);
            createMatterGenAccs.add(matterGeneralAccount);
        }

        // Saving in DB
        Iterable<MatterGenAcc> createdMatterGenAccs = matterGenAccRepository.saveAll(createMatterGenAccs);
        log.info("createdMatterGenAccs : " + createdMatterGenAccs);

        //-------------------- Docketwise Integration-----------------------------------------------------------------
        for (MatterGenAcc matterGenAcc : createdMatterGenAccs) {
            if (matterGenAcc.getClassId().longValue() == 2) {
                Matter matter = populateDocketwiseMatter(matterGenAcc);
                log.info("Matter for Docketwise: " + matter);

                // Get AuthToken for CommonService
                AuthToken authTokenForCommonService = authTokenService.getCommonServiceAuthToken();
                Matter createdMatter = commonService.createDocketwiseMatter(matter, authTokenForCommonService.getAccess_token());
                log.info("createdMatter------------> : " + createdMatter);

                // Updating MatterGenAcc with new created ID
                MatterGenAcc updateMatterGenAcc = getMatterGenAcc(matterGenAcc.getMatterNumber());
                updateMatterGenAcc.setReferenceField9(String.valueOf(createdMatter.getId()));
                updateMatterGenAcc.setUpdatedBy(loginUserID);
                updateMatterGenAcc.setUpdatedOn(new Date());
                MatterGenAcc updatedMatterGenAcc = matterGenAccRepository.save(updateMatterGenAcc);
                log.info("updatedMatterGenAcc------------> : " + updatedMatterGenAcc);
            }
        }

        log.info("Bulk processing completed.");
    }

    /**
     * @param matterNumber
     * @param updateMatterGenAcc
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws ParseException
     */
    public MatterGenAcc updateMatterAccounting(String matterNumber, UpdateMatterGenAcc updateMatterGenAcc,
                                               String loginUserID) throws IllegalAccessException, InvocationTargetException, ParseException {
        MatterGenAcc dbMatterGenAcc = getMatterGenAcc(matterNumber);
        log.info("dbMatterGenAcc : " + dbMatterGenAcc);

        // Get AuthToken for SetupService
        AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();

        // MATTER_TEXT
        if (updateMatterGenAcc.getMatterDescription() != null
                && !updateMatterGenAcc.getMatterDescription().equalsIgnoreCase(dbMatterGenAcc.getMatterDescription())) {
            log.info("Inserting Audit log for MATTER_TEXT");
            setupService.createAuditLogRecord(loginUserID, updateMatterGenAcc.getMatterDescription(), 3L, MATTERGENACC,
                    "MATTER_TEXT", dbMatterGenAcc.getMatterDescription(), updateMatterGenAcc.getMatterDescription(),
                    authTokenForSetupService.getAccess_token());
        }

        // ADMIN_COST
        Double ADMIN_COST = updateMatterGenAcc.getAdministrativeCost();
        if (ADMIN_COST != null && ADMIN_COST != dbMatterGenAcc.getAdministrativeCost()) {
            log.info("Inserting Audit log for ADMIN_COST");
            setupService.createAuditLogRecord(loginUserID, String.valueOf(ADMIN_COST), 3L, MATTERGENACC, "ADMIN_COST",
                    dbMatterGenAcc.getReceiptNoticeNo(), String.valueOf(ADMIN_COST), authTokenForSetupService.getAccess_token());
        }

        BeanUtils.copyProperties(updateMatterGenAcc, dbMatterGenAcc, CommonUtils.getNullPropertyNames(updateMatterGenAcc));


        // BILL_MODE_ID
        dbMatterGenAcc.setBillingModeId(updateMatterGenAcc.getBillingModeId());

        // BILL_FREQ_ID
        dbMatterGenAcc.setBillingFrequencyId(updateMatterGenAcc.getBillingFrequencyId());

        // BILL_FORMAT_ID
        dbMatterGenAcc.setBillingFormatId(updateMatterGenAcc.getBillingFormatId());

        // FLAT_FEE
        dbMatterGenAcc.setFlatFeeAmount(updateMatterGenAcc.getFlatFeeAmount());

        log.info("updateMatterGenAcc.getRemainingAmount() : " + updateMatterGenAcc.getRemainingAmount());
        dbMatterGenAcc.setRemainingAmount(updateMatterGenAcc.getRemainingAmount());

        // CONTIG_FEE
        dbMatterGenAcc.setContigencyFeeAmount(updateMatterGenAcc.getContigencyFeeAmount());

        // ADMIN_COST
        dbMatterGenAcc.setAdministrativeCost(updateMatterGenAcc.getAdministrativeCost());

        // AR_ACCOUNT_NO
        dbMatterGenAcc.setArAccountNumber(updateMatterGenAcc.getArAccountNumber());

        // TRUST_DEPOSIT_NO
        dbMatterGenAcc.setTrustDepositNo(updateMatterGenAcc.getTrustDepositNo());

        dbMatterGenAcc.setUpdatedBy(loginUserID);
        dbMatterGenAcc.setUpdatedOn(new Date());

        log.info("dbMatterGenAcc.getRemainingAmount() : " + dbMatterGenAcc.getRemainingAmount());
        return matterGenAccRepository.save(dbMatterGenAcc);
    }

    /**
     * deleteMatterGenAcc
     *
     * @param loginUserID
     * @param matterNumber
     */
    public void deleteMatterGenAcc(String matterNumber, String loginUserID) {
        MatterGenAcc mattergenacc = getMatterGenAcc(matterNumber);
        log.info("mattergenacc : " + mattergenacc);
        if (mattergenacc != null && mattergenacc.getStatusId() == 26L) {
            mattergenacc.setDeletionIndicator(1L);
            mattergenacc.setUpdatedBy(loginUserID);
            mattergenacc.setUpdatedOn(new Date());
            matterGenAccRepository.save(mattergenacc);
        } else if (mattergenacc != null && mattergenacc.getStatusId() != 26L) {
            throw new EntityNotFoundException("Error in deleting Id: " + matterNumber + " because StatusId is NOT 26.");
        } else {
            throw new EntityNotFoundException("Error in deleting Id: " + matterNumber);
        }
    }

    /**
     * @param loginUserID
     * @return
     */
    private Long getUserProfileClassId(String loginUserID) {
        // Get AuthToken for SetupService
        AuthToken authTokenForSetupService = authTokenService.getSetupServiceAuthToken();
        UserProfile userProfile = setupService.getUserProfile(loginUserID, authTokenForSetupService.getAccess_token());
        log.info("userProfile : " + userProfile);
        return userProfile.getClassId();
    }

    /**
     * getDashboardTotal
     *
     * @param loginUserID
     * @return
     */
    public DashboardReport getDashboardTotal(String loginUserID) {
        DashboardReport dashboardReport = new DashboardReport();
        Long classId = getUserProfileClassId(loginUserID);

        // If CLASS_ID = 03 for the passed USR_ID, fetch the count of CLIENT_ID values from MATTERGENACC table
        if (classId != null && classId.longValue() == 3) {
            List<MatterGenAcc> matterGenAccList = matterGenAccRepository.findAllByDeletionIndicator(0L);
            dashboardReport.setCount(matterGenAccList.size());
            return dashboardReport;
        }

        List<MatterGenAcc> matterGenAccList = matterGenAccRepository.findByClassIdAndDeletionIndicator(classId, 0L);
        List<String> clientIdList = matterGenAccList.stream().map(c -> c.getClientId()).collect(Collectors.toList());
        dashboardReport.setListOfValues(clientIdList);
        dashboardReport.setCount(clientIdList.size());
        return dashboardReport;
    }

    /**
     * getDashboardCountByStatus
     *
     * @param loginUserID
     * @param statusId
     * @return
     */
    public DashboardReport getDashboardCountByStatus(Long statusId, String loginUserID) {
        DashboardReport dashboardReport = new DashboardReport();
        Long classId = getUserProfileClassId(loginUserID);

        // If CLASS_ID = 03 for the passed USR_ID, fetch the count of CLIENT_ID values from MATTERGENACC table
        if (classId != null && classId.longValue() == 3) {
            List<MatterGenAcc> matterGenAccList = matterGenAccRepository.findAllByStatusIdAndDeletionIndicator(statusId, 0L);
            dashboardReport.setCount(matterGenAccList.size());
            return dashboardReport;
        }

        List<MatterGenAcc> matterGenAccList = matterGenAccRepository.findByClassIdAndStatusIdAndDeletionIndicator(classId, statusId, 0L);
        List<String> clientIdList = matterGenAccList.stream().map(c -> c.getClientId()).collect(Collectors.toList());
        dashboardReport.setListOfValues(clientIdList);
        dashboardReport.setCount(clientIdList.size());
        return dashboardReport;
    }

    /**
     * @param fullTextSearch
     * @return
     */
    public List<MatterGenAcc> findRecords(String fullTextSearch) {
        List<MatterGenAcc> matterGenAcc = matterGenAccRepository.findRecords(fullTextSearch);
        return matterGenAcc;
    }

    /**
     * @param matterNumber
     * @return
     */
    public MatterGenAcc push2Docketwise(String matterNumber) {
        MatterGenAcc matterGenAcc = getMatterGenAcc(matterNumber);
        Matter matter = populateDocketwiseMatter(matterGenAcc);
        log.info("matter: " + matter);

        // Get AuthToken for CommonService
        AuthToken authTokenForCommonService = authTokenService.getCommonServiceAuthToken();
        Matter createdMatter = commonService.createDocketwiseMatter(matter, authTokenForCommonService.getAccess_token());

        log.info("createdMatter------------> : " + createdMatter);

        // Updating MatterGenAcc with new created ID
        MatterGenAcc updateMatterGenAcc = getMatterGenAcc(matterGenAcc.getMatterNumber());
        updateMatterGenAcc.setReferenceField9(String.valueOf(createdMatter.getId()));
        updateMatterGenAcc.setUpdatedBy(updateMatterGenAcc.getCreatedBy());
        updateMatterGenAcc.setUpdatedOn(new Date());
        updateMatterGenAcc = matterGenAccRepository.save(updateMatterGenAcc);
        return updateMatterGenAcc;
    }

    //Attorney Productivity Report
    public List<AttorneyProductivityReport> getAttorneyProductivityReport(AttorneyProductivityInput attorneyProductivityInput) throws ParseException {

        //Add Time to Date field
        Date[] dates = new Date[0];
        Date[] filedDates = new Date[0];
        if (attorneyProductivityInput.getFromDate() != null && attorneyProductivityInput.getToDate() != null) {
            Date fromDate = DateUtils.convertStringToDate3(attorneyProductivityInput.getFromDate());
            Date toDate = DateUtils.convertStringToDate3(attorneyProductivityInput.getToDate());
            dates = DateUtils.addTimeToDatesForSearch(fromDate, toDate);
            attorneyProductivityInput.setCaseOpenDate(dates[0]);
            attorneyProductivityInput.setCaseEndDate(dates[1]);
        }
        //Filed Dates
        if (attorneyProductivityInput.getFromFiledDate() != null && attorneyProductivityInput.getToFiledDate() != null) {
            Date fromFiledDate = DateUtils.convertStringToDate3(attorneyProductivityInput.getFromFiledDate());
            Date toFiledDate = DateUtils.convertStringToDate3(attorneyProductivityInput.getToFiledDate());
            filedDates = DateUtils.addTimeToDatesForSearch(fromFiledDate, toFiledDate);
            attorneyProductivityInput.setCaseFiledStartDate(filedDates[0]);
            attorneyProductivityInput.setCaseFiledEndDate(filedDates[1]);
        }

        //Declare New Report List
        List<AttorneyProductivityReport> newAttorneyProductivityReportList = new ArrayList<>();

        //Get Attorney Productivity Report List
        List<AttorneyProductivityReportImpl> dbAttorneyProductivityReportList = matterGenAccRepository.getAttorneyProductivityReport(
                                                                                    attorneyProductivityInput.getClassId(),
                                                                                    attorneyProductivityInput.getMatterNumber(),
                                                                                    attorneyProductivityInput.getResponsibleTimeKeeper(),
                                                                                    attorneyProductivityInput.getAssignedTimeKeeper(),
                                                                                    attorneyProductivityInput.getOriginatingTimeKeeper(),
                                                                                    attorneyProductivityInput.getCaseCategoryId(),
                                                                                    attorneyProductivityInput.getCaseSubCategoryId(),
                                                                                    attorneyProductivityInput.getCaseOpenDate(),
                                                                                    attorneyProductivityInput.getCaseEndDate(),
                                                                                    attorneyProductivityInput.getCaseFiledStartDate(),
                                                                                    attorneyProductivityInput.getCaseFiledEndDate());

        //Loop the results to set in report output
        for (AttorneyProductivityReportImpl newAttorneyProductivityReport : dbAttorneyProductivityReportList) {
            AttorneyProductivityReport dbAttorneyProductivityReport = new AttorneyProductivityReport();
            dbAttorneyProductivityReport.setClassId(newAttorneyProductivityReport.getClassId());
            dbAttorneyProductivityReport.setClientId(newAttorneyProductivityReport.getClientId());
            dbAttorneyProductivityReport.setClientName(newAttorneyProductivityReport.getClientName());
            dbAttorneyProductivityReport.setMatterNumber(newAttorneyProductivityReport.getMatterNumber());
            dbAttorneyProductivityReport.setMatterDescription(newAttorneyProductivityReport.getMatterDescription());
            dbAttorneyProductivityReport.setCaseCategoryId(newAttorneyProductivityReport.getCaseCategoryId());
            dbAttorneyProductivityReport.setCaseSubCategoryId(newAttorneyProductivityReport.getCaseSubCategoryId());
            dbAttorneyProductivityReport.setFlatFee(newAttorneyProductivityReport.getFlatFee());
            dbAttorneyProductivityReport.setOriginatingTimeKeeper(newAttorneyProductivityReport.getOriginatingTimeKeeper());
            dbAttorneyProductivityReport.setAssignedTimeKeeper(newAttorneyProductivityReport.getAssignedTimeKeeper());
            dbAttorneyProductivityReport.setResponsibleTimeKeeper(newAttorneyProductivityReport.getResponsibleTimeKeeper());
            dbAttorneyProductivityReport.setCaseOpenDate(newAttorneyProductivityReport.getCaseOpenDate());
            dbAttorneyProductivityReport.setCaseFiledDate(newAttorneyProductivityReport.getCaseFiledDate());
            dbAttorneyProductivityReport.setStatusDescription(newAttorneyProductivityReport.getStatusDescription());
            dbAttorneyProductivityReport.setLegalAssistant(newAttorneyProductivityReport.getLegalAssistant());
            dbAttorneyProductivityReport.setParaLegal(newAttorneyProductivityReport.getParalegal());

            //fetch fee sharing % and calculate fee sharing amount by passing matter number in matter fees sharing table [tblmatterfeesharingid]
            List<AttorneyProductivityReportFeeShareImpl> feeShares = matterGenAccRepository.getAttorneyFeeSharing(
                    newAttorneyProductivityReport.getMatterNumber());

            Double billedFees = 0D;
            //calculate billed fees amount by passing matter number, case open date as invoice posting date in invoice header and
            //sum the bill_amount in invoice line table where item_code = 1
            if (attorneyProductivityInput.getCaseFiledStartDate() == null && attorneyProductivityInput.getCaseFiledEndDate() == null) {
                billedFees = matterGenAccRepository.getAttorneyBilledFees(newAttorneyProductivityReport.getMatterNumber(),
                        attorneyProductivityInput.getCaseOpenDate(),
                        attorneyProductivityInput.getCaseEndDate());
            }
            //priority to calculate billed fees based on case filed date as requested by client
            if (attorneyProductivityInput.getCaseFiledStartDate() != null && attorneyProductivityInput.getCaseFiledEndDate() != null) {
                billedFees = matterGenAccRepository.getAttorneyBilledFees(newAttorneyProductivityReport.getMatterNumber(),
                        attorneyProductivityInput.getCaseFiledStartDate(),
                        attorneyProductivityInput.getCaseFiledEndDate());
            }

            dbAttorneyProductivityReport.setInvoiceAmount(billedFees);

            List<FeeShareAttorneyReport> newFeeShareAttorneyReportList = new ArrayList<>();
            int i = 1;
            if (feeShares.size() > 0) {
                for (AttorneyProductivityReportFeeShareImpl newFeeShare : feeShares) {
                    FeeShareAttorneyReport dbFeeShareAttorneyReport = new FeeShareAttorneyReport();
                    dbFeeShareAttorneyReport.setMatterNumber(newAttorneyProductivityReport.getMatterNumber());
                    dbFeeShareAttorneyReport.setTimeKeeperCode(newFeeShare.getTimeKeeperCode());
                    dbFeeShareAttorneyReport.setFeeSharingPercentage(newFeeShare.getFeeSharePercentage());

                    Double feeShareAmount = 0d;

                    if (dbFeeShareAttorneyReport.getFeeSharingPercentage() != null && dbAttorneyProductivityReport.getFlatFee() != null) {
                        feeShareAmount = (Double.parseDouble(dbFeeShareAttorneyReport.getFeeSharingPercentage()) * Double.parseDouble(dbAttorneyProductivityReport.getFlatFee())) / 100;
                    }

                    dbFeeShareAttorneyReport.setFeeSharingAmount(feeShareAmount);
                    newFeeShareAttorneyReportList.add(dbFeeShareAttorneyReport);
                    //code added for client requirement - requested from UI
                    if (i == 1) {
                        dbAttorneyProductivityReport.setFeeSharingAmount1(feeShareAmount);
                        dbAttorneyProductivityReport.setTimeKeeperCode1(newFeeShare.getTimeKeeperCode());
                        dbAttorneyProductivityReport.setFeeSharingPercentage1(newFeeShare.getFeeSharePercentage());
                    }
                    if (i == 2) {
                        dbAttorneyProductivityReport.setFeeSharingAmount2(feeShareAmount);
                        dbAttorneyProductivityReport.setTimeKeeperCode2(newFeeShare.getTimeKeeperCode());
                        dbAttorneyProductivityReport.setFeeSharingPercentage2(newFeeShare.getFeeSharePercentage());
                    }
                    if (i == 3) {
                        dbAttorneyProductivityReport.setFeeSharingAmount3(feeShareAmount);
                        dbAttorneyProductivityReport.setTimeKeeperCode3(newFeeShare.getTimeKeeperCode());
                        dbAttorneyProductivityReport.setFeeSharingPercentage3(newFeeShare.getFeeSharePercentage());
                    }
                    i = i + 1;
                }
            }

            dbAttorneyProductivityReport.setFeeShareAttorneyReports(newFeeShareAttorneyReportList);
            newAttorneyProductivityReportList.add(dbAttorneyProductivityReport);
        }

        return newAttorneyProductivityReportList;
    }

    /**
     * @param immigrationMatter
     * @return
     * @throws ParseException
     */
//	public List<MatterImmigrationReport> getMatterImmigrationReport(ImmigrationMatter immigrationMatter) throws ParseException {
//		SearchMatterAssignmentIMMReport searchMatterAssignmentIMMReport =
//				immigrationMatter.getSearchMatterAssignmentIMMReport();
//		SearchMatterIMMReport searchMatterIMMReport = immigrationMatter.getSearchMatterIMMReport();
//
//		if (searchMatterIMMReport.getFromCaseOpenDate() != null && searchMatterIMMReport.getToCaseOpenDate() != null) {
//			Date[] dates = DateUtils.addTimeToDatesForSearch(searchMatterIMMReport.getFromCaseOpenDate(), searchMatterIMMReport.getToCaseOpenDate());
//			searchMatterIMMReport.setFromCaseOpenDate(dates[0]);
//			searchMatterIMMReport.setToCaseOpenDate(dates[1]);
//		}
//
//		if (searchMatterIMMReport.getFromCaseClosedDate() != null && searchMatterIMMReport.getToCaseClosedDate() != null) {
//			Date[] dates = DateUtils.addTimeToDatesForSearch(searchMatterIMMReport.getFromCaseClosedDate(), searchMatterIMMReport.getToCaseClosedDate());
//			searchMatterIMMReport.setFromCaseClosedDate(dates[0]);
//			searchMatterIMMReport.setToCaseClosedDate(dates[1]);
//		}
//
//		MatterGeneralIMMSpecification spec = new MatterGeneralIMMSpecification (searchMatterIMMReport);
//		List<MatterGenAcc> matterGenImmSearchResults = matterGenAccRepository.findAll(spec);
//		log.info("matterGenImmSearchResults: " + matterGenImmSearchResults);
//
//		// Extracting Matternumber
//		List<String> matterNumberList = matterGenImmSearchResults.stream().map(MatterGenAcc::getMatterNumber).collect(Collectors.toList());
//		searchMatterAssignmentIMMReport.setMatterNumber(matterNumberList);
//
//		MatterAssignmentIMMSpecification assignmentSpec = new MatterAssignmentIMMSpecification (searchMatterAssignmentIMMReport);
//		List<MatterAssignment> matterAssignmentImmSearchResults = matterAssignmentRepository.findAll(assignmentSpec);
//		log.info("matterAssignmentImmSearchResults: " + matterAssignmentImmSearchResults);
//
//		List<MatterImmigrationReport> matterImmigrationReportList = new ArrayList<>();
//		for (MatterGenAcc matterGenAcc : matterGenImmSearchResults) {
//			try {
//				MatterImmigrationReport matterImmigrationReport = new MatterImmigrationReport();
//
//				// CLIENT_ID
//				matterImmigrationReport.setClientId(matterGenAcc.getClientId());
//
//				// MATTER_NO
//				matterImmigrationReport.setMatterNumber(matterGenAcc.getMatterNumber());
//				//CASE_FILED_DATE
//				matterImmigrationReport.setCaseFiledDate(matterGenAcc.getCaseFiledDate());
//				if(matterGenAcc.getCreatedBy()!=null){
//				matterImmigrationReport.setCreatedBy(matterGenAcc.getCreatedBy());}
//				//Referred By
//				if(matterGenAcc.getReferenceField12()!=null){
//				matterImmigrationReport.setReferredBy(matterGenAcc.getReferenceField12());}
//				/*
//				 * Active/Inactive
//				 * --------------------
//				 * Pass MATTER_NO in MATTERGENACC table and Validate STATUS_ID, If STATUS_ID = 30, HARD CODE as "INACTIVE" else "ACTIVE"
//				 */
//				if (matterGenAcc.getStatusId() == 30L) {
//					matterImmigrationReport.setStatus("INACTIVE");
//				} else {
//					matterImmigrationReport.setStatus("ACTIVE");
//				}
//
//				//-----------------------------ClientGeneral-------------------------------------------------------------
//				ClientGeneral clientGeneral = clientGeneralService.getClientGeneral(matterGenAcc.getClientId());
//
//				matterImmigrationReport.setClientName(clientGeneral.getFirstNameLastName());
//
//				/*
//				 * Petitioner's Name
//				 * -------------------------
//				 * If CLIENT_CAT_ID=3 ,Pass CLIENT_ID in CLIENTGENERAL table and fetch CORP_CLIENT_ID.
//				 * Pass CORP_CLIENT_ID as CLIENT_ID in CLIENTGENERAL table and fetch FIRST_LAST_NM
//				 */
//				if (clientGeneral.getClientCategoryId() == 3L && clientGeneral.getCorporationClientId() != null &&
//						clientGeneral.getCorporationClientId().trim().length() > 0) {
//					ClientGeneral corpClientGeneral = clientGeneralService.getClientGeneral(clientGeneral.getCorporationClientId());
//					if (corpClientGeneral != null) {
//						matterImmigrationReport.setPetitionerName(corpClientGeneral.getFirstNameLastName());
//					}
//				} else {
//					matterImmigrationReport.setPetitionerName(clientGeneral.getReferenceField2());
//				}
//
//				// Beneficiary's Name
//				// Pass CLIENT_ID in CLIENTGENERAL table and Validate CLLIENT_CAT_ID=4, if yes fetch FIRST_LAST_NM
//				if (clientGeneral.getClientCategoryId() == 4L || clientGeneral.getClientCategoryId() == 3L) {
//					matterImmigrationReport.setFirstNameLastName(clientGeneral.getFirstNameLastName());
//				}
//
//				// If REF_FIELD_2 is not null. Pass REF_FIELD_2 value as CLIENT_ID in CLIENTGENERAL table and fetch CONT_NO
//				if (clientGeneral.getReferenceField2() != null && clientGeneral.getClientCategoryId() == 4L) {
//					ClientGeneral petClientGeneral = clientGeneralService.getClientGeneral(clientGeneral.getReferenceField2());
//					// Petitioner's Phone Number - CONT_NO
//					matterImmigrationReport.setPetitionerContactNumber(petClientGeneral.getContactNumber());
//
//					// Petitioner's Email Address
//					matterImmigrationReport.setPetitionerEmailId(petClientGeneral.getEmailId());
//				}
//
//				/*
//				 * If CLIENT_CAT_ID=3 ,Pass CLIENT_ID in CLIENTGENERAL table and fetch CORP_CLIENT_ID.
//				 * Pass CORP_CLIENT_ID as CLIENT_ID in CLIENTGENERAL table and fetch CONT_NO
//				 */
//				if (clientGeneral.getClientCategoryId() == 3L && clientGeneral.getCorporationClientId() != null &&
//						clientGeneral.getCorporationClientId().trim().length() > 0) {
//					ClientGeneral corpClientGeneral = clientGeneralService.getClientGeneral(clientGeneral.getCorporationClientId());
//
//					log.info("client corp2 : " + corpClientGeneral);
//					if (corpClientGeneral != null) {
//						matterImmigrationReport.setPetitionerContactNumber(corpClientGeneral.getContactNumber());
//						matterImmigrationReport.setPetitionerEmailId(corpClientGeneral.getEmailId());
//					}
//				}
//
//				// Beneficiary's Phone Number & EmailId
//				if (clientGeneral.getFirstNameLastName() != null) {
//					matterImmigrationReport.setBeneficiaryContactNumber(clientGeneral.getContactNumber());
//					matterImmigrationReport.setBenerficiaryEmailId(clientGeneral.getEmailId());
//				}
//
//				// New Matter Existing Client
//				// VALIDATE MATTER_NO's Number series if the End Number is > 01, then Hard code as Y, else Hardcode as N
//				if (matterGenAcc.getMatterNumber().endsWith("1")) {
//					matterImmigrationReport.setNewMatterExistingClient("Y");
//				} else {
//					matterImmigrationReport.setNewMatterExistingClient("N4");
//				}
//
//				/*
//				 * REFERRAL_ID
//				 * -------------------------
//				 * Fetch from POTENTIALCLIENT table. Pass REFERRAL_ID in referral table and
//				 * fetch REFERRAL_TEXT. Need to display both the values
//				 */
////				if (clientGeneral.getReferralId() != null) {
//////					Referral referral = setupService.getReferralId(clientGeneral.getReferralId(), authToken.getAccess_token());
////					com.mnrclara.api.management.model.setup.Referral referral = referralRepository.findByReferralId(clientGeneral.getReferralId());
////					if (referral != null) {
////						matterImmigrationReport.setReferredBy(referral.getReferralDescription());
////					}
////				}
//
//				/*
//				 * CASE_CATEGORY
//				 * ----------------------
//				 * Fetch CASE_CATEGORY_ID from CLIENTGENERAL table by passing CLIENT_ID. Pass CAE_CATEGORY_ID
//				 * into CASECATEGORY table and fetch CASE_CATEGORY values. Display both the values
//				 */
////				CaseCategory caseCategory = setupService.getCaseCategory(matterGenAcc.getCaseCategoryId(), authToken.getAccess_token());
//				com.mnrclara.api.management.model.setup.CaseCategory caseCategory =
//						caseCategoryRepository.findByCaseCategoryIdAndDeletionIndicator(matterGenAcc.getCaseCategoryId(), 0L);
//				if (caseCategory != null) {
//					matterImmigrationReport.setCaseCategoryId(caseCategory.getCaseCategory());
//				}
//
//				/*
//				 * CASE_SUB_CATEGORY
//				 * ------------------------
//				 * Fetch CASE_SUB_CATEGORY_ID from CLIENTGENERAL table by passing CLIENT_ID.
//				 * Pass CAE_SUB_CATEGORY_ID into CASESUBCATEGORY table and fetch CASE_SUB_CATEGORY values.
//				 * Display both the values
//				 */
////				CaseSubcategory caseSubCategory = setupService.getCaseSubcategory(matterGenAcc.getCaseSubCategoryId(),
////						matterGenAcc.getLanguageId(), matterGenAcc.getClassId(), matterGenAcc.getCaseCategoryId(), authToken.getAccess_token());
//				com.mnrclara.api.management.model.setup.CaseSubcategory caseSubCategory =
//						caseSubcategoryRepository.findByLanguageIdAndClassIdAndCaseCategoryIdAndCaseSubcategoryIdAndDeletionIndicator(
//								matterGenAcc.getLanguageId(), matterGenAcc.getClassId(), matterGenAcc.getCaseCategoryId(),
//								matterGenAcc.getCaseSubCategoryId(), 0L);
//				if (caseSubCategory != null) {
//					matterImmigrationReport.setCaseSubCategoryId(caseSubCategory.getSubCategory());
//				}
//
//				// Matter Description
//				matterImmigrationReport.setMatterDescription(matterGenAcc.getMatterDescription());
//
//				/*
//				 * Billing Mode
//				 * -----------------
//				 * Fetch BILL_MODE_ID from MATTERGENACC table. Pass BILL_MODE_ID in BILLMODE table and
//				 * fetch BILL_MODE_TEXT values
//				 */
//				if (matterGenAcc.getBillingModeId() != null) {
////					BillingMode billingMode = setupService.getBillingMode(Long.valueOf(matterGenAcc.getBillingModeId()), authToken.getAccess_token());
//					com.mnrclara.api.management.model.setup.BillingMode billingMode =
//							billingModeRepository.findByBillingModeId(Long.valueOf(matterGenAcc.getBillingModeId()));
//					if (billingMode != null) {
//						matterImmigrationReport.setBillModeText(billingMode.getBillingModeDescription());
//					}
//				}
//
//				// Client opened Date
//				matterImmigrationReport.setClientOpenedDate(clientGeneral.getCreatedOn());
//
//				// Matter Opened Date
//				matterImmigrationReport.setMatterOpenedDate(matterGenAcc.getCaseOpenedDate());
//
//				// Agrmnt. Amount
//				matterImmigrationReport.setFlatFeeAmount(matterGenAcc.getFlatFeeAmount());
//
//				// Expenses Fee
//				matterImmigrationReport.setExpensesFee(matterGenAcc.getAdministrativeCost());
//
//				// Matter Closed Date
//				matterImmigrationReport.setMatterClosedDate(matterGenAcc.getCaseClosedDate());
//
//				if (matterGenAcc.getReferenceField14() != null && matterGenAcc.getReferenceField14().trim().length() > 0) {
//					matterImmigrationReport.setRetainerPaid(Double.valueOf(matterGenAcc.getReferenceField14()));
//				}
//
//				/*
//				 * Case Sold by, Assigned Assistant, Atty. Handling case
//				 */
//				for (MatterAssignment matterAssignment : matterAssignmentImmSearchResults) {
//					if (matterAssignment != null && matterAssignment.getMatterNumber().equalsIgnoreCase(matterGenAcc.getMatterNumber())) {
//						matterImmigrationReport.setOriginatingTimeKeeper(matterAssignment.getOriginatingTimeKeeper());
//						matterImmigrationReport.setResponsibleTimeKeeper(matterAssignment.getResponsibleTimeKeeper());
//						matterImmigrationReport.setAssignedTimeKeeper(matterAssignment.getAssignedTimeKeeper());
//						matterImmigrationReport.setLegalAssistant(matterAssignment.getLegalAssistant());
//						matterImmigrationReport.setPartner(matterAssignment.getPartner());
//					}
//				}
//
//				// Retainer Paid - PAYMENTUPDATE
//				// Yet to do
//
//				/*
//				 * Attorney's Notes
//				 * -----------------------
//				 * 1. Fetch CLIENT_ID from MATTERGENACC table and pass into CLIENTGENERAL table and fetch POT_CLIENT_ID
//				 * 2. Pass POT_CLIENT_ID in POTENTIALCLIENT table and fetch PC_NOTE_NO
//				 * 3. Pass PC_NOTE_NO as NOTE_NO into NOTES table and fetch NOTES_TEXT values and display
//				 */
////				PotentialClient potentialClient =
////						crmService.getPotentialClient(clientGeneral.getPotentialClientId(), crmAuthToken.getAccess_token());
//				com.mnrclara.api.management.model.crm.PotentialClient potentialClient =
//						potentialClientRepository.findByPotentialClientId(clientGeneral.getPotentialClientId());
//
//				if (potentialClient != null && potentialClient.getPcNotesNumber() != null &&
//						potentialClient.getPcNotesNumber().trim().length() > 0) {
////					Notes notes = crmService.getNotes(potentialClient.getPcNotesNumber(), crmAuthToken.getAccess_token());
//					com.mnrclara.api.management.model.crm.Notes notes =
//							notesRepository.findByNotesNumber(potentialClient.getPcNotesNumber());
//					matterImmigrationReport.setAttorneysNotes(notes.getNotesDescription());
//				}
//
//				/*
//				 * MatterNotes
//				 * -----------------
//				 * Pass MATTERNO in MATTERNOTE table and fetch NOTE_TEXT for the oldest date and display
//				 */
//				MatterNote matterNote = matterNoteService.getMatterNote(matterGenAcc.getMatterNumber());
//				log.info("matterNote : " + matterNote);
//				if (matterNote != null) {
//					matterImmigrationReport.setMatterNotes(matterNote.getNotesDescription());
//				}
//				matterImmigrationReportList.add(matterImmigrationReport);
//
//				MatterAssignment matterAssignment = matterAssignmentRepository.findByMatterNumber(matterGenAcc.getMatterNumber()).orElse(null);
//				log.info("matterAssignment : " + matterAssignment);
//				if (matterAssignment != null) {
//					matterImmigrationReport.setParalegal(matterAssignment.getReferenceField2());
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//				throw e;
//			}
//		}
//		if(immigrationMatter.getSearchMatterAssignmentIMMReport().getLegalAssistant()!=null||
//				immigrationMatter.getSearchMatterAssignmentIMMReport().getLawClerks()!=null||
//				immigrationMatter.getSearchMatterAssignmentIMMReport().getOriginatingTimeKeeper()!=null||
//				immigrationMatter.getSearchMatterAssignmentIMMReport().getResponsibleTimeKeeper()!=null||
//				immigrationMatter.getSearchMatterAssignmentIMMReport().getAssignedTimeKeeper()!=null||
//				immigrationMatter.getSearchMatterAssignmentIMMReport().getPartner()!=null){
//			List<MatterImmigrationReport> newMatterImmigrationReportList = new ArrayList<>();
//			for(MatterAssignment matterAssignment : matterAssignmentImmSearchResults){
//				for(MatterImmigrationReport newMatterImmigrationReport : matterImmigrationReportList){
//					if(matterAssignment.getMatterNumber().equalsIgnoreCase(newMatterImmigrationReport.getMatterNumber())){
//						newMatterImmigrationReportList.add(newMatterImmigrationReport);
//					}
//				}
//			}
//			matterImmigrationReportList=newMatterImmigrationReportList;
//		}
//
//		return matterImmigrationReportList;
//	}
    public List<ImmigrationReportImpl> getMatterImmigrationReport(ImmigrationMatter immigrationMatter) throws ParseException {
        SearchMatterAssignmentIMMReport searchMatterAssignmentIMMReport =
                immigrationMatter.getSearchMatterAssignmentIMMReport();
        SearchMatterIMMReport searchMatterIMMReport = immigrationMatter.getSearchMatterIMMReport();

        if (searchMatterIMMReport.getFromCaseOpenDate() != null && searchMatterIMMReport.getToCaseOpenDate() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(searchMatterIMMReport.getFromCaseOpenDate(), searchMatterIMMReport.getToCaseOpenDate());
            searchMatterIMMReport.setFromCaseOpenDate(dates[0]);
            searchMatterIMMReport.setToCaseOpenDate(dates[1]);
        }

        if (searchMatterIMMReport.getFromCaseClosedDate() != null && searchMatterIMMReport.getToCaseClosedDate() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(searchMatterIMMReport.getFromCaseClosedDate(), searchMatterIMMReport.getToCaseClosedDate());
            searchMatterIMMReport.setFromCaseClosedDate(dates[0]);
            searchMatterIMMReport.setToCaseClosedDate(dates[1]);
        }
        List<ImmigrationReportImpl> iMatterImmigrationReport = matterGenAccRepository.getImmigrationReport(
                searchMatterAssignmentIMMReport.getResponsibleTimeKeeper(),
                searchMatterAssignmentIMMReport.getAssignedTimeKeeper(),
                searchMatterAssignmentIMMReport.getOriginatingTimeKeeper(),
                searchMatterAssignmentIMMReport.getLegalAssistant(),
                searchMatterAssignmentIMMReport.getLawClerks(),
                searchMatterIMMReport.getCaseCategoryId(),
                searchMatterIMMReport.getCaseSubCategoryId(),
                searchMatterAssignmentIMMReport.getPartner(),
                searchMatterIMMReport.getBillingModeId(),
                searchMatterIMMReport.getStatusId(),
                searchMatterIMMReport.getRefferedBy(),
                searchMatterIMMReport.getClassId(),
                searchMatterIMMReport.getFromCaseOpenDate(),
                searchMatterIMMReport.getToCaseOpenDate(),
                searchMatterIMMReport.getFromCaseClosedDate(),
                searchMatterIMMReport.getToCaseClosedDate());

        return iMatterImmigrationReport;
    }

    /**
     * @param lneMatter
     * @return
     * @throws ParseException
     */
//	public List<MatterLNEReport> getMatterLNEReport(LNEMatter lneMatter) throws ParseException {
//		SearchMatterLNEReport searchMatterLNEReport = lneMatter.getSearchMatterLNEReport();
//		SearchMatterAssignmentLNEReport searchMatterAssignmentLNEReport = lneMatter.getSearchMatterAssignmentLNEReport();
//
//		if (searchMatterLNEReport.getFromCaseOpenDate() != null && searchMatterLNEReport.getToCaseOpenDate() != null) {
//			Date[] dates = DateUtils.addTimeToDatesForSearch(searchMatterLNEReport.getFromCaseOpenDate(), searchMatterLNEReport.getToCaseOpenDate());
//			searchMatterLNEReport.setFromCaseOpenDate(dates[0]);
//			searchMatterLNEReport.setToCaseOpenDate(dates[1]);
//		}
//
//		if (searchMatterLNEReport.getFromCaseClosedDate() != null && searchMatterLNEReport.getToCaseClosedDate() != null) {
//			Date[] dates = DateUtils.addTimeToDatesForSearch(searchMatterLNEReport.getFromCaseClosedDate(), searchMatterLNEReport.getToCaseClosedDate());
//			searchMatterLNEReport.setFromCaseClosedDate(dates[0]);
//			searchMatterLNEReport.setToCaseClosedDate(dates[1]);
//		}
//
//		MatterGeneralLNESpecification spec = new MatterGeneralLNESpecification (searchMatterLNEReport);
//		List<MatterGenAcc> matterGenLneSearchResults = matterGenAccRepository.findAll(spec); // Status 26,30
//		log.info("matterGenLneSearchResults: " + matterGenLneSearchResults.size());
//
//		// Extracting Matternumber
//		List<String> matterNumberList = matterGenLneSearchResults.stream().map(MatterGenAcc::getMatterNumber).collect(Collectors.toList());
//		searchMatterAssignmentLNEReport.setMatterNumber(matterNumberList);
//
//		MatterAssignmentLNESpecification assignmentSpec = new MatterAssignmentLNESpecification (searchMatterAssignmentLNEReport);
//		List<MatterAssignment> matterAssignmentLneSearchResults = matterAssignmentRepository.findAll(assignmentSpec);
//		log.info("matterAssignmentLneSearchResults: " + matterAssignmentLneSearchResults.size());
//
//		AuthToken crmAuthToken = authTokenService.getCrmServiceAuthToken();
//		AuthToken authToken = authTokenService.getSetupServiceAuthToken();
//		List<MatterLNEReport> matterLNEReportList = new ArrayList<>();
//		for (MatterGenAcc matterGenAcc : matterGenLneSearchResults) {
//			try {
//				MatterLNEReport matterLNEReport = new MatterLNEReport();
//
//				// CLIENT_ID
//				matterLNEReport.setClientId(matterGenAcc.getClientId());
//
//				// MATTER_NO
//				matterLNEReport.setMatterNumber(matterGenAcc.getMatterNumber());
//
//				// Referred by
//				if(matterGenAcc.getReferenceField12()!=null){
//				matterLNEReport.setReferredBy(matterGenAcc.getReferenceField12());}
//				/*
//				 * Active/Inactive
//				 * --------------------
//				 * Pass MATTER_NO in MATTERGENACC table and Validate STATUS_ID, If STATUS_ID = 30, HARD CODE as "INACTIVE" else "ACTIVE"
//				 */
//				if (matterGenAcc.getStatusId() == 30L) {
//					matterLNEReport.setStatusId("INACTIVE");
//				} else {
//					matterLNEReport.setStatusId("ACTIVE");
//				}
//
//				/*
//				 * CASE_CATEGORY
//				 * ----------------------
//				 * Fetch CASE_CATEGORY_ID from CLIENTGENERAL table by passing CLIENT_ID. Pass CAE_CATEGORY_ID
//				 * into CASECATEGORY table and fetch CASE_CATEGORY values. Display both the values
//				 */
//				CaseCategory caseCategory = setupService.getCaseCategory(matterGenAcc.getCaseCategoryId(), authToken.getAccess_token());
//				log.info("caseCategory : " + caseCategory);
//				if (caseCategory != null) {
//					matterLNEReport.setCaseCategoryId(caseCategory.getCaseCategory());
//				}
//
//				/*
//				 * CASE_SUB_CATEGORY
//				 * ------------------------
//				 * Fetch CASE_SUB_CATEGORY_ID from CLIENTGENERAL table by passing CLIENT_ID.
//				 * Pass CAE_SUB_CATEGORY_ID into CASESUBCATEGORY table and fetch CASE_SUB_CATEGORY values.
//				 * Display both the values
//				 */
//				CaseSubcategory caseSubCategory = setupService.getCaseSubcategory(matterGenAcc.getCaseSubCategoryId(),
//						matterGenAcc.getLanguageId(), matterGenAcc.getClassId(), matterGenAcc.getCaseCategoryId(), authToken.getAccess_token());
//				log.info("caseSubCategory : " + caseSubCategory);
//				if (caseSubCategory != null) {
//					matterLNEReport.setCaseSubCategoryId(caseSubCategory.getSubCategory());
//				}
//
//				// Matter Description
//				matterLNEReport.setMatterDescription(matterGenAcc.getMatterDescription());
//
//				//-----------------------------ClientGeneral-------------------------------------------------------------
//				ClientGeneral clientGeneral = clientGeneralService.getClientGeneral(matterGenAcc.getClientId());
//
//				/*
//				 * Billing Mode
//				 * -----------------
//				 * Fetch BILL_MODE_ID from MATTERGENACC table. Pass BILL_MODE_ID in BILLMODE table and
//				 * fetch BILL_MODE_TEXT values
//				 */
//				if (matterGenAcc.getBillingModeId() != null) {
//					BillingMode billingMode = setupService.getBillingMode(Long.valueOf(matterGenAcc.getBillingModeId()), authToken.getAccess_token());
//					if (billingMode != null) {
//						matterLNEReport.setBillModeText(billingMode.getBillingModeDescription());
//					}
//				}
//
//				// Matter Opened Date
//				matterLNEReport.setMatterOpenedDate(matterGenAcc.getCaseOpenedDate());
//
//				// Matter Closed Date
//				matterLNEReport.setMatterClosedDate(matterGenAcc.getCaseClosedDate());
//
//				// Created by
//				matterLNEReport.setCreatedBy(matterGenAcc.getCreatedBy());
//
//				// Updated by
//				matterLNEReport.setUpdatedBy(matterGenAcc.getUpdatedBy());
//
//				/*
//				 * REFERRAL_ID
//				 * -------------------------
//				 * Fetch from POTENTIALCLIENT table. Pass REFERRAL_ID in referral table and
//				 * fetch REFERRAL_TEXT. Need to display both the values
//				 */
////				if (clientGeneral.getReferralId() != null) {
////					Referral referral = setupService.getReferralId(clientGeneral.getReferralId(), authToken.getAccess_token());
////					if (referral != null) {
////						matterLNEReport.setReferredBy(referral.getReferralDescription());
////					}
////				}
//
//				log.info("clientGeneral.getCorporationClientId() :  " + clientGeneral.getCorporationClientId());
//				// Corporate Name/Client Name
//				if (clientGeneral.getCorporationClientId() != null && clientGeneral.getCorporationClientId().trim().length() > 0) {
//					ClientGeneral corpClientGeneral = clientGeneralService.getClientGeneral(clientGeneral.getCorporationClientId());
//					matterLNEReport.setCorporateName(corpClientGeneral.getFirstNameLastName());
//				}
//
//				/*
//				 * Case Sold by, Assigned Assistant, Atty. Handling case
//				 */
//				for (MatterAssignment matterAssignment : matterAssignmentLneSearchResults) {
//					if (matterAssignment != null && matterAssignment.getMatterNumber().equalsIgnoreCase(matterGenAcc.getMatterNumber())) {
//						matterLNEReport.setOriginatingTimeKeeper(matterAssignment.getOriginatingTimeKeeper());
//						matterLNEReport.setResponsibleTimeKeeper(matterAssignment.getResponsibleTimeKeeper());
//						matterLNEReport.setAssignedTimeKeeper(matterAssignment.getAssignedTimeKeeper());
//					}
//				}
//
//				/*
//				 * Attorney's Notes
//				 * -----------------------
//				 * 1. Fetch CLIENT_ID from MATTERGENACC table and pass into CLIENTGENERAL table and fetch POT_CLIENT_ID
//				 * 2. Pass POT_CLIENT_ID in POTENTIALCLIENT table and fetch PC_NOTE_NO
//				 * 3. Pass PC_NOTE_NO as NOTE_NO into NOTES table and fetch NOTES_TEXT values and display
//				 */
//				PotentialClient potentialClient =
//						crmService.getPotentialClient(clientGeneral.getPotentialClientId(), crmAuthToken.getAccess_token());
//				if (potentialClient != null) {
//					Notes notes = crmService.getNotes(potentialClient.getPcNotesNumber(), crmAuthToken.getAccess_token());
//					matterLNEReport.setNotesText(notes.getNotesDescription());
//				}
//
//				//----------------------------LECASEINFOSHEET----------------------------------------------------------
//				matterLNEReport = populateLECaseInfoSheet (matterGenAcc.getCaseInformationNo(), matterLNEReport);
//				if (matterLNEReport != null) {
//					matterLNEReportList.add(matterLNEReport);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		if(lneMatter.getSearchMatterAssignmentLNEReport().getLegalAssistant()!=null||
//				lneMatter.getSearchMatterAssignmentLNEReport().getLawClerks()!=null||
//				lneMatter.getSearchMatterAssignmentLNEReport().getOriginatingTimeKeeper()!=null||
//				lneMatter.getSearchMatterAssignmentLNEReport().getResponsibleTimeKeeper()!=null||
//				lneMatter.getSearchMatterAssignmentLNEReport().getAssignedTimeKeeper()!=null||
//				lneMatter.getSearchMatterAssignmentLNEReport().getPartner()!=null){
//			List<MatterLNEReport> newMatterLNEReportList = new ArrayList<>();
//			for(MatterAssignment matterAssignment : matterAssignmentLneSearchResults){
//				for(MatterLNEReport newMatterLNEReport : matterLNEReportList){
//					if(matterAssignment.getMatterNumber().equalsIgnoreCase(newMatterLNEReport.getMatterNumber())){
//						newMatterLNEReportList.add(newMatterLNEReport);
//					}
//				}
//			}
//			matterLNEReportList=newMatterLNEReportList;
//		}
//		log.info("matterLNEReportList : " + matterLNEReportList.size());
//		return matterLNEReportList;
//	}
    public List<MatterLNEReport> getMatterLNEReport(LNEMatter lneMatter) throws ParseException {
        SearchMatterLNEReport searchMatterLNEReport = lneMatter.getSearchMatterLNEReport();
        SearchMatterAssignmentLNEReport searchMatterAssignmentLNEReport = lneMatter.getSearchMatterAssignmentLNEReport();

        if (searchMatterLNEReport.getFromCaseOpenDate() != null && searchMatterLNEReport.getToCaseOpenDate() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(searchMatterLNEReport.getFromCaseOpenDate(), searchMatterLNEReport.getToCaseOpenDate());
            searchMatterLNEReport.setFromCaseOpenDate(dates[0]);
            searchMatterLNEReport.setToCaseOpenDate(dates[1]);
        }

        if (searchMatterLNEReport.getFromCaseClosedDate() != null && searchMatterLNEReport.getToCaseClosedDate() != null) {
            Date[] dates = DateUtils.addTimeToDatesForSearch(searchMatterLNEReport.getFromCaseClosedDate(), searchMatterLNEReport.getToCaseClosedDate());
            searchMatterLNEReport.setFromCaseClosedDate(dates[0]);
            searchMatterLNEReport.setToCaseClosedDate(dates[1]);
        }
        List<LNEReportImpl> iMatterLNEReport = matterGenAccRepository.getLNEReport(
                searchMatterAssignmentLNEReport.getResponsibleTimeKeeper(),
                searchMatterAssignmentLNEReport.getAssignedTimeKeeper(),
                searchMatterAssignmentLNEReport.getOriginatingTimeKeeper(),
                searchMatterAssignmentLNEReport.getLegalAssistant(),
                searchMatterAssignmentLNEReport.getLawClerks(),
                searchMatterLNEReport.getCaseCategoryId(),
                searchMatterLNEReport.getCaseSubCategoryId(),
                searchMatterAssignmentLNEReport.getPartner(),
                searchMatterLNEReport.getBillingModeId(),
                searchMatterLNEReport.getStatusId(),
                searchMatterLNEReport.getRefferedBy(),
                searchMatterLNEReport.getClassId(),
                searchMatterLNEReport.getFromCaseOpenDate(),
                searchMatterLNEReport.getToCaseOpenDate(),
                searchMatterLNEReport.getFromCaseClosedDate(),
                searchMatterLNEReport.getToCaseClosedDate());

        List<MatterLNEReport> matterLNEReportList = new ArrayList<>();
        for (LNEReportImpl matterGenAcc : iMatterLNEReport) {
            try {
                MatterLNEReport matterLNEReport = new MatterLNEReport();
                // CLIENT_ID
                matterLNEReport.setClientId(matterGenAcc.getClientId());
                // MATTER_NO
                matterLNEReport.setMatterNumber(matterGenAcc.getMatterNumber());
                // Referred by
                matterLNEReport.setReferredBy(matterGenAcc.getReferredBy());
                /*
                 * Active/Inactive
                 * --------------------
                 * Pass MATTER_NO in MATTERGENACC table and Validate STATUS_ID, If STATUS_ID = 30, HARD CODE as "INACTIVE" else "ACTIVE"
                 */
                matterLNEReport.setStatus(matterGenAcc.getStatus());
                /*
                 * CASE_CATEGORY
                 * ----------------------
                 * Fetch CASE_CATEGORY_ID from CLIENTGENERAL table by passing CLIENT_ID. Pass CAE_CATEGORY_ID
                 * into CASECATEGORY table and fetch CASE_CATEGORY values. Display both the values
                 */
                matterLNEReport.setCaseCategoryId(matterGenAcc.getCaseCategory());
                /*
                 * CASE_SUB_CATEGORY
                 * ------------------------
                 * Fetch CASE_SUB_CATEGORY_ID from CLIENTGENERAL table by passing CLIENT_ID.
                 * Pass CAE_SUB_CATEGORY_ID into CASESUBCATEGORY table and fetch CASE_SUB_CATEGORY values.
                 * Display both the values
                 */
                matterLNEReport.setCaseSubCategoryId(matterGenAcc.getCaseSubCategory());
                // Matter Description
                matterLNEReport.setMatterDescription(matterGenAcc.getMatterDescription());
                //-----------------------------ClientGeneral-------------------------------------------------------------
                /*
                 * Billing Mode
                 * -----------------
                 * Fetch BILL_MODE_ID from MATTERGENACC table. Pass BILL_MODE_ID in BILLMODE table and
                 * fetch BILL_MODE_TEXT values
                 */
                matterLNEReport.setBillModeText(matterGenAcc.getBillingmodeText());
                // Matter Opened Date
                matterLNEReport.setMatterOpenedDate(matterGenAcc.getMatterOpenedDate());
                // Matter Closed Date
                matterLNEReport.setMatterClosedDate(matterGenAcc.getMatterClosedDate());
                // Created by
                matterLNEReport.setCreatedBy(matterGenAcc.getCreatedBy());
                // Updated by
                matterLNEReport.setUpdatedBy(matterGenAcc.getUpdatedBy());

                /*
                 * REFERRAL_ID
                 * -------------------------
                 * Fetch from POTENTIALCLIENT table. Pass REFERRAL_ID in referral table and
                 * fetch REFERRAL_TEXT. Need to display both the values
                 */
//				if (clientGeneral.getReferralId() != null) {
//					Referral referral = setupService.getReferralId(clientGeneral.getReferralId(), authToken.getAccess_token());
//					if (referral != null) {
//						matterLNEReport.setReferredBy(referral.getReferralDescription());
//					}
//				}
                matterLNEReport.setCorporateName(matterGenAcc.getCorporateName());
                /*
                 * Case Sold by, Assigned Assistant, Atty. Handling case
                 */
                matterLNEReport.setOriginatingTimeKeeper(matterGenAcc.getOriginatingTimeKeeper());
                matterLNEReport.setResponsibleTimeKeeper(matterGenAcc.getResponsibleTimeKeeper());
                matterLNEReport.setAssignedTimeKeeper(matterGenAcc.getAssignedTimeKeeper());
                matterLNEReport.setPartner(matterGenAcc.getPartner());
                matterLNEReport.setLegalAssitant(matterGenAcc.getLegalAssistant());
                matterLNEReport.setLawClerk(matterGenAcc.getLawclerk());
                /*
                 * Attorney's Notes
                 * -----------------------
                 * 1. Fetch CLIENT_ID from MATTERGENACC table and pass into CLIENTGENERAL table and fetch POT_CLIENT_ID
                 * 2. Pass POT_CLIENT_ID in POTENTIALCLIENT table and fetch PC_NOTE_NO
                 * 3. Pass PC_NOTE_NO as NOTE_NO into NOTES table and fetch NOTES_TEXT values and display
                 */
                matterLNEReport.setNotesText(matterGenAcc.getNoteDescription());
                //----------------------------LECASEINFOSHEET----------------------------------------------------------
                matterLNEReport = populateLECaseInfoSheet(matterGenAcc.getCaseInfoNumber(), matterLNEReport);
                if (matterLNEReport != null) {
                    matterLNEReportList.add(matterLNEReport);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        log.info("matterLNEReportList : " + matterLNEReportList.size());
        return matterLNEReportList;
    }

//	/**
//	 *
//	 * @param lneMatter
//	 * @return
//	 * @throws ParseException
//	 */
//	public List<MatterLNEReport> getMatterLNEReport(LNEMatter lneMatter) throws ParseException {
//		SearchMatterLNEReport searchMatterLNEReport = lneMatter.getSearchMatterLNEReport();
//		SearchMatterAssignmentLNEReport searchMatterAssignmentLNEReport = lneMatter.getSearchMatterAssignmentLNEReport();
//
//		SearchMatterLNENAssignmentReport newSearchMatterLNENAssignmentReport = new SearchMatterLNENAssignmentReport();
//
//		BeanUtils.copyProperties(searchMatterLNEReport, newSearchMatterLNENAssignmentReport, CommonUtils.getNullPropertyNames(searchMatterLNEReport));
//		BeanUtils.copyProperties(searchMatterAssignmentLNEReport, newSearchMatterLNENAssignmentReport, CommonUtils.getNullPropertyNames(searchMatterAssignmentLNEReport));
//
//		if (newSearchMatterLNENAssignmentReport.getFromCaseOpenDate() != null && newSearchMatterLNENAssignmentReport.getToCaseOpenDate() != null) {
//			Date[] dates = DateUtils.addTimeToDatesForSearch(newSearchMatterLNENAssignmentReport.getFromCaseOpenDate(),
//					newSearchMatterLNENAssignmentReport.getToCaseOpenDate());
//			newSearchMatterLNENAssignmentReport.setFromCaseOpenDate(dates[0]);
//			newSearchMatterLNENAssignmentReport.setToCaseOpenDate(dates[1]);
//		}
//
//		if (newSearchMatterLNENAssignmentReport.getFromCaseClosedDate() != null && newSearchMatterLNENAssignmentReport.getToCaseClosedDate() != null) {
//			Date[] dates = DateUtils.addTimeToDatesForSearch(newSearchMatterLNENAssignmentReport.getFromCaseClosedDate(),
//					newSearchMatterLNENAssignmentReport.getToCaseClosedDate());
//			newSearchMatterLNENAssignmentReport.setFromCaseClosedDate(dates[0]);
//			newSearchMatterLNENAssignmentReport.setToCaseClosedDate(dates[1]);
//		}
//
//		MatterLNEReportSpecification matterLNEAssignmentSpec = new MatterLNEReportSpecification (newSearchMatterLNENAssignmentReport);
//		List<MatterLNEReport> matterLNEReportSearchResults = matterLNEReportRepository.findAll(matterLNEAssignmentSpec);
//		log.info("matterLNEReportSearchResults: " + matterLNEReportSearchResults.size());
//		return matterLNEReportSearchResults;
//	}
//
//	@Scheduled(fixedRate=30*60*1000)
//	public void scheduleMatterLNEReport () throws Exception {
//		log.info("scheduleMatterLNEReport started------>: " + new Date());
//		SearchMatterLNEReport searchMatterLNEReport = new SearchMatterLNEReport ();
//		SearchMatterAssignmentLNEReport searchMatterAssignmentLNEReport = new SearchMatterAssignmentLNEReport();
//
//		Date startDate = DateUtils.convertStringToDate2 ("01-01-2000");
//		Date endDate = new Date();
//
//		Date[] dates = DateUtils.addTimeToDatesForSearch(startDate, endDate);
//		searchMatterLNEReport.setFromCaseOpenDate(dates[0]);
//		searchMatterLNEReport.setToCaseOpenDate(dates[1]);
//		searchMatterLNEReport.setStatusId(Arrays.asList(26L, 30L)); // Status 26,30
//		searchMatterLNEReport.setClassId(1L);
//
//		MatterGeneralLNESpecification spec = new MatterGeneralLNESpecification (searchMatterLNEReport);
//		List<MatterGenAcc> matterGenLneSearchResults = matterGenAccRepository.findAll(spec);
//		log.info("matterGenLneSearchResults: " + matterGenLneSearchResults.size());
//
//		// Extracting Matternumber
//		List<String> matterNumberList = matterGenLneSearchResults.stream().map(MatterGenAcc::getMatterNumber).collect(Collectors.toList());
//		searchMatterAssignmentLNEReport.setMatterNumber(matterNumberList);
//
//		MatterAssignmentLNESpecification assignmentSpec = new MatterAssignmentLNESpecification (searchMatterAssignmentLNEReport);
//		List<MatterAssignment> matterAssignmentLneSearchResults = matterAssignmentRepository.findAll(assignmentSpec);
//		log.info("matterAssignmentLneSearchResults: " + matterAssignmentLneSearchResults.size());
//
//		AuthToken crmAuthToken = authTokenService.getCrmServiceAuthToken();
//		AuthToken authToken = authTokenService.getSetupServiceAuthToken();
//		List<MatterLNEReport> matterLNEReportList = new ArrayList<>();
//		for (MatterGenAcc matterGenAcc : matterGenLneSearchResults) {
//			try {
//				MatterLNEReport matterLNEReport = new MatterLNEReport();
//				matterLNEReport.setMatterLNEReportID(System.currentTimeMillis());
//				matterLNEReport.setClientId(matterGenAcc.getClientId());
//				matterLNEReport.setMatterNumber(matterGenAcc.getMatterNumber());
//				matterLNEReport.setClassId(matterGenAcc.getClassId());
//				/*
//				 * Active/Inactive
//				 * --------------------
//				 * Pass MATTER_NO in MATTERGENACC table and Validate STATUS_ID, If STATUS_ID = 30, HARD CODE as "INACTIVE" else "ACTIVE"
//				 */
//				if (matterGenAcc.getStatusId() == 30L) {
//					matterLNEReport.setStatusId("INACTIVE");
//				} else {
//					matterLNEReport.setStatusId("ACTIVE");
//				}
//
//				/*
//				 * CASE_CATEGORY
//				 * ----------------------
//				 * Fetch CASE_CATEGORY_ID from CLIENTGENERAL table by passing CLIENT_ID. Pass CAE_CATEGORY_ID
//				 * into CASECATEGORY table and fetch CASE_CATEGORY values. Display both the values
//				 */
//				CaseCategory caseCategory = setupService.getCaseCategory(matterGenAcc.getCaseCategoryId(), authToken.getAccess_token());
//				log.info("caseCategory : " + caseCategory);
//				if (caseCategory != null) {
//					matterLNEReport.setCaseCategoryId(caseCategory.getCaseCategory());
//				}
//
//				/*
//				 * CASE_SUB_CATEGORY
//				 * ------------------------
//				 * Fetch CASE_SUB_CATEGORY_ID from CLIENTGENERAL table by passing CLIENT_ID.
//				 * Pass CAE_SUB_CATEGORY_ID into CASESUBCATEGORY table and fetch CASE_SUB_CATEGORY values.
//				 * Display both the values
//				 */
//				CaseSubcategory caseSubCategory = setupService.getCaseSubcategory(matterGenAcc.getCaseSubCategoryId(),
//						matterGenAcc.getLanguageId(), matterGenAcc.getClassId(), matterGenAcc.getCaseCategoryId(), authToken.getAccess_token());
//				log.info("caseSubCategory : " + caseSubCategory);
//				if (caseSubCategory != null) {
//					matterLNEReport.setCaseSubCategoryId(caseSubCategory.getSubCategory());
//				}
//
//				// Matter Description
//				matterLNEReport.setMatterDescription(matterGenAcc.getMatterDescription());
//
//				//-----------------------------ClientGeneral-------------------------------------------------------------
//				ClientGeneral clientGeneral = clientGeneralService.getClientGeneral(matterGenAcc.getClientId());
//
//				/*
//				 * Billing Mode
//				 * -----------------
//				 * Fetch BILL_MODE_ID from MATTERGENACC table. Pass BILL_MODE_ID in BILLMODE table and
//				 * fetch BILL_MODE_TEXT values
//				 */
//				if (matterGenAcc.getBillingModeId() != null) {
//					BillingMode billingMode = setupService.getBillingMode(Long.valueOf(matterGenAcc.getBillingModeId()), authToken.getAccess_token());
//					if (billingMode != null) {
//						matterLNEReport.setBillModeText(billingMode.getBillingModeDescription());
//					}
//				}
//
//				// Matter Opened Date
//				matterLNEReport.setMatterOpenedDate(matterGenAcc.getCaseOpenedDate());
//
//				// Matter Closed Date
//				matterLNEReport.setMatterClosedDate(matterGenAcc.getCaseClosedDate());
//
//				// Created by
//				matterLNEReport.setCreatedBy(matterGenAcc.getCreatedBy());
//
//				// Updated by
//				matterLNEReport.setUpdatedBy(matterGenAcc.getUpdatedBy());
//
//				/*
//				 * REFERRAL_ID
//				 * -------------------------
//				 * Fetch from POTENTIALCLIENT table. Pass REFERRAL_ID in referral table and
//				 * fetch REFERRAL_TEXT. Need to display both the values
//				 */
//				if (clientGeneral != null && clientGeneral.getReferralId() != null) {
//					Referral referral = setupService.getReferralId(clientGeneral.getReferralId(), authToken.getAccess_token());
//					if (referral != null) {
//						matterLNEReport.setReferredBy(referral.getReferralDescription());
//					}
//				}
//
//				log.info("clientGeneral.getCorporationClientId() :  " + clientGeneral.getCorporationClientId());
//				// Corporate Name/Client Name
//				if (clientGeneral.getCorporationClientId() != null && clientGeneral.getCorporationClientId().trim().length() > 0) {
//					ClientGeneral corpClientGeneral = clientGeneralService.getClientGeneral(clientGeneral.getCorporationClientId());
//					if (corpClientGeneral != null) {
//						matterLNEReport.setCorporateName(corpClientGeneral.getFirstNameLastName());
//					}
//				}
//
//				/*
//				 * Case Sold by, Assigned Assistant, Atty. Handling case
//				 */
//				for (MatterAssignment matterAssignment : matterAssignmentLneSearchResults) {
//					if (matterAssignment != null && matterAssignment.getMatterNumber().equalsIgnoreCase(matterGenAcc.getMatterNumber())) {
//						matterLNEReport.setOriginatingTimeKeeper(matterAssignment.getOriginatingTimeKeeper());
//						matterLNEReport.setResponsibleTimeKeeper(matterAssignment.getResponsibleTimeKeeper());
//						matterLNEReport.setAssignedTimeKeeper(matterAssignment.getAssignedTimeKeeper());
//					}
//				}
//
//				/*
//				 * Attorney's Notes
//				 * -----------------------
//				 * 1. Fetch CLIENT_ID from MATTERGENACC table and pass into CLIENTGENERAL table and fetch POT_CLIENT_ID
//				 * 2. Pass POT_CLIENT_ID in POTENTIALCLIENT table and fetch PC_NOTE_NO
//				 * 3. Pass PC_NOTE_NO as NOTE_NO into NOTES table and fetch NOTES_TEXT values and display
//				 */
//				PotentialClient potentialClient =
//						crmService.getPotentialClient(clientGeneral.getPotentialClientId(), crmAuthToken.getAccess_token());
//				if (potentialClient != null) {
//					Notes notes = crmService.getNotes(potentialClient.getPcNotesNumber(), crmAuthToken.getAccess_token());
//					matterLNEReport.setNotesText(notes.getNotesDescription());
//				}
//
//				//----------------------------LECASEINFOSHEET----------------------------------------------------------
//				matterLNEReport = populateLECaseInfoSheet (matterGenAcc.getCaseInformationNo(), matterLNEReport);
//				if (matterLNEReport != null) {
//					matterLNEReportList.add(matterLNEReport);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		log.info("matterLNEReportList : " + matterLNEReportList.size());
//		matterLNEReportRepository.deleteAll();
//		matterLNEReportRepository.saveAll(matterLNEReportList);
//		log.info("matterLNEReportList completed.... ");
//	}

    /**
     * @param caseInfoNumber
     * @param matterLNEReport
     * @return
     */
    private MatterLNEReport populateLECaseInfoSheet(String caseInfoNumber, MatterLNEReport matterLNEReport) {
        // Fetch CASEINFO_NO from MATTERGENACC table by passing MATTER_NO, pass CASEINFO_NO in LECASEINFOSHEET table and
        // fetch the values
        LeCaseInfoSheet leCaseInfoSheet = leCaseInfoSheetService.getLeCaseInfoSheet(caseInfoNumber);
        if (leCaseInfoSheet != null) {
            BeanUtils.copyProperties(leCaseInfoSheet, matterLNEReport, CommonUtils.getNullPropertyNames(leCaseInfoSheet));
            return matterLNEReport;
        }
        return matterLNEReport;
    }

    /**
     * @param currentDates
     * @return
     */
    private Double[] getHardCosftCost(Date[] currentDates, String matterNumber) {
        Double hardCostCurrent = matterExpenseRepository.findHarcCostExpAmount(currentDates[1], currentDates[0], matterNumber);
        Double softCostCurrent = matterExpenseRepository.findSoftCostExpAmount(currentDates[1], currentDates[0], matterNumber);

//		log.info("getHardCosftCost----1-----> : " + matterNumber + ":" + currentDates[1] + "," + currentDates[0] );
//		log.info("getHardCosftCost-----2----> : " + hardCostCurrent + "," + softCostCurrent);

        Double[] cost = new Double[2];
        cost[0] = hardCostCurrent;
        cost[1] = softCostCurrent;
        return cost;
    }

    /**
     * getWIPAgedPBReport
     *
     * @param wipAgedPBReportInput
     * @return
     * @throws ParseException
     */
    public List<WIPAgedPBReport> getWIPAgedPBReport(WIPAgedPBReportInput wipAgedPBReportInput) throws Exception {
        try {
            List<String[]> matterGenAccs = matterGenAccRepository.getMatterNumbers(wipAgedPBReportInput);
            List<WIPAgedPBReport> reportList = new ArrayList<>();

            // Extracting only MatterNumbers from String array
            Set<String> matterNumberList = new HashSet<>();
            matterGenAccs.forEach(a -> matterNumberList.add(a[0]));

            List<ITimeTicket> current = null;
            List<ITimeTicket> days_30_60 = null;
            List<ITimeTicket> days_60_90 = null;
            List<ITimeTicket> days_90_120 = null;
            List<ITimeTicket> days_120_over = null;

            log.info("wipAgedPBReportInput.getFromDate()---------> : " + wipAgedPBReportInput.getFromDate());
            log.info("wipAgedPBReportInput.getToDate()---------> : " + wipAgedPBReportInput.getToDate());

            // Converting String to Date
            Date fromDate = DateUtils.convertStringToYYYMMDDFormat(wipAgedPBReportInput.getFromDate());
            Date toDate = DateUtils.convertStringToYYYMMDDFormat(wipAgedPBReportInput.getToDate());
            List<Date[]> againgDates = DateUtils.splitAgingDates(toDate, fromDate);
            Map<String, Date[]> mapHardSoftCost = new HashMap<>();

            Date initialDate = DateUtils.convertStringToYYYMMDDFormat("2000-01-01 00:00:00");
            Date previousDate = DateUtils.subtractDaysFromDate(fromDate, 1);

            // TotalAmount Details
            if (!againgDates.isEmpty()) {
                if (againgDates.get(0) != null) {        // Current - days_0_30
                    Date[] currentDates = againgDates.get(0);
                    current = matterTimeTicketRepository.getAccountAgingDetails(currentDates[1], currentDates[0], matterNumberList);
                    log.info("currentDates---------> : " + currentDates[1] + "," + currentDates[0]);
                    mapHardSoftCost.put("CURRENT", currentDates);
                }

                if (againgDates.size() > 1 && againgDates.get(1) != null) {        // days_30_60
                    Date[] currentDates = againgDates.get(1);
                    days_30_60 = matterTimeTicketRepository.getAccountAgingDetails(currentDates[1], currentDates[0], matterNumberList);
                    log.info("days_30_60---------> : " + currentDates[1] + "," + currentDates[0]);
                    mapHardSoftCost.put("days_30_60", currentDates);
                }

                if (againgDates.size() > 2 && againgDates.get(2) != null) {        // days_60_90
                    Date[] currentDates = againgDates.get(2);
                    days_60_90 = matterTimeTicketRepository.getAccountAgingDetails(currentDates[1], currentDates[0], matterNumberList);
                    log.info("days_60_90---------> : " + currentDates[1] + "," + currentDates[0]);
                    mapHardSoftCost.put("days_60_90", currentDates);
                }

                if (againgDates.size() > 3 && againgDates.get(3) != null) {        // days_90_120
                    Date[] currentDates = againgDates.get(3);
                    days_90_120 = matterTimeTicketRepository.getAccountAgingDetails(currentDates[1], currentDates[0], matterNumberList);
                    log.info("days_90_120---------> : " + currentDates[1] + "," + currentDates[0]);
                    mapHardSoftCost.put("days_90_120", currentDates);
                }

                if (againgDates.size() > 4 && againgDates.get(4) != null) {        // days_120_over
                    Date[] currentDates = againgDates.get(4);
                    days_120_over = matterTimeTicketRepository.getAccountAgingDetails(currentDates[1], currentDates[0], matterNumberList);
                    log.info("days_120_over---------> : " + currentDates[1] + "," + currentDates[0]);
                    mapHardSoftCost.put("days_120_over", currentDates);
                }
            }

            boolean is_Current_FullyZero = false;
            boolean is_30_60_FullyZero = false;
            boolean is_60_90_FullyZero = false;
            boolean is_90_120_FullyZero = false;
            boolean is_120_over_FullyZero = false;

            // Populate Timeticket
            for (String matterNumber : matterNumberList) {
                List<String[]> matterDetailList = matterGenAccRepository.getMatterDetail(matterNumber);

                if (!matterDetailList.isEmpty()) {
                    WIPAgedPBReport wipAgedPBReport = new WIPAgedPBReport();
                    String[] matterDetail = matterDetailList.get(0);

                    // Current
                    WIPAgedPBReport.Current objCurrent = wipAgedPBReport.new Current();
                    WIPAgedPBReport.From30To60Days objDays_30_60 = wipAgedPBReport.new From30To60Days();
                    WIPAgedPBReport.From61To90Days objDays_60_90 = wipAgedPBReport.new From61To90Days();
                    WIPAgedPBReport.From91To120DDays objDays_90_120 = wipAgedPBReport.new From91To120DDays();
                    WIPAgedPBReport.Over120Days objOver120Days = wipAgedPBReport.new Over120Days();

                    Optional<ITimeTicket> matchingObject_days_current = current.stream()
                            .filter(p -> p.getMatterNumber().equalsIgnoreCase(matterNumber))
                            .findFirst();
                    if (!matchingObject_days_current.isEmpty()) {
                        ITimeTicket timeTicket_days_current = matchingObject_days_current.get();
                        objCurrent.setFees((timeTicket_days_current.getFees() != null ? timeTicket_days_current.getFees() : 0D));
                        objCurrent.setTimeTicketHours((timeTicket_days_current.getTimeTicketHours() != null ? timeTicket_days_current.getTimeTicketHours() : 0D));
                    }

                    //--------------------------------CURRENT-----------------------------------------------
                    Double[] currentCost = getHardCosftCost(mapHardSoftCost.get("CURRENT"), matterNumber);
                    objCurrent.setHardCost(currentCost[0]);
                    objCurrent.setSoftCosts(currentCost[1]);

                    log.info("currentCost: " + currentCost);
                    log.info("objCurrent.getFees(): " + objCurrent.getFees());
                    log.info("objCurrent.getTimeTicketHours(): " + objCurrent.getTimeTicketHours());
                    log.info("objCurrent.getHardCost(): " + objCurrent.getHardCost());
                    log.info("objCurrent.getSoftCosts(): " + objCurrent.getSoftCosts());

                    // Check whether all values are Zero.
                    if ((objCurrent.getFees() == null || objCurrent.getFees() == 0D) &&
                            (objCurrent.getTimeTicketHours() == null || objCurrent.getTimeTicketHours() == 0D) &&
                            (objCurrent.getHardCost() == null || objCurrent.getHardCost() == 0D) &&
                            (objCurrent.getSoftCosts() == null || objCurrent.getSoftCosts() == 0D)) {
                        is_Current_FullyZero = true;
                    } else {
                        is_Current_FullyZero = false;
                    }
//					wipAgedPBReport.setCurrent(objCurrent);	

                    //--------------------------------days_30_60-----------------------------------------------
                    if (days_30_60 != null) {
                        log.info("days_30_60: " + days_30_60.size());
                        Optional<ITimeTicket> matchingObject_days_30_60 = days_30_60.stream()
                                .filter(p -> p.getMatterNumber().equalsIgnoreCase(matterNumber))
                                .findFirst();
                        if (!matchingObject_days_30_60.isEmpty()) {
                            ITimeTicket timeTicket_days_30_60 = matchingObject_days_30_60.get();
                            objDays_30_60.setFees((timeTicket_days_30_60.getFees() != null ? timeTicket_days_30_60.getFees() : 0D));
                            objDays_30_60.setTimeTicketHours((timeTicket_days_30_60.getTimeTicketHours() != null ? timeTicket_days_30_60.getTimeTicketHours() : 0D));
                            log.info("------matchingObject_days_30_60-------->: " + matterNumber + ":" + timeTicket_days_30_60.getTimeTicketHours());
                        }

                        Double[] cost3060 = getHardCosftCost(mapHardSoftCost.get("days_30_60"), matterNumber);
                        if (cost3060 != null && cost3060.length > 0) {
                            objDays_30_60.setHardCost(cost3060[0]);
                            objDays_30_60.setSoftCosts(cost3060[1]);
                        }

                        log.info("objDays_30_60.getFees(): " + objDays_30_60.getFees());
                        log.info("objDays_30_60.getTimeTicketHours(): " + objDays_30_60.getTimeTicketHours());
                        log.info("objDays_30_60.getHardCost(): " + objDays_30_60.getHardCost());
                        log.info("objDays_30_60.getSoftCosts(): " + objDays_30_60.getSoftCosts());

                        // Check whether all values are Zero.
                        if (days_30_60.size() == 0 || (objDays_30_60.getFees() == null || objDays_30_60.getFees() == 0D) &&
                                (objDays_30_60.getTimeTicketHours() == null || objDays_30_60.getTimeTicketHours() == 0D) &&
                                (objDays_30_60.getHardCost() == null || objDays_30_60.getHardCost() == 0D) &&
                                (objDays_30_60.getSoftCosts() == null || objDays_30_60.getSoftCosts() == 0D)) {
                            is_30_60_FullyZero = true;
                        } else {
                            is_30_60_FullyZero = false;
                        }
//						wipAgedPBReport.setFrom30To60Days(objDays_30_60);
                    }

                    //--------------------------------days_60_90-----------------------------------------------
                    if (days_60_90 != null) {
                        log.info("days_60_90: " + days_60_90.size());
                        Optional<ITimeTicket> matchingObject_days_60_90 = days_60_90.stream()
                                .filter(p -> p.getMatterNumber().equalsIgnoreCase(matterNumber))
                                .findFirst();
                        if (!matchingObject_days_60_90.isEmpty()) {
                            ITimeTicket timeTicket_days_60_90 = matchingObject_days_60_90.get();
                            objDays_60_90.setFees((timeTicket_days_60_90.getFees() != null ? timeTicket_days_60_90.getFees() : 0D));
                            objDays_60_90.setTimeTicketHours((timeTicket_days_60_90.getTimeTicketHours() != null ? timeTicket_days_60_90.getTimeTicketHours() : 0D));
                            log.info("------matchingObject_days_60_90-------->: " + timeTicket_days_60_90.getTimeTicketHours());
                        }

                        Double[] cost6090 = getHardCosftCost(mapHardSoftCost.get("days_60_90"), matterNumber);
                        if (cost6090 != null && cost6090.length > 0) {
                            objDays_60_90.setHardCost(cost6090[0]);
                            objDays_60_90.setSoftCosts(cost6090[1]);
                        }

                        log.info("objDays_60_90.getFees(): " + objDays_60_90.getFees());
                        log.info("objDays_60_90.getTimeTicketHours(): " + objDays_60_90.getTimeTicketHours());
                        log.info("objDays_60_90.getHardCost(): " + objDays_60_90.getHardCost());
                        log.info("objDays_60_90.getSoftCosts(): " + objDays_60_90.getSoftCosts());

                        // Check whether all values are Zero.
                        if ((objDays_60_90.getFees() == null || objDays_60_90.getFees() == 0D) &&
                                (objDays_60_90.getTimeTicketHours() == null || objDays_60_90.getTimeTicketHours() == 0D) &&
                                (objDays_60_90.getHardCost() == null || objDays_60_90.getHardCost() == 0D) &&
                                (objDays_60_90.getSoftCosts() == null || objDays_60_90.getSoftCosts() == 0D)) {
                            is_60_90_FullyZero = true;
                        } else {
                            is_60_90_FullyZero = false;
                        }
//						wipAgedPBReport.setFrom61To90Days(objDays_60_90);	
                    } else {
                        is_60_90_FullyZero = true;
                    }

                    //--------------------------------days_90_120-----------------------------------------------
                    if (days_90_120 != null) {
                        log.info("days_90_120: " + days_90_120.size());
                        Optional<ITimeTicket> matchingObject_days_90_120 = days_90_120.stream()
                                .filter(p -> p.getMatterNumber().equalsIgnoreCase(matterNumber))
                                .findFirst();
                        if (!matchingObject_days_90_120.isEmpty()) {
                            ITimeTicket timeTicket_days_90_120 = matchingObject_days_90_120.get();
                            objDays_90_120.setFees((timeTicket_days_90_120.getFees() != null ? timeTicket_days_90_120.getFees() : 0D));
                            objDays_90_120.setTimeTicketHours((timeTicket_days_90_120.getTimeTicketHours() != null ? timeTicket_days_90_120.getTimeTicketHours() : 0D));
                            log.info("------matchingObject_days_90_120-------->: " + timeTicket_days_90_120.getTimeTicketHours());
                        }

                        Double[] cost90120 = getHardCosftCost(mapHardSoftCost.get("days_90_120"), matterNumber);
                        if (cost90120 != null && cost90120.length > 0) {
                            objDays_90_120.setHardCost(cost90120[0]);
                            objDays_90_120.setSoftCosts(cost90120[1]);
                        }

                        log.info("objDays_90_120.getFees(): " + objDays_90_120.getFees());
                        log.info("objDays_90_120.getTimeTicketHours(): " + objDays_90_120.getTimeTicketHours());
                        log.info("objDays_90_120.getHardCost(): " + objDays_90_120.getHardCost());
                        log.info("objDays_90_120.getSoftCosts(): " + objDays_90_120.getSoftCosts());

                        // Check whether all values are Zero.
                        if ((objDays_90_120.getFees() == null || objDays_90_120.getFees() == 0D) &&
                                (objDays_90_120.getTimeTicketHours() == null || objDays_90_120.getTimeTicketHours() == 0D) &&
                                (objDays_90_120.getHardCost() == null || objDays_90_120.getHardCost() == 0D) &&
                                (objDays_90_120.getSoftCosts() == null || objDays_90_120.getSoftCosts() == 0D)) {
                            is_90_120_FullyZero = true;
                        } else {
                            is_90_120_FullyZero = false;
                        }
//						wipAgedPBReport.setFrom91To120DDays(objDays_90_120);
                    } else {
                        is_90_120_FullyZero = true;
                    }

                    //--------------------------------days_120_over-----------------------------------------------
                    if (days_120_over != null) {
                        log.info("days_120_over: " + days_120_over.size());
                        Optional<ITimeTicket> matchingObject_days_120_over = days_120_over.stream()
                                .filter(p -> p.getMatterNumber().equalsIgnoreCase(matterNumber))
                                .findFirst();
                        if (!matchingObject_days_120_over.isEmpty()) {
                            ITimeTicket timeTicket_days_120_over = matchingObject_days_120_over.get();
                            objOver120Days.setFees((timeTicket_days_120_over.getFees() != null ? timeTicket_days_120_over.getFees() : 0D));
                            objOver120Days.setTimeTicketHours((timeTicket_days_120_over.getTimeTicketHours() != null ? timeTicket_days_120_over.getTimeTicketHours() : 0D));
                        }

                        Double[] costOver120 = getHardCosftCost(mapHardSoftCost.get("days_120_over"), matterNumber);
                        if (costOver120 != null && costOver120.length > 0) {
                            objOver120Days.setHardCost(costOver120[0]);
                            objOver120Days.setSoftCosts(costOver120[1]);
                        }

                        log.info("objOver120Days.getFees(): " + objOver120Days.getFees());
                        log.info("objOver120Days.getTimeTicketHours(): " + objOver120Days.getTimeTicketHours());
                        log.info("objOver120Days.getHardCost(): " + objOver120Days.getHardCost());
                        log.info("objOver120Days.getSoftCosts(): " + objOver120Days.getSoftCosts());

                        // Check whether all values are Zero.
                        if ((objOver120Days.getFees() == null || objOver120Days.getFees() == 0D) &&
                                (objOver120Days.getTimeTicketHours() == null || objOver120Days.getTimeTicketHours() == 0D) &&
                                (objOver120Days.getHardCost() == null || objOver120Days.getHardCost() == 0D) &&
                                (objOver120Days.getSoftCosts() == null || objOver120Days.getSoftCosts() == 0D)) {
                            is_120_over_FullyZero = true;
                        } else {
                            is_120_over_FullyZero = false;
                        }
//						wipAgedPBReport.setOver120Days(objOver120Days);
                    } else {
                        is_120_over_FullyZero = true;
                    }

                    log.info("flags------> : " + is_Current_FullyZero + "," + is_30_60_FullyZero + "," +
                            is_60_90_FullyZero + "," + is_90_120_FullyZero + "," + is_120_over_FullyZero);

                    if (!is_Current_FullyZero || !is_30_60_FullyZero || !is_60_90_FullyZero || !is_90_120_FullyZero || !is_120_over_FullyZero) {
                        wipAgedPBReport.setMatterNumber(matterNumber);    // MATTER_NO
                        wipAgedPBReport.setMatterName(matterDetail[1]);                    // MATTER_TEXT
                        wipAgedPBReport.setClientNumber(matterDetail[2]);                // CLIENT_ID
                        wipAgedPBReport.setClassId(Long.valueOf(matterDetail[3]));        // CLASS_ID
                        wipAgedPBReport.setBillingModeId(matterDetail[6]);                // BILL_MODE_ID
                        wipAgedPBReport.setClientName(matterDetail[5]);                    // CLIENT_NAMEm

                        // Prior Balance
//						Double REM_BAL = invoiceHeaderRepository.getRemainingBalance(matterNumber);
                        Double paymentPaidAmount = invoiceHeaderRepository.getSumOfPmtRecByMatterNumber(matterNumber, initialDate, previousDate);
                        paymentPaidAmount = (paymentPaidAmount != null) ? paymentPaidAmount : 0D;

                        Double REM_BAL = invoiceHeaderRepository.getPriorBalance(matterNumber, paymentPaidAmount, initialDate, previousDate);
                        REM_BAL = (REM_BAL != null) ? REM_BAL : 0D;

                        log.info("paymentPaidAmount : " + paymentPaidAmount);
                        log.info("REM_BAL : " + REM_BAL);

                        wipAgedPBReport.setPriorBalance(REM_BAL);
                        wipAgedPBReport.setPartners(matterDetail[7]);
                        wipAgedPBReport.setResponsibleAttorneys(matterDetail[8]);

                        wipAgedPBReport.setCurrent(objCurrent);
                        wipAgedPBReport.setFrom30To60Days(objDays_30_60);
                        wipAgedPBReport.setFrom61To90Days(objDays_60_90);
                        wipAgedPBReport.setFrom91To120DDays(objDays_90_120);
                        wipAgedPBReport.setOver120Days(objOver120Days);

                        log.info("wipAgedPBReport------> : " + wipAgedPBReport);
                        reportList.add(wipAgedPBReport);

                        is_Current_FullyZero = false;
                        is_30_60_FullyZero = false;
                        is_60_90_FullyZero = false;
                        is_90_120_FullyZero = false;
                        is_120_over_FullyZero = false;
                    }
                }
            }
            log.info("reportList------> : " + reportList.size());

//			if (reportList.size() == 0) {
//				for (String matterNumber : matterNumberList) {
//					reportList.add(prepareWipReport (matterNumber, days_30_60, days_60_90, days_90_120, days_120_over, mapHardSoftCost));
//				}
//			}
            return reportList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param matterNumber
     * @param days_30_60
     * @param days_60_90
     * @param days_90_120
     * @param days_120_over
     * @param mapHardSoftCost
     * @return
     */
    private WIPAgedPBReport prepareWipReport(String matterNumber, List<ITimeTicket> days_30_60,
                                             List<ITimeTicket> days_60_90, List<ITimeTicket> days_90_120, List<ITimeTicket> days_120_over,
                                             Map<String, Date[]> mapHardSoftCost) {
        List<String[]> matterDetailList = matterGenAccRepository.getMatterDetail(matterNumber);
        if (!matterDetailList.isEmpty()) {
            String[] matterDetail = matterDetailList.get(0);
            WIPAgedPBReport wipAgedPBReport = new WIPAgedPBReport();
            wipAgedPBReport.setMatterNumber(matterNumber);    // MATTER_NO
            wipAgedPBReport.setMatterName(matterDetail[1]);                    // MATTER_TEXT
            wipAgedPBReport.setClientNumber(matterDetail[2]);                // CLIENT_ID
            wipAgedPBReport.setClassId(Long.valueOf(matterDetail[3]));        // CLASS_ID
            wipAgedPBReport.setBillingModeId(matterDetail[6]);                // BILL_MODE_ID
            wipAgedPBReport.setClientName(matterDetail[5]);                    // CLIENT_NAME

            // Prior Balance
            Double REM_BAL = invoiceHeaderRepository.getRemainingBalance(matterNumber);
            wipAgedPBReport.setPriorBalance(REM_BAL);
            wipAgedPBReport.setPartners(matterDetail[7]);
            wipAgedPBReport.setResponsibleAttorneys(matterDetail[8]);

            // Current
            WIPAgedPBReport.Current objCurrent = wipAgedPBReport.new Current();
            objCurrent.setFees(0D);
            objCurrent.setTimeTicketHours(0D);

            Double[] currentCost = getHardCosftCost(mapHardSoftCost.get("CURRENT"), matterNumber);
            objCurrent.setHardCost(currentCost[0]);
            objCurrent.setSoftCosts(currentCost[1]);
            wipAgedPBReport.setCurrent(objCurrent);

            // days_30_60
            if (days_30_60 != null) {
                WIPAgedPBReport.From30To60Days objDays_30_60 = wipAgedPBReport.new From30To60Days();
                Double[] cost3060 = getHardCosftCost(mapHardSoftCost.get("days_30_60"), matterNumber);
                if (cost3060 != null && cost3060.length > 0) {
                    objDays_30_60.setHardCost(cost3060[0]);
                    objDays_30_60.setSoftCosts(cost3060[1]);
                }
                wipAgedPBReport.setFrom30To60Days(objDays_30_60);
            }

            // days_60_90
            if (days_60_90 != null) {
                WIPAgedPBReport.From61To90Days objDays_60_90 = wipAgedPBReport.new From61To90Days();
                Double[] cost6090 = getHardCosftCost(mapHardSoftCost.get("days_60_90"), matterNumber);
                if (cost6090 != null && cost6090.length > 0) {
                    objDays_60_90.setHardCost(cost6090[0]);
                    objDays_60_90.setSoftCosts(cost6090[1]);
                }
                wipAgedPBReport.setFrom61To90Days(objDays_60_90);
            }

            // days_90_120
            if (days_90_120 != null) {
                WIPAgedPBReport.From91To120DDays objDays_90_120 = wipAgedPBReport.new From91To120DDays();
                Double[] cost90120 = getHardCosftCost(mapHardSoftCost.get("days_90_120"), matterNumber);
                if (cost90120 != null && cost90120.length > 0) {
                    objDays_90_120.setHardCost(cost90120[0]);
                    objDays_90_120.setSoftCosts(cost90120[1]);
                }
                wipAgedPBReport.setFrom91To120DDays(objDays_90_120);
            }

            // days_120_over
            if (days_120_over != null) {
                WIPAgedPBReport.Over120Days objOver120Days = wipAgedPBReport.new Over120Days();
                Double[] costOver120 = getHardCosftCost(mapHardSoftCost.get("days_120_over"), matterNumber);
                if (costOver120 != null && costOver120.length > 0) {
                    objOver120Days.setHardCost(costOver120[0]);
                    objOver120Days.setSoftCosts(costOver120[1]);
                }
                wipAgedPBReport.setOver120Days(objOver120Days);
            }
            return wipAgedPBReport;
        }
        return null;
    }

    /**
     * @param academicReportInput
     * @return
     * @throws ParseException
     */
    public List<AcademicReportImpl> getAcademicReport(AcademicReportInput academicReportInput) throws ParseException {

        if (academicReportInput.getFromDate() != null && academicReportInput.getToDate() != null) {

            academicReportInput.setStartDate(DateUtils.convertStringToYYYYMMDD(academicReportInput.getFromDate()));
            academicReportInput.setEndDate(DateUtils.convertStringToYYYYMMDD(academicReportInput.getToDate()));

            Date[] dates = DateUtils.addTimeToDatesForSearch(academicReportInput.getStartDate(), academicReportInput.getEndDate());
            academicReportInput.setStartDate(dates[0]);
            academicReportInput.setEndDate(dates[1]);
        }

        if (academicReportInput.getFromFiledDate() != null && academicReportInput.getToFiledDate() != null) {

            academicReportInput.setCaseFiledStartDate(DateUtils.convertStringToYYYYMMDD(academicReportInput.getFromFiledDate()));
            academicReportInput.setCaseFiledEndDate(DateUtils.convertStringToYYYYMMDD(academicReportInput.getToFiledDate()));
            Date[] dates = DateUtils.addTimeToDatesForSearch(academicReportInput.getCaseFiledStartDate(), academicReportInput.getCaseFiledEndDate());
            academicReportInput.setCaseFiledStartDate(dates[0]);
            academicReportInput.setCaseFiledEndDate(dates[1]);
        }
        List<AcademicReportImpl> iAcademicReport = matterGenAccRepository.getAcademicReport(
                academicReportInput.getClassId(),
                academicReportInput.getClientId(),
                academicReportInput.getCorpClientId(),
                academicReportInput.getMatterNumber(),
                academicReportInput.getResponsibleTimeKeeper(),
                academicReportInput.getAssignedTimeKeeper(),
                academicReportInput.getOriginatingTimeKeeper(),
                academicReportInput.getPartner(),
                academicReportInput.getLegalAssistant(),
                academicReportInput.getParalegal(),
                academicReportInput.getCaseCategory(),
                academicReportInput.getCaseSubCategory(),
//				academicReportInput.getDocumentType(),
                academicReportInput.getStartDate(),
                academicReportInput.getEndDate(),
                academicReportInput.getCaseFiledStartDate(),
                academicReportInput.getCaseFiledEndDate());

        return iAcademicReport;
    }
}
