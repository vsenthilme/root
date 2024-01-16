package com.mnrclara.api.cg.setup.service;

import com.mnrclara.api.cg.setup.exception.BadRequestException;
import com.mnrclara.api.cg.setup.model.IKeyValuePair;
import com.mnrclara.api.cg.setup.model.clientstore.AddClientStore;
import com.mnrclara.api.cg.setup.model.clientstore.ClientStore;
import com.mnrclara.api.cg.setup.model.clientstore.FindClientStore;
import com.mnrclara.api.cg.setup.model.clientstore.UpdateClientStore;
import com.mnrclara.api.cg.setup.repository.ClientRepository;
import com.mnrclara.api.cg.setup.repository.ClientStoreRepository;
import com.mnrclara.api.cg.setup.repository.StoreIdRepository;
import com.mnrclara.api.cg.setup.repository.specification.ClientStoreSpecification;
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
public class ClientStoreService {
    @Autowired
    private ClientStoreRepository clientStoreRepository;
    @Autowired
    private StoreIdRepository storeIdRepository;
    @Autowired
    private ClientRepository clientRepository;

    //Get All
    /**
     * @return
     */
    public List<ClientStore> getAllClientStore() {
        List<ClientStore> clientStoreList = clientStoreRepository.findAll();
        clientStoreList = clientStoreList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        List<ClientStore> newClientStore = new ArrayList<>();
        for (ClientStore clientStore : clientStoreList) {
            IKeyValuePair iKeyValuePair = storeIdRepository.getStoreIdAndDescription(
                    clientStore.getStoreId(), clientStore.getCompanyId(), clientStore.getLanguageId());
            IKeyValuePair iKeyValuePair1 = clientRepository.getClientIdAndDescription(
                    clientStore.getClientId(), clientStore.getCompanyId(), clientStore.getLanguageId());

            if (iKeyValuePair != null && iKeyValuePair1 != null) {
                clientStore.setStoreName(iKeyValuePair.getDescription());
                clientStore.setClientName(iKeyValuePair1.getDescription());
            }
            newClientStore.add(clientStore);
        }
        return newClientStore;
    }

    //Get ClientStore
    /**
     * @param clientId
     * @param storeId
     * @param companyId
     * @param languageId
     * @param versionNumber
     * @return
     */
    public ClientStore getClientStore(Long clientId, Long storeId, String companyId, String languageId, Long versionNumber) {
        log.info("client Id: " + clientId);
        Optional<ClientStore> dbClientStore =
                clientStoreRepository.findByClientIdAndStoreIdAndCompanyIdAndLanguageIdAndVersionNumberAndDeletionIndicator(
                        clientId, storeId, companyId, languageId, versionNumber, 0L);
        if (dbClientStore.isEmpty()) {
            throw new BadRequestException("The given values of clientId " + clientId + " storeId " + storeId +
                    " companyId " + companyId + " languageId " + languageId +
                    " versionNumber " + versionNumber + " doesn't exists");
        }
        ClientStore newClientStore = new ClientStore();
        BeanUtils.copyProperties(dbClientStore.get(), newClientStore, CommonUtils.getNullPropertyNames(dbClientStore));

        IKeyValuePair iKeyValuePair =
                storeIdRepository.getStoreIdAndDescription(storeId, companyId, languageId);
        IKeyValuePair iKeyValuePair1 =
                clientRepository.getClientIdAndDescription(clientId, companyId, languageId);

        if (iKeyValuePair != null && iKeyValuePair1 != null) {
            newClientStore.setStoreName(iKeyValuePair.getDescription());
            newClientStore.setClientName(iKeyValuePair1.getDescription());
        }
        return newClientStore;
    }

