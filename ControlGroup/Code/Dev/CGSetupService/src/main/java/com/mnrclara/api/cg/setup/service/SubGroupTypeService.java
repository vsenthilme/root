package com.mnrclara.api.cg.setup.service;


import com.mnrclara.api.cg.setup.model.IKeyValuePair;
import com.mnrclara.api.cg.setup.repository.CompanyIdRepository;
import com.mnrclara.api.cg.setup.repository.ControlGroupTypeRepository;
import com.mnrclara.api.cg.setup.repository.specification.SubGroupSpecification;
import com.mnrclara.api.cg.setup.util.CommonUtils;
import com.mnrclara.api.cg.setup.exception.BadRequestException;
import com.mnrclara.api.cg.setup.model.subgrouptype.AddSubGroupType;
import com.mnrclara.api.cg.setup.model.subgrouptype.FindSubGroupType;
import com.mnrclara.api.cg.setup.model.subgrouptype.SubGroupType;
import com.mnrclara.api.cg.setup.model.subgrouptype.UpdateSubGroupType;
import com.mnrclara.api.cg.setup.repository.SubGroupTypeRepository;
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
public class SubGroupTypeService {
    @Autowired
    private ControlGroupTypeRepository controlGroupTypeRepository;
    @Autowired
    private SubGroupTypeRepository subGroupTypeRepository;
    @Autowired
    private SetupService setupService;
    @Autowired
    private CompanyIdRepository companyIdRepository;
    @Autowired
    private NumberRangeService numberRangeService;



    /**
     * getAllSubGroupType
     *
     * @return
     */
    public List<SubGroupType> getAllSubgroupType() {
        List<SubGroupType> subGroupTypeList = subGroupTypeRepository.findAll();
        subGroupTypeList = subGroupTypeList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        List<SubGroupType> newSubGroupType = new ArrayList<>();
        for (SubGroupType dbSubGroupType : subGroupTypeList) {
            IKeyValuePair iKeyValuePair =
                    companyIdRepository.getCompanyIdAndDescription(dbSubGroupType.getCompanyId(), dbSubGroupType.getLanguageId());

            IKeyValuePair iKeyValuePair1 =
                    controlGroupTypeRepository.getControlGroupTypeIdAndDescription(dbSubGroupType.getGroupTypeId(),
                            dbSubGroupType.getCompanyId(), dbSubGroupType.getLanguageId());

            if (iKeyValuePair != null) {
                dbSubGroupType.setCompanyIdAndDescription(iKeyValuePair.getDescription());
            }
            if (iKeyValuePair1 != null) {
                dbSubGroupType.setGroupTypeName(iKeyValuePair1.getDescription());
            }

            newSubGroupType.add(dbSubGroupType);
        }
        return newSubGroupType;
    }

    /**
     * @param subGroupTypeId
     * @param companyId
     * @param groupTypeId
     * @param versionNumber
     * @param languageId
     * @return
     */
    public SubGroupType getSubGroupType(Long subGroupTypeId, String companyId, Long groupTypeId,
                                        Long versionNumber, String languageId) {
        log.info("SubGroup Id: " + subGroupTypeId);
        Optional<SubGroupType> dbSubGroup =
                subGroupTypeRepository.findByCompanyIdAndLanguageIdAndGroupTypeIdAndSubGroupTypeIdAndVersionNumberAndDeletionIndicator(
                        companyId, languageId, groupTypeId, subGroupTypeId, versionNumber, 0L);

        if (dbSubGroup.isEmpty()) {
            throw new BadRequestException("The given ID doesn't exist : " +
                    " subGroupTypeId " + subGroupTypeId +
                    " companyId " + companyId +
                    " groupTypeId " + groupTypeId +
                    "languageId Id " + languageId +
                    "versionNumber " + versionNumber);
        }
        SubGroupType newSubgroupType = new SubGroupType();
        BeanUtils.copyProperties(dbSubGroup.get(), newSubgroupType, CommonUtils.getNullPropertyNames(dbSubGroup));
        IKeyValuePair iKeyValuePair = companyIdRepository.getCompanyIdAndDescription(companyId, languageId);

        IKeyValuePair iKeyValuePair1 =
                controlGroupTypeRepository.getControlGroupTypeIdAndDescription(groupTypeId, companyId, languageId);

        if (iKeyValuePair != null) {
            newSubgroupType.setCompanyIdAndDescription(iKeyValuePair.getDescription());
        }
        if (iKeyValuePair1 != null) {
            newSubgroupType.setGroupTypeName(iKeyValuePair1.getDescription());
        }
        return newSubgroupType;
    }

