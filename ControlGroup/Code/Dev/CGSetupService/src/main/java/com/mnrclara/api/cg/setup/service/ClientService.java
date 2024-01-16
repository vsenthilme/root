package com.mnrclara.api.cg.setup.service;


import com.mnrclara.api.cg.setup.exception.BadRequestException;
import com.mnrclara.api.cg.setup.model.IKeyValuePair;
import com.mnrclara.api.cg.setup.model.auth.AuthToken;
import com.mnrclara.api.cg.setup.model.client.AddClient;
import com.mnrclara.api.cg.setup.model.client.Client;
import com.mnrclara.api.cg.setup.model.client.FindClient;
import com.mnrclara.api.cg.setup.model.client.UpdateClient;
import com.mnrclara.api.cg.setup.repository.ClientRepository;
import com.mnrclara.api.cg.setup.repository.CompanyIdRepository;
import com.mnrclara.api.cg.setup.repository.specification.ClientSpecification;
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
public class ClientService {
    @Autowired
    private CompanyIdRepository companyIdRepository;

    @Autowired
    private NumberRangeService numberRangeService;

    @Autowired
    private SetupService setupService;

    @Autowired
    ClientRepository clientRepository;

    /**
     * getAllClientId
     *
     * @return
     */
    public List<Client> getAllClientId() {
        List<Client> clientList = clientRepository.findAll();
        clientList = clientList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        List<Client> newClientList = new ArrayList<>();
        for (Client dbClient : clientList) {
            IKeyValuePair iKeyValuePair =
                    companyIdRepository.getCompanyIdAndDescription(dbClient.getCompanyId(), dbClient.getLanguageId());

            if (iKeyValuePair != null) {
                dbClient.setCompanyIdAndDescription(iKeyValuePair.getDescription());
            }
            newClientList.add(dbClient);
        }

        return newClientList;
    }

    /**
     * @param companyId
     * @param languageId
     * @param clientId
     * @return
     */
    public Client getClientId(String companyId, String languageId, Long clientId) {

        Optional<Client> dbClient = clientRepository.findByCompanyIdAndLanguageIdAndClientIdAndDeletionIndicator(
                companyId, languageId, clientId, 0L);

        if (dbClient.isEmpty()) {
            throw new BadRequestException("The given values : " +
                    " companyCodeId - " + companyId +
                    " languageId - " + languageId +
                    " clientId - " + clientId +
                    " doesn't exist.");

        }
        Client newClient = new Client();
        BeanUtils.copyProperties(dbClient.get(), newClient, CommonUtils.getNullPropertyNames(dbClient));
        IKeyValuePair iKeyValuePair = companyIdRepository.getCompanyIdAndDescription(companyId, languageId);

        if (iKeyValuePair != null) {
            newClient.setCompanyIdAndDescription(iKeyValuePair.getDescription());
        }
        return newClient;
    }

    /**
     * @param searchClientId
     * @return
     * @throws ParseException
     */
    public List<Client> findClientId(FindClient searchClientId)
            throws ParseException {

        if (searchClientId.getStartCreatedOn() != null && searchClientId.getStartCreatedOn() != null) {
            Date date = DateUtils.convertStringToYYYYMMDD(searchClientId.getStartCreatedOn());
            Date date1 = DateUtils.convertStringToYYYYMMDD(searchClientId.getEndCreatedOn());

            Date[] dates = DateUtils.addTimeToDatesForSearch(date, date1);
            searchClientId.setFromDate(dates[0]);
            searchClientId.setToDate(dates[1]);
        }

        ClientSpecification spec = new ClientSpecification(searchClientId);
        List<Client> results = clientRepository.findAll(spec);
        results = results.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        log.info("results: " + results);

        List<Client> newClient = new ArrayList<>();
        for (Client dbClient : results) {
            IKeyValuePair iKeyValuePair =
                    companyIdRepository.getCompanyIdAndDescription(dbClient.getCompanyId(), dbClient.getLanguageId());

            if (iKeyValuePair != null) {
                dbClient.setCompanyIdAndDescription(iKeyValuePair.getDescription());
            }
            newClient.add(dbClient);
        }
        return newClient;
    }


    /**
     * @param newClient
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public Client createClient(AddClient newClient, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {

        Optional<Client> duplicateClient =
                clientRepository.findByCompanyIdAndLanguageIdAndClientIdAndDeletionIndicator(
                        newClient.getCompanyId(),
                        newClient.getLanguageId(),
                        newClient.getClientId(),
                        0L);

        if (!duplicateClient.isEmpty()) {
            throw new EntityNotFoundException("Record is Getting Duplicated");
        } else {

            IKeyValuePair iKeyValuePair =
                    companyIdRepository.getCompanyIdAndDescription(newClient.getCompanyId(), newClient.getLanguageId());

            Client dbClient = new Client();
            BeanUtils.copyProperties(newClient, dbClient, CommonUtils.getNullPropertyNames(newClient));

            if (iKeyValuePair != null) {
                dbClient.setCompanyIdAndDescription(iKeyValuePair.getDescription());
            } else {
                throw new RuntimeException("The given values of companyId -"
                        + newClient.getCompanyId() + " LanguageId -"
                        + newClient.getLanguageId() + " ClientId - "
                        + newClient.getClientId() + " doesn't exists");
            }
            Long NUM_RAN_CODE = 4L;
            String NUM_RAN_OBJ = "CGCLIENT";
            String C_ID = "1000";
            String LANG_ID = "EN";
            String CLIENT_ID = numberRangeService.getNextNumberRange(NUM_RAN_CODE,NUM_RAN_OBJ,LANG_ID,C_ID);
            log.info("nextVal from NumberRange for CLIENT_ID: " + CLIENT_ID);
            dbClient.setClientId(Long.valueOf(CLIENT_ID));

            log.info("newClient : " + newClient);
            dbClient.setDeletionIndicator(0L);
            dbClient.setCreatedBy(loginUserID);
            dbClient.setUpdatedBy(loginUserID);
            dbClient.setCreatedOn(new Date());
            dbClient.setUpdatedOn(new Date());
            return clientRepository.save(dbClient);
        }
    }

    /**
     * @param companyId
     * @param languageId
     * @param clientId
     * @param loginUserID
     * @param updateClient
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public Client updateClient(String companyId, String languageId, Long clientId, String loginUserID,
                               UpdateClient updateClient)
            throws IllegalAccessException, InvocationTargetException {
        Client dbClient = getClientId(companyId, languageId, clientId);
        BeanUtils.copyProperties(updateClient, dbClient, CommonUtils.getNullPropertyNames(updateClient));
        dbClient.setUpdatedBy(loginUserID);
        dbClient.setUpdatedOn(new Date());

        return clientRepository.save(dbClient);
    }

    /**
     * @param companyId
     * @param languageId
     * @param clientId
     * @param loginUserID
     */
    public void deleteClient(String companyId, String languageId, Long clientId, String loginUserID) {
        Client dbClient = getClientId(companyId, languageId, clientId);
        if (dbClient != null) {
            dbClient.setDeletionIndicator(1L);
            dbClient.setUpdatedBy(loginUserID);
            dbClient.setUpdatedOn(new Date());
            clientRepository.save(dbClient);
        } else {
            throw new EntityNotFoundException("Error in deleting Id: " + clientId);
        }
    }
}
