package com.mnrclara.api.cg.setup.service;


import com.mnrclara.api.cg.setup.model.IKeyValuePair;
import com.mnrclara.api.cg.setup.model.relationshipid.AddRelationShipId;
import com.mnrclara.api.cg.setup.model.relationshipid.FindRelationShipId;
import com.mnrclara.api.cg.setup.model.relationshipid.RelationShipId;
import com.mnrclara.api.cg.setup.model.relationshipid.UpdateRelationShipId;
import com.mnrclara.api.cg.setup.repository.CompanyIdRepository;
import com.mnrclara.api.cg.setup.repository.RelationShipIdRepository;
import com.mnrclara.api.cg.setup.repository.specification.RelationShipIdSpecification;
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

@Service
@Slf4j
public class RelationShipIdService {
    @Autowired
    private CompanyIdRepository companyIdRepository;
    @Autowired
    private SetupService setupService;
    @Autowired
    private RelationShipIdRepository relationShipIdRepository;
    @Autowired
    private NumberRangeService numberRangeService;

    /**
     * getAllRelationShip
     *
     * @return
     */

    public List<RelationShipId> getAllRelationShip() {
        List<RelationShipId> relationShipIdList = relationShipIdRepository.findAll();
        relationShipIdList = relationShipIdList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());

