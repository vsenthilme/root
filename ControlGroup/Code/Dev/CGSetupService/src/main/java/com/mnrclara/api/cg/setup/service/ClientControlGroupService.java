package com.mnrclara.api.cg.setup.service;


import com.mnrclara.api.cg.setup.exception.BadRequestException;
import com.mnrclara.api.cg.setup.model.IKeyValuePair;
import com.mnrclara.api.cg.setup.model.clientcontrolgroup.AddClientControlGroup;
import com.mnrclara.api.cg.setup.model.clientcontrolgroup.ClientControlGroup;
import com.mnrclara.api.cg.setup.model.clientcontrolgroup.FindClientControlGroup;
import com.mnrclara.api.cg.setup.model.clientcontrolgroup.UpdateClientControlGroup;
import com.mnrclara.api.cg.setup.repository.ClientControlGroupRepository;
import com.mnrclara.api.cg.setup.repository.ClientRepository;
import com.mnrclara.api.cg.setup.repository.CompanyIdRepository;
import com.mnrclara.api.cg.setup.repository.ControlGroupRepository;
import com.mnrclara.api.cg.setup.repository.specification.ClientControlGroupSpecification;
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
public class ClientControlGroupService {
    @Autowired
    private ControlGroupRepository controlGroupRepository;
    @Autowired
    private ClientControlGroupRepository clientControlGroupRepository;

    @Autowired
    private CompanyIdRepository companyIdRepository;

    @Autowired
    ClientRepository clientRepository;

    /**
     * getAllClientControlGroup
     *
     * @return
     */
    public List<ClientControlGroup> getAllClientControlGroup() {
        List<ClientControlGroup> clientControlGroupList = clientControlGroupRepository.findAll();
        clientControlGroupList = clientControlGroupList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        List<ClientControlGroup> controlGroupList = new ArrayList<>();

        for (ClientControlGroup clientControlGroup : clientControlGroupList) {

            IKeyValuePair iKeyValuePair =
                    clientControlGroupRepository.getDescription(clientControlGroup.getCompanyId(),
                            clientControlGroup.getLanguageId(), clientControlGroup.getClientId(),
                            clientControlGroup.getGroupTypeId(), clientControlGroup.getSubGroupTypeId());

            if (iKeyValuePair != null) {
                clientControlGroup.setCompanyIdAndDescription(iKeyValuePair.getCompanyDescription());
                clientControlGroup.setClientName(iKeyValuePair.getClientName());
                clientControlGroup.setSubGroupTypeName(iKeyValuePair.getSubGroupTypeName());
                clientControlGroup.setGroupTypeName(iKeyValuePair.getGroupTypeName());
            }
            controlGroupList.add(clientControlGroup);
        }
        return controlGroupList;
    }


    /**
     * @param companyId
     * @param languageId
     * @param clientId
     * @param subGroupTypeId
     * @param groupTypeId
     * @param versionNumber
     * @return
     */
    public ClientControlGroup getCLientControlGroup(String companyId, String languageId, Long clientId,
                                                    Long subGroupTypeId, Long groupTypeId, Long versionNumber) {

        Optional<ClientControlGroup> dbClientControlGroupId =
                clientControlGroupRepository.findByCompanyIdAndLanguageIdAndClientIdAndSubGroupTypeIdAndGroupTypeIdAndVersionNumberAndDeletionIndicator(
                        companyId, languageId, clientId, subGroupTypeId, groupTypeId, versionNumber, 0L);

        if (dbClientControlGroupId.isEmpty()) {
            throw new BadRequestException("The given values : " +
                    " companyCodeId - " + companyId +
                    " languageId - " + languageId +
                    " clientId - " + clientId +
                    " subGroupTypeId - " + subGroupTypeId +
                    " groupTypeId - " + groupTypeId +
                    " versionNumber - " + versionNumber +
                    " doesn't exist.");

        } else {
            ClientControlGroup newClientControlGroupId = new ClientControlGroup();
            BeanUtils.copyProperties(dbClientControlGroupId.get(), newClientControlGroupId, CommonUtils.getNullPropertyNames(dbClientControlGroupId));

            IKeyValuePair iKeyValuePair =
                    clientControlGroupRepository.getDescription(companyId, languageId, clientId, groupTypeId, subGroupTypeId);
            if (iKeyValuePair != null) {
                newClientControlGroupId.setCompanyIdAndDescription(iKeyValuePair.getCompanyDescription());
                newClientControlGroupId.setClientName(iKeyValuePair.getClientName());
                newClientControlGroupId.setSubGroupTypeName(iKeyValuePair.getSubGroupTypeName());
                newClientControlGroupId.setGroupTypeName(iKeyValuePair.getGroupTypeName());
            }
            return newClientControlGroupId;
        }
    }


