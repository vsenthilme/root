package com.mnrclara.api.cg.setup.service;


import com.mnrclara.api.cg.setup.exception.BadRequestException;
import com.mnrclara.api.cg.setup.model.IKeyValuePair;
import com.mnrclara.api.cg.setup.model.auth.AuthToken;
import com.mnrclara.api.cg.setup.model.controlgroup.AddControlGroup;
import com.mnrclara.api.cg.setup.model.controlgroup.ControlGroup;
import com.mnrclara.api.cg.setup.model.controlgroup.FindControlGroup;
import com.mnrclara.api.cg.setup.model.controlgroup.UpdateControlGroup;
import com.mnrclara.api.cg.setup.repository.ClientRepository;
import com.mnrclara.api.cg.setup.repository.CompanyIdRepository;
import com.mnrclara.api.cg.setup.repository.ControlGroupRepository;
import com.mnrclara.api.cg.setup.repository.ControlGroupTypeRepository;
import com.mnrclara.api.cg.setup.repository.specification.ControlGroupSpecification;
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
public class ControlGroupService {
    @Autowired
    private ControlGroupRepository controlGroupRepository;

    @Autowired
    private NumberRangeService numberRangeService;

    @Autowired
    private SetupService setupService;

    @Autowired
    private ControlGroupTypeRepository controlGroupTypeRepository;

    @Autowired
    private CompanyIdRepository companyIdRepository;

    @Autowired
    ClientRepository clientRepository;

    /**
     * getAllControlGroup
     *
     * @return
     */
    public List<ControlGroup> getAllControlGroup() {
        List<ControlGroup> controlGroupList = controlGroupRepository.findAll();
        controlGroupList = controlGroupList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        List<ControlGroup> newControlGroup = new ArrayList<>();
        for (ControlGroup controlGroup : controlGroupList) {
            IKeyValuePair iKeyValuePair =
                    companyIdRepository.getCompanyIdAndDescription(controlGroup.getCompanyId(), controlGroup.getLanguageId());

            IKeyValuePair iKeyValuePair1 =
                    controlGroupTypeRepository.getControlGroupTypeIdAndDescription(
                            controlGroup.getGroupTypeId(), controlGroup.getCompanyId(), controlGroup.getLanguageId());

            if (iKeyValuePair != null && iKeyValuePair1 != null) {
                controlGroup.setCompanyIdAndDescription(iKeyValuePair.getDescription());
                controlGroup.setGroupTypeName(iKeyValuePair1.getDescription());
            }
            newControlGroup.add(controlGroup);
        }
        return newControlGroup;
    }

    /**
     * @param companyId
     * @param languageId
     * @param groupId
     * @param groupTypeId
     * @return
     */
    public ControlGroup getControlGroup(String companyId, String languageId, Long groupId, Long groupTypeId, Long versionNumber) {

        Optional<ControlGroup> dbControlGroup =
                controlGroupRepository.findByCompanyIdAndLanguageIdAndGroupIdAndGroupTypeIdAndVersionNumberAndDeletionIndicator(
                        companyId, languageId, groupId, groupTypeId, versionNumber, 0L);

        if (dbControlGroup.isEmpty()) {
            throw new BadRequestException("The given values : " +
                    " companyCodeId - " + companyId +
                    " languageId - " + languageId +
                    " groupId - " + groupId +
                    " groupTypeId - " + groupTypeId +
                    " versionNumber - " + versionNumber +
                    " doesn't exist.");

        }
        ControlGroup newControlgroup = new ControlGroup();
        BeanUtils.copyProperties(dbControlGroup.get(), newControlgroup, CommonUtils.getNullPropertyNames(dbControlGroup));

        IKeyValuePair iKeyValuePair =
                companyIdRepository.getCompanyIdAndDescription(companyId, languageId);
        IKeyValuePair iKeyValuePair1 =
                controlGroupTypeRepository.getControlGroupTypeIdAndDescription(groupTypeId, companyId, languageId);

        if (iKeyValuePair != null && iKeyValuePair1 != null) {
            newControlgroup.setCompanyIdAndDescription(iKeyValuePair.getDescription());
            newControlgroup.setGroupTypeName(iKeyValuePair1.getDescription());
        }
        return newControlgroup;
    }


    /**
     * @param searchControlGroup
     * @return
     * @throws ParseException
     */
    public List<ControlGroup> findControlGroup(FindControlGroup searchControlGroup)
            throws ParseException {
        if (searchControlGroup.getStartCreatedOn() != null && searchControlGroup.getStartCreatedOn() != null) {
            Date date = DateUtils.convertStringToYYYYMMDD(searchControlGroup.getStartCreatedOn());
            Date date1 = DateUtils.convertStringToYYYYMMDD(searchControlGroup.getEndCreatedOn());

            Date[] dates = DateUtils.addTimeToDatesForSearch(date, date1);
            searchControlGroup.setFromDate(dates[0]);
            searchControlGroup.setToDate(dates[1]);
        }

        ControlGroupSpecification spec = new ControlGroupSpecification(searchControlGroup);
        List<ControlGroup> results = controlGroupRepository.findAll(spec);
        results = results.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        log.info("results: " + results);

        List<ControlGroup> newControlGroup = new ArrayList<>();
        for (ControlGroup dbControlGroup : results) {
            IKeyValuePair iKeyValuePair =
                    companyIdRepository.getCompanyIdAndDescription(dbControlGroup.getCompanyId(), dbControlGroup.getLanguageId());

            IKeyValuePair iKeyValuePair1 =
                    controlGroupTypeRepository.getControlGroupTypeIdAndDescription(
                            dbControlGroup.getGroupTypeId(), dbControlGroup.getCompanyId(), dbControlGroup.getLanguageId());

            if (iKeyValuePair != null && iKeyValuePair1 != null) {
                dbControlGroup.setCompanyIdAndDescription(iKeyValuePair.getDescription());
                dbControlGroup.setGroupTypeName(iKeyValuePair1.getDescription());
            }
            newControlGroup.add(dbControlGroup);
        }
        return newControlGroup;
    }