    /**
     *
     * @param newSubGroupType
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public SubGroupType createSubGroup(AddSubGroupType newSubGroupType, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {

        SubGroupType duplicateSubGroup =
                subGroupTypeRepository.findByCompanyIdAndLanguageIdAndGroupTypeIdAndSubGroupTypeIdAndStatusIdAndDeletionIndicator(
                        newSubGroupType.getCompanyId(),
                        newSubGroupType.getLanguageId(),
                        newSubGroupType.getGroupTypeId(),
                        newSubGroupType.getSubGroupTypeId(),
                        0L,
                        0L);

        if (duplicateSubGroup != null) {
            duplicateSubGroup.setStatusId(1L);
            duplicateSubGroup.setValidityDateTo(new Date());
        }
        IKeyValuePair iKeyValuePair =
                companyIdRepository.getCompanyIdAndDescription(newSubGroupType.getCompanyId(), newSubGroupType.getLanguageId());

        IKeyValuePair iKeyValuePair1 =
                controlGroupTypeRepository.getControlGroupTypeIdAndDescription(newSubGroupType.getGroupTypeId(),
                        newSubGroupType.getCompanyId(), newSubGroupType.getLanguageId());

        SubGroupType dbSubGroupType = new SubGroupType();
        BeanUtils.copyProperties(newSubGroupType, dbSubGroupType, CommonUtils.getNullPropertyNames(newSubGroupType));
        // Generate unique sub-group-type ID
        Long NUM_RAN_CODE = 2L;
        String NUM_RAN_OBJ = "CGSUBGROUPTYPE";
        String C_ID = "1000";
        String LANG_ID = "EN";
        Long versionId = subGroupTypeRepository.getVersionNo();
        String SUB_GRP_ID = numberRangeService.getNextNumberRange(NUM_RAN_CODE,NUM_RAN_OBJ,LANG_ID,C_ID);
        log.info("nextVal from NumberRange for SUB_GRP_ID: " + SUB_GRP_ID);
        dbSubGroupType.setSubGroupTypeId(Long.valueOf(SUB_GRP_ID));

        if (iKeyValuePair != null && iKeyValuePair1 != null) {
            dbSubGroupType.setCompanyIdAndDescription(iKeyValuePair.getDescription());
            dbSubGroupType.setGroupTypeName(iKeyValuePair1.getDescription());
        } else {
            throw new RuntimeException("The given values companyId " + newSubGroupType.getCompanyId() +
                    " controlGroupTypeId " + newSubGroupType.getGroupTypeId() +
                    " languageId " + newSubGroupType.getLanguageId() + " doesn't exists");
        }

        if(versionId != null){
            dbSubGroupType.setVersionNumber(versionId);
        }else {
            dbSubGroupType.setVersionNumber(1L);
        }

        dbSubGroupType.setDeletionIndicator(0L);
        dbSubGroupType.setStatusId(0L);
        dbSubGroupType.setValidityDateFrom(new Date());
        dbSubGroupType.setValidityDateTo(null);
        dbSubGroupType.setCreatedBy(loginUserID);
        dbSubGroupType.setUpdatedBy(loginUserID);
        dbSubGroupType.setCreatedOn(new Date());
        dbSubGroupType.setUpdatedOn(new Date());
        return subGroupTypeRepository.save(dbSubGroupType);
    }


    /**
     * @param subGroupTypeId
     * @param groupTypeId
     * @param companyId
     * @param languageId
     * @param loginUserID
     * @param versionNumber
     * @param updateSubGroupType
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public SubGroupType updateSubGroup(Long subGroupTypeId, Long groupTypeId, String companyId, String languageId,
                                       String loginUserID, Long versionNumber, UpdateSubGroupType updateSubGroupType)
            throws IllegalAccessException, InvocationTargetException {
        SubGroupType dbSubGroupType = getSubGroupType(subGroupTypeId, companyId, groupTypeId, versionNumber, languageId);
        BeanUtils.copyProperties(updateSubGroupType, dbSubGroupType, CommonUtils.getNullPropertyNames(updateSubGroupType));

        if (updateSubGroupType.getStatusId() != 0) {
            dbSubGroupType.setValidityDateTo(new Date());
        }
        dbSubGroupType.setDeletionIndicator(0L);
        dbSubGroupType.setUpdatedBy(loginUserID);
        dbSubGroupType.setUpdatedOn(new Date());
        log.info("Updated SubGroup: " + dbSubGroupType);
        return subGroupTypeRepository.save(dbSubGroupType);
    }

    /**
     * @param subGroupIdTypeId
     * @param groupTypeId
     * @param companyId
     * @param languageId
     * @param versionNumber
     * @param loginUserID
     */
    public void deleteSubGroup(Long subGroupIdTypeId, Long groupTypeId, String companyId,
                               String languageId, Long versionNumber, String loginUserID) {

        SubGroupType dbSubGroupType = getSubGroupType(subGroupIdTypeId, companyId, groupTypeId, versionNumber, languageId);
        if (dbSubGroupType != null) {
            dbSubGroupType.setDeletionIndicator(1L);
            dbSubGroupType.setUpdatedBy(loginUserID);
            dbSubGroupType.setUpdatedOn(new Date());
            subGroupTypeRepository.save(dbSubGroupType);
        } else {
            throw new EntityNotFoundException("Error in deleting Id: " + subGroupIdTypeId);
        }
    }

