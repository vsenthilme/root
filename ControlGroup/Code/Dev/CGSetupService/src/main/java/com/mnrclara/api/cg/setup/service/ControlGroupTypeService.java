package com.mnrclara.api.cg.setup.service;

import com.mnrclara.api.cg.setup.repository.ControlGroupTypeRepository;
import com.mnrclara.api.cg.setup.repository.specification.ControlGroupTypeSpecification;
import com.mnrclara.api.cg.setup.exception.BadRequestException;
import com.mnrclara.api.cg.setup.model.IKeyValuePair;
import com.mnrclara.api.cg.setup.model.controlgrouptype.AddControlGroupType;
import com.mnrclara.api.cg.setup.model.controlgrouptype.ControlGroupType;
import com.mnrclara.api.cg.setup.model.controlgrouptype.FindControlGroupType;
import com.mnrclara.api.cg.setup.model.controlgrouptype.UpdateControlGroup;
import com.mnrclara.api.cg.setup.repository.CompanyIdRepository;
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
public class ControlGroupTypeService {

    @Autowired
    private CompanyIdRepository companyIdRepository;

    @Autowired
    private SetupService setupService;

    @Autowired
    private NumberRangeService numberRangeService;

    @Autowired
    private ControlGroupTypeRepository controlGroupTypeRepository;

    @Autowired
    private LanguageIdService languageIdService;

    /**
     * getAllControlGroupType
     *
     * @return
     */
    public List<ControlGroupType> getAllControlGroupType() {
        List<ControlGroupType> controlGroupTypeList = controlGroupTypeRepository.findAll();
        controlGroupTypeList = controlGroupTypeList.stream().
                filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        List<ControlGroupType> newControlGroupType = new ArrayList<>();
        for (ControlGroupType controlGroupType : controlGroupTypeList) {
            IKeyValuePair iKeyValuePair =
                    companyIdRepository.getCompanyIdAndDescription(controlGroupType.getCompanyId(), controlGroupType.getLanguageId());

            if (iKeyValuePair != null) {
                controlGroupType.setCompanyIdAndDescription(iKeyValuePair.getDescription());
            }
            newControlGroupType.add(controlGroupType);
        }
        return newControlGroupType;
    }

    /**
     * @param groupTypeId
     * @param companyId
     * @param languageId
     * @return
     */
    public ControlGroupType getControlGroupType(Long groupTypeId, String companyId, String languageId, Long versionNumber) {
        log.info("controlGroupType Id: " + groupTypeId);

        Optional<ControlGroupType> dbControlGroup =
                controlGroupTypeRepository.findByCompanyIdAndLanguageIdAndGroupTypeIdAndVersionNumberAndDeletionIndicator(
                        companyId, languageId, groupTypeId, versionNumber, 0L);

        if (dbControlGroup.isEmpty()) {
            throw new BadRequestException("The given values of companyId: " + companyId +
                    " groupTypeId " + groupTypeId +
                    " languageId " + languageId +
                    " versionNumber " + versionNumber + "doesn't exists");
        }
        ControlGroupType newControlgroupType = new ControlGroupType();
        BeanUtils.copyProperties(dbControlGroup.get(), newControlgroupType, CommonUtils.getNullPropertyNames(dbControlGroup));
        IKeyValuePair iKeyValuePair = companyIdRepository.getCompanyIdAndDescription(companyId, languageId);
        if (iKeyValuePair != null) {
            newControlgroupType.setCompanyIdAndDescription(iKeyValuePair.getDescription());
        }
        return newControlgroupType;
    }