    /**
     * @param searchClientControlGroup
     * @return
     * @throws ParseException
     */
    public List<ClientControlGroup> findClientControlGroup(FindClientControlGroup searchClientControlGroup)
            throws ParseException {


        if (searchClientControlGroup.getStartCreatedOn() != null && searchClientControlGroup.getStartCreatedOn() != null) {
            Date date = DateUtils.convertStringToYYYYMMDD(searchClientControlGroup.getStartCreatedOn());
            Date date1 = DateUtils.convertStringToYYYYMMDD(searchClientControlGroup.getEndCreatedOn());

            Date[] dates = DateUtils.addTimeToDatesForSearch(date, date1);
            searchClientControlGroup.setFromDate(dates[0]);
            searchClientControlGroup.setToDate(dates[1]);
        }

        ClientControlGroupSpecification spec = new ClientControlGroupSpecification(searchClientControlGroup);
        List<ClientControlGroup> results = clientControlGroupRepository.findAll(spec);
        log.info("results: " + results);
        List<ClientControlGroup> clientControlGroupList = new ArrayList<>();

        for (ClientControlGroup clientControlGroup : results) {

            IKeyValuePair iKeyValuePair = clientControlGroupRepository.getDescription(
                    clientControlGroup.getCompanyId(), clientControlGroup.getLanguageId(), clientControlGroup.getClientId(),
                    clientControlGroup.getGroupTypeId(), clientControlGroup.getSubGroupTypeId());

            if (iKeyValuePair != null) {
                clientControlGroup.setCompanyIdAndDescription(iKeyValuePair.getCompanyDescription());
                clientControlGroup.setClientName(iKeyValuePair.getClientName());
                clientControlGroup.setSubGroupTypeName(iKeyValuePair.getSubGroupTypeName());
                clientControlGroup.setGroupTypeName(iKeyValuePair.getGroupTypeName());
            }
            clientControlGroupList.add(clientControlGroup);
        }
        return clientControlGroupList;
    }

