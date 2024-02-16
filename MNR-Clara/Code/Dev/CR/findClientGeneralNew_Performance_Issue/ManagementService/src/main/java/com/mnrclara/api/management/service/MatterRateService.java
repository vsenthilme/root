package com.mnrclara.api.management.service;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import com.mnrclara.api.management.model.dto.PreBillDetails;
import com.mnrclara.api.management.model.matterexpense.MatterExpense;
import com.mnrclara.api.management.model.mattertimeticket.MatterTimeTicket;
import com.mnrclara.api.management.model.mattertimeticket.SearchMatterTimeTicket;
import com.mnrclara.api.management.repository.MatterExpenseRepository;
import com.mnrclara.api.management.repository.MatterTimeTicketRepository;
import com.mnrclara.api.management.repository.PreBillDetailsRepository;
import com.mnrclara.api.management.repository.specification.MatterTimeTicketSpecification;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnrclara.api.management.controller.exception.BadRequestException;
import com.mnrclara.api.management.model.mattergeneral.MatterGenAcc;
import com.mnrclara.api.management.model.matterrate.AddMatterRate;
import com.mnrclara.api.management.model.matterrate.MatterRate;
import com.mnrclara.api.management.model.matterrate.UpdateMatterRate;
import com.mnrclara.api.management.repository.MatterRateRepository;
import com.mnrclara.api.management.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MatterRateService {

    @Autowired
    private MatterRateRepository matterRateRepository;

    @Autowired
    private MatterGenAccService matterGenAccService;

    @Autowired
    private MatterTimeTicketRepository matterTimeTicketRepository;

    @Autowired
    private MatterExpenseRepository matterExpenseRepository;

    @Autowired
    private PreBillDetailsRepository preBillDetailsRepository;

    /**
     * getMatterRates
     *
     * @return
     */
    public List<MatterRate> getMatterRates() {
        List<MatterRate> matterRateList = matterRateRepository.findAll();
        matterRateList = matterRateList.stream()
                .filter(n -> n.getDeletionIndicator() != null && n.getDeletionIndicator() == 0)
                .collect(Collectors.toList());
        return matterRateList;
    }

    /**
     * getMatterRate
     *
     * @param matterRateId
     * @return
     */
    public MatterRate getMatterRate(String matterNumber, String timeKeeperCode) {
        MatterRate matterRate = matterRateRepository.findByMatterNumberAndTimeKeeperCode(matterNumber, timeKeeperCode).orElse(null);
        if (matterRate != null && matterRate.getDeletionIndicator() != null && matterRate.getDeletionIndicator() == 0) {
            return matterRate;
        } else {
            throw new BadRequestException("MatterRate is not assigned for the selected Timekeeper: " + matterNumber);
        }
    }

    /**
     * @param matterNumber
     * @return
     */
    public List<MatterRate> getMatterRate(String matterNumber) {
        List<MatterRate> matterRates = matterRateRepository.findByMatterNumberAndDeletionIndicator(matterNumber, 0L);
        return matterRates;
    }

    /**
     * createMatterRate
     *
     * @param newMatterRate
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public MatterRate createMatterRate(AddMatterRate newMatterRate, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        MatterRate dbMatterRate = new MatterRate();
        BeanUtils.copyProperties(newMatterRate, dbMatterRate, CommonUtils.getNullPropertyNames(newMatterRate));

        MatterGenAcc matterGenAcc = matterGenAccService.getMatterGenAcc(newMatterRate.getMatterNumber());

        // LANG_ID
        dbMatterRate.setLanguageId(matterGenAcc.getLanguageId());

        // CLASS_ID
        dbMatterRate.setClassId(matterGenAcc.getClassId());

        // CLIENT_ID
        dbMatterRate.setClientId(matterGenAcc.getClientId());

        // CASE_CATEGORY_ID
        dbMatterRate.setCaseCategoryId(matterGenAcc.getCaseCategoryId());

        // CASE_SUB_CATEGORY_ID
        dbMatterRate.setCaseSubCategoryId(matterGenAcc.getCaseSubCategoryId());

        // STATUS_ID
        dbMatterRate.setStatusId(matterGenAcc.getStatusId());

        dbMatterRate.setDeletionIndicator(0L);
        dbMatterRate.setCreatedBy(loginUserID);
        dbMatterRate.setUpdatedBy(loginUserID);
        dbMatterRate.setCreatedOn(new Date());
        dbMatterRate.setUpdatedOn(new Date());
        return matterRateRepository.save(dbMatterRate);
    }

    /**
     * @param newMatterRate
     * @param loginUserID
     * @return
     */
    public List<MatterRate> createBulkMatterRate(@Valid AddMatterRate[] newMatterRates, String loginUserID) {
        List<MatterRate> newMatterRateList = new ArrayList<>();
        for (AddMatterRate newMatterRate : newMatterRates) {
            MatterRate dbMatterRate = new MatterRate();
            BeanUtils.copyProperties(newMatterRate, dbMatterRate, CommonUtils.getNullPropertyNames(newMatterRate));
            dbMatterRate.setDeletionIndicator(0L);
            newMatterRateList.add(dbMatterRate);
        }

        return matterRateRepository.saveAll(newMatterRateList);
    }

    /**
     * updateMatterRate
     *
     * @param matterNumber
     * @param updateMatterRate
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public MatterRate updateMatterRate(String matterNumber, String timeKeeperCode, UpdateMatterRate updateMatterRate, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        MatterRate dbMatterRate = getMatterRate(matterNumber, timeKeeperCode);
        BeanUtils.copyProperties(updateMatterRate, dbMatterRate, CommonUtils.getNullPropertyNames(updateMatterRate));
        dbMatterRate.setUpdatedBy(loginUserID);
        dbMatterRate.setUpdatedOn(new Date());
        MatterRate matterRate = matterRateRepository.save(dbMatterRate);
        updateTimeTicket(matterRate);
        return matterRate;
    }

    /**
     * deleteMatterRate
     *
     * @param matterrateCode
     */
    public void deleteMatterRate(String matterNumber, String timeKeeperCode, String loginUserID) {
        MatterRate matterRate = getMatterRate(matterNumber, timeKeeperCode);
        if (matterRate != null) {
            matterRate.setDeletionIndicator(1L);
            matterRate.setUpdatedBy(loginUserID);
            matterRate.setUpdatedOn(new Date());
            matterRateRepository.save(matterRate);
        } else {
            throw new EntityNotFoundException("Error in deleting Id: " + matterNumber);
        }
    }

    /**
     * 
     * @param matterRate
     */
    public void updateTimeTicket(MatterRate matterRate) {
        SearchMatterTimeTicket searchMatterTimeTicket = new SearchMatterTimeTicket();
        searchMatterTimeTicket.setMatterNumber(Arrays.asList(matterRate.getMatterNumber()));
        searchMatterTimeTicket.setTimeKeeperCode(Arrays.asList(matterRate.getTimeKeeperCode()));
        MatterTimeTicketSpecification spec = new MatterTimeTicketSpecification(searchMatterTimeTicket);

        try {
            List<MatterTimeTicket> matterTimeTicketList = matterTimeTicketRepository.findAll(spec);
            List<String> preBillList = new ArrayList<>();
            if (matterTimeTicketList != null && !matterTimeTicketList.isEmpty()) {
                matterTimeTicketList.forEach(data -> {
                    if (data.getBillType().equalsIgnoreCase("billable") && (data.getStatusId().equals(33L) || data.getStatusId().equals(34L) || data.getStatusId().equals(46L) || data.getStatusId().equals(56L))
                            && data.getDeletionIndicator().equals(0L)) {
                        if (data.getReferenceField1() != null) {
                            if (!preBillList.contains(data.getReferenceField1())) {
                                preBillList.add(data.getReferenceField1());
                            }
                        }
                        data.setTimeTicketAmount(matterRate.getAssignedRatePerHour() * (data.getTimeTicketHours() != null ? data.getTimeTicketHours() : 0));
                        //data.setApprovedBillableAmount(matterRate.getAssignedRatePerHour() * (data.getApprovedBillableTimeInHours() != null ? data.getApprovedBillableTimeInHours() : 0));
                        if (data.getStatusId() != 33L && data.getStatusId() != 34L) {
	                        if(data.getReferenceField1() == null) {
	                            data.setApprovedBillableAmount (matterRate.getAssignedRatePerHour() * (data.getTimeTicketHours() != null ? data.getTimeTicketHours() : 0));
	                        } else {
	                            data.setApprovedBillableAmount (matterRate.getAssignedRatePerHour() * (data.getApprovedBillableTimeInHours() != null ? data.getApprovedBillableTimeInHours() : 0));
	                        }
                        }
                        matterTimeTicketRepository.save(data);
                    }
                });
                if (!preBillList.isEmpty()) {
                    preBillList.forEach(preBillNumber -> {
                        Double matterExpenseAmount = matterExpenseRepository.findExpAmountByPreBillNumber(preBillNumber);
                        Double approvedBillableAmount = matterTimeTicketRepository.findApprovedBillableAmountByPreBillNumber(preBillNumber);
                        Double timeTicketAmount = matterTimeTicketRepository.findTimTicketAmountByPreBillNumber(preBillNumber);

                        PreBillDetails preBillDetails = preBillDetailsRepository.findByPreBillNumber(preBillNumber);
                        preBillDetails.setReferenceField5(String.valueOf((matterExpenseAmount != null ? matterExpenseAmount : 0 ) + (approvedBillableAmount != null ? approvedBillableAmount : 0)));
                        preBillDetails.setTotalAmount((matterExpenseAmount != null ? matterExpenseAmount : 0 ) + (timeTicketAmount != null ? timeTicketAmount : 0));
                        preBillDetailsRepository.save(preBillDetails);
                    });
                }
            }
        } catch (Exception e) {
            log.error("Error during matter time ticket update during matter rate update");
        }
    }
}
