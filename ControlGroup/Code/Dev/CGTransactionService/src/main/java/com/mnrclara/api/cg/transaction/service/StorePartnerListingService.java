package com.mnrclara.api.cg.transaction.service;


import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import com.mnrclara.api.cg.transaction.model.storepartnerlisting.*;
import com.mnrclara.api.cg.transaction.util.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mnrclara.api.cg.transaction.exception.BadRequestException;
import com.mnrclara.api.cg.transaction.model.IkeyValuePair;
import com.mnrclara.api.cg.transaction.repository.OwnerShipRequestRepository;
import com.mnrclara.api.cg.transaction.repository.StorePartnerListingRepository;
import com.mnrclara.api.cg.transaction.repository.specification.StorePartnerListingSpecification;
import com.mnrclara.api.cg.transaction.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StorePartnerListingService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private StorePartnerListingRepository storePartnerListingRepository;

    @Autowired
    private OwnerShipRequestRepository ownerShipRequestRepository;

    /**
     * getAllStorePartnerListing
     *
     * @return
     */
    public List<StorePartnerListing> getAllStorePartnerListing() {
        List<StorePartnerListing> storePartnerListings = storePartnerListingRepository.findAll();
        storePartnerListings = storePartnerListings.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        log.info("results: " + storePartnerListings);
        return storePartnerListings;
    }

    /**
     * @param versionNumber
     * @param storeId
     * @param companyId
     * @param languageId
     * @return
     */
    public StorePartnerListing getStorePartnerListing(Long versionNumber, String storeId, String companyId, String languageId) {

        Optional<StorePartnerListing> storePartnerListing =
                storePartnerListingRepository.findByCompanyIdAndLanguageIdAndVersionNumberAndStoreIdAndDeletionIndicator(
                        companyId, languageId, versionNumber, storeId, 0L);

        if (storePartnerListing.isEmpty()) {
            throw new BadRequestException("The given values of companyId: " + companyId +
                    " versionNumber " + versionNumber +
                    " storeId " + storeId +
                    " languageId " + languageId + "doesn't exists");
        }
        return storePartnerListing.get();
    }