    /**
     * @param newClientControlGroup
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public ClientControlGroup createClientControlGroup(AddClientControlGroup newClientControlGroup, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {

        ClientControlGroup duplicateClientControlGroup =
                clientControlGroupRepository.findByCompanyIdAndLanguageIdAndClientIdAndSubGroupTypeIdAndGroupTypeIdAndStatusIdAndDeletionIndicator(
                        newClientControlGroup.getCompanyId(),
                        newClientControlGroup.getLanguageId(),
                        newClientControlGroup.getClientId(),
                        newClientControlGroup.getSubGroupTypeId(),
                        newClientControlGroup.getGroupTypeId(),
                        0L,
                        0L);

        if (duplicateClientControlGroup != null) {
            duplicateClientControlGroup.setStatusId(1L);
            duplicateClientControlGroup.setValidityDateTo(new Date());
        }
        IKeyValuePair iKeyValuePair = clientControlGroupRepository.getDescription(
                newClientControlGroup.getCompanyId(), newClientControlGroup.getLanguageId(),
                newClientControlGroup.getClientId(), newClientControlGroup.getGroupTypeId(),
                newClientControlGroup.getSubGroupTypeId());

        ClientControlGroup dbClientControlGroup = new ClientControlGroup();
        BeanUtils.copyProperties(newClientControlGroup, dbClientControlGroup, CommonUtils.getNullPropertyNames(newClientControlGroup));

        if (iKeyValuePair != null) {
            dbClientControlGroup.setCompanyIdAndDescription(iKeyValuePair.getCompanyDescription());
            dbClientControlGroup.setClientName(iKeyValuePair.getClientName());
            dbClientControlGroup.setSubGroupTypeName(iKeyValuePair.getSubGroupTypeName());
            dbClientControlGroup.setGroupTypeName(iKeyValuePair.getGroupTypeName());
        } else {
            throw new EntityNotFoundException("The Given values of companyId - "
                    + newClientControlGroup.getCompanyId() + " languageId - "
                    + newClientControlGroup.getLanguageId() + " clientId - "
                    + newClientControlGroup.getClientId() + " subGroupTypeId - "
                    + newClientControlGroup.getSubGroupTypeId() + " groupTypeId - "
                    + newClientControlGroup.getGroupTypeId() + " doesn't exists ");
        }

        Long versionNo = clientControlGroupRepository.getVersionNo();
        if(versionNo != null){
            dbClientControlGroup.setVersionNumber(versionNo);
        }else {
            dbClientControlGroup.setVersionNumber(1L);
        }

        log.info("newClientControlGroupId : " + newClientControlGroup);
        dbClientControlGroup.setDeletionIndicator(0L);
        dbClientControlGroup.setStatusId(0L);
        dbClientControlGroup.setValidityDateFrom(new Date());
        dbClientControlGroup.setValidityDateTo(null);
        dbClientControlGroup.setCreatedBy(loginUserID);
        dbClientControlGroup.setUpdatedBy(loginUserID);
        dbClientControlGroup.setCreatedOn(new Date());
        dbClientControlGroup.setUpdatedOn(new Date());
        return clientControlGroupRepository.save(dbClientControlGroup);
    }

    /**
     * @param companyId
     * @param languageId
     * @param clientId
     * @param versionNumber
     * @param subGroupTypeId
     * @param groupTypeId
     * @param loginUserID
     * @param updateClientControlGroup
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public ClientControlGroup updateClientControlGroup(String companyId, String languageId, Long clientId, Long versionNumber,
                                                       Long subGroupTypeId, Long groupTypeId, String loginUserID,
                                                       UpdateClientControlGroup updateClientControlGroup)
            throws IllegalAccessException, InvocationTargetException {
        ClientControlGroup dbClientControlGroup =
                getCLientControlGroup(companyId, languageId, clientId, subGroupTypeId, groupTypeId, versionNumber);
        BeanUtils.copyProperties(updateClientControlGroup, dbClientControlGroup, CommonUtils.getNullPropertyNames(updateClientControlGroup));

        if (updateClientControlGroup.getStatusId() != 0) {
            dbClientControlGroup.setValidityDateTo(new Date());
        }
        dbClientControlGroup.setUpdatedBy(loginUserID);
        dbClientControlGroup.setUpdatedOn(new Date());
        return clientControlGroupRepository.save(dbClientControlGroup);
    }

    /**
     * @param companyId
     * @param languageId
     * @param clientId
     * @param versionNumber
     * @param subGroupTypeId
     * @param groupTypeId
     * @param loginUserID
     */
    public void deleteClientControlGroup(String companyId, String languageId, Long clientId, Long versionNumber,
                                         Long subGroupTypeId, Long groupTypeId, String loginUserID) {
        ClientControlGroup dbClientControlGroup =
                getCLientControlGroup(companyId, languageId, clientId, subGroupTypeId, groupTypeId, versionNumber);

        if (dbClientControlGroup != null) {
            dbClientControlGroup.setDeletionIndicator(1L);
            dbClientControlGroup.setUpdatedBy(loginUserID);
            clientControlGroupRepository.save(dbClientControlGroup);
        } else {
            throw new EntityNotFoundException("Error in deleting Id: " + clientId);
        }
    }

}
