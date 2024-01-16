package com.mnrclara.api.cg.setup.service;

import com.mnrclara.api.cg.setup.model.state.AddState;
import com.mnrclara.api.cg.setup.model.state.FindState;
import com.mnrclara.api.cg.setup.model.state.State;
import com.mnrclara.api.cg.setup.model.state.UpdateState;
import com.mnrclara.api.cg.setup.repository.CompanyIdRepository;
import com.mnrclara.api.cg.setup.repository.CountryRepository;
import com.mnrclara.api.cg.setup.repository.StateRepository;
import com.mnrclara.api.cg.setup.repository.specification.StateSpecification;
import com.mnrclara.api.cg.setup.exception.BadRequestException;
import com.mnrclara.api.cg.setup.model.IKeyValuePair;
import com.mnrclara.api.cg.setup.util.CommonUtils;
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

@Slf4j
@Service
public class StateService {
    @Autowired
    private CompanyIdRepository companyIdRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private CountryService countryService;

    /**
     * getCompanies
     *
     * @return
     */
    public List<State> getStates() {
        List<State> stateList = stateRepository.findAll();
        stateList = stateList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        List<State> newState = new ArrayList<>();
        for (State dbState : stateList) {
            IKeyValuePair iKeyValuePair =
                    countryRepository.getCountryIdAndDescription(dbState.getCountryId(),
                            dbState.getCompanyId(), dbState.getLanguageId());

            IKeyValuePair iKeyValuePair1 =
                    companyIdRepository.getCompanyIdAndDescription(dbState.getCompanyId(), dbState.getLanguageId());

            if (iKeyValuePair != null) {
                dbState.setCountryIdAndDescription(iKeyValuePair.getDescription());
            }
            if (iKeyValuePair1 != null) {
                dbState.setCompanyIdAndDescription(iKeyValuePair1.getDescription());
            }
            newState.add(dbState);
        }
        return newState;
    }

    /**
     * @param stateId
     * @param companyId
     * @param languageId
     * @return
     */
    public State getState(String stateId, String companyId, String languageId) {
        log.info("state Id: " + stateId);

        Optional<State> dbState = stateRepository.findByStateIdAndCompanyIdAndLanguageIdAndDeletionIndicator(
                stateId, companyId, languageId, 0L);

        if (dbState.isEmpty()) {
            throw new BadRequestException("The given ID doesn't exist : " + stateId);
        }
        State newState = new State();
        BeanUtils.copyProperties(dbState.get(), newState, CommonUtils.getNullPropertyNames(dbState));
        IKeyValuePair iKeyValuePair = countryRepository.getCountryIdAndDescription(newState.getCountryId(), companyId, languageId);
        IKeyValuePair iKeyValuePair1 = companyIdRepository.getCompanyIdAndDescription(companyId, languageId);

        if (iKeyValuePair != null) {
            newState.setCountryIdAndDescription(iKeyValuePair.getDescription());
        }
        if (iKeyValuePair1 != null) {
            newState.setCompanyIdAndDescription(iKeyValuePair1.getDescription());
        }
        return newState;
    }

