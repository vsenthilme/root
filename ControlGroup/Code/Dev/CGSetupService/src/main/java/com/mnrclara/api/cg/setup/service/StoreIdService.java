package com.mnrclara.api.cg.setup.service;


import com.mnrclara.api.cg.setup.model.IKeyValuePair;
import com.mnrclara.api.cg.setup.model.auth.AuthToken;
import com.mnrclara.api.cg.setup.model.store.*;
import com.mnrclara.api.cg.setup.repository.*;
import com.mnrclara.api.cg.setup.util.CommonUtils;
import com.mnrclara.api.cg.setup.exception.BadRequestException;
import com.mnrclara.api.cg.setup.repository.specification.StoreSpecification;
import com.mnrclara.api.cg.setup.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StoreIdService {
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private StateRepository stateRepository;
    @Autowired
    private SetupService setupService;
    @Autowired
    private NumberRangeService numberRangeService;
    @Autowired
    private CompanyIdRepository companyIdRepository;

    @Autowired
    private StoreIdRepository storeIdRepository;

    /**
     * getAllStoreId
     * @return
     */
    public List<StoreId> getAllStoreId () {
        List<StoreId> storeIdList =  storeIdRepository.findAll();
        storeIdList = storeIdList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        List<StoreId>newState=new ArrayList<>();
        for(StoreId dbStoreId:storeIdList) {

            IKeyValuePair iKeyValuePair =
                    storeIdRepository.getDescription(dbStoreId.getCompanyId(), dbStoreId.getLanguageId(),
                            dbStoreId.getCountry(), dbStoreId.getState(), dbStoreId.getCity());

            if(iKeyValuePair != null) {
                dbStoreId.setCompanyIdAndDescription(iKeyValuePair.getCompanyDescription());
                dbStoreId.setCountryIdAndDescription(iKeyValuePair.getCountryDescription());
                dbStoreId.setStateIdAndDescription(iKeyValuePair.getStateDescription());
                dbStoreId.setCityIdAndDescription(iKeyValuePair.getCityDescription());
            }
            newState.add(dbStoreId);
        }
        return newState;
    }

    /**
     *
     * @param storeId
     * @param companyId
     * @param languageId
     * @return
     */
    public StoreId getStoreId (Long storeId, String companyId, String languageId) {
        Optional<StoreId> dbStoreId = storeIdRepository.findByCompanyIdAndLanguageIdAndStoreIdAndDeletionIndicator(
                companyId, languageId, storeId,0L);

        if (dbStoreId.isEmpty()) {
            throw new BadRequestException("The given values of companyId : " + companyId +
            " languageId :" + languageId + " storeId : " + storeId + "doesn't exists");
        }

        StoreId newStoreId = new StoreId();
        BeanUtils.copyProperties(dbStoreId.get(), newStoreId, CommonUtils.getNullPropertyNames(dbStoreId));

       IKeyValuePair iKeyValuePair =
               storeIdRepository.getDescription(companyId,languageId, newStoreId.getCountry(),
                       newStoreId.getState(), newStoreId.getCity());

        if(iKeyValuePair != null) {
            newStoreId.setCompanyIdAndDescription(iKeyValuePair.getCompanyDescription());
            newStoreId.setCountryIdAndDescription(iKeyValuePair.getCountryDescription());
            newStoreId.setStateIdAndDescription(iKeyValuePair.getStateDescription());
            newStoreId.setCityIdAndDescription(iKeyValuePair.getCityDescription());
        }
        return newStoreId;
    }

    /**
     *
     * @param newStoreId
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public StoreId createStoreId (AddStoreId newStoreId, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {

        Optional<StoreId> duplicateStoreId =
                storeIdRepository.findByCompanyIdAndLanguageIdAndStoreIdAndDeletionIndicator(newStoreId.getCompanyId(),
                        newStoreId.getLanguageId(), newStoreId.getStoreId(), 0L);

        if (!duplicateStoreId.isEmpty()) {
            throw new EntityNotFoundException("Record is Getting Duplicated");
        } else {
            IKeyValuePair iKeyValuePair = storeIdRepository.getDescription(newStoreId.getCompanyId(),
                    newStoreId.getLanguageId(), newStoreId.getCountry(), newStoreId.getState(), newStoreId.getCity());

            StoreId dbStoreId = new StoreId();
            BeanUtils.copyProperties(newStoreId, dbStoreId, CommonUtils.getNullPropertyNames(newStoreId));

            if(iKeyValuePair!= null){
                dbStoreId.setCompanyIdAndDescription(iKeyValuePair.getCompanyDescription());
                dbStoreId.setCountryIdAndDescription(iKeyValuePair.getCountryDescription());
                dbStoreId.setStateIdAndDescription(iKeyValuePair.getStateDescription());
                dbStoreId.setCityIdAndDescription(iKeyValuePair.getCityDescription());
            }else {
                throw new RuntimeException("The given values of Company Id "
                        +newStoreId.getCompanyId() + " languageId "
                        +newStoreId.getLanguageId() + "countryId "
                        +newStoreId.getCountry() + " stateId "
                        +newStoreId.getState() + "cityId "
                        +newStoreId.getCity() + " doesn't exists");
            }
            Long NUM_RAN_CODE = 3L;
            String NUM_RAN_OBJ = "CGSTORE";
            String C_ID = "1000";
            String LANG_ID = "EN";
            String STORE_ID = numberRangeService.getNextNumberRange(NUM_RAN_CODE,NUM_RAN_OBJ,LANG_ID,C_ID);
            log.info("nextVal from NumberRange for STORE_ID: " + STORE_ID);
            dbStoreId.setStoreId(Long.valueOf(STORE_ID));

            dbStoreId.setDeletionIndicator(0L);
            dbStoreId.setCreatedBy(loginUserID);
            dbStoreId.setUpdatedBy(loginUserID);
            dbStoreId.setCreatedOn(new Date());
            dbStoreId.setUpdatedOn(new Date());
            return storeIdRepository.save(dbStoreId);
        }
    }

    /**
     *
     * @param storeId
     * @param languageId
     * @param companyId
     * @param loginUserID
     * @param updateStoreId
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public StoreId updateStoreId (Long storeId, String languageId,
                              String companyId, String loginUserID, UpdateStoreId updateStoreId)
            throws IllegalAccessException, InvocationTargetException {
        StoreId dbStoreId = storeIdRepository.findByCompanyIdAndStoreIdAndLanguageIdAndDeletionIndicator(
                companyId, storeId, languageId, 0L);

        if(dbStoreId != null) {
            IKeyValuePair iKeyValuePair =
                    countryRepository.getCountryIdAndDescription(updateStoreId.getCountry(), companyId, languageId);

            IKeyValuePair iKeyValuePair1 =
                    stateRepository.getStateIdAndDescription(updateStoreId.getState(), companyId, languageId, updateStoreId.getCountry());

            IKeyValuePair iKeyValuePair2 =
                    cityRepository.getCityIdAndDescription(updateStoreId.getCity(), companyId, languageId,
                            updateStoreId.getState(), updateStoreId.getCountry());

            BeanUtils.copyProperties(updateStoreId, dbStoreId, CommonUtils.getNullPropertyNames(updateStoreId));
            if (iKeyValuePair != null && iKeyValuePair1 != null && iKeyValuePair2 != null) {

                dbStoreId.setCountryIdAndDescription(iKeyValuePair.getDescription());
                dbStoreId.setStateIdAndDescription(iKeyValuePair1.getDescription());
                dbStoreId.setCityIdAndDescription(iKeyValuePair2.getDescription());
            } else {
                throw new RuntimeException("The given values countryId"
                        +updateStoreId.getCountry()+ " stateId "
                        +updateStoreId.getState()+ " cityId "
                        +updateStoreId.getCity()+ " doesn't exists ");
            }

            storeIdRepository.updateStorePartnerList(dbStoreId.getStoreId(), dbStoreId.getStoreName());
            storeIdRepository.updateOwnershipRequest(dbStoreId.getStoreId(), dbStoreId.getStoreName());
            dbStoreId.setUpdatedBy(loginUserID);
            dbStoreId.setUpdatedOn(new Date());
             storeIdRepository.save(dbStoreId);
        }else {
            throw new RuntimeException("The given values of companyId"
                    + companyId + " languageId "
                    + languageId + " storeId "
                    + storeId + "doesn't exists");
        }
        return dbStoreId;
    }


    /**
     *
     * @param storeId
     * @param companyId
     * @param languageId
     * @param loginUserID
     */
    public void deleteStoreId (Long storeId, String companyId, String languageId, String loginUserID) {
        StoreId dbStoreId = getStoreId(storeId, companyId, languageId);
        if ( dbStoreId != null) {
            dbStoreId.setDeletionIndicator(1L);
            dbStoreId.setUpdatedBy(loginUserID);
            dbStoreId.setUpdatedOn(new Date());
            storeIdRepository.save(dbStoreId);
        } else {
            throw new EntityNotFoundException("Error in deleting Id: " + storeId);
        }
    }

    //Find StoreId

    public List<StoreId> findStoreId(FindStoreId findStoreId) throws ParseException {
        if (findStoreId.getStartCreatedOn() != null && findStoreId.getStartCreatedOn() != null) {
            Date date = DateUtils.convertStringToYYYYMMDD(findStoreId.getStartCreatedOn());
            Date date1 = DateUtils.convertStringToYYYYMMDD(findStoreId.getEndCreatedOn());

            Date[] dates = DateUtils.addTimeToDatesForSearch(date, date1);
            findStoreId.setFromDate(dates[0]);
            findStoreId.setToDate(dates[1]);
        }

        StoreSpecification spec = new StoreSpecification(findStoreId);
        List<StoreId> results = storeIdRepository.findAll(spec);
        results = results.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        log.info("results: " + results);
        List<StoreId>newStore=new ArrayList<>();
        for(StoreId dbStoreId:results) {

            IKeyValuePair iKeyValuePair = storeIdRepository.getDescription(dbStoreId.getCompanyId(),
                    dbStoreId.getLanguageId(), dbStoreId.getCountry(), dbStoreId.getState(), dbStoreId.getCity());

            if(iKeyValuePair != null){
                dbStoreId.setCompanyIdAndDescription(iKeyValuePair.getCompanyDescription());
                dbStoreId.setCountryIdAndDescription(iKeyValuePair.getCountryDescription());
                dbStoreId.setStateIdAndDescription(iKeyValuePair.getStateDescription());
                dbStoreId.setCityIdAndDescription(iKeyValuePair.getCityDescription());
            }
            newStore.add(dbStoreId);
        }
        return newStore;
    }

    //StoreDropDown
    public List<StoreDropDown> getStoreDropDown() {
        List<IKeyValuePair> storeDropDownList = storeIdRepository.getStoreDropDown();

        List<StoreDropDown> storeDropList = new ArrayList<>();
        if (storeDropDownList != null) {
            for (IKeyValuePair ikeyValuePair : storeDropDownList) {
                StoreDropDown storeDropDown = new StoreDropDown();

                String storeId = String.valueOf(ikeyValuePair.getStoreId());
                String storeName = ikeyValuePair.getDescription();
                storeDropDown.setStoreId(storeId);
                storeDropDown.setStoreName(storeName);
                storeDropList.add(storeDropDown);
            }
        }
        log.info("results: " + storeDropDownList);
        return storeDropList;
    }
}
