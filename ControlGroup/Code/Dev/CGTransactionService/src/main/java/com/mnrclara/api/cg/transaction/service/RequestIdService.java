package com.mnrclara.api.cg.transaction.service;

import com.mnrclara.api.cg.transaction.exception.BadRequestException;
import com.mnrclara.api.cg.transaction.model.Requestid.AddRequestId;
import com.mnrclara.api.cg.transaction.model.Requestid.FindRequestId;
import com.mnrclara.api.cg.transaction.model.Requestid.RequestId;
import com.mnrclara.api.cg.transaction.model.Requestid.UpdateRequestId;
import com.mnrclara.api.cg.transaction.repository.RequestIdRepository;
import com.mnrclara.api.cg.transaction.repository.specification.RequestIdSpecification;
import com.mnrclara.api.cg.transaction.util.CommonUtils;
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
import java.util.stream.Collectors;


@Slf4j
@Service
public class RequestIdService {

    @Autowired
    RequestIdRepository requestIdRepository;


    /**
     * getLanguageIds
     *
     * @return
     */
    public List<RequestId> getRequestIdAll() {
        List<RequestId> dbRequestId = requestIdRepository.findAll();
        dbRequestId = dbRequestId.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        return dbRequestId;
    }

    /**
     * @param requestId
     * @return
     */
    public List<RequestId> getRequestId(Long requestId) {
        List<RequestId> dbRequestId = requestIdRepository.findByRequestIdAndDeletionIndicator(requestId, 0L);

        if (dbRequestId == null || dbRequestId.isEmpty()) {
            throw new BadRequestException("RequestId with ID: " + requestId + " doesn't exist");
        }

        return dbRequestId;
    }


    /**
     * @param requestIds
     * @param loginUserID
     * @return
     */
    public List<RequestId> createRequestId(List<AddRequestId> requestIds, String loginUserID) {
        List<RequestId> savedRequestIds = new ArrayList<>();
        for (AddRequestId requestId : requestIds) {
            RequestId dbRequestId = new RequestId();
            log.info("newRequestId: " + requestId);
            BeanUtils.copyProperties(requestId, dbRequestId, CommonUtils.getNullPropertyNames(requestId));
            dbRequestId.setUploadedBy(loginUserID);
            dbRequestId.setReUploadedBy(loginUserID);
            dbRequestId.setUploadedOn(new Date());
            dbRequestId.setReUploadedOn(new Date());
            savedRequestIds.add(requestIdRepository.save(dbRequestId));
        }
        return savedRequestIds;
    }

    /**
     * @param requestId
     * @param loginUserID
     * @param updateRequestIdList
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public List<RequestId> updateRequestId(Long requestId, String loginUserID, List<AddRequestId> updateRequestIdList)
            throws IllegalAccessException, InvocationTargetException {
        List<RequestId> requestIds = requestIdRepository.findByRequestIdAndDeletionIndicator(requestId, 0L);
        List<RequestId> requestIdList = new ArrayList<>();
        if (requestIds == null || requestIds.isEmpty()) {
            throw new BadRequestException("RequestIds with ID: " + requestId + " doesn't exist");
        } else {
//        if (requestIds != null) {
            for (RequestId requestId1 : requestIds) {
                for (AddRequestId updateRequestId : updateRequestIdList) {
                    if (updateRequestId.getId().equals(requestId1.getId())) {
                        BeanUtils.copyProperties(updateRequestId, requestId1, CommonUtils.getNullPropertyNames(updateRequestId));
                        requestId1.setReUploadedBy(loginUserID);
                        requestId1.setDeletionIndicator(0L);
                        requestId1.setReUploadedOn(new Date());
                        requestIdList.add(requestIdRepository.save(requestId1));
                    } else {
                        List<RequestId> createRequest = createRequestId(updateRequestIdList, loginUserID);
                        return createRequest;
                    }
                }
            }
        }
        return requestIdList;
    }

    /**
     * @param requestId
     * @param loginUserID
     */
    public void deleteRequestId(Long id, Long requestId, String loginUserID) {
        RequestId request = requestIdRepository.findByIdAndRequestIdAndDeletionIndicator(id, requestId, 0L);

        if (request == null) {
            throw new EntityNotFoundException("Request with ID: " + id + "RequestId " + requestId + " doesn't exist");
        }
        request.setDeletionIndicator(1L);
        request.setUploadedBy(loginUserID);
        request.setUploadedOn(new Date());
        requestIdRepository.save(request);
    }


    /**
     * @param findRequestId
     * @return
     * @throws ParseException
     */
    public List<RequestId> findRequestId(FindRequestId findRequestId) throws ParseException {

        RequestIdSpecification spec = new RequestIdSpecification(findRequestId);
        List<RequestId> results = requestIdRepository.findAll(spec);
        results = results.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        log.info("results: " + results);
        return results;
    }


}