    //CREATE ClientStore
    /**
     * @param newClientStore
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public ClientStore createClientStore(AddClientStore newClientStore, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        ClientStore duplicateClientStore =
                clientStoreRepository.findByClientIdAndStoreIdAndCompanyIdAndLanguageIdAndStatusIdAndDeletionIndicator(
                        newClientStore.getClientId(), newClientStore.getStoreId(),
                        newClientStore.getCompanyId(), newClientStore.getLanguageId(),
                        0L, 0L);

        if (duplicateClientStore != null) {
            duplicateClientStore.setStatusId(1L);
            duplicateClientStore.setValidityDateTo(new Date());
        }
        IKeyValuePair iKeyValuePair =
                storeIdRepository.getStoreIdAndDescription(newClientStore.getStoreId(),
                        newClientStore.getCompanyId(), newClientStore.getLanguageId());
        IKeyValuePair iKeyValuePair1 =
                clientRepository.getClientIdAndDescription(newClientStore.getClientId(),
                        newClientStore.getCompanyId(), newClientStore.getLanguageId());

        ClientStore dbClientStore = new ClientStore();
        BeanUtils.copyProperties(newClientStore, dbClientStore, CommonUtils.getNullPropertyNames(newClientStore));

        if (iKeyValuePair != null && iKeyValuePair1 != null) {
            dbClientStore.setStoreName(iKeyValuePair.getDescription());
            dbClientStore.setClientName(iKeyValuePair1.getDescription());
        } else {
            throw new RuntimeException("The given values of clientId " + newClientStore.getClientId() +
                    " storeId " + newClientStore.getStoreId() +
                    " companyId " + newClientStore.getCompanyId() +
                    " languageId " + newClientStore.getLanguageId() + " doesn't exists ");
        }
        Long versionId = clientStoreRepository.versionId();
        if(versionId != null){
            dbClientStore.setVersionNumber(versionId);
        }else {
            dbClientStore.setVersionNumber(1L);
        }

        dbClientStore.setDeletionIndicator(0L);
        dbClientStore.setStatusId(0L);
        dbClientStore.setValidityDateFrom(new Date());
        dbClientStore.setValidityDateTo(null);
        dbClientStore.setCreatedBy(loginUserID);
        dbClientStore.setCreatedOn(new Date());
        dbClientStore.setUpdatedBy(loginUserID);
        dbClientStore.setUpdatedOn(new Date());
        return clientStoreRepository.save(dbClientStore);
    }


    //UPDATE ClientStore
    /**
     * @param clientId
     * @param storeId
     * @param companyId
     * @param languageId
     * @param versionNumber
     * @param loginUserID
     * @param updateClientStore
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public ClientStore updateClientStore(Long clientId, Long storeId, String companyId, String languageId,
                                         Long versionNumber, String loginUserID, UpdateClientStore updateClientStore)
            throws IllegalAccessException, InvocationTargetException {
        ClientStore dbClientStore = getClientStore(clientId, storeId, companyId, languageId, versionNumber);
        BeanUtils.copyProperties(updateClientStore, dbClientStore, CommonUtils.getNullPropertyNames(updateClientStore));

        if (updateClientStore.getStatusId() != 0) {
            dbClientStore.setValidityDateTo(new Date());
        }
        dbClientStore.setDeletionIndicator(0L);
        dbClientStore.setUpdatedBy(loginUserID);
        dbClientStore.setUpdatedOn(new Date());
        return clientStoreRepository.save(dbClientStore);
    }

    //DELETE ClientStore
    /**
     * @param clientId
     * @param storeId
     * @param companyId
     * @param languageId
     * @param versionNumber
     * @param loginUserID
     */
    public void deleteClientStore(Long clientId, Long storeId, String companyId, String languageId,
                                  Long versionNumber, String loginUserID) {
        ClientStore dbClientStore = getClientStore(clientId, storeId, companyId, languageId, versionNumber);
        if (dbClientStore != null) {
            dbClientStore.setDeletionIndicator(1L);
            dbClientStore.setUpdatedBy(loginUserID);
            dbClientStore.setUpdatedOn(new Date());
            clientStoreRepository.save(dbClientStore);
        } else {
            throw new EntityNotFoundException("Error in deleting clientId: " + clientId);
        }
    }

    //FIND ClientStore

    /**
     * @param findClientStore
     * @return
     * @throws ParseException
     */
    public List<ClientStore> findClientStore(FindClientStore findClientStore) throws ParseException {

        if (findClientStore.getStartCreatedOn() != null && findClientStore.getStartCreatedOn() != null) {
            Date date = DateUtils.convertStringToYYYYMMDD(findClientStore.getStartCreatedOn());
            Date date1 = DateUtils.convertStringToYYYYMMDD(findClientStore.getEndCreatedOn());

            Date[] dates = DateUtils.addTimeToDatesForSearch(date, date1);
            findClientStore.setFromDate(dates[0]);
            findClientStore.setToDate(dates[1]);
        }

        ClientStoreSpecification spec = new ClientStoreSpecification(findClientStore);
        List<ClientStore> results = clientStoreRepository.findAll(spec);
        results = results.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        log.info("results: " + results);
        List<ClientStore> newClientStore = new ArrayList<>();
        for (ClientStore dbClientStore : results) {
            IKeyValuePair iKeyValuePair =
                    storeIdRepository.getStoreIdAndDescription(dbClientStore.getStoreId(),
                            dbClientStore.getCompanyId(), dbClientStore.getLanguageId());

            IKeyValuePair iKeyValuePair1 =
                    clientRepository.getClientIdAndDescription(dbClientStore.getClientId(),
                            dbClientStore.getCompanyId(), dbClientStore.getLanguageId());

            if (iKeyValuePair != null && iKeyValuePair1 != null) {
                dbClientStore.setStoreName(iKeyValuePair.getDescription());
                dbClientStore.setClientName(iKeyValuePair1.getDescription());
            }
            newClientStore.add(dbClientStore);
        }
        return newClientStore;
    }
}