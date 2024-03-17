package com.mnrclara.api.accounting.service;

import com.mnrclara.api.accounting.config.PropertiesConfig;
import com.mnrclara.api.accounting.model.prebill.NotificationSave;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CRMService {

    @Autowired
    PropertiesConfig propertiesConfig;

    private RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }

    private String getCRMServiceApiUrl() {
        return propertiesConfig.getCrmServiceUrl();
    }

    /**
     * 
     * @param topic
     * @param message
     * @param userId
     * @param userType
     * @param createdOn
     * @param createdBy
     * @param authToken
     */
    public void setNotificationMessage(String topic, String message, List<String> userId,
                                       String userType, Date createdOn, String createdBy, String authToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("User-Agent", "MNRClara's RestTemplate");
            headers.add("Authorization", "Bearer " + authToken);

            NotificationSave notificationSave = new NotificationSave();
            notificationSave.setMessage(message);
            notificationSave.setTopic(topic);
            notificationSave.setUserId(userId);
            notificationSave.setUserType(userType);
            notificationSave.setCreatedOn(createdOn);
            notificationSave.setCreatedBy(createdBy);

            HttpEntity<?> entity = new HttpEntity<>(notificationSave,headers);

            UriComponents builder =
                    UriComponentsBuilder.fromHttpUrl(getCRMServiceApiUrl() + "notification-message/create").build();

            getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST,entity, Optional.class);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Error in sending notification to CRMService :" + e.toString());
//            throw e;
        }
    }
}