    /**
     * createState
     *
     * @param newState
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public State createState(AddState newState, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {

        Optional<State> duplicateState =
                stateRepository.findByStateIdAndCompanyIdAndLanguageIdAndDeletionIndicator(
                        newState.getStateId(), newState.getCompanyId(), newState.getLanguageId(), 0L);

        if (!duplicateState.isEmpty()) {
            throw new EntityNotFoundException("Record is Getting Duplicated");
        } else {
            IKeyValuePair iKeyValuePair =
                    countryRepository.getCountryIdAndDescription(newState.getCountryId(), newState.getCompanyId(),
                            newState.getLanguageId());

            IKeyValuePair iKeyValuePair1 =
                    companyIdRepository.getCompanyIdAndDescription(newState.getCompanyId(), newState.getLanguageId());

            State dbState = new State();
            BeanUtils.copyProperties(newState, dbState, CommonUtils.getNullPropertyNames(newState));

            if (iKeyValuePair != null && iKeyValuePair1 != null) {
                dbState.setCompanyIdAndDescription(iKeyValuePair1.getDescription());
                dbState.setCountryIdAndDescription(iKeyValuePair.getDescription());
            } else {

                throw new RuntimeException("The given values of CompanyId"
                        + newState.getCompanyId() + " languageId "
                        + newState.getLanguageId() + " countryId "
                        + newState.getCountryId() + " stateId "
                        + newState.getStateId() + " doesn't exists");
            }
            dbState.setDeletionIndicator(0L);
            dbState.setCreatedBy(loginUserID);
            dbState.setUpdatedBy(loginUserID);
            dbState.setCreatedOn(new Date());
            dbState.setUpdatedOn(new Date());
            return stateRepository.save(dbState);
        }
    }

    /**
     * updateState
     *
     * @param stateId
     * @param updateState
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public State updateState(String stateId, String languageId,
                             String companyId, String loginUserID, UpdateState updateState)
            throws IllegalAccessException, InvocationTargetException {
        State dbState = stateRepository.findByCompanyIdAndLanguageIdAndStateIdAndDeletionIndicator(
                companyId, languageId, stateId, 0L);

        if (dbState == null) {
            throw new RuntimeException("The given values of companyId "
                    + companyId + " languageId "
                    + languageId + " stateId "
                    + stateId + "doesn't exists");
        } else {

            IKeyValuePair iKeyValuePair =
                    countryRepository.getCountryIdAndDescription(updateState.getCountryId(), companyId, languageId);

            BeanUtils.copyProperties(updateState, dbState, CommonUtils.getNullPropertyNames(updateState));
            if (iKeyValuePair != null) {
                dbState.setCountryIdAndDescription(iKeyValuePair.getDescription());
            } else {
                throw new RuntimeException("Country Id doesn't exists");
            }
            dbState.setUpdatedBy(loginUserID);
            dbState.setUpdatedOn(new Date());
            stateRepository.save(dbState);
        }
        return dbState;
    }

    /**
     * @param stateId
     * @param companyId
     * @param languageId
     * @param loginUserID
     */
    public void deleteState(String stateId, String companyId, String languageId, String loginUserID) {
        State dbState = getState(stateId, companyId, languageId);
        if (dbState != null) {
            dbState.setDeletionIndicator(1L);
            dbState.setUpdatedBy(loginUserID);
            dbState.setUpdatedOn(new Date());
            stateRepository.save(dbState);
        } else {
            throw new EntityNotFoundException("Error in deleting Id: " + stateId);
        }
    }

    //Find State
    public List<State> findState(FindState findState) throws ParseException {
        if (findState.getStartCreatedOn() != null && findState.getStartCreatedOn() != null) {
            Date date = DateUtils.convertStringToYYYYMMDD(findState.getStartCreatedOn());
            Date date1 = DateUtils.convertStringToYYYYMMDD(findState.getEndCreatedOn());

            Date[] dates = DateUtils.addTimeToDatesForSearch(date, date1);
            findState.setFromDate(dates[0]);
            findState.setToDate(dates[1]);
        }

        StateSpecification spec = new StateSpecification(findState);
        List<State> results = stateRepository.findAll(spec);
        results = results.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        log.info("results: " + results);
        List<State> newState = new ArrayList<>();
        for (State dbState : results) {
            IKeyValuePair iKeyValuePair =
                    countryRepository.getCountryIdAndDescription(dbState.getCountryId(),
                            dbState.getCompanyId(), dbState.getLanguageId());

            IKeyValuePair iKeyValuePair1 =
                    companyIdRepository.getCompanyIdAndDescription(dbState.getCompanyId(), dbState.getLanguageId());

            if (iKeyValuePair != null) {
                dbState.setCountryIdAndDescription(iKeyValuePair.getDescription());
            }
            if (iKeyValuePair1 != null) {
                dbState.setCompanyIdAndDescription(iKeyValuePair1.getDescription());
            }
            newState.add(dbState);
        }
        return newState;
    }
}