    /**
     * @param findSubGroupType
     * @return
     * @throws ParseException
     */
    public List<SubGroupType> findSubGroupType(FindSubGroupType findSubGroupType)
            throws ParseException {

        if (findSubGroupType.getStartCreatedOn() != null && findSubGroupType.getStartCreatedOn() != null) {
            Date date = DateUtils.convertStringToYYYYMMDD(findSubGroupType.getStartCreatedOn());
            Date date1 = DateUtils.convertStringToYYYYMMDD(findSubGroupType.getEndCreatedOn());

            Date[] dates = DateUtils.addTimeToDatesForSearch(date, date1);
            findSubGroupType.setFromDate(dates[0]);
            findSubGroupType.setToDate(dates[1]);
        }
        SubGroupSpecification spec = new SubGroupSpecification(findSubGroupType);
        List<SubGroupType> results = subGroupTypeRepository.findAll(spec);
        results = results.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        log.info("results: " + results);
        List<SubGroupType> newCity = new ArrayList<>();
        for (SubGroupType dbSubGroupType : results) {
            IKeyValuePair iKeyValuePair =
                    controlGroupTypeRepository.getControlGroupTypeIdAndDescription(
                            dbSubGroupType.getGroupTypeId(), dbSubGroupType.getCompanyId(), dbSubGroupType.getLanguageId());

            IKeyValuePair iKeyValuePair2 =
                    companyIdRepository.getCompanyIdAndDescription(dbSubGroupType.getCompanyId(), dbSubGroupType.getLanguageId());

            if (iKeyValuePair != null) {
                dbSubGroupType.setGroupTypeName(iKeyValuePair.getDescription());
            }
            if (iKeyValuePair2 != null) {
                dbSubGroupType.setCompanyIdAndDescription(iKeyValuePair2.getDescription());
            }
            newCity.add(dbSubGroupType);
        }
        return newCity;
    }
}