    /**
     * @param newControlGroupType
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public ControlGroupType createControlGroupType(AddControlGroupType newControlGroupType, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {

        ControlGroupType duplicateControlGroupType =
                controlGroupTypeRepository.findByCompanyIdAndLanguageIdAndGroupTypeIdAndStatusIdAndDeletionIndicator(
                        newControlGroupType.getCompanyId(),
                        newControlGroupType.getLanguageId(),
                        newControlGroupType.getGroupTypeId(),
                        0L, 0L);

        if (duplicateControlGroupType != null) {
            duplicateControlGroupType.setStatusId(1L);
            duplicateControlGroupType.setValidityDateTo(new Date());
        }
        IKeyValuePair iKeyValuePair =
                companyIdRepository.getCompanyIdAndDescription(newControlGroupType.getCompanyId(), newControlGroupType.getLanguageId());

        ControlGroupType dbControlGroupType = new ControlGroupType();
        BeanUtils.copyProperties(newControlGroupType, dbControlGroupType, CommonUtils.getNullPropertyNames(newControlGroupType));

        Long NUM_RAN_CODE = 1L;
        String NUM_RAN_OBJ = "CGGROUPTYPE";
        String C_ID = "1000";
        String LANG_ID = "EN";
        String GROUP_ID = numberRangeService.getNextNumberRange(NUM_RAN_CODE,NUM_RAN_OBJ,LANG_ID,C_ID);
        log.info("nextVal from NumberRange for GROUP_ID: " + GROUP_ID);
        dbControlGroupType.setGroupTypeId(Long.valueOf(GROUP_ID));

        if (iKeyValuePair != null) {
            dbControlGroupType.setCompanyIdAndDescription(iKeyValuePair.getDescription());
        } else {
            throw new RuntimeException("The given values of companyId " + newControlGroupType.getCompanyId() +
                    "languageId" + newControlGroupType.getLanguageId() + "doesn't exists");
        }
        Long versionId = controlGroupTypeRepository.getVersionId();
        if(versionId != null){
            dbControlGroupType.setVersionNumber(versionId);
        }else {
            dbControlGroupType.setVersionNumber(1L);
        }
        dbControlGroupType.setDeletionIndicator(0L);
        dbControlGroupType.setValidityDateFrom(new Date());
        dbControlGroupType.setValidityDateTo(null);
        dbControlGroupType.setStatusId(0L);
        dbControlGroupType.setCreatedBy(loginUserID);
        dbControlGroupType.setUpdatedBy(loginUserID);
        dbControlGroupType.setCreatedOn(new Date());
        dbControlGroupType.setUpdatedOn(new Date());
        return controlGroupTypeRepository.save(dbControlGroupType);
    }

    /**
     * @param groupTypeId
     * @param languageId
     * @param companyId
     * @param loginUserID
     * @param updateControlGroup
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public ControlGroupType updateControlGroupType(Long groupTypeId, String languageId, String companyId,
                                                   String loginUserID, Long versionNumber, UpdateControlGroup updateControlGroup)
            throws IllegalAccessException, InvocationTargetException {
        ControlGroupType dbControlGroup = getControlGroupType(groupTypeId, companyId, languageId, versionNumber);
        BeanUtils.copyProperties(updateControlGroup, dbControlGroup, CommonUtils.getNullPropertyNames(updateControlGroup));

        if (updateControlGroup.getStatusId() != 0) {
            dbControlGroup.setValidityDateTo(new Date());
        }
        dbControlGroup.setUpdatedBy(loginUserID);
        dbControlGroup.setDeletionIndicator(0L);
        dbControlGroup.setUpdatedOn(new Date());
        return controlGroupTypeRepository.save(dbControlGroup);
    }

    /**
     * @param groupTypeId
     * @param companyId
     * @param languageId
     * @param versionNumber
     * @param loginUserID
     */
    public void deleteControlGroup(Long groupTypeId, String companyId, String languageId, Long versionNumber, String loginUserID) {
        ControlGroupType dbControlGroupType = getControlGroupType(groupTypeId, companyId, languageId, versionNumber);
        if (dbControlGroupType != null) {
            dbControlGroupType.setDeletionIndicator(1L);
            dbControlGroupType.setUpdatedBy(loginUserID);
            dbControlGroupType.setUpdatedOn(new Date());
            controlGroupTypeRepository.save(dbControlGroupType);
        } else {
            throw new EntityNotFoundException("Error in deleting Id: " + groupTypeId);
        }
    }


    //Find ControlGroupType
    public List<ControlGroupType> findControlGroupType(FindControlGroupType findControlGroupType)
            throws ParseException {

        if (findControlGroupType.getStartCreatedOn() != null && findControlGroupType.getStartCreatedOn() != null) {
            Date date = DateUtils.convertStringToYYYYMMDD(findControlGroupType.getStartCreatedOn());
            Date date1 = DateUtils.convertStringToYYYYMMDD(findControlGroupType.getEndCreatedOn());

            Date[] dates = DateUtils.addTimeToDatesForSearch(date, date1);
            findControlGroupType.setFromDate(dates[0]);
            findControlGroupType.setToDate(dates[1]);
        }

        ControlGroupTypeSpecification spec = new ControlGroupTypeSpecification(findControlGroupType);
        List<ControlGroupType> results = controlGroupTypeRepository.findAll(spec);
        results = results.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        log.info("results: " + results);

        List<ControlGroupType> newControlGroupType = new ArrayList<>();
        for (ControlGroupType dbControlGroupType : results) {
            IKeyValuePair iKeyValuePair =
                    companyIdRepository.getCompanyIdAndDescription(dbControlGroupType.getCompanyId(), dbControlGroupType.getLanguageId());

            if (iKeyValuePair != null) {
                dbControlGroupType.setCompanyIdAndDescription(iKeyValuePair.getDescription());
            }
            newControlGroupType.add(dbControlGroupType);
        }
        return newControlGroupType;
    }
}