        List<RelationShipId> dbRelationShip = new ArrayList<>();
        for (RelationShipId relationShipId : relationShipIdList) {
            IKeyValuePair iKeyValuePair = companyIdRepository.getCompanyIdAndDescription(relationShipId.getCompanyId(), relationShipId.getLanguageId());

            if (iKeyValuePair != null) {
                relationShipId.setCompanyIdDescription(iKeyValuePair.getDescription());
            }
            dbRelationShip.add(relationShipId);
        }
        return dbRelationShip;
    }


    /**
     * @param companyId
     * @param languageId
     * @param relationShipId
     * @return
     */
    public RelationShipId getRelationShipId(String companyId, String languageId, Long relationShipId) {

        Optional<RelationShipId> dbRelationId =
                relationShipIdRepository.findByCompanyIdAndLanguageIdAndRelationShipIdAndDeletionIndicator(
                        companyId, languageId, relationShipId, 0L);

        if (dbRelationId.isEmpty())
            throw new RuntimeException("The given Values of companyId - " + companyId +
                    " languageId - " + languageId +
                    " relationShipId - " + relationShipId + "doesn't exist");

        RelationShipId newRelationShipId = new RelationShipId();
        BeanUtils.copyProperties(dbRelationId.get(), newRelationShipId, CommonUtils.getNullPropertyNames(dbRelationId));

        IKeyValuePair iKeyValuePair = companyIdRepository.getCompanyIdAndDescription(companyId, languageId);
        if (iKeyValuePair != null) {
            newRelationShipId.setCompanyIdDescription(iKeyValuePair.getDescription());
        }
        return newRelationShipId;
    }


    /**
     * @param addRelationShipId
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */

    public RelationShipId createRelationShip(AddRelationShipId addRelationShipId, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {

        Optional<RelationShipId> duplicateRelationShipId = relationShipIdRepository.findByCompanyIdAndLanguageIdAndRelationShipIdAndDeletionIndicator(
                addRelationShipId.getCompanyId(), addRelationShipId.getLanguageId(), addRelationShipId.getRelationShipId(), 0L);

        if (!duplicateRelationShipId.isEmpty()) {
            throw new EntityNotFoundException("Record is getting duplicate");
        } else {

            IKeyValuePair iKeyValuePair =
                    companyIdRepository.getCompanyIdAndDescription(addRelationShipId.getCompanyId(), addRelationShipId.getLanguageId());
            RelationShipId dbRelationShipId = new RelationShipId();
            BeanUtils.copyProperties(addRelationShipId, dbRelationShipId, CommonUtils.getNullPropertyNames(addRelationShipId));

            Long NUM_RAN_CODE = 6L;
            String NUM_RAN_OBJ = "CGRELATIONSHIP";
            String C_ID = "1000";
            String LANG_ID = "EN";
            String RE_SHIP_ID = numberRangeService.getNextNumberRange(NUM_RAN_CODE,NUM_RAN_OBJ,LANG_ID,C_ID);
            log.info("nextVal from NumberRange for RE_SHIP: " + RE_SHIP_ID);
            dbRelationShipId.setRelationShipId(Long.valueOf(RE_SHIP_ID));

            if (iKeyValuePair != null) {
                dbRelationShipId.setCompanyIdDescription(iKeyValuePair.getDescription());
            } else {
                throw new RuntimeException("The given values of companyId "
                        + addRelationShipId.getCompanyId() + " languageId "
                        + addRelationShipId.getLanguageId() + " relationShipId "
                        + addRelationShipId.getRelationShipId() + " doesn't exist ");
            }
            dbRelationShipId.setDeletionIndicator(0L);
            dbRelationShipId.setCreatedBy(loginUserID);
            dbRelationShipId.setUpdatedBy(loginUserID);
            dbRelationShipId.setCreatedOn(new Date());
            dbRelationShipId.setUpdatedOn(new Date());
            return relationShipIdRepository.save(dbRelationShipId);
        }
    }


    /**
     * @param companyId
     * @param languageId
     * @param relationShipId
     * @param loginUserID
     * @param updateRelationShipId
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */

    public RelationShipId updateRelationShipId(String companyId, String languageId, Long relationShipId, String loginUserID, UpdateRelationShipId updateRelationShipId)
            throws IllegalAccessException, InvocationTargetException {

        RelationShipId dbRelationShipId = getRelationShipId(companyId, languageId, relationShipId);

        BeanUtils.copyProperties(updateRelationShipId, dbRelationShipId, CommonUtils.getNullPropertyNames(updateRelationShipId));
        dbRelationShipId.setUpdatedBy(loginUserID);
        dbRelationShipId.setUpdatedOn(new Date());
        return relationShipIdRepository.save(dbRelationShipId);
    }


    /**
     * @param companyId
     * @param languageId
     * @param relationShipId
     * @param loginUserID
     * @return
     */
    public RelationShipId deleteRelationShipId(String companyId, String languageId, Long relationShipId, String loginUserID) {

        RelationShipId dbRelationShipId = getRelationShipId(companyId, languageId, relationShipId);

        if (dbRelationShipId != null) {
            dbRelationShipId.setDeletionIndicator(1L);
            dbRelationShipId.setUpdatedBy(loginUserID);
            dbRelationShipId.setUpdatedOn(new Date());
            return relationShipIdRepository.save(dbRelationShipId);
        } else {
            throw new RuntimeException("Error in deleting Id - " + relationShipId);
        }
    }

    /**
     * @param findRelationShipId
     * @return
     * @throws ParseException
     */
    public List<RelationShipId> findRelationShipId(FindRelationShipId findRelationShipId)
            throws ParseException {
        if (findRelationShipId.getStartCreatedOn() != null && findRelationShipId.getStartCreatedOn() != null) {
            Date date = DateUtils.convertStringToYYYYMMDD(findRelationShipId.getStartCreatedOn());
            Date date1 = DateUtils.convertStringToYYYYMMDD(findRelationShipId.getEndCreatedOn());

            Date[] dates = DateUtils.addTimeToDatesForSearch(date, date1);
            findRelationShipId.setFromDate(dates[0]);
            findRelationShipId.setToDate(dates[1]);
        }

        RelationShipIdSpecification spec = new RelationShipIdSpecification(findRelationShipId);
        List<RelationShipId> results = relationShipIdRepository.findAll(spec);
        results = results.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        log.info("results: " + results);
        List<RelationShipId> newRelationShipId = new ArrayList<>();
        for (RelationShipId dbRelationShipId : results) {
            IKeyValuePair iKeyValuePair =
                    companyIdRepository.getCompanyIdAndDescription(dbRelationShipId.getCompanyId(), dbRelationShipId.getLanguageId());

            if (iKeyValuePair != null) {
                dbRelationShipId.setCompanyIdDescription(iKeyValuePair.getDescription());
            }
            newRelationShipId.add(dbRelationShipId);
        }
        return newRelationShipId;
    }

}
