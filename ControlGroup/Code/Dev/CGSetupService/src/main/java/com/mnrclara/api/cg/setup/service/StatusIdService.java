package com.mnrclara.api.cg.setup.service;

import com.mnrclara.api.cg.setup.exception.BadRequestException;
import com.mnrclara.api.cg.setup.model.status.AddStatusId;
import com.mnrclara.api.cg.setup.model.status.FindStatus;
import com.mnrclara.api.cg.setup.model.status.StatusId;
import com.mnrclara.api.cg.setup.model.status.UpdateStatusId;
import com.mnrclara.api.cg.setup.repository.StatusIdRepository;
import com.mnrclara.api.cg.setup.repository.specification.StatusSpecification;
import com.mnrclara.api.cg.setup.util.CommonUtils;
import com.mnrclara.api.cg.setup.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StatusIdService {

    @Autowired
    private StatusIdRepository statusIdRepository;

    /**
     * getCompanies
     *
     * @return
     */
    public List<StatusId> getAllStatusId() {
        List<StatusId> statusIdList = statusIdRepository.findAll();
        statusIdList = statusIdList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        return statusIdList;
    }

    /**
     * @param statusId
     * @param languageId
     * @return
     */
    public StatusId getStatusId(Long statusId, String languageId) {
        Optional<StatusId> status = statusIdRepository.findByLanguageIdAndStatusIdAndDeletionIndicator(languageId, statusId, 0L);

        if (status.isEmpty()) {
            throw new BadRequestException("The given Status ID : " + statusId +
                    "LanguageId" + languageId + " doesn't exist.");
        }
        return status.get();
    }

    /**
     * createStatus
     *
     * @param newStatus
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public StatusId createStatusId(AddStatusId newStatus, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {
        Optional<StatusId> status =
                statusIdRepository.findByLanguageIdAndStatusIdAndDeletionIndicator(
                        newStatus.getLanguageId(),
                        newStatus.getStatusId(),
                        0L);
        if (!status.isEmpty()) {
            throw new BadRequestException("Record is getting duplicated with the given values");
        }
        StatusId dbStatusId = new StatusId();
        BeanUtils.copyProperties(newStatus, dbStatusId);
        dbStatusId.setDeletionIndicator(0L);
        dbStatusId.setCreatedBy(loginUserID);
        dbStatusId.setUpdatedBy(loginUserID);
        dbStatusId.setCreatedOn(new Date());
        dbStatusId.setUpdatedOn(new Date());
        return statusIdRepository.save(dbStatusId);
    }

    /**
     * @param statusId
     * @param languageId
     * @param loginUserID
     * @param updateStatusId
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public StatusId updateStatusId(Long statusId, String languageId, String loginUserID, UpdateStatusId updateStatusId)
            throws IllegalAccessException, InvocationTargetException {
        StatusId dbStatusId = getStatusId(statusId, languageId);
        BeanUtils.copyProperties(updateStatusId, dbStatusId, CommonUtils.getNullPropertyNames(updateStatusId));
        dbStatusId.setUpdatedBy(loginUserID);
        dbStatusId.setUpdatedOn(new Date());
        return statusIdRepository.save(dbStatusId);
    }

    /**
     * @param statusId
     * @param languageId
     * @param loginUserID
     */
    public void deleteStatusId(Long statusId, String languageId, String loginUserID) {
        StatusId status = getStatusId(statusId, languageId);
        if (status != null) {
            status.setDeletionIndicator(1L);
            status.setUpdatedBy(loginUserID);
            statusIdRepository.save(status);
        } else {
            throw new EntityNotFoundException("Error in deleting Id: " + statusId);
        }
    }


    /**
     *
     * @param findStatus
     * @return
     * @throws Exception
     */
    public List<StatusId> findStatusId(FindStatus findStatus) throws Exception {

        if (findStatus.getStartCreatedOn() != null && findStatus.getStartCreatedOn() != null) {
            Date date = DateUtils.convertStringToYYYYMMDD(findStatus.getStartCreatedOn());
            Date date1 = DateUtils.convertStringToYYYYMMDD(findStatus.getEndCreatedOn());

            Date[] dates = DateUtils.addTimeToDatesForSearch(date, date1);
            findStatus.setFromDate(dates[0]);
            findStatus.setToDate(dates[1]);
        }
        StatusSpecification spec = new StatusSpecification(findStatus);
        List<StatusId> results = statusIdRepository.findAll(spec);
        results = results.stream().filter(n->n.getDeletionIndicator()==0).collect(Collectors.toList());
        return results;
    }

}
