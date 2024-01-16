package com.mnrclara.api.cg.setup.service;

import com.mnrclara.api.cg.setup.exception.BadRequestException;
import com.mnrclara.api.cg.setup.model.IKeyValuePair;
import com.mnrclara.api.cg.setup.model.cgentity.AddCgEntity;
import com.mnrclara.api.cg.setup.model.cgentity.CgEntity;
import com.mnrclara.api.cg.setup.model.cgentity.FindCgEntity;
import com.mnrclara.api.cg.setup.model.cgentity.UpdateCgEntity;
import com.mnrclara.api.cg.setup.repository.CgEntityRepository;
import com.mnrclara.api.cg.setup.repository.ClientRepository;
import com.mnrclara.api.cg.setup.repository.specification.CgEntitySpecification;
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
public class CgEntityService {

    @Autowired
    private CgEntityRepository cgEntityRepository;
    @Autowired
    private NumberRangeService numberRangeService;
    @Autowired
    private ClientRepository clientRepository;

    /**
     * GET ALL ENTITIES
     * @return
     */
    public List<CgEntity> getAllCgEntities() {
        List<CgEntity> cgEntityList = cgEntityRepository.findAll();
        cgEntityList = cgEntityList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        List<CgEntity> newCgEntityList = new ArrayList<>();
        for (CgEntity dbCgEntity : cgEntityList) {
            IKeyValuePair iKeyValuePair = clientRepository.getClientIdAndDescription(
                    dbCgEntity.getClientId(), dbCgEntity.getCompanyId(), dbCgEntity.getLanguageId());

            if (iKeyValuePair != null) {
                dbCgEntity.setClientName(iKeyValuePair.getDescription());
            }
            newCgEntityList.add(dbCgEntity);
        }
        return newCgEntityList;
    }

    /**
     * GET A ENTITY
     * @param entityId
     * @param clientId
     * @param companyId
     * @param languageId
     * @return
     */
    public CgEntity getCgEntity(Long entityId, Long clientId, String companyId, String languageId) {

        Optional<CgEntity> dbCgEntity = cgEntityRepository.findByEntityIdAndClientIdAndCompanyIdAndLanguageIdAndDeletionIndicator(
                entityId, clientId, companyId, languageId, 0L);
        if (dbCgEntity.isEmpty()) {
            throw new BadRequestException("The given values of entityId " + entityId +
                    " clientId " + clientId +
                    " companyId " + companyId +
                    " languageId " + languageId + " doesn't exists");
        }
        CgEntity newCgEntity = new CgEntity();
        BeanUtils.copyProperties(dbCgEntity.get(), newCgEntity, CommonUtils.getNullPropertyNames(dbCgEntity));

        IKeyValuePair iKeyValuePair = clientRepository.getClientIdAndDescription(
                newCgEntity.getClientId(), newCgEntity.getCompanyId(), newCgEntity.getLanguageId());
        if (iKeyValuePair != null) {
            newCgEntity.setClientName(iKeyValuePair.getDescription());
        }
        return newCgEntity;
    }