//    /**
//     * @param newStorePartnerListing
//     * @param loginUserID
//     * @return
//     * @throws IllegalAccessException
//     * @throws InvocationTargetException
//     */
//    public StorePartnerListing createStorePartnerListing(AddStorePartnerListing newStorePartnerListing, String loginUserID)
//            throws IllegalAccessException, InvocationTargetException {
//
//        Optional<StorePartnerListing> duplicatePartnerListing =
//                storePartnerListingRepository.findByCompanyIdAndLanguageIdAndVersionNumberAndStoreIdAndDeletionIndicator(
//                        newStorePartnerListing.getCompanyId(),
//                        newStorePartnerListing.getLanguageId(),
//                        newStorePartnerListing.getVersionNumber(),
//                        newStorePartnerListing.getStoreId(),
//                        0L);
//
//
//        if (!duplicatePartnerListing.isEmpty()) {
//            throw new EntityNotFoundException("Record is Getting Duplicated");
//        } else {
//            StorePartnerListing dbStorePartnerListing = new StorePartnerListing();
//            BeanUtils.copyProperties(newStorePartnerListing, dbStorePartnerListing, CommonUtils.getNullPropertyNames(newStorePartnerListing));
//
//            Long versionId = storePartnerListingRepository.getVersionId();
//            if (versionId != null) {
//                dbStorePartnerListing.setVersionNumber(versionId);
//            } else {
//                dbStorePartnerListing.setVersionNumber(1L);
//            }
//            dbStorePartnerListing.setDeletionIndicator(0L);
//            dbStorePartnerListing.setCreatedBy(loginUserID);
//            dbStorePartnerListing.setUpdatedBy(loginUserID);
//            dbStorePartnerListing.setCreatedOn(new Date());
//            dbStorePartnerListing.setUpdatedOn(new Date());
//            return storePartnerListingRepository.save(dbStorePartnerListing);
//        }
//    }

    /**
     * @param newStorePartnerListing
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public StorePartnerListing createStorePartnerListing(AddStorePartnerListing newStorePartnerListing, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {

        StorePartnerListing duplicatePartnerListing =
                storePartnerListingRepository.findByCompanyIdAndLanguageIdAndStoreIdAndStatusId2AndDeletionIndicator(
                        newStorePartnerListing.getCompanyId(),
                        newStorePartnerListing.getLanguageId(),
                        newStorePartnerListing.getStoreId(),
                        0L,
                        0L);


        if (duplicatePartnerListing != null) {
            duplicatePartnerListing.setStatusId2(1L);
        }
        StorePartnerListing dbStorePartnerListing = new StorePartnerListing();
        BeanUtils.copyProperties(newStorePartnerListing, dbStorePartnerListing, CommonUtils.getNullPropertyNames(newStorePartnerListing));

        Long versionId = storePartnerListingRepository.getVersionId();
        if (versionId != null) {
            dbStorePartnerListing.setVersionNumber(versionId);
        } else {
            dbStorePartnerListing.setVersionNumber(1L);
        }
        dbStorePartnerListing.setDeletionIndicator(0L);
        dbStorePartnerListing.setStatusId2(0L);
        dbStorePartnerListing.setCreatedBy(loginUserID);
        dbStorePartnerListing.setUpdatedBy(loginUserID);
        dbStorePartnerListing.setCreatedOn(new Date());
        dbStorePartnerListing.setUpdatedOn(new Date());
        return storePartnerListingRepository.save(dbStorePartnerListing);

    }

    /**
     * @param versionNumber
     * @param storeId
     * @param languageId
     * @param companyId
     * @param loginUserID
     * @param updateStorePartnerListing
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public StorePartnerListing updateStorepartnerListing(Long versionNumber, String storeId, String languageId, String companyId,
                                                         String loginUserID, UpdateStorePartnerListing updateStorePartnerListing)
            throws IllegalAccessException, InvocationTargetException {

        StorePartnerListing dbStorePartnerListing = getStorePartnerListing(versionNumber, storeId, companyId, languageId);

        BeanUtils.copyProperties(updateStorePartnerListing, dbStorePartnerListing,
                CommonUtils.getNullPropertyNames(updateStorePartnerListing));
        dbStorePartnerListing.setUpdatedBy(loginUserID);
        dbStorePartnerListing.setDeletionIndicator(0L);
        dbStorePartnerListing.setUpdatedOn(new Date());
        return storePartnerListingRepository.save(dbStorePartnerListing);
    }

//    /**
//     *
//     * @param versionNumber
//     * @param storeId
//     * @param languageId
//     * @param companyId
//     * @param loginUserID
//     * @param addStorePartnerListing
//     * @return
//     * @throws IllegalAccessException
//     * @throws InvocationTargetException
//     */
//    public StorePartnerListing updateStorePartner(Long versionNumber, String storeId, String languageId, String companyId,
//                                                  String loginUserID, AddStorePartnerListing addStorePartnerListing)
//            throws IllegalAccessException, InvocationTargetException {
//
//        StorePartnerListing dbStorePartnerListing = getStorePartnerListing(versionNumber,storeId, companyId, languageId);
//        if (dbStorePartnerListing != null) {
//            dbStorePartnerListing.setStatusId2(1L);
//            dbStorePartnerListing.setUpdatedBy(loginUserID);
//            dbStorePartnerListing.setUpdatedOn(new Date());
//            dbStorePartnerListing = storePartnerListingRepository.save(dbStorePartnerListing);
//        }
//// Create a new StorePartnerListing
//        if (dbStorePartnerListing != null) {
//            StorePartnerListing newStorePartnerListing = createStorePartnerListing(addStorePartnerListing, loginUserID);
//            return newStorePartnerListing;
//        } else {
//            // Handle the case where the existing record doesn't exist
//            throw new EntityNotFoundException("Record not found");
//        }
//
//    }


    /**
     * @param versionNumber
     * @param storeId
     * @param companyId
     * @param languageId
     * @param loginUserID
     */
    public void deleteStorePartnerListingService(Long versionNumber, String storeId, String companyId,
                                                 String languageId, String loginUserID) {

        StorePartnerListing dbStorePartnerListing = getStorePartnerListing(versionNumber, storeId, companyId, languageId);
        if (dbStorePartnerListing != null) {
            dbStorePartnerListing.setDeletionIndicator(1L);
            dbStorePartnerListing.setUpdatedBy(loginUserID);
            dbStorePartnerListing.setUpdatedOn(new Date());
            storePartnerListingRepository.save(dbStorePartnerListing);
        } else {
            throw new EntityNotFoundException("Error in deleting Id: " + storeId);
        }
    }

    /**
     * @param findStorePartnerListing
     * @return
     * @throws ParseException
     */
    public List<StorePartnerListing> findStorePartnerListing(FindStorePartnerListing findStorePartnerListing)
            throws ParseException {

        if (findStorePartnerListing.getStartCreatedOn() != null && findStorePartnerListing.getStartCreatedOn() != null) {
            Date date = DateUtils.convertStringToYYYYMMDD(findStorePartnerListing.getStartCreatedOn());
            Date date1 = DateUtils.convertStringToYYYYMMDD(findStorePartnerListing.getEndCreatedOn());

            Date[] dates = DateUtils.addTimeToDatesForSearch(date, date1);
            findStorePartnerListing.setFromDate(dates[0]);
            findStorePartnerListing.setToDate(dates[1]);
        }

        StorePartnerListingSpecification spec = new StorePartnerListingSpecification(findStorePartnerListing);
        List<StorePartnerListing> results = storePartnerListingRepository.findAll(spec);
        log.info("results: " + results);
        return results;
    }

    /**
     * @param findStorePartnerListing
     * @return
     * @throws ParseException
     */
    public List<StorePartnerListingImpl> findStorePartnerListingByVersion(FindStorePartnerListing findStorePartnerListing)
            throws ParseException {

        if (findStorePartnerListing.getCompanyId() == null || findStorePartnerListing.getCompanyId().isEmpty()) {
            findStorePartnerListing.setCompanyId(null);
        }
        if (findStorePartnerListing.getLanguageId() == null || findStorePartnerListing.getLanguageId().isEmpty()) {
            findStorePartnerListing.setLanguageId(null);
        }
        if (findStorePartnerListing.getVersionNumber() == null || findStorePartnerListing.getVersionNumber().isEmpty()) {
            findStorePartnerListing.setVersionNumber(null);
        }
        if (findStorePartnerListing.getGroupId() == null || findStorePartnerListing.getGroupId().isEmpty()) {
            findStorePartnerListing.setGroupTypeId(null);
        }
        if (findStorePartnerListing.getStoreId() == null || findStorePartnerListing.getStoreId().isEmpty()) {
            findStorePartnerListing.setStoreId(null);
        }
        if (findStorePartnerListing.getGroupId() == null || findStorePartnerListing.getGroupId().isEmpty()) {
            findStorePartnerListing.setGroupId(null);
        }
        if (findStorePartnerListing.getSubGroupId() == null || findStorePartnerListing.getSubGroupId().isEmpty()) {
            findStorePartnerListing.setSubGroupId(null);
        }
        List<StorePartnerListingImpl> results = storePartnerListingRepository.findStorePartnerListing(
                findStorePartnerListing.getCompanyId(),
                findStorePartnerListing.getLanguageId(),
                findStorePartnerListing.getVersionNumber(),
                findStorePartnerListing.getGroupTypeId(),
                findStorePartnerListing.getStoreId(),
                findStorePartnerListing.getGroupId(),
                findStorePartnerListing.getSubGroupId()
        );
        return results;
    }


    /**
     * @param findMatchResult
     * @return
     * @throws ParseException
     */
    public ResponceObject findResponseObject(FindMatchResult findMatchResult) throws Exception {

        ResponceObject responceObject = new ResponceObject();
        responceObject.setExactMatchResult(new ArrayList<>());
        responceObject.setLikeMatchResult(new ArrayList<>());

        List<String[]> ikeyValuePair = storePartnerListingRepository.findStorePartnerListingByCOOwnerId(findMatchResult);
        List<IkeyValuePair> ikeyValuePairList = storePartnerListingRepository.getLikeMatchResult(
                findMatchResult.getCoOwnerId1(),
                findMatchResult.getCoOwnerId2(),
                findMatchResult.getCoOwnerId3(),
                findMatchResult.getCoOwnerId4(),
                findMatchResult.getCoOwnerId5(),
                findMatchResult.getCoOwnerId6(),
                findMatchResult.getCoOwnerId7(),
                findMatchResult.getCoOwnerId8(),
                findMatchResult.getCoOwnerId9(),
                findMatchResult.getCoOwnerId10()
        );
        if (!ikeyValuePair.isEmpty()) {
            for (String[] ikeyValuePair1 : ikeyValuePair) {
                ExactMatchResultV2 exactMatchResult = new ExactMatchResultV2();

                if (Long.valueOf(ikeyValuePair1[32]) == 0 && Long.valueOf(ikeyValuePair1[33]) == 0) {

                    if (ikeyValuePair1[0] != null) {
                        exactMatchResult.setCoOwnerId1(Long.valueOf(ikeyValuePair1[0]));
                    }
                    if (ikeyValuePair1[1] != null) {
                        exactMatchResult.setCoOwnerId2(Long.valueOf(ikeyValuePair1[1]));
                    }
                    if (ikeyValuePair1[2] != null) {
                        exactMatchResult.setCoOwnerId3(Long.valueOf(ikeyValuePair1[2]));
                    }
                    if (ikeyValuePair1[3] != null) {
                        exactMatchResult.setCoOwnerId4(Long.valueOf(ikeyValuePair1[3]));
                    }
                    if (ikeyValuePair1[4] != null) {
                        exactMatchResult.setCoOwnerId5(Long.valueOf(ikeyValuePair1[4]));
                    }
                    if (ikeyValuePair1[5] != null) {
                        exactMatchResult.setCoOwnerId6(Long.valueOf(ikeyValuePair1[5]));
                    }
                    if (ikeyValuePair1[6] != null) {
                        exactMatchResult.setCoOwnerId7(Long.valueOf(ikeyValuePair1[6]));
                    }
                    if (ikeyValuePair1[7] != null) {
                        exactMatchResult.setCoOwnerId8(Long.valueOf(ikeyValuePair1[7]));
                    }
                    if (ikeyValuePair1[8] != null) {
                        exactMatchResult.setCoOwnerId9(Long.valueOf(ikeyValuePair1[8]));
                    }
                    if (ikeyValuePair1[9] != null) {
                        exactMatchResult.setCoOwnerId10(Long.valueOf(ikeyValuePair1[9]));
                    }


                    exactMatchResult.setCoOwnerName1(ikeyValuePair1[10]);
                    exactMatchResult.setCoOwnerName2(ikeyValuePair1[11]);
                    exactMatchResult.setCoOwnerName3(ikeyValuePair1[12]);
                    exactMatchResult.setCoOwnerName4(ikeyValuePair1[13]);
                    exactMatchResult.setCoOwnerName5(ikeyValuePair1[14]);
                    exactMatchResult.setCoOwnerName6(ikeyValuePair1[15]);
                    exactMatchResult.setCoOwnerName7(ikeyValuePair1[16]);
                    exactMatchResult.setCoOwnerName8(ikeyValuePair1[17]);
                    exactMatchResult.setCoOwnerName9(ikeyValuePair1[18]);
                    exactMatchResult.setCoOwnerName10(ikeyValuePair1[19]);

                    if (ikeyValuePair1[20] != null) {
                        exactMatchResult.setCoOwnerPercentage1(Double.valueOf(ikeyValuePair1[20]));
                    }
                    if (ikeyValuePair1[21] != null) {
                        exactMatchResult.setCoOwnerPercentage2(Double.valueOf(ikeyValuePair1[21]));
                    }
                    if (ikeyValuePair1[22] != null) {
                        exactMatchResult.setCoOwnerPercentage3(Double.valueOf(ikeyValuePair1[22]));
                    }
                    if (ikeyValuePair1[23] != null) {
                        exactMatchResult.setCoOwnerPercentage4(Double.valueOf(ikeyValuePair1[23]));
                    }
                    if (ikeyValuePair1[24] != null) {
                        exactMatchResult.setCoOwnerPercentage5(Double.valueOf(ikeyValuePair1[24]));
                    }
                    if (ikeyValuePair1[25] != null) {
                        exactMatchResult.setCoOwnerPercentage6(Double.valueOf(ikeyValuePair1[25]));
                    }
                    if (ikeyValuePair1[26] != null) {
                        exactMatchResult.setCoOwnerPercentage7(Double.valueOf(ikeyValuePair1[26]));
                    }
                    if (ikeyValuePair1[27] != null) {
                        exactMatchResult.setCoOwnerPercentage8(Double.valueOf(ikeyValuePair1[27]));
                    }
                    if (ikeyValuePair1[28] != null) {
                        exactMatchResult.setCoOwnerPercentage9(Double.valueOf(ikeyValuePair1[28]));
                    }
                    if (ikeyValuePair1[29] != null) {
                        exactMatchResult.setCoOwnerPercentage10(Double.valueOf(ikeyValuePair1[29]));
                    }
                    exactMatchResult.setStoreId(ikeyValuePair1[30]);
                    exactMatchResult.setStoreName(ikeyValuePair1[31]);
                    responceObject.getExactMatchResult().add(exactMatchResult);
                }
            }
        }
        if (!ikeyValuePairList.isEmpty()) {
            for (IkeyValuePair ikeyValuePair1 : ikeyValuePairList) {
                LikeMatchResultV2 likeMatchResult = new LikeMatchResultV2();
                likeMatchResult.setCoOwnerId1(ikeyValuePair1.getCoOwnerId1());
                likeMatchResult.setCoOwnerId2(ikeyValuePair1.getCoOwnerId2());
                likeMatchResult.setCoOwnerId3(ikeyValuePair1.getCoOwnerId3());
                likeMatchResult.setCoOwnerId4(ikeyValuePair1.getCoOwnerId4());
                likeMatchResult.setCoOwnerId5(ikeyValuePair1.getCoOwnerId5());
                likeMatchResult.setCoOwnerId6(ikeyValuePair1.getCoOwnerId6());
                likeMatchResult.setCoOwnerId7(ikeyValuePair1.getCoOwnerId7());
                likeMatchResult.setCoOwnerId8(ikeyValuePair1.getCoOwnerId8());
                likeMatchResult.setCoOwnerId9(ikeyValuePair1.getCoOwnerId9());
                likeMatchResult.setCoOwnerId10(ikeyValuePair1.getCoOwnerId10());
                likeMatchResult.setCoOwnerPercentage1(ikeyValuePair1.getCoOwnerPercentage1());
                likeMatchResult.setCoOwnerPercentage2(ikeyValuePair1.getCoOwnerPercentage2());
                likeMatchResult.setCoOwnerPercentage3(ikeyValuePair1.getCoOwnerPercentage3());
                likeMatchResult.setCoOwnerPercentage4(ikeyValuePair1.getCoOwnerPercentage4());
                likeMatchResult.setCoOwnerPercentage5(ikeyValuePair1.getCoOwnerPercentage5());
                likeMatchResult.setCoOwnerPercentage6(ikeyValuePair1.getCoOwnerPercentage6());
                likeMatchResult.setCoOwnerPercentage7(ikeyValuePair1.getCoOwnerPercentage7());
                likeMatchResult.setCoOwnerPercentage8(ikeyValuePair1.getCoOwnerPercentage8());
                likeMatchResult.setCoOwnerPercentage9(ikeyValuePair1.getCoOwnerPercentage9());
                likeMatchResult.setCoOwnerPercentage10(ikeyValuePair1.getCoOwnerPercentage10());
                likeMatchResult.setCoOwnerName1(ikeyValuePair1.getCoOwnerId1() + "-" + ikeyValuePair1.getCoOwnerName1());
                likeMatchResult.setCoOwnerName2(ikeyValuePair1.getCoOwnerId2() + "-" + ikeyValuePair1.getCoOwnerName2());
                likeMatchResult.setCoOwnerName3(ikeyValuePair1.getCoOwnerId3() + "-" + ikeyValuePair1.getCoOwnerName3());
                likeMatchResult.setCoOwnerName4(ikeyValuePair1.getCoOwnerId4() + "-" + ikeyValuePair1.getCoOwnerName4());
                likeMatchResult.setCoOwnerName5(ikeyValuePair1.getCoOwnerId5() + "-" + ikeyValuePair1.getCoOwnerName5());
                likeMatchResult.setCoOwnerName6(ikeyValuePair1.getCoOwnerId6() + "-" + ikeyValuePair1.getCoOwnerName6());
                likeMatchResult.setCoOwnerName7(ikeyValuePair1.getCoOwnerId7() + "-" + ikeyValuePair1.getCoOwnerName7());
                likeMatchResult.setCoOwnerName8(ikeyValuePair1.getCoOwnerId8() + "-" + ikeyValuePair1.getCoOwnerName8());
                likeMatchResult.setCoOwnerName9(ikeyValuePair1.getCoOwnerId9() + "-" + ikeyValuePair1.getCoOwnerName9());
                likeMatchResult.setCoOwnerName10(ikeyValuePair1.getCoOwnerId10() + "-" + ikeyValuePair1.getCoOwnerName10());
                likeMatchResult.setStoreId(ikeyValuePair1.getStoreId());
                likeMatchResult.setStoreName(ikeyValuePair1.getStoreName());
                responceObject.getLikeMatchResult().add(likeMatchResult);
            }
        }

//        ProcessExactLogic processExactLogic = new ProcessExactLogic();
//        responceObject.setExactMatchResult(processExactLogic.doProcessExactSearch(findMatchResult, responceObject.getExactMatchResult()));
        return responceObject;
    }


