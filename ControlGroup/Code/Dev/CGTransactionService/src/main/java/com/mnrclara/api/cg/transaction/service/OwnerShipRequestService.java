package com.mnrclara.api.cg.transaction.service;

import com.mnrclara.api.cg.transaction.exception.BadRequestException;
import com.mnrclara.api.cg.transaction.model.auth.AuthToken;
import com.mnrclara.api.cg.transaction.model.ownershiprequest.AddOwnerShipRequest;
import com.mnrclara.api.cg.transaction.model.ownershiprequest.FindOwnerShipRequest;
import com.mnrclara.api.cg.transaction.model.ownershiprequest.OwnerShipRequest;
import com.mnrclara.api.cg.transaction.model.ownershiprequest.UpdateOwnerShipRequest;
import com.mnrclara.api.cg.transaction.repository.OwnerShipRequestRepository;
import com.mnrclara.api.cg.transaction.repository.specification.OwnerShipRequestSpecification;
import com.mnrclara.api.cg.transaction.util.CommonUtils;
import com.mnrclara.api.cg.transaction.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OwnerShipRequestService {


    @Autowired
    private SetupService setupService;

    @Autowired
    private OwnerShipRequestRepository ownerShipRequestRepository;

    /**
     * getAllOwnerShipRequest
     *
     * @return
     */
    public List<OwnerShipRequest> getAllOwnerShipRequest() {
        List<OwnerShipRequest> ownerShipRequestList = ownerShipRequestRepository.findAll();
        ownerShipRequestList = ownerShipRequestList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        log.info("results: " + ownerShipRequestList);
        return ownerShipRequestList;
    }

    /**
     * @param requestId
     * @param companyId
     * @param languageId
     * @return
     */
    public OwnerShipRequest getOwnerShipRequest(Long requestId, String companyId, String languageId) {
        log.info("requestId: " + requestId);

        Optional<OwnerShipRequest> ownerShipRequest =
                ownerShipRequestRepository.findByCompanyIdAndLanguageIdAndRequestIdAndDeletionIndicator(
                        companyId, languageId, requestId, 0L);

        if (ownerShipRequest.isEmpty()) {
            throw new BadRequestException("The given values of companyId: " + companyId +
                    " requestId " + requestId +
                    " languageId " + languageId + "doesn't exists");
        }
        return ownerShipRequest.get();
    }

    /**
     * @param newOwnerRequest
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public OwnerShipRequest createOwnerRequest(AddOwnerShipRequest newOwnerRequest, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {

        Optional<OwnerShipRequest> duplicateOwnerShipRequest =
                ownerShipRequestRepository.findByCompanyIdAndLanguageIdAndRequestIdAndDeletionIndicator(newOwnerRequest.getCompanyId(),
                        newOwnerRequest.getLanguageId(), newOwnerRequest.getRequestId(), 0L);

        if (!duplicateOwnerShipRequest.isEmpty()) {
            throw new EntityNotFoundException("Record is Getting Duplicated");
        } else {
            OwnerShipRequest dbOwnerShipRequest = new OwnerShipRequest();
            BeanUtils.copyProperties(newOwnerRequest, dbOwnerShipRequest, CommonUtils.getNullPropertyNames(newOwnerRequest));
            Long NUM_RAN_CODE = 7L;
            String NUM_RAN_OBJ = "CGREQUEST";
            String C_ID = "1000";
            String LANG_ID = "EN";
            String STORE_ID = setupService.getNextNumberRange(NUM_RAN_CODE,NUM_RAN_OBJ,LANG_ID,C_ID);
            log.info("nextVal from NumberRange for STORE_ID: " + STORE_ID);
            dbOwnerShipRequest.setRequestId(Long.valueOf(STORE_ID));


            dbOwnerShipRequest.setDeletionIndicator(0L);
            dbOwnerShipRequest.setCreatedBy(loginUserID);
            dbOwnerShipRequest.setUpdatedBy(loginUserID);
            dbOwnerShipRequest.setCreatedOn(new Date());
            dbOwnerShipRequest.setUpdatedOn(new Date());
            return ownerShipRequestRepository.save(dbOwnerShipRequest);
        }
    }

    /**
     * @param requestId
     * @param languageId
     * @param companyId
     * @param loginUserID
     * @param updateOwnerShipRequest
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public OwnerShipRequest updateOwnerShipRequest(Long requestId, String languageId, String companyId,
                                                   String loginUserID, UpdateOwnerShipRequest updateOwnerShipRequest)
            throws IllegalAccessException, InvocationTargetException {
        OwnerShipRequest dbOwnerShipRequest = getOwnerShipRequest(requestId, companyId, languageId);
        BeanUtils.copyProperties(updateOwnerShipRequest, dbOwnerShipRequest, CommonUtils.getNullPropertyNames(updateOwnerShipRequest));
        if (updateOwnerShipRequest.getCoOwnerId1() == null) {
            dbOwnerShipRequest.setCoOwnerId1(null);
            dbOwnerShipRequest.setCoOwnerName1(null);
            dbOwnerShipRequest.setCoOwnerPercentage1(null);
        }
        if (updateOwnerShipRequest.getCoOwnerId2() == null) {
            dbOwnerShipRequest.setCoOwnerId2(null);
            dbOwnerShipRequest.setCoOwnerName2(null);
            dbOwnerShipRequest.setCoOwnerPercentage2(null);
        }
        if (updateOwnerShipRequest.getCoOwnerId3() == null) {
            dbOwnerShipRequest.setCoOwnerId3(null);
            dbOwnerShipRequest.setCoOwnerName3(null);
            dbOwnerShipRequest.setCoOwnerPercentage3(null);
        }
        if (updateOwnerShipRequest.getCoOwnerId4() == null) {
            dbOwnerShipRequest.setCoOwnerId4(null);
            dbOwnerShipRequest.setCoOwnerName4(null);
            dbOwnerShipRequest.setCoOwnerPercentage4(null);
        }
        if (updateOwnerShipRequest.getCoOwnerId5() == null) {
            dbOwnerShipRequest.setCoOwnerId5(null);
            dbOwnerShipRequest.setCoOwnerName5(null);
            dbOwnerShipRequest.setCoOwnerPercentage5(null);
        }
        if (updateOwnerShipRequest.getCoOwnerId6() == null) {
            dbOwnerShipRequest.setCoOwnerId6(null);
            dbOwnerShipRequest.setCoOwnerName6(null);
            dbOwnerShipRequest.setCoOwnerPercentage6(null);
        }
        if (updateOwnerShipRequest.getCoOwnerId7() == null) {
            dbOwnerShipRequest.setCoOwnerId7(null);
            dbOwnerShipRequest.setCoOwnerName7(null);
            dbOwnerShipRequest.setCoOwnerPercentage7(null);
        }
        if (updateOwnerShipRequest.getCoOwnerId8() == null) {
            dbOwnerShipRequest.setCoOwnerId8(null);
            dbOwnerShipRequest.setCoOwnerName8(null);
            dbOwnerShipRequest.setCoOwnerPercentage8(null);
        }
        if (updateOwnerShipRequest.getCoOwnerId9() == null) {
            dbOwnerShipRequest.setCoOwnerId9(null);
            dbOwnerShipRequest.setCoOwnerName9(null);
            dbOwnerShipRequest.setCoOwnerPercentage9(null);
        }
        if (updateOwnerShipRequest.getCoOwnerId10() == null) {
            dbOwnerShipRequest.setCoOwnerId10(null);
            dbOwnerShipRequest.setCoOwnerName10(null);
            dbOwnerShipRequest.setCoOwnerPercentage10(null);
        }
        dbOwnerShipRequest.setUpdatedBy(loginUserID);
        dbOwnerShipRequest.setDeletionIndicator(0L);
        dbOwnerShipRequest.setUpdatedOn(new Date());
        return ownerShipRequestRepository.save(dbOwnerShipRequest);
    }

    /**
     * @param requestId
     * @param companyId
     * @param languageId
     * @param loginUserID
     */
    public void deleteOwnerShipRequest(Long requestId, String companyId, String languageId, String loginUserID) {
        OwnerShipRequest dbOwnerRequest = getOwnerShipRequest(requestId, companyId, languageId);
        if (dbOwnerRequest != null) {
            dbOwnerRequest.setDeletionIndicator(1L);
            dbOwnerRequest.setUpdatedBy(loginUserID);
            dbOwnerRequest.setUpdatedOn(new Date());
            ownerShipRequestRepository.save(dbOwnerRequest);
        } else {
            throw new EntityNotFoundException("Error in deleting Id: " + requestId);
        }
    }

    /**
     * @param findOwnerShipRequest
     * @return
     * @throws ParseException
     */
    public List<OwnerShipRequest> findOwnerShipRequest(FindOwnerShipRequest findOwnerShipRequest) throws ParseException {

        if (findOwnerShipRequest.getStartCreatedOn() != null && findOwnerShipRequest.getStartCreatedOn() != null) {
            Date date = DateUtils.convertStringToYYYYMMDD(findOwnerShipRequest.getStartCreatedOn());
            Date date1 = DateUtils.convertStringToYYYYMMDD(findOwnerShipRequest.getEndCreatedOn());

            Date[] dates = DateUtils.addTimeToDatesForSearch(date, date1);
            findOwnerShipRequest.setFromDate(dates[0]);
            findOwnerShipRequest.setToDate(dates[1]);
        }

        OwnerShipRequestSpecification spec = new OwnerShipRequestSpecification(findOwnerShipRequest);
        List<OwnerShipRequest> results = ownerShipRequestRepository.findAll(spec);
        results = results.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        log.info("results: " + results);
        return results;
    }

//    /**
//     *
//     * @param companyId
//     * @param languageId
//     * @param requestId
//     * @param loginUserID
//     * @param updateOwnerShipRequest
//     * @return
//     * @throws IllegalAccessException
//     * @throws InvocationTargetException
//     */
//    public OwnerShipRequest updateShipRequestId(String companyId, String languageId,
//                                                   Long requestId, String loginUserID, AddOwnerShipRequest updateOwnerShipRequest)
//            throws IllegalAccessException, InvocationTargetException {
//
//        OwnerShipRequest dbOwnerShipRequest = getOwnerShipRequest(requestId, companyId, languageId);
//        if (dbOwnerShipRequest != null) {
//            dbOwnerShipRequest.setStatusId2(1L);
//            dbOwnerShipRequest.setUpdatedBy(loginUserID);
//            dbOwnerShipRequest.setUpdatedOn(new Date());
//            dbOwnerShipRequest = ownerShipRequestRepository.save(dbOwnerShipRequest);
//        }
//// Create a new OwnerShipRequest
//        if (dbOwnerShipRequest != null) {
//            OwnerShipRequest newOwnerShipRequest = createOwnerRequest(updateOwnerShipRequest, loginUserID);
//            return newOwnerShipRequest;
//        } else {
//            // Handle the case where the existing record doesn't exist
//            throw new EntityNotFoundException("Record not found");
//        }
//    }
}
