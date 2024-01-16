package com.mnrclara.api.cg.setup.service;

import com.mnrclara.api.cg.setup.model.languageid.AddLanguageId;
import com.mnrclara.api.cg.setup.model.languageid.FindLanguageId;
import com.mnrclara.api.cg.setup.model.languageid.LanguageId;
import com.mnrclara.api.cg.setup.model.languageid.UpdateLanguageId;
import com.mnrclara.api.cg.setup.repository.LanguageIdRepository;
import com.mnrclara.api.cg.setup.exception.BadRequestException;
import com.mnrclara.api.cg.setup.repository.specification.LanguageIdSpecification;
import com.mnrclara.api.cg.setup.util.CommonUtils;
import com.mnrclara.api.cg.setup.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LanguageIdService {

    @Autowired
    private LanguageIdRepository languageIdRepository;

    /**
     * getLanguageIds
     *
     * @return
     */
    public List<LanguageId> getLanguageIds() {
        List<LanguageId> languageIdList = languageIdRepository.findAll();
        languageIdList = languageIdList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        return languageIdList;
    }

    /**
     * getLanguageId
     *
     * @param languageId
     * @return
     */
    public LanguageId getLanguageId(String languageId) {
        Optional<LanguageId> dbLanguageId =
                languageIdRepository.findByLanguageIdAndDeletionIndicator(
                        languageId,
                        0L
                );
        if (dbLanguageId.isEmpty()) {
            throw new BadRequestException("The given values : " +
                    "languageId - " + languageId +
                    " doesn't exist.");

        }
        return dbLanguageId.get();
    }

    /**
     * createLanguageId
     *
     * @param newLanguageId
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public LanguageId createLanguageId(AddLanguageId newLanguageId, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {

        Optional<LanguageId> duplicateLanguageId =
                languageIdRepository.findByLanguageIdAndDeletionIndicator(newLanguageId.getLanguageId(), 0L);

        if (!duplicateLanguageId.isEmpty()) {
            throw new EntityNotFoundException("Record is Getting Duplicated");
        } else {
            LanguageId dbLanguageId = new LanguageId();
            log.info("newLanguageId : " + newLanguageId);
            BeanUtils.copyProperties(newLanguageId, dbLanguageId, CommonUtils.getNullPropertyNames(newLanguageId));
            dbLanguageId.setDeletionIndicator(0L);
            dbLanguageId.setCreatedBy(loginUserID);
            dbLanguageId.setUpdatedBy(loginUserID);
            dbLanguageId.setCreatedOn(new Date());
            dbLanguageId.setUpdatedOn(new Date());
            return languageIdRepository.save(dbLanguageId);
        }
    }

    /**
     * updateLanguageId
     *
     * @param loginUserID
     * @param languageId
     * @param updateLanguageId
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public LanguageId updateLanguageId(String languageId, String loginUserID,
                                       UpdateLanguageId updateLanguageId)
            throws IllegalAccessException, InvocationTargetException {
        LanguageId dbLanguageId = getLanguageId(languageId);
        BeanUtils.copyProperties(updateLanguageId, dbLanguageId, CommonUtils.getNullPropertyNames(updateLanguageId));
        dbLanguageId.setUpdatedBy(loginUserID);
        dbLanguageId.setUpdatedOn(new Date());
        return languageIdRepository.save(dbLanguageId);
    }

    /**
     * deleteLanguageId
     *
     * @param loginUserID
     * @param languageId
     */
    public void deleteLanguageId(String languageId, String loginUserID) {
        LanguageId dbLanguageId = getLanguageId(languageId);
        if (dbLanguageId != null) {
            dbLanguageId.setDeletionIndicator(1L);
            dbLanguageId.setUpdatedBy(loginUserID);
            dbLanguageId.setUpdatedOn(new Date());
            languageIdRepository.save(dbLanguageId);
        } else {
            throw new EntityNotFoundException("Error in deleting Id: " + languageId);
        }
    }

    //	//Find LanguageId
    public List<LanguageId> findLanguageId(FindLanguageId findLanguageId)
            throws ParseException {
        if (findLanguageId.getStartCreatedOn() != null && findLanguageId.getStartCreatedOn() != null) {
            Date date = DateUtils.convertStringToYYYYMMDD(findLanguageId.getStartCreatedOn());
            Date date1 = DateUtils.convertStringToYYYYMMDD(findLanguageId.getEndCreatedOn());

            Date[] dates = DateUtils.addTimeToDatesForSearch(date, date1);
            findLanguageId.setFromDate(dates[0]);
            findLanguageId.setToDate(dates[1]);
        }

        LanguageIdSpecification spec = new LanguageIdSpecification(findLanguageId);
        List<LanguageId> results = languageIdRepository.findAll(spec);
        results = results.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        log.info("results: " + results);
        return results;
    }
}