//
//    public Group findGroup(FindMatchResult findMatchResult) throws ParseException {
//
//        List<IkeyValuePair> ikeyValuePair = storePartnerListingRepository.getExactMatchResultGroup(
//                findMatchResult.getCoOwnerId1(),
//                findMatchResult.getCoOwnerId2(),
//                findMatchResult.getCoOwnerId3(),
//                findMatchResult.getCoOwnerId4(),
//                findMatchResult.getCoOwnerId5(),
//                findMatchResult.getCoOwnerId6(),
//                findMatchResult.getCoOwnerId7(),
//                findMatchResult.getCoOwnerId8(),
//                findMatchResult.getCoOwnerId9(),
//                findMatchResult.getCoOwnerId10(),
//                findMatchResult.getGroupId()
//        );
//        List<IkeyValuePair> ikeyValuePairList = storePartnerListingRepository.getLikeMatchGroup(
//                findMatchResult.getCoOwnerId1(),
//                findMatchResult.getCoOwnerId2(),
//                findMatchResult.getCoOwnerId3(),
//                findMatchResult.getCoOwnerId4(),
//                findMatchResult.getCoOwnerId5(),
//                findMatchResult.getCoOwnerId6(),
//                findMatchResult.getCoOwnerId7(),
//                findMatchResult.getCoOwnerId5(),
//                findMatchResult.getCoOwnerId5(),
//                findMatchResult.getCoOwnerId5(),
//                findMatchResult.getGroupId()
//        );
//        Group match = new Group();
//        match.setExactMatchGroups(new ArrayList<>());
//        match.setLikeMatchGroup(new ArrayList<>());
//
//        if (!ikeyValuePair.isEmpty()) {
//            ExactMatchGroup exactMatchGroup = null;
//            for (IkeyValuePair ikeyValuePair1 : ikeyValuePair) {
//                if (exactMatchGroup == null || !exactMatchGroup.getGroupId().equals(ikeyValuePair1.getGroupId())) {
//                    exactMatchGroup = new ExactMatchGroup();
//                    exactMatchGroup.setGroupName(ikeyValuePair1.getGroupName());
//                    exactMatchGroup.setGroupId(ikeyValuePair1.getGroupId());
//                    exactMatchGroup.setStores(new ArrayList<>());
//                    match.getExactMatchGroups().add(exactMatchGroup);
//                }
//                Stores stores = new Stores();
//                stores.setStoreId(ikeyValuePair1.getStoreId());
//                stores.setStoreName(ikeyValuePair1.getStoreId() + "-" + ikeyValuePair1.getStoreName());
//                stores.setCoOwnerName1(ikeyValuePair1.getCoOwnerId1() + "-" + ikeyValuePair1.getCoOwnerName1());
//                stores.setCoOwnerName2(ikeyValuePair1.getCoOwnerId2() + "-" + ikeyValuePair1.getCoOwnerName2());
//                stores.setCoOwnerName3(ikeyValuePair1.getCoOwnerId3() + "-" + ikeyValuePair1.getCoOwnerName3());
//                stores.setCoOwnerName4(ikeyValuePair1.getCoOwnerId4() + "-" + ikeyValuePair1.getCoOwnerName4());
//                stores.setCoOwnerName5(ikeyValuePair1.getCoOwnerId5() + "-" + ikeyValuePair1.getCoOwnerName5());
//                stores.setCoOwnerName6(ikeyValuePair1.getCoOwnerId6() + "-" + ikeyValuePair1.getCoOwnerName6());
//                stores.setCoOwnerName7(ikeyValuePair1.getCoOwnerId7() + "-" + ikeyValuePair1.getCoOwnerName7());
//                stores.setCoOwnerName8(ikeyValuePair1.getCoOwnerId8() + "-" + ikeyValuePair1.getCoOwnerName8());
//                stores.setCoOwnerName9(ikeyValuePair1.getCoOwnerId9() + "-" + ikeyValuePair1.getCoOwnerName9());
//                stores.setCoOwnerName10(ikeyValuePair1.getCoOwnerId10() + "-" + ikeyValuePair1.getCoOwnerName10());
//                stores.setCoOwnerPercentage1(ikeyValuePair1.getCoOwnerPercentage1());
//                stores.setCoOwnerPercentage2(ikeyValuePair1.getCoOwnerPercentage2());
//                stores.setCoOwnerPercentage3(ikeyValuePair1.getCoOwnerPercentage3());
//                stores.setCoOwnerPercentage4(ikeyValuePair1.getCoOwnerPercentage4());
//                stores.setCoOwnerPercentage5(ikeyValuePair1.getCoOwnerPercentage5());
//                stores.setCoOwnerPercentage6(ikeyValuePair1.getCoOwnerPercentage6());
//                stores.setCoOwnerPercentage7(ikeyValuePair1.getCoOwnerPercentage7());
//                stores.setCoOwnerPercentage8(ikeyValuePair1.getCoOwnerPercentage8());
//                stores.setCoOwnerPercentage9(ikeyValuePair1.getCoOwnerPercentage9());
//                stores.setCoOwnerPercentage10(ikeyValuePair1.getCoOwnerPercentage10());
//                stores.setGroupType(ikeyValuePair1.getGroupTypeId() + "-" + ikeyValuePair1.getGroupTypeName());
//                stores.setSubGroupType(ikeyValuePair1.getSubGroupTypeId() + "-" + ikeyValuePair1.getSubGroupTypeName());
//                exactMatchGroup.getStores().add(stores);
//            }
//        }
//
//        if (!ikeyValuePairList.isEmpty()) {
//            LikeMatchGroup currentLikeMatchGroup = null;
//            for (IkeyValuePair ikeyValuePair1 : ikeyValuePairList) {
//                if (currentLikeMatchGroup == null || !currentLikeMatchGroup.getGroupId().equals(ikeyValuePair1.getGroupId())) {
//                    currentLikeMatchGroup = new LikeMatchGroup();
//                    currentLikeMatchGroup.setGroupName(ikeyValuePair1.getGroupName());
//                    currentLikeMatchGroup.setGroupId(ikeyValuePair1.getGroupId());
//                    currentLikeMatchGroup.setStores(new ArrayList<>());
//                    match.getLikeMatchGroup().add(currentLikeMatchGroup);
//                }
//                Stores stores = new Stores();
//                stores.setStoreId(ikeyValuePair1.getStoreId());
//                stores.setStoreName(ikeyValuePair1.getStoreId() + "-" + ikeyValuePair1.getStoreName());
//                stores.setCoOwnerName1(ikeyValuePair1.getCoOwnerId1() + "-" + ikeyValuePair1.getCoOwnerName1());
//                stores.setCoOwnerName2(ikeyValuePair1.getCoOwnerId2() + "-" + ikeyValuePair1.getCoOwnerName2());
//                stores.setCoOwnerName3(ikeyValuePair1.getCoOwnerId3() + "-" + ikeyValuePair1.getCoOwnerName3());
//                stores.setCoOwnerName4(ikeyValuePair1.getCoOwnerId4() + "-" + ikeyValuePair1.getCoOwnerName4());
//                stores.setCoOwnerName5(ikeyValuePair1.getCoOwnerId5() + "-" + ikeyValuePair1.getCoOwnerName5());
//                stores.setCoOwnerName6(ikeyValuePair1.getCoOwnerId6() + "-" + ikeyValuePair1.getCoOwnerName6());
//                stores.setCoOwnerName7(ikeyValuePair1.getCoOwnerId7() + "-" + ikeyValuePair1.getCoOwnerName7());
//                stores.setCoOwnerName8(ikeyValuePair1.getCoOwnerId8() + "-" + ikeyValuePair1.getCoOwnerName8());
//                stores.setCoOwnerName9(ikeyValuePair1.getCoOwnerId9() + "-" + ikeyValuePair1.getCoOwnerName9());
//                stores.setCoOwnerName10(ikeyValuePair1.getCoOwnerId10() + "-" + ikeyValuePair1.getCoOwnerName10());
//                stores.setCoOwnerPercentage1(ikeyValuePair1.getCoOwnerPercentage1());
//                stores.setCoOwnerPercentage2(ikeyValuePair1.getCoOwnerPercentage2());
//                stores.setCoOwnerPercentage3(ikeyValuePair1.getCoOwnerPercentage3());
//                stores.setCoOwnerPercentage4(ikeyValuePair1.getCoOwnerPercentage4());
//                stores.setCoOwnerPercentage5(ikeyValuePair1.getCoOwnerPercentage5());
//                stores.setCoOwnerPercentage6(ikeyValuePair1.getCoOwnerPercentage6());
//                stores.setCoOwnerPercentage7(ikeyValuePair1.getCoOwnerPercentage7());
//                stores.setCoOwnerPercentage8(ikeyValuePair1.getCoOwnerPercentage8());
//                stores.setCoOwnerPercentage9(ikeyValuePair1.getCoOwnerPercentage9());
//                stores.setCoOwnerPercentage10(ikeyValuePair1.getCoOwnerPercentage10());
//                stores.setGroupType(ikeyValuePair1.getGroupTypeId() + "-" + ikeyValuePair1.getGroupTypeName());
//                stores.setSubGroupType(ikeyValuePair1.getSubGroupTypeId() + "-" + ikeyValuePair1.getSubGroupTypeName());
//                currentLikeMatchGroup.getStores().add(stores);
//            }
//        }
//
//        return match;
//    }


    public GroupStoreList getAllGroupStore() {
        List<StorePartnerListing> groupStoreLists = storePartnerListingRepository.getStorePartner();

        GroupStoreList groupStoreList = new GroupStoreList();
//        List<GroupIdList> groupIdList = new ArrayList<>();
        Map<String, GroupIdList> groupIdMap = new HashMap<>();
        Map<String, StoreList> storeGroupMap = new HashMap<>();

        if (!groupStoreLists.isEmpty()) {
            for (StorePartnerListing groupList : groupStoreLists) {
                String group = String.valueOf(groupList.getGroupId());
                String groupNM = groupList.getGroupName();
                if (!groupIdMap.containsKey(group)) {
                    GroupIdList groupIdListItem = new GroupIdList();
                    groupIdListItem.setGroupId(group);
                    groupIdListItem.setGroupName(groupNM);
                    groupIdListItem.setNoOfStores(1L);
                    groupIdMap.put(group, groupIdListItem);
                } else {
                    groupIdMap.get(group).setNoOfStores(groupIdMap.get(group).getNoOfStores() + 1);
                }
            }
        }
        if (!groupStoreLists.isEmpty()) {
            for (StorePartnerListing groupStoreList1 : groupStoreLists) {
                String groupId = String.valueOf(groupStoreList1.getGroupId());
                String groupName = groupStoreList1.getGroupName();
                if (!storeGroupMap.containsKey(groupId)) {
                    StoreList storeGroup = new StoreList();
                    storeGroup.setGroupId(groupId);
                    storeGroup.setGroupName(groupName);
                    storeGroup.setStores(new ArrayList<>());
                    storeGroupMap.put(groupId, storeGroup);
                }
                Store store = new Store();
                store.setStoreId(groupStoreList1.getStoreId());
                store.setStoreName(groupStoreList1.getStoreName());
                storeGroupMap.get(groupId).getStores().add(store);
            }
        }
        List<StoreList> storeLists = new ArrayList<>(storeGroupMap.values());
        List<GroupIdList> groupIdLists = new ArrayList<>(groupIdMap.values());
        groupStoreList.setGroupIdList(groupIdLists);
        groupStoreList.setStoreLists(storeLists);
        return groupStoreList;
    }


    /**
     * @param findGroupList
     * @return
     * @throws ParseException
     */
    public BrotherSisterResult findStore(FindMatchResults findGroupList) throws ParseException {

        List<IkeyValuePair> ikeyValuePair = storePartnerListingRepository.getExcerptMatchResult(
                findGroupList.getCoOwnerId1(),
                findGroupList.getCoOwnerId2(),
                findGroupList.getCoOwnerId3(),
                findGroupList.getCoOwnerId4(),
                findGroupList.getCoOwnerId5(),
                findGroupList.getCoOwnerId6(),
                findGroupList.getCoOwnerId7(),
                findGroupList.getCoOwnerId8(),
                findGroupList.getCoOwnerId9(),
                findGroupList.getCoOwnerId10()
        );
        List<IkeyValuePair> ikeyValuePairList = storePartnerListingRepository.getLikeMatchResult(
                findGroupList.getCoOwnerId1(),
                findGroupList.getCoOwnerId2(),
                findGroupList.getCoOwnerId3(),
                findGroupList.getCoOwnerId4(),
                findGroupList.getCoOwnerId5(),
                findGroupList.getCoOwnerId6(),
                findGroupList.getCoOwnerId7(),
                findGroupList.getCoOwnerId8(),
                findGroupList.getCoOwnerId9(),
                findGroupList.getCoOwnerId10()
        );
        BrotherSisterResult match = new BrotherSisterResult();
        match.setExactBrotherSisterResults(new ArrayList<>());
        match.setLikeBrotherSisterResults(new ArrayList<>());

        if (!ikeyValuePair.isEmpty()) {
            for (IkeyValuePair ikeyValuePair1 : ikeyValuePair) {
                ExactBrotherSisterResult exactBrotherSisterResult = new ExactBrotherSisterResult();
                exactBrotherSisterResult.setCoOwnerId1(ikeyValuePair1.getCoOwnerId1());
                exactBrotherSisterResult.setCoOwnerId2(ikeyValuePair1.getCoOwnerId2());
                exactBrotherSisterResult.setCoOwnerId3(ikeyValuePair1.getCoOwnerId3());
                exactBrotherSisterResult.setCoOwnerId4(ikeyValuePair1.getCoOwnerId4());
                exactBrotherSisterResult.setCoOwnerId5(ikeyValuePair1.getCoOwnerId5());
                exactBrotherSisterResult.setCoOwnerId6(ikeyValuePair1.getCoOwnerId6());
                exactBrotherSisterResult.setCoOwnerId7(ikeyValuePair1.getCoOwnerId7());
                exactBrotherSisterResult.setCoOwnerId8(ikeyValuePair1.getCoOwnerId8());
                exactBrotherSisterResult.setCoOwnerId9(ikeyValuePair1.getCoOwnerId9());
                exactBrotherSisterResult.setCoOwnerId10(ikeyValuePair1.getCoOwnerId10());

                if (ikeyValuePair1.getCoOwnerId1() != null && ikeyValuePair1.getCoOwnerId1() != 0) {
                    exactBrotherSisterResult.setCoOwnerName1(ikeyValuePair1.getCoOwnerId1() + "-" + ikeyValuePair1.getCoOwnerName1());
                }
                if (ikeyValuePair1.getCoOwnerId2() != null && ikeyValuePair1.getCoOwnerId2() != 0) {
                    exactBrotherSisterResult.setCoOwnerName2(ikeyValuePair1.getCoOwnerId2() + "-" + ikeyValuePair1.getCoOwnerName2());
                }
                if (ikeyValuePair1.getCoOwnerId3() != null && ikeyValuePair1.getCoOwnerId3() != 0) {
                    exactBrotherSisterResult.setCoOwnerName3(ikeyValuePair1.getCoOwnerId3() + "-" + ikeyValuePair1.getCoOwnerName3());
                }
                if (ikeyValuePair1.getCoOwnerId4() != null && ikeyValuePair1.getCoOwnerId4() != 0) {
                    exactBrotherSisterResult.setCoOwnerName4(ikeyValuePair1.getCoOwnerId4() + "-" + ikeyValuePair1.getCoOwnerName4());
                }
                if (ikeyValuePair1.getCoOwnerId5() != null && ikeyValuePair1.getCoOwnerId5() != 0) {
                    exactBrotherSisterResult.setCoOwnerName5(ikeyValuePair1.getCoOwnerId5() + "-" + ikeyValuePair1.getCoOwnerName5());
                }
                if (ikeyValuePair1.getCoOwnerId6() != null && ikeyValuePair1.getCoOwnerId6() != 0) {
                    exactBrotherSisterResult.setCoOwnerName6(ikeyValuePair1.getCoOwnerId6() + "-" + ikeyValuePair1.getCoOwnerName6());
                }
                if (ikeyValuePair1.getCoOwnerId7() != null && ikeyValuePair1.getCoOwnerId7() != 0) {
                    exactBrotherSisterResult.setCoOwnerName7(ikeyValuePair1.getCoOwnerId7() + "-" + ikeyValuePair1.getCoOwnerName7());
                }
                if (ikeyValuePair1.getCoOwnerId8() != null && ikeyValuePair1.getCoOwnerId8() != 0) {
                    exactBrotherSisterResult.setCoOwnerName8(ikeyValuePair1.getCoOwnerId8() + "-" + ikeyValuePair1.getCoOwnerName8());
                }
                if (ikeyValuePair1.getCoOwnerId9() != null && ikeyValuePair1.getCoOwnerId9() != 0) {
                    exactBrotherSisterResult.setCoOwnerName9(ikeyValuePair1.getCoOwnerId9() + "-" + ikeyValuePair1.getCoOwnerName9());
                }
                if (ikeyValuePair1.getCoOwnerId10() != null && ikeyValuePair1.getCoOwnerId10() != 0) {
                    exactBrotherSisterResult.setCoOwnerName10(ikeyValuePair1.getCoOwnerId10() + "-" + ikeyValuePair1.getCoOwnerName10());
                }
                exactBrotherSisterResult.setCoOwnerPercentage1(ikeyValuePair1.getCoOwnerPercentage1());
                exactBrotherSisterResult.setCoOwnerPercentage2(ikeyValuePair1.getCoOwnerPercentage2());
                exactBrotherSisterResult.setCoOwnerPercentage3(ikeyValuePair1.getCoOwnerPercentage3());
                exactBrotherSisterResult.setCoOwnerPercentage4(ikeyValuePair1.getCoOwnerPercentage4());
                exactBrotherSisterResult.setCoOwnerPercentage5(ikeyValuePair1.getCoOwnerPercentage5());
                exactBrotherSisterResult.setCoOwnerPercentage6(ikeyValuePair1.getCoOwnerPercentage6());
                exactBrotherSisterResult.setCoOwnerPercentage7(ikeyValuePair1.getCoOwnerPercentage7());
                exactBrotherSisterResult.setCoOwnerPercentage8(ikeyValuePair1.getCoOwnerPercentage8());
                exactBrotherSisterResult.setCoOwnerPercentage9(ikeyValuePair1.getCoOwnerPercentage9());
                exactBrotherSisterResult.setCoOwnerPercentage10(ikeyValuePair1.getCoOwnerPercentage10());
                exactBrotherSisterResult.setStoreId(ikeyValuePair1.getStoreId());
                exactBrotherSisterResult.setStoreName(ikeyValuePair1.getStoreName());
                match.getExactBrotherSisterResults().add(exactBrotherSisterResult);
            }
        }
        if (!ikeyValuePairList.isEmpty()) {
            for (IkeyValuePair ikeyValuePair1 : ikeyValuePairList) {
                LikeBrotherSisterResult likeBrotherSisterResult = new LikeBrotherSisterResult();
                likeBrotherSisterResult.setCoOwnerId1(ikeyValuePair1.getCoOwnerId1());
                likeBrotherSisterResult.setCoOwnerId2(ikeyValuePair1.getCoOwnerId2());
                likeBrotherSisterResult.setCoOwnerId3(ikeyValuePair1.getCoOwnerId3());
                likeBrotherSisterResult.setCoOwnerId4(ikeyValuePair1.getCoOwnerId4());
                likeBrotherSisterResult.setCoOwnerId5(ikeyValuePair1.getCoOwnerId5());
                likeBrotherSisterResult.setCoOwnerId6(ikeyValuePair1.getCoOwnerId6());
                likeBrotherSisterResult.setCoOwnerId7(ikeyValuePair1.getCoOwnerId7());
                likeBrotherSisterResult.setCoOwnerId8(ikeyValuePair1.getCoOwnerId8());
                likeBrotherSisterResult.setCoOwnerId9(ikeyValuePair1.getCoOwnerId9());
                likeBrotherSisterResult.setCoOwnerId10(ikeyValuePair1.getCoOwnerId10());

                likeBrotherSisterResult.setCoOwnerPercentage1(ikeyValuePair1.getCoOwnerPercentage1());
                likeBrotherSisterResult.setCoOwnerPercentage2(ikeyValuePair1.getCoOwnerPercentage2());
                likeBrotherSisterResult.setCoOwnerPercentage3(ikeyValuePair1.getCoOwnerPercentage3());
                likeBrotherSisterResult.setCoOwnerPercentage4(ikeyValuePair1.getCoOwnerPercentage4());
                likeBrotherSisterResult.setCoOwnerPercentage5(ikeyValuePair1.getCoOwnerPercentage5());
                likeBrotherSisterResult.setCoOwnerPercentage6(ikeyValuePair1.getCoOwnerPercentage6());
                likeBrotherSisterResult.setCoOwnerPercentage7(ikeyValuePair1.getCoOwnerPercentage7());
                likeBrotherSisterResult.setCoOwnerPercentage8(ikeyValuePair1.getCoOwnerPercentage8());
                likeBrotherSisterResult.setCoOwnerPercentage9(ikeyValuePair1.getCoOwnerPercentage9());
                likeBrotherSisterResult.setCoOwnerPercentage10(ikeyValuePair1.getCoOwnerPercentage10());

                if (ikeyValuePair1.getCoOwnerId1() != null && ikeyValuePair1.getCoOwnerId1() != 0) {
                    likeBrotherSisterResult.setCoOwnerName1(ikeyValuePair1.getCoOwnerId1() + "-" + ikeyValuePair1.getCoOwnerName1());
                }
                if (ikeyValuePair1.getCoOwnerId2() != null && ikeyValuePair1.getCoOwnerId2() != 0) {
                    likeBrotherSisterResult.setCoOwnerName2(ikeyValuePair1.getCoOwnerId2() + "-" + ikeyValuePair1.getCoOwnerName2());
                }
                if (ikeyValuePair1.getCoOwnerId3() != null && ikeyValuePair1.getCoOwnerId3() != 0) {
                    likeBrotherSisterResult.setCoOwnerName3(ikeyValuePair1.getCoOwnerId3() + "-" + ikeyValuePair1.getCoOwnerName3());
                }
                if (ikeyValuePair1.getCoOwnerId4() != null && ikeyValuePair1.getCoOwnerId4() != 0) {
                    likeBrotherSisterResult.setCoOwnerName4(ikeyValuePair1.getCoOwnerId4() + "-" + ikeyValuePair1.getCoOwnerName4());
                }
                if (ikeyValuePair1.getCoOwnerId5() != null && ikeyValuePair1.getCoOwnerId5() != 0) {
                    likeBrotherSisterResult.setCoOwnerName5(ikeyValuePair1.getCoOwnerId5() + "-" + ikeyValuePair1.getCoOwnerName5());
                }
                if (ikeyValuePair1.getCoOwnerId6() != null && ikeyValuePair1.getCoOwnerId6() != 0) {
                    likeBrotherSisterResult.setCoOwnerName6(ikeyValuePair1.getCoOwnerId6() + "-" + ikeyValuePair1.getCoOwnerName6());
                }
                if (ikeyValuePair1.getCoOwnerId7() != null && ikeyValuePair1.getCoOwnerId7() != 0) {
                    likeBrotherSisterResult.setCoOwnerName7(ikeyValuePair1.getCoOwnerId7() + "-" + ikeyValuePair1.getCoOwnerName7());
                }
                if (ikeyValuePair1.getCoOwnerId8() != null && ikeyValuePair1.getCoOwnerId8() != 0) {
                    likeBrotherSisterResult.setCoOwnerName8(ikeyValuePair1.getCoOwnerId8() + "-" + ikeyValuePair1.getCoOwnerName8());
                }
                if (ikeyValuePair1.getCoOwnerId9() != null && ikeyValuePair1.getCoOwnerId9() != 0) {
                    likeBrotherSisterResult.setCoOwnerName9(ikeyValuePair1.getCoOwnerId9() + "-" + ikeyValuePair1.getCoOwnerName9());
                }
                if (ikeyValuePair1.getCoOwnerId10() != null && ikeyValuePair1.getCoOwnerId10() != 0) {
                    likeBrotherSisterResult.setCoOwnerName10(ikeyValuePair1.getCoOwnerId10() + "-" + ikeyValuePair1.getCoOwnerName10());
                }

                likeBrotherSisterResult.setStoreId(ikeyValuePair1.getStoreId());
                likeBrotherSisterResult.setStoreName(ikeyValuePair1.getStoreName());
                match.getLikeBrotherSisterResults().add(likeBrotherSisterResult);
            }
        }
        return match;
    }


    /**
     * @param findMatchResult
     * @return
     * @throws Exception
     */
    public List<MatchResult> findMatchResult(FindMatchResult findMatchResult) throws Exception {
        List<MatchResult> responseList = new ArrayList<>();
        List<String[]> ikeyValuePair = storePartnerListingRepository.findStorePartnerListingByCOOwnerId(findMatchResult);
        List<IkeyValuePair> ikeyValuePairV2 = storePartnerListingRepository.getLikeMatchResult(
                findMatchResult.getCoOwnerId1(),
                findMatchResult.getCoOwnerId2(),
                findMatchResult.getCoOwnerId3(),
                findMatchResult.getCoOwnerId4(),
                findMatchResult.getCoOwnerId5(),
                findMatchResult.getCoOwnerId6(),
                findMatchResult.getCoOwnerId7(),
                findMatchResult.getCoOwnerId8(),
                findMatchResult.getCoOwnerId9(),
                findMatchResult.getCoOwnerId10());

        Map<String, ExactMatchResult> exactMatchMap = new HashMap<>();
        Map<Long, LikeMatchResult> likeMatchMap = new HashMap<>();

        // Process ikeyValuePair
        for (String[] ikeyValuePair1 : ikeyValuePair) {
            for (int i = 0; i < 10; i++) {
                String coOwnerId = ikeyValuePair1[i];
                String coOwnerName = ikeyValuePair1[i + 10];
                String coOwnerPer = ikeyValuePair1[i + 20];
                if (coOwnerId != null) {
                    ExactMatchResult exactMatchResponse = exactMatchMap.get(coOwnerId);

                    if (exactMatchResponse == null) {
                        exactMatchResponse = new ExactMatchResult();
                        exactMatchResponse.setCoOwnerName(coOwnerId + "-" + coOwnerName);
                        exactMatchResponse.setStore(new ArrayList<>());
                        exactMatchMap.put(coOwnerId, exactMatchResponse);
                    }

                    StoreInfo storeInfo = new StoreInfo();
                    storeInfo.setStoreId(ikeyValuePair1[30]);
                    storeInfo.setStoreName(ikeyValuePair1[30] + "-" + ikeyValuePair1[31]);
                    storeInfo.setStorePercentage(Double.valueOf(coOwnerPer));
                    exactMatchResponse.getStore().add(storeInfo);
                }
            }
        }

        // Process ikeyValuePairV2
        for (IkeyValuePair ikeyValuePair1 : ikeyValuePairV2) {
            for (int i = 1; i <= 10; i++) {
                Long coOwnerId = ikeyValuePair1.getCoOwnerId(i);
                String coOwnerName = ikeyValuePair1.getCoOwnerName(i);
                if (coOwnerId != null && coOwnerId != 0) {
                    LikeMatchResult likeMatchResponse = likeMatchMap.get(coOwnerId);

                    if (likeMatchResponse == null) {
                        likeMatchResponse = new LikeMatchResult();
                        likeMatchResponse.setCoOwnerName(coOwnerId + "-" + coOwnerName);
                        likeMatchResponse.setStore(new ArrayList<>());
                        likeMatchMap.put(coOwnerId, likeMatchResponse);
                    }

                    StoreInfo storeInfo = new StoreInfo();
                    storeInfo.setStoreId(ikeyValuePair1.getStoreId());
                    storeInfo.setStoreName(ikeyValuePair1.getStoreId() + "-" + ikeyValuePair1.getStoreName());
                    storeInfo.setStorePercentage(ikeyValuePair1.getCoOwnerPercentage(i));
                    likeMatchResponse.getStore().add(storeInfo);
                }
            }
        }

        MatchResult matchResult = new MatchResult();
        matchResult.setExactMatchResult(new ArrayList<>(exactMatchMap.values()));
        matchResult.setLikeMatchResult(new ArrayList<>(likeMatchMap.values()));

//        ProcessExactLogic processExactLogic = new ProcessExactLogic();
//        matchResult.setExactMatchResult(processExactLogic.doProcessExactSearch(findMatchResult, matchResult.getExactMatchResult()));
        responseList.add(matchResult);
        return responseList;
    }

    /**
     * @param findMatchResult
     * @return
     * @throws Exception
     */
    public List<MatchResult> findMatchResultResponse(List<MatchResultResponse> findMatchResult) throws Exception {
        List<MatchResult> responseList = new ArrayList<>();
        Map<String, ExactMatchResult> exactMatchMap = new HashMap<>();

        for (MatchResultResponse ikeyValuePair1 : findMatchResult) {
            String storeId = ikeyValuePair1.getStoreId();
            String storeName = ikeyValuePair1.getStoreName();

            for (int i = 1; i <= 10; i++) {
                String coOwnerId = String.valueOf(ikeyValuePair1.getCoOwnerId(i));
                String coOwnerName = ikeyValuePair1.getCoOwnerName(i);
                String coOwnerPercentage = String.valueOf(ikeyValuePair1.getCoOwnerPercentage(i));

                if (coOwnerId != null && !coOwnerId.equals("null") && !coOwnerId.equals("0") && coOwnerPercentage != null && !coOwnerPercentage.equals("null")) {
                    ExactMatchResult exactMatchResponse = exactMatchMap.get(coOwnerId);

                    if (exactMatchResponse == null) {
                        exactMatchResponse = new ExactMatchResult();
                        exactMatchResponse.setCoOwnerName(coOwnerId + "-" + coOwnerName);
                        exactMatchResponse.setStore(new ArrayList<>());
                        exactMatchMap.put(coOwnerId, exactMatchResponse);
                    }
                    StoreInfo storeInfo = new StoreInfo();
                    storeInfo.setStoreId(storeId);
                    storeInfo.setStoreName(storeId + "-" + storeName);

                    try {
                        storeInfo.setStorePercentage(Double.valueOf(coOwnerPercentage));
                    } catch (NumberFormatException e) {
                    }

                    exactMatchResponse.getStore().add(storeInfo);
                }
            }
        }

        MatchResult responseObject = new MatchResult();
        responseObject.setExactMatchResult(new ArrayList<>(exactMatchMap.values()));

        responseList.add(responseObject);

        return responseList;
    }

    /**
     *
     * @param findMatchResult
     * @return
     * @throws ParseException
     */
    public Group findGroup(FindMatchResult findMatchResult) throws ParseException {


        List<String[]> ikeyValuePair = storePartnerListingRepository.findStorePartnerListingByCOOwnerId(findMatchResult);
        List<IkeyValuePair> ikeyValuePairList = storePartnerListingRepository.getLikeMatchGroup(
                findMatchResult.getCoOwnerId1(),
                findMatchResult.getCoOwnerId2(),
                findMatchResult.getCoOwnerId3(),
                findMatchResult.getCoOwnerId4(),
                findMatchResult.getCoOwnerId5(),
                findMatchResult.getCoOwnerId6(),
                findMatchResult.getCoOwnerId7(),
                findMatchResult.getCoOwnerId8(),
                findMatchResult.getCoOwnerId9(),
                findMatchResult.getCoOwnerId10(),
                findMatchResult.getGroupId()
        );

        Group match = new Group();
        match.setExactMatchGroups(new ArrayList<>());
        match.setLikeMatchGroup(new ArrayList<>());

        Map<String, ExactMatchGroup> exactGroupMap = new HashMap<>();
        Map<String, LikeMatchGroup> likeGroupMap = new HashMap<>();

        if (!ikeyValuePair.isEmpty()) {
            for (String[] ikeyValuePair1 : ikeyValuePair) {
                String groupId = ikeyValuePair1[34];
                if (!exactGroupMap.containsKey(groupId)) {
                    ExactMatchGroup exactMatchGroup = new ExactMatchGroup();
                    exactMatchGroup.setGroupName(ikeyValuePair1[35]);
                    exactMatchGroup.setGroupId(groupId);
                    exactMatchGroup.setStores(new ArrayList<>());
                    exactGroupMap.put(groupId, exactMatchGroup);
                }

                Stores stores = new Stores();
                stores.setStoreId(ikeyValuePair1[30]);
                stores.setStoreName(ikeyValuePair1[30] + "-" + ikeyValuePair1[31]);
                stores.setCoOwnerName1(ikeyValuePair1[10]);
                stores.setCoOwnerName2(ikeyValuePair1[11]);
                stores.setCoOwnerName3(ikeyValuePair1[12]);
                stores.setCoOwnerName4(ikeyValuePair1[13]);
                stores.setCoOwnerName5(ikeyValuePair1[14]);
                stores.setCoOwnerName6(ikeyValuePair1[15]);
                stores.setCoOwnerName7(ikeyValuePair1[16]);
                stores.setCoOwnerName8(ikeyValuePair1[17]);
                stores.setCoOwnerName9(ikeyValuePair1[18]);
                stores.setCoOwnerName10(ikeyValuePair1[19]);
                if (ikeyValuePair1[20] != null) {
                    stores.setCoOwnerPercentage1(Double.valueOf(ikeyValuePair1[20]));
                }
                if (ikeyValuePair1[21] != null) {
                    stores.setCoOwnerPercentage2(Double.valueOf(ikeyValuePair1[21]));
                }
                if (ikeyValuePair1[22] != null) {
                    stores.setCoOwnerPercentage3(Double.valueOf(ikeyValuePair1[22]));
                }
                if (ikeyValuePair1[23] != null) {
                    stores.setCoOwnerPercentage4(Double.valueOf(ikeyValuePair1[23]));
                }
                if (ikeyValuePair1[24] != null) {
                    stores.setCoOwnerPercentage5(Double.valueOf(ikeyValuePair1[24]));
                }
                if (ikeyValuePair1[25] != null) {
                    stores.setCoOwnerPercentage6(Double.valueOf(ikeyValuePair1[25]));
                }
                if (ikeyValuePair1[26] != null) {
                    stores.setCoOwnerPercentage7(Double.valueOf(ikeyValuePair1[26]));
                }
                if (ikeyValuePair1[27] != null) {
                    stores.setCoOwnerPercentage8(Double.valueOf(ikeyValuePair1[27]));
                }
                if (ikeyValuePair1[28] != null) {
                    stores.setCoOwnerPercentage9(Double.valueOf(ikeyValuePair1[28]));
                }
                if (ikeyValuePair1[29] != null) {
                    stores.setCoOwnerPercentage10(Double.valueOf(ikeyValuePair1[29]));
                }
                stores.setGroupTypeId(Long.valueOf(ikeyValuePair1[36]));
                stores.setSubGroupTypeId(Long.valueOf(ikeyValuePair1[39]));
                stores.setGroupType(ikeyValuePair1[36] + "-" + ikeyValuePair1[37]);
                stores.setSubGroupType(ikeyValuePair1[39] + "-" + ikeyValuePair1[38]);
                exactGroupMap.get(groupId).getStores().add(stores);

                match.setExactMatchGroups(new ArrayList<>(exactGroupMap.values()));
            }
        }

        if (!ikeyValuePairList.isEmpty()) {
            for (IkeyValuePair ikeyValuePair1 : ikeyValuePairList) {
                String groupId = ikeyValuePair1.getGroupId();
                if (!likeGroupMap.containsKey(groupId)) {
                    LikeMatchGroup likeMatchGroup = new LikeMatchGroup();
                    likeMatchGroup.setGroupName(ikeyValuePair1.getGroupName());
                    likeMatchGroup.setGroupId(groupId);
                    likeMatchGroup.setStores(new ArrayList<>());
                    likeGroupMap.put(groupId, likeMatchGroup);
                }

                Stores stores = new Stores();
                stores.setStoreId(ikeyValuePair1.getStoreId());
                stores.setStoreName(ikeyValuePair1.getStoreId() + "-" + ikeyValuePair1.getStoreName());
                if (ikeyValuePair1.getCoOwnerId1() != null && ikeyValuePair1.getCoOwnerId1() != 0) {
                    stores.setCoOwnerName1(ikeyValuePair1.getCoOwnerId1() + "-" + ikeyValuePair1.getCoOwnerName1());
                }
                if (ikeyValuePair1.getCoOwnerId2() != null && ikeyValuePair1.getCoOwnerId2() != 0) {
                    stores.setCoOwnerName2(ikeyValuePair1.getCoOwnerId2() + "-" + ikeyValuePair1.getCoOwnerName2());
                }
                if (ikeyValuePair1.getCoOwnerId3() != null && ikeyValuePair1.getCoOwnerId3() != 0) {
                    stores.setCoOwnerName3(ikeyValuePair1.getCoOwnerId3() + "-" + ikeyValuePair1.getCoOwnerName3());
                }
                if (ikeyValuePair1.getCoOwnerId4() != null && ikeyValuePair1.getCoOwnerId4() != 0) {
                    stores.setCoOwnerName4(ikeyValuePair1.getCoOwnerId4() + "-" + ikeyValuePair1.getCoOwnerName4());
                }
                if (ikeyValuePair1.getCoOwnerId5() != null && ikeyValuePair1.getCoOwnerId5() != 0) {
                    stores.setCoOwnerName5(ikeyValuePair1.getCoOwnerId5() + "-" + ikeyValuePair1.getCoOwnerName5());
                }
                if (ikeyValuePair1.getCoOwnerId6() != null && ikeyValuePair1.getCoOwnerId6() != 0) {
                    stores.setCoOwnerName6(ikeyValuePair1.getCoOwnerId6() + "-" + ikeyValuePair1.getCoOwnerName6());
                }
                if (ikeyValuePair1.getCoOwnerId7() != null && ikeyValuePair1.getCoOwnerId7() != 0) {
                    stores.setCoOwnerName7(ikeyValuePair1.getCoOwnerId7() + "-" + ikeyValuePair1.getCoOwnerName7());
                }
                if (ikeyValuePair1.getCoOwnerId8() != null && ikeyValuePair1.getCoOwnerId8() != 0) {
                    stores.setCoOwnerName8(ikeyValuePair1.getCoOwnerId8() + "-" + ikeyValuePair1.getCoOwnerName8());
                }
                if (ikeyValuePair1.getCoOwnerId9() != null && ikeyValuePair1.getCoOwnerId9() != 0) {
                    stores.setCoOwnerName9(ikeyValuePair1.getCoOwnerId9() + "-" + ikeyValuePair1.getCoOwnerName9());
                }
                if (ikeyValuePair1.getCoOwnerId10() != null && ikeyValuePair1.getCoOwnerId10() != 0) {
                    stores.setCoOwnerName10(ikeyValuePair1.getCoOwnerId10() + "-" + ikeyValuePair1.getCoOwnerName10());
                }
                stores.setCoOwnerPercentage1(ikeyValuePair1.getCoOwnerPercentage1());
                stores.setCoOwnerPercentage2(ikeyValuePair1.getCoOwnerPercentage2());
                stores.setCoOwnerPercentage3(ikeyValuePair1.getCoOwnerPercentage3());
                stores.setCoOwnerPercentage4(ikeyValuePair1.getCoOwnerPercentage4());
                stores.setCoOwnerPercentage5(ikeyValuePair1.getCoOwnerPercentage5());
                stores.setCoOwnerPercentage6(ikeyValuePair1.getCoOwnerPercentage6());
                stores.setCoOwnerPercentage7(ikeyValuePair1.getCoOwnerPercentage7());
                stores.setCoOwnerPercentage8(ikeyValuePair1.getCoOwnerPercentage8());
                stores.setCoOwnerPercentage9(ikeyValuePair1.getCoOwnerPercentage9());
                stores.setCoOwnerPercentage10(ikeyValuePair1.getCoOwnerPercentage10());
                stores.setGroupTypeId(Long.valueOf(ikeyValuePair1.getGroupTypeId()));
                stores.setSubGroupTypeId(Long.valueOf(ikeyValuePair1.getSubGroupTypeId()));
                stores.setGroupType(ikeyValuePair1.getGroupTypeId() + "-" + ikeyValuePair1.getGroupTypeName());
                stores.setSubGroupType(ikeyValuePair1.getSubGroupTypeId() + "-" + ikeyValuePair1.getSubGroupTypeName());
                likeGroupMap.get(groupId).getStores().add(stores);

                match.setLikeMatchGroup(new ArrayList<>(likeGroupMap.values()));
            }
        }
        return match;
    }


    /**
     * @param findMatchResult
     * @return
     * @throws ParseException
     */
    public Group findGroupCount(FindMatchResult findMatchResult) throws ParseException {


        List<String[]> ikeyValuePair = storePartnerListingRepository.findStorePartnerListingByCOOwnerId(findMatchResult);
        List<IkeyValuePair> ikeyValuePairList = storePartnerListingRepository.getLikeMatchGroup(
                findMatchResult.getCoOwnerId1(),
                findMatchResult.getCoOwnerId2(),
                findMatchResult.getCoOwnerId3(),
                findMatchResult.getCoOwnerId4(),
                findMatchResult.getCoOwnerId5(),
                findMatchResult.getCoOwnerId6(),
                findMatchResult.getCoOwnerId7(),
                findMatchResult.getCoOwnerId8(),
                findMatchResult.getCoOwnerId9(),
                findMatchResult.getCoOwnerId10(),
                findMatchResult.getGroupId()
        );

        Group match = new Group();
        match.setExactMatchGroups(new ArrayList<>());
        match.setLikeMatchGroup(new ArrayList<>());

        Map<String, ExactMatchGroup> exactGroupMap = new HashMap<>();
        Map<String, LikeMatchGroup> likeGroupMap = new HashMap<>();
        Map<String, Integer> groupCountMap = new HashMap<>();

        if (!ikeyValuePair.isEmpty()) {
            for (String[] ikeyValuePair1 : ikeyValuePair) {
                String groupId = ikeyValuePair1[34];
                groupCountMap.put(groupId, groupCountMap.getOrDefault(groupId, 0) + 1);

                if (!exactGroupMap.containsKey(groupId)) {
                    ExactMatchGroup exactMatchGroup = new ExactMatchGroup();
                    exactMatchGroup.setGroupName(ikeyValuePair1[35]);
                    exactMatchGroup.setGroupId(groupId);
                    exactMatchGroup.setStores(new ArrayList<>());
                    exactGroupMap.put(groupId, exactMatchGroup);
                }

                Stores stores = new Stores();
                stores.setStoreId(ikeyValuePair1[30]);
                stores.setStoreName(ikeyValuePair1[30] + "-" + ikeyValuePair1[31]);
                stores.setCoOwnerName1(ikeyValuePair1[10]);
                stores.setCoOwnerName2(ikeyValuePair1[11]);
                stores.setCoOwnerName3(ikeyValuePair1[12]);
                stores.setCoOwnerName4(ikeyValuePair1[13]);
                stores.setCoOwnerName5(ikeyValuePair1[14]);
                stores.setCoOwnerName6(ikeyValuePair1[15]);
                stores.setCoOwnerName7(ikeyValuePair1[16]);
                stores.setCoOwnerName8(ikeyValuePair1[17]);
                stores.setCoOwnerName9(ikeyValuePair1[18]);
                stores.setCoOwnerName10(ikeyValuePair1[19]);
                if (ikeyValuePair1[20] != null) {
                    stores.setCoOwnerPercentage1(Double.valueOf(ikeyValuePair1[20]));
                }
                if (ikeyValuePair1[21] != null) {
                    stores.setCoOwnerPercentage2(Double.valueOf(ikeyValuePair1[21]));
                }
                if (ikeyValuePair1[22] != null) {
                    stores.setCoOwnerPercentage3(Double.valueOf(ikeyValuePair1[22]));
                }
                if (ikeyValuePair1[23] != null) {
                    stores.setCoOwnerPercentage4(Double.valueOf(ikeyValuePair1[23]));
                }
                if (ikeyValuePair1[24] != null) {
                    stores.setCoOwnerPercentage5(Double.valueOf(ikeyValuePair1[24]));
                }
                if (ikeyValuePair1[25] != null) {
                    stores.setCoOwnerPercentage6(Double.valueOf(ikeyValuePair1[25]));
                }
                if (ikeyValuePair1[26] != null) {
                    stores.setCoOwnerPercentage7(Double.valueOf(ikeyValuePair1[26]));
                }
                if (ikeyValuePair1[27] != null) {
                    stores.setCoOwnerPercentage8(Double.valueOf(ikeyValuePair1[27]));
                }
                if (ikeyValuePair1[28] != null) {
                    stores.setCoOwnerPercentage9(Double.valueOf(ikeyValuePair1[28]));
                }
                if (ikeyValuePair1[29] != null) {
                    stores.setCoOwnerPercentage10(Double.valueOf(ikeyValuePair1[29]));
                }
                stores.setGroupTypeId(Long.valueOf(ikeyValuePair1[36]));
                stores.setSubGroupTypeId(Long.valueOf(ikeyValuePair1[39]));
                stores.setGroupType(ikeyValuePair1[36] + "-" + ikeyValuePair1[37]);
                stores.setSubGroupType(ikeyValuePair1[39] + "-" + ikeyValuePair1[38]);
                exactGroupMap.get(groupId).getStores().add(stores);
                match.setExactMatchGroups(new ArrayList<>(exactGroupMap.values()));
            }
        }

        if (!ikeyValuePairList.isEmpty()) {
            for (IkeyValuePair ikeyValuePair1 : ikeyValuePairList) {
                String groupId = ikeyValuePair1.getGroupId();
                if (!likeGroupMap.containsKey(groupId)) {
                    LikeMatchGroup likeMatchGroup = new LikeMatchGroup();
                    likeMatchGroup.setGroupName(ikeyValuePair1.getGroupName());
                    likeMatchGroup.setGroupId(groupId);
                    likeMatchGroup.setStores(new ArrayList<>());
                    likeGroupMap.put(groupId, likeMatchGroup);
                }

                Stores stores = new Stores();
                stores.setStoreId(ikeyValuePair1.getStoreId());
                stores.setStoreName(ikeyValuePair1.getStoreId() + "-" + ikeyValuePair1.getStoreName());
                if (ikeyValuePair1.getCoOwnerId1() != null && ikeyValuePair1.getCoOwnerId1() != 0) {
                    stores.setCoOwnerName1(ikeyValuePair1.getCoOwnerId1() + "-" + ikeyValuePair1.getCoOwnerName1());
                }
                if (ikeyValuePair1.getCoOwnerId2() != null && ikeyValuePair1.getCoOwnerId2() != 0) {
                    stores.setCoOwnerName2(ikeyValuePair1.getCoOwnerId2() + "-" + ikeyValuePair1.getCoOwnerName2());
                }
                if (ikeyValuePair1.getCoOwnerId3() != null && ikeyValuePair1.getCoOwnerId3() != 0) {
                    stores.setCoOwnerName3(ikeyValuePair1.getCoOwnerId3() + "-" + ikeyValuePair1.getCoOwnerName3());
                }
                if (ikeyValuePair1.getCoOwnerId4() != null && ikeyValuePair1.getCoOwnerId4() != 0) {
                    stores.setCoOwnerName4(ikeyValuePair1.getCoOwnerId4() + "-" + ikeyValuePair1.getCoOwnerName4());
                }
                if (ikeyValuePair1.getCoOwnerId5() != null && ikeyValuePair1.getCoOwnerId5() != 0) {
                    stores.setCoOwnerName5(ikeyValuePair1.getCoOwnerId5() + "-" + ikeyValuePair1.getCoOwnerName5());
                }
                if (ikeyValuePair1.getCoOwnerId6() != null && ikeyValuePair1.getCoOwnerId6() != 0) {
                    stores.setCoOwnerName6(ikeyValuePair1.getCoOwnerId6() + "-" + ikeyValuePair1.getCoOwnerName6());
                }
                if (ikeyValuePair1.getCoOwnerId7() != null && ikeyValuePair1.getCoOwnerId7() != 0) {
                    stores.setCoOwnerName7(ikeyValuePair1.getCoOwnerId7() + "-" + ikeyValuePair1.getCoOwnerName7());
                }
                if (ikeyValuePair1.getCoOwnerId8() != null && ikeyValuePair1.getCoOwnerId8() != 0) {
                    stores.setCoOwnerName8(ikeyValuePair1.getCoOwnerId8() + "-" + ikeyValuePair1.getCoOwnerName8());
                }
                if (ikeyValuePair1.getCoOwnerId9() != null && ikeyValuePair1.getCoOwnerId9() != 0) {
                    stores.setCoOwnerName9(ikeyValuePair1.getCoOwnerId9() + "-" + ikeyValuePair1.getCoOwnerName9());
                }
                if (ikeyValuePair1.getCoOwnerId10() != null && ikeyValuePair1.getCoOwnerId10() != 0) {
                    stores.setCoOwnerName10(ikeyValuePair1.getCoOwnerId10() + "-" + ikeyValuePair1.getCoOwnerName10());
                }
                stores.setCoOwnerPercentage1(ikeyValuePair1.getCoOwnerPercentage1());
                stores.setCoOwnerPercentage2(ikeyValuePair1.getCoOwnerPercentage2());
                stores.setCoOwnerPercentage3(ikeyValuePair1.getCoOwnerPercentage3());
                stores.setCoOwnerPercentage4(ikeyValuePair1.getCoOwnerPercentage4());
                stores.setCoOwnerPercentage5(ikeyValuePair1.getCoOwnerPercentage5());
                stores.setCoOwnerPercentage6(ikeyValuePair1.getCoOwnerPercentage6());
                stores.setCoOwnerPercentage7(ikeyValuePair1.getCoOwnerPercentage7());
                stores.setCoOwnerPercentage8(ikeyValuePair1.getCoOwnerPercentage8());
                stores.setCoOwnerPercentage9(ikeyValuePair1.getCoOwnerPercentage9());
                stores.setCoOwnerPercentage10(ikeyValuePair1.getCoOwnerPercentage10());
                stores.setGroupTypeId(Long.valueOf(ikeyValuePair1.getGroupTypeId()));
                stores.setSubGroupTypeId(Long.valueOf(ikeyValuePair1.getSubGroupTypeId()));
                stores.setGroupType(ikeyValuePair1.getGroupTypeId() + "-" + ikeyValuePair1.getGroupTypeName());
                stores.setSubGroupType(ikeyValuePair1.getSubGroupTypeId() + "-" + ikeyValuePair1.getSubGroupTypeName());
                likeGroupMap.get(groupId).getStores().add(stores);

                match.setLikeMatchGroup(new ArrayList<>(likeGroupMap.values()));
            }
        }

        int maxGroupCount = Collections.max(groupCountMap.values());

        List<String> maxGroupIds = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : groupCountMap.entrySet()) {
            if (entry.getValue() == maxGroupCount) {
                maxGroupIds.add(entry.getKey());
            }
        }
        match.setExactMatchGroups(maxGroupIds.stream().map(exactGroupMap::get).collect(Collectors.toList()));

        return match;
    }



    /**
     * findEntity
     *
     * @param findMatchResult
     * @return
     */
    public Group findEntity(FindMatchResult findMatchResult) {
        ExactResultInGroup exactResultInGroup = null;
        Group match = new Group();
        match.setExactMatchGroups(new ArrayList<>());
        match.setLikeMatchGroup(new ArrayList<>());

        Map<String, ExactMatchGroup> exactGroupMap = new HashMap<>();
        Map<String, LikeMatchGroup> likeGroupMap = new HashMap<>();

        for (int i = 1; i <= 10; i++) {
            Long coOwnerId = getCoOwnerId(findMatchResult, i);

            List<Long> coOwnerIds = Collections.singletonList(coOwnerId);
            List<IkeyValuePair> entityIds = storePartnerListingRepository.getEntityIds(coOwnerIds);

            for (IkeyValuePair entityPair : entityIds) {
                Long entityId = entityPair.getEntityId();

                exactResultInGroup = new ExactResultInGroup();
                for (int j = i; j <= 10; j++) {
                    if (!entityIds.isEmpty()) {
//                    Long entityId = entityIds.get(0).getEntityId();
                        if (j == i) {
                            setCoOwnerId(exactResultInGroup, findMatchResult, j, entityId);
                        } else {
                            setCoOwnerId(exactResultInGroup, findMatchResult, j, null);
                        }
                    }
                }

                List<String[]> storePartnerListingByExact = storePartnerListingRepository.findByExact(
                        exactResultInGroup.getCoOwnerId1(),
                        exactResultInGroup.getCoOwnerId2(),
                        exactResultInGroup.getCoOwnerId3(),
                        exactResultInGroup.getCoOwnerId4(),
                        exactResultInGroup.getCoOwnerId5(),
                        exactResultInGroup.getCoOwnerId6(),
                        exactResultInGroup.getCoOwnerId7(),
                        exactResultInGroup.getCoOwnerId8(),
                        exactResultInGroup.getCoOwnerId9(),
                        exactResultInGroup.getCoOwnerId10()
                );

                if (!storePartnerListingByExact.isEmpty()) {
                    for (String[] ikeyValuePair1 : storePartnerListingByExact) {
                        String groupId = ikeyValuePair1[34];
                        if (!exactGroupMap.containsKey(groupId)) {
                            ExactMatchGroup exactMatchGroup = new ExactMatchGroup();
                            exactMatchGroup.setGroupName(ikeyValuePair1[35]);
                            exactMatchGroup.setGroupId(groupId);
                            exactMatchGroup.setStores(new ArrayList<>());
                            exactGroupMap.put(groupId, exactMatchGroup);
                        }

                        Stores stores = new Stores();
                        stores.setStoreId(ikeyValuePair1[30]);
                        stores.setStoreName(ikeyValuePair1[30] + "-" + ikeyValuePair1[31]);
                        stores.setCoOwnerName1(ikeyValuePair1[10]);
                        stores.setCoOwnerName2(ikeyValuePair1[11]);
                        stores.setCoOwnerName3(ikeyValuePair1[12]);
                        stores.setCoOwnerName4(ikeyValuePair1[13]);
                        stores.setCoOwnerName5(ikeyValuePair1[14]);
                        stores.setCoOwnerName6(ikeyValuePair1[15]);
                        stores.setCoOwnerName7(ikeyValuePair1[16]);
                        stores.setCoOwnerName8(ikeyValuePair1[17]);
                        stores.setCoOwnerName9(ikeyValuePair1[18]);
                        stores.setCoOwnerName10(ikeyValuePair1[19]);
                        if (ikeyValuePair1[20] != null) {
                            stores.setCoOwnerPercentage1(Double.valueOf(ikeyValuePair1[20]));
                        }
                        if (ikeyValuePair1[21] != null) {
                            stores.setCoOwnerPercentage2(Double.valueOf(ikeyValuePair1[21]));
                        }
                        if (ikeyValuePair1[22] != null) {
                            stores.setCoOwnerPercentage3(Double.valueOf(ikeyValuePair1[22]));
                        }
                        if (ikeyValuePair1[23] != null) {
                            stores.setCoOwnerPercentage4(Double.valueOf(ikeyValuePair1[23]));
                        }
                        if (ikeyValuePair1[24] != null) {
                            stores.setCoOwnerPercentage5(Double.valueOf(ikeyValuePair1[24]));
                        }
                        if (ikeyValuePair1[25] != null) {
                            stores.setCoOwnerPercentage6(Double.valueOf(ikeyValuePair1[25]));
                        }
                        if (ikeyValuePair1[26] != null) {
                            stores.setCoOwnerPercentage7(Double.valueOf(ikeyValuePair1[26]));
                        }
                        if (ikeyValuePair1[27] != null) {
                            stores.setCoOwnerPercentage8(Double.valueOf(ikeyValuePair1[27]));
                        }
                        if (ikeyValuePair1[28] != null) {
                            stores.setCoOwnerPercentage9(Double.valueOf(ikeyValuePair1[28]));
                        }
                        if (ikeyValuePair1[29] != null) {
                            stores.setCoOwnerPercentage10(Double.valueOf(ikeyValuePair1[29]));
                        }
                        stores.setGroupTypeId(Long.valueOf(ikeyValuePair1[36]));
                        stores.setSubGroupTypeId(Long.valueOf(ikeyValuePair1[39]));
                        stores.setGroupType(ikeyValuePair1[36] + "-" + ikeyValuePair1[37]);
                        stores.setSubGroupType(ikeyValuePair1[39] + "-" + ikeyValuePair1[38]);
                        exactGroupMap.get(groupId).getStores().add(stores);
                        match.setExactMatchGroups(new ArrayList<>(exactGroupMap.values()));
                    }
                }
            }
            List<IkeyValuePair> ikeyValuePairList = storePartnerListingRepository.getLikeMatchGroup(
                    findMatchResult.getCoOwnerId1(),
                    findMatchResult.getCoOwnerId2(),
                    findMatchResult.getCoOwnerId3(),
                    findMatchResult.getCoOwnerId4(),
                    findMatchResult.getCoOwnerId5(),
                    findMatchResult.getCoOwnerId6(),
                    findMatchResult.getCoOwnerId7(),
                    findMatchResult.getCoOwnerId8(),
                    findMatchResult.getCoOwnerId9(),
                    findMatchResult.getCoOwnerId10(),
                    findMatchResult.getGroupId()
            );
            if (!ikeyValuePairList.isEmpty()) {
                for (IkeyValuePair ikeyValuePair1 : ikeyValuePairList) {
                    String groupId = ikeyValuePair1.getGroupId();
                    if (!likeGroupMap.containsKey(groupId)) {
                        LikeMatchGroup likeMatchGroup = new LikeMatchGroup();
                        likeMatchGroup.setGroupName(ikeyValuePair1.getGroupName());
                        likeMatchGroup.setGroupId(groupId);
                        likeMatchGroup.setStores(new ArrayList<>());
                        likeGroupMap.put(groupId, likeMatchGroup);
                    }

                    Stores stores = new Stores();
                    stores.setStoreId(ikeyValuePair1.getStoreId());
                    stores.setStoreName(ikeyValuePair1.getStoreId() + "-" + ikeyValuePair1.getStoreName());
                    if (ikeyValuePair1.getCoOwnerId1() != null && ikeyValuePair1.getCoOwnerId1() != 0) {
                        stores.setCoOwnerName1(ikeyValuePair1.getCoOwnerId1() + "-" + ikeyValuePair1.getCoOwnerName1());
                    }
                    if (ikeyValuePair1.getCoOwnerId2() != null && ikeyValuePair1.getCoOwnerId2() != 0) {
                        stores.setCoOwnerName2(ikeyValuePair1.getCoOwnerId2() + "-" + ikeyValuePair1.getCoOwnerName2());
                    }
                    if (ikeyValuePair1.getCoOwnerId3() != null && ikeyValuePair1.getCoOwnerId3() != 0) {
                        stores.setCoOwnerName3(ikeyValuePair1.getCoOwnerId3() + "-" + ikeyValuePair1.getCoOwnerName3());
                    }
                    if (ikeyValuePair1.getCoOwnerId4() != null && ikeyValuePair1.getCoOwnerId4() != 0) {
                        stores.setCoOwnerName4(ikeyValuePair1.getCoOwnerId4() + "-" + ikeyValuePair1.getCoOwnerName4());
                    }
                    if (ikeyValuePair1.getCoOwnerId5() != null && ikeyValuePair1.getCoOwnerId5() != 0) {
                        stores.setCoOwnerName5(ikeyValuePair1.getCoOwnerId5() + "-" + ikeyValuePair1.getCoOwnerName5());
                    }
                    if (ikeyValuePair1.getCoOwnerId6() != null && ikeyValuePair1.getCoOwnerId6() != 0) {
                        stores.setCoOwnerName6(ikeyValuePair1.getCoOwnerId6() + "-" + ikeyValuePair1.getCoOwnerName6());
                    }
                    if (ikeyValuePair1.getCoOwnerId7() != null && ikeyValuePair1.getCoOwnerId7() != 0) {
                        stores.setCoOwnerName7(ikeyValuePair1.getCoOwnerId7() + "-" + ikeyValuePair1.getCoOwnerName7());
                    }
                    if (ikeyValuePair1.getCoOwnerId8() != null && ikeyValuePair1.getCoOwnerId8() != 0) {
                        stores.setCoOwnerName8(ikeyValuePair1.getCoOwnerId8() + "-" + ikeyValuePair1.getCoOwnerName8());
                    }
                    if (ikeyValuePair1.getCoOwnerId9() != null && ikeyValuePair1.getCoOwnerId9() != 0) {
                        stores.setCoOwnerName9(ikeyValuePair1.getCoOwnerId9() + "-" + ikeyValuePair1.getCoOwnerName9());
                    }
                    if (ikeyValuePair1.getCoOwnerId10() != null && ikeyValuePair1.getCoOwnerId10() != 0) {
                        stores.setCoOwnerName10(ikeyValuePair1.getCoOwnerId10() + "-" + ikeyValuePair1.getCoOwnerName10());
                    }
                    stores.setCoOwnerPercentage1(ikeyValuePair1.getCoOwnerPercentage1());
                    stores.setCoOwnerPercentage2(ikeyValuePair1.getCoOwnerPercentage2());
                    stores.setCoOwnerPercentage3(ikeyValuePair1.getCoOwnerPercentage3());
                    stores.setCoOwnerPercentage4(ikeyValuePair1.getCoOwnerPercentage4());
                    stores.setCoOwnerPercentage5(ikeyValuePair1.getCoOwnerPercentage5());
                    stores.setCoOwnerPercentage6(ikeyValuePair1.getCoOwnerPercentage6());
                    stores.setCoOwnerPercentage7(ikeyValuePair1.getCoOwnerPercentage7());
                    stores.setCoOwnerPercentage8(ikeyValuePair1.getCoOwnerPercentage8());
                    stores.setCoOwnerPercentage9(ikeyValuePair1.getCoOwnerPercentage9());
                    stores.setCoOwnerPercentage10(ikeyValuePair1.getCoOwnerPercentage10());
                    stores.setGroupTypeId(Long.valueOf(ikeyValuePair1.getGroupTypeId()));
                    stores.setSubGroupTypeId(Long.valueOf(ikeyValuePair1.getSubGroupTypeId()));
                    stores.setGroupType(ikeyValuePair1.getGroupTypeId() + "-" + ikeyValuePair1.getGroupTypeName());
                    stores.setSubGroupType(ikeyValuePair1.getSubGroupTypeId() + "-" + ikeyValuePair1.getSubGroupTypeName());
                    likeGroupMap.get(groupId).getStores().add(stores);

                    match.setLikeMatchGroup(new ArrayList<>(likeGroupMap.values()));
                }
            }
        }
        return match;
    }

    /**
     * @param findMatchResult
     * @param index
     * @return
     */
    private Long getCoOwnerId(FindMatchResult findMatchResult, int index) {
        switch (index) {
            case 1:
                return findMatchResult.getCoOwnerId1();
            case 2:
                return findMatchResult.getCoOwnerId2();
            case 3:
                return findMatchResult.getCoOwnerId3();
            case 4:
                return findMatchResult.getCoOwnerId4();
            case 5:
                return findMatchResult.getCoOwnerId5();
            case 6:
                return findMatchResult.getCoOwnerId6();
            case 7:
                return findMatchResult.getCoOwnerId7();
            case 8:
                return findMatchResult.getCoOwnerId8();
            case 9:
                return findMatchResult.getCoOwnerId9();
            case 10:
                return findMatchResult.getCoOwnerId10();
            default:
                throw new IllegalArgumentException("Invalid index for coOwnerId: " + index);
        }
    }

    /**
     * @param exactResultInGroup
     * @param findMatchResult
     * @param index
     * @param entityId
     */
    private void setCoOwnerId(ExactResultInGroup exactResultInGroup, FindMatchResult findMatchResult, int index, Long entityId) {
        Long coOwnerId;

        if (entityId != null) {
            coOwnerId = entityId;
        } else {
            switch (index) {
                case 1:
                    coOwnerId = findMatchResult.getCoOwnerId1();
                    break;
                case 2:
                    coOwnerId = findMatchResult.getCoOwnerId2();
                    break;
                case 3:
                    coOwnerId = findMatchResult.getCoOwnerId3();
                    break;
                case 4:
                    coOwnerId = findMatchResult.getCoOwnerId4();
                    break;
                case 5:
                    coOwnerId = findMatchResult.getCoOwnerId5();
                    break;
                case 6:
                    coOwnerId = findMatchResult.getCoOwnerId6();
                    break;
                case 7:
                    coOwnerId = findMatchResult.getCoOwnerId7();
                    break;
                case 8:
                    coOwnerId = findMatchResult.getCoOwnerId8();
                    break;
                case 9:
                    coOwnerId = findMatchResult.getCoOwnerId9();
                    break;
                case 10:
                    coOwnerId = findMatchResult.getCoOwnerId10();
                    break;
                default:
                    throw new IllegalArgumentException("Invalid index for coOwnerId: " + index);
            }
        }

        switch (index) {
            case 1:
                exactResultInGroup.setCoOwnerId1(coOwnerId);
                break;
            case 2:
                exactResultInGroup.setCoOwnerId2(coOwnerId);
                break;
            case 3:
                exactResultInGroup.setCoOwnerId3(coOwnerId);
                break;
            case 4:
                exactResultInGroup.setCoOwnerId4(coOwnerId);
                break;
            case 5:
                exactResultInGroup.setCoOwnerId5(coOwnerId);
                break;
            case 6:
                exactResultInGroup.setCoOwnerId6(coOwnerId);
                break;
            case 7:
                exactResultInGroup.setCoOwnerId7(coOwnerId);
                break;
            case 8:
                exactResultInGroup.setCoOwnerId8(coOwnerId);
                break;
            case 9:
                exactResultInGroup.setCoOwnerId9(coOwnerId);
                break;
            case 10:
                exactResultInGroup.setCoOwnerId10(coOwnerId);
                break;
            default:
                throw new IllegalArgumentException("Invalid index for coOwnerId: " + index);
        }
    }


}