    /**
     * CREATE A ENTITY
     * @param addCgEntity
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public CgEntity createCgEntity(AddCgEntity addCgEntity, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        Optional<CgEntity> duplicateCgEntity =
                cgEntityRepository.findByEntityIdAndClientIdAndCompanyIdAndLanguageIdAndDeletionIndicator(
                        addCgEntity.getEntityId(), addCgEntity.getClientId(), addCgEntity.getCompanyId(),
                        addCgEntity.getLanguageId(), 0L);
        if (duplicateCgEntity.isPresent()) {
            throw new BadRequestException("Record is getting duplicated");
        } else {
            IKeyValuePair iKeyValuePair = clientRepository.getClientIdAndDescription(
                    addCgEntity.getClientId(), addCgEntity.getCompanyId(), addCgEntity.getLanguageId());
            CgEntity dbCgEntity = new CgEntity();
            BeanUtils.copyProperties(addCgEntity, dbCgEntity, CommonUtils.getNullPropertyNames(addCgEntity));
            if (iKeyValuePair != null) {
                dbCgEntity.setClientName(iKeyValuePair.getDescription());
            } else {
                throw new RuntimeException("The given values of clientId " + addCgEntity.getClientId() +
                        " companyId " + addCgEntity.getCompanyId() +
                        " languageId " + addCgEntity.getLanguageId() + " doesn't exists");
            }
            Long NUM_RAN_CODE = 5L;
            String NUM_RAN_OBJ = "CGENTITY";
            String C_ID = "1000";
            String LANG_ID = "EN";
            String CLIENT_ID = numberRangeService.getNextNumberRange(NUM_RAN_CODE,NUM_RAN_OBJ,LANG_ID,C_ID);
            log.info("nextVal from NumberRange for CLIENT_ID: " + CLIENT_ID);
            dbCgEntity.setEntityId(Long.valueOf(CLIENT_ID));
            dbCgEntity.setDeletionIndicator(0L);
            dbCgEntity.setCreatedBy(loginUserID);
            dbCgEntity.setCreatedOn(new Date());
            dbCgEntity.setUpdatedBy(loginUserID);
            dbCgEntity.setUpdatedOn(new Date());
            return cgEntityRepository.save(dbCgEntity);
        }
    }

    /**
     * UPDATE A ENTITY
     * @param entityId
     * @param clientId
     * @param companyId
     * @param languageId
     * @param loginUserID
     * @param updateCgEntity
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public CgEntity updateCgEntity(Long entityId, Long clientId, String companyId, String languageId,
                                   String loginUserID, UpdateCgEntity updateCgEntity)
            throws IllegalAccessException, InvocationTargetException {
        CgEntity dbCgEntity = getCgEntity(entityId, clientId, companyId, languageId);
        BeanUtils.copyProperties(updateCgEntity, dbCgEntity, CommonUtils.getNullPropertyNames(updateCgEntity));
        dbCgEntity.setUpdatedBy(loginUserID);
        dbCgEntity.setUpdatedOn(new Date());
        return cgEntityRepository.save(dbCgEntity);
    }

    /**
     * DELETE A ENTITY
     * @param entityId
     * @param clientId
     * @param companyId
     * @param languageId
     * @param loginUserID
     */
    public void deleteCgEntity(Long entityId, Long clientId, String companyId, String languageId, String loginUserID) {
        CgEntity dbCgEntity = getCgEntity(entityId, clientId, companyId, languageId);
        if (dbCgEntity != null) {
            dbCgEntity.setDeletionIndicator(1L);
            dbCgEntity.setUpdatedBy(loginUserID);
            dbCgEntity.setUpdatedOn(new Date());
            cgEntityRepository.save(dbCgEntity);
        } else {
            throw new EntityNotFoundException("Error in deleting entityId: " + entityId);
        }
    }

    /**
     * FIND A ENTITY
     * @param findCgEntity
     * @return
     * @throws ParseException
     */
    public List<CgEntity> findCgEntity(FindCgEntity findCgEntity) throws ParseException {

        if (findCgEntity.getStartCreatedOn() != null && findCgEntity.getStartCreatedOn() != null) {
            Date date = DateUtils.convertStringToYYYYMMDD(findCgEntity.getStartCreatedOn());
            Date date1 = DateUtils.convertStringToYYYYMMDD(findCgEntity.getEndCreatedOn());

            Date[] dates = DateUtils.addTimeToDatesForSearch(date, date1);
            findCgEntity.setFromDate(dates[0]);
            findCgEntity.setToDate(dates[1]);
        }

        CgEntitySpecification spec = new CgEntitySpecification(findCgEntity);
        List<CgEntity> results = cgEntityRepository.findAll(spec);
        log.info("Results: " + results);
        List<CgEntity> cgEntityList = new ArrayList<>();
        for (CgEntity dbCgEntity : results) {
            IKeyValuePair iKeyValuePair = clientRepository.getClientIdAndDescription(dbCgEntity.getClientId(), dbCgEntity.getCompanyId(), dbCgEntity.getLanguageId());
            if (iKeyValuePair != null) {
                dbCgEntity.setClientName(iKeyValuePair.getDescription());
            }
            cgEntityList.add(dbCgEntity);
        }
        return cgEntityList;
    }
}
