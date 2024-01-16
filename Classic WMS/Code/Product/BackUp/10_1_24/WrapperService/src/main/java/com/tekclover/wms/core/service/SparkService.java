package com.tekclover.wms.core.service;

import com.tekclover.wms.core.config.PropertiesConfig;
import com.tekclover.wms.core.model.idmaster.FindUserManagement;
import com.tekclover.wms.core.model.spark.FindStagingLine;
import com.tekclover.wms.core.model.spark.FindStorageBin;
import com.tekclover.wms.core.model.spark.StagingLine;
import com.tekclover.wms.core.model.spark.StorageBin;
import com.tekclover.wms.core.model.user.UserManagement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;

@Slf4j
@Service
public class SparkService {

    @Autowired
    PropertiesConfig propertiesConfig;

    private RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }

//    public StorageBin[] findStorageBin(FindStorageBin findStorageBin    , String authToken){
//        try{
//            HttpHeaders headers = new HttpHeaders();
//            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//            headers.add("User-Agent", "RestTemplate");
//            headers.add("Authorization", "Bearer " + authToken);
//            UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"/storagebin/findStorageBin");
//            HttpEntity<?> entity = new HttpEntity<>(findStorageBin,headers);
//            ResponseEntity<StorageBin[]> result =
//                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, StorageBin[].class);
//            log.info("result : " + result.getStatusCode());
//            return result.getBody();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e;
//        }
//    }


//    public StagingLine[] findStagingLine(FindStagingLine findStagingLine, String authToken){
//        try{
//            HttpHeaders headers = new HttpHeaders();
//            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//            headers.add("User-Agent", "RestTemplate");
//            headers.add("Authorization", "Bearer " + authToken);
//            UriComponentsBuilder builder=UriComponentsBuilder.fromHttpUrl(getIDMasterServiceApiUrl() +"/stagingline");
//            HttpEntity<?> entity = new HttpEntity<>(findStagingLine,headers);
//            ResponseEntity<StagingLine[]> result =
//                    getRestTemplate().exchange(builder.toUriString(), HttpMethod.POST, entity, StagingLine[].class);
//            log.info("result : " + result.getStatusCode());
//            return result.getBody();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e;
//        }
//    }
}