    /**
     * @param newControlGroup
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public ControlGroup createControlGroup(AddControlGroup newControlGroup, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {

        ControlGroup duplicateControlGroup =
                controlGroupRepository.findByCompanyIdAndLanguageIdAndGroupIdAndGroupTypeIdAndStatusIdAndDeletionIndicator(
                        newControlGroup.getCompanyId(),
                        newControlGroup.getLanguageId(),
                        newControlGroup.getGroupId(),
                        newControlGroup.getGroupTypeId(),
                        0L,
                        0L);

        if (duplicateControlGroup != null) {
            duplicateControlGroup.setStatusId(1L);
            duplicateControlGroup.setValidityDateTo(new Date());
        }
        IKeyValuePair iKeyValuePair =
                companyIdRepository.getCompanyIdAndDescription(newControlGroup.getCompanyId(), newControlGroup.getLanguageId());

        IKeyValuePair iKeyValuePair1 =
                controlGroupTypeRepository.getControlGroupTypeIdAndDescription(
                        newControlGroup.getGroupTypeId(), newControlGroup.getCompanyId(), newControlGroup.getLanguageId());

        ControlGroup dbControlGroup = new ControlGroup();
        BeanUtils.copyProperties(newControlGroup, dbControlGroup, CommonUtils.getNullPropertyNames(newControlGroup));

        if (iKeyValuePair != null && iKeyValuePair1 != null) {
            dbControlGroup.setCompanyIdAndDescription(iKeyValuePair.getDescription());
            dbControlGroup.setGroupTypeName(iKeyValuePair1.getDescription());
        } else {
            throw new RuntimeException("The given values of companyId - "
                    + newControlGroup.getCompanyId() + " LanguageId "
                    + newControlGroup.getLanguageId() + " groupTypeId "
                    + newControlGroup.getGroupTypeId() + " doesn't exists ");
        }
 
        Long NUM_RAN_CODE = 8L;
        String NUM_RAN_OBJ = "CGCONTROLGROUP";
        String C_ID = "1000";
        String LANG_ID = "EN";
        String GROUP_ID = numberRangeService.getNextNumberRange(NUM_RAN_CODE,NUM_RAN_OBJ,LANG_ID,C_ID);
        log.info("nextVal from NumberRange for GROUP_ID: " + GROUP_ID);
        dbControlGroup.setGroupId(Long.valueOf(GROUP_ID));

        Long versionId = controlGroupRepository.getVersionId();
        if(versionId !=null){
            dbControlGroup.setVersionNumber(versionId);
        }else {
            dbControlGroup.setVersionNumber(1L);
        }
        dbControlGroup.setDeletionIndicator(0L);
        dbControlGroup.setCreatedBy(loginUserID);
        dbControlGroup.setStatusId(0L);
        dbControlGroup.setValidityDateFrom(new Date());
        dbControlGroup.setValidityDateTo(null);
        dbControlGroup.setUpdatedBy(loginUserID);
        dbControlGroup.setCreatedOn(new Date());
        dbControlGroup.setUpdatedOn(new Date());
        return controlGroupRepository.save(dbControlGroup);
    }

    /**
     * @param companyId
     * @param languageId
     * @param groupId
     * @param groupTypeId
     * @param loginUserID
     * @param updateControlGroup
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public ControlGroup updateControlGroup(String companyId, String languageId, Long groupId, Long versionNUmber,
                                           Long groupTypeId, String loginUserID, UpdateControlGroup updateControlGroup)
            throws IllegalAccessException, InvocationTargetException {
        ControlGroup dbControlGroup = getControlGroup(companyId, languageId, groupId, groupTypeId, versionNUmber);
        BeanUtils.copyProperties(updateControlGroup, dbControlGroup, CommonUtils.getNullPropertyNames(updateControlGroup));

        if (updateControlGroup.getStatusId() != 0) {
            dbControlGroup.setValidityDateTo(new Date());
        }
        dbControlGroup.setUpdatedBy(loginUserID);
        dbControlGroup.setUpdatedOn(new Date());
        return controlGroupRepository.save(dbControlGroup);
    }

    /**
     * @param companyId
     * @param languageId
     * @param groupId
     * @param groupTypeId
     * @param loginUserID
     */
    public void deleteControlGroup(String companyId, String languageId, Long versionNumber,
                                   Long groupId, Long groupTypeId, String loginUserID) {
        ControlGroup dbControlGroup = getControlGroup(companyId, languageId, groupId, groupTypeId, versionNumber);

        if (dbControlGroup != null) {
            dbControlGroup.setDeletionIndicator(1L);
            dbControlGroup.setUpdatedBy(loginUserID);
            controlGroupRepository.save(dbControlGroup);
        } else {
            throw new EntityNotFoundException("Error in deleting Id: " + groupId);
        }
    }
}
