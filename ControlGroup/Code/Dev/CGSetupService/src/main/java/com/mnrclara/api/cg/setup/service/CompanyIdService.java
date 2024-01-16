package com.mnrclara.api.cg.setup.service;

import com.mnrclara.api.cg.setup.exception.BadRequestException;
import com.mnrclara.api.cg.setup.model.companyid.AddCompanyId;
import com.mnrclara.api.cg.setup.model.companyid.CompanyId;
import com.mnrclara.api.cg.setup.model.companyid.FindCompanyId;
import com.mnrclara.api.cg.setup.model.companyid.UpdateCompanyId;
import com.mnrclara.api.cg.setup.model.languageid.LanguageId;
import com.mnrclara.api.cg.setup.repository.CompanyIdRepository;
import com.mnrclara.api.cg.setup.repository.LanguageIdRepository;
import com.mnrclara.api.cg.setup.repository.specification.CompanyIdSpecification;
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
public class CompanyIdService {
    @Autowired
    private CompanyIdRepository companyRepository;
    @Autowired
    private LanguageIdRepository languageIdRepository;

    @Autowired
    private LanguageIdService languageIdService;

    @Autowired
    private CompanyIdRepository companyIdRepository;

    /**
     * getCompanyIds
     *
     * @return
     */
    public List<CompanyId> getCompanyIds() {
        List<CompanyId> companyIdList = companyIdRepository.findAll();
        companyIdList = companyIdList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        return companyIdList;
    }

    /**
     * @param companyId
     * @param languageId
     * @return
     */
    public CompanyId getCompanyId(String companyId, String languageId) {
        Optional<CompanyId> dbCompanyId = companyIdRepository.findByCompanyIdAndLanguageIdAndDeletionIndicator(
                companyId, languageId, 0L);

        if (dbCompanyId.isEmpty()) {
            throw new BadRequestException("The given values : " +
                    "companyCodeId - " + companyId +
                    "languageId -" + languageId +
                    " doesn't exist.");

        }
        return dbCompanyId.get();
    }

    /**
     * @param searchCompanyId
     * @return
     * @throws ParseException
     */
    public List<CompanyId> findCompanyId(FindCompanyId searchCompanyId)
            throws ParseException {

        if (searchCompanyId.getStartCreatedOn() != null && searchCompanyId.getStartCreatedOn() != null) {
            Date date = DateUtils.convertStringToYYYYMMDD(searchCompanyId.getStartCreatedOn());
            Date date1 = DateUtils.convertStringToYYYYMMDD(searchCompanyId.getEndCreatedOn());

            Date[] dates = DateUtils.addTimeToDatesForSearch(date, date1);
            searchCompanyId.setFromDate(dates[0]);
            searchCompanyId.setToDate(dates[1]);
        }

        CompanyIdSpecification spec = new CompanyIdSpecification(searchCompanyId);
        List<CompanyId> results = companyIdRepository.findAll(spec);
        log.info("results: " + results);
        return results;
    }

    /**
     * createCompanyId
     *
     * @param newCompanyId
     * @param loginUserID
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public CompanyId createCompanyId(AddCompanyId newCompanyId, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {

        Optional<CompanyId> duplicateCompanyId =
                companyIdRepository.findByCompanyIdAndLanguageIdAndDeletionIndicator(newCompanyId.getCompanyId(),
                        newCompanyId.getLanguageId(), 0L);

        if (!duplicateCompanyId.isEmpty()) {
            throw new EntityNotFoundException("Record is Getting Duplicated");
        } else {

            CompanyId dbCompanyId = new CompanyId();
            BeanUtils.copyProperties(newCompanyId, dbCompanyId, CommonUtils.getNullPropertyNames(newCompanyId));

            LanguageId dbLanguageId = languageIdService.getLanguageId(newCompanyId.getLanguageId());
            log.info("newCompanyId : " + newCompanyId);
            dbCompanyId.setDeletionIndicator(0L);
            dbCompanyId.setCreatedBy(loginUserID);
            dbCompanyId.setUpdatedBy(loginUserID);
            dbCompanyId.setCreatedOn(new Date());
            dbCompanyId.setUpdatedOn(new Date());
            return companyIdRepository.save(dbCompanyId);
        }
    }

    /**
     * updateCompanyId
     *
     * @param loginUserID
     * @param companyId
     * @param updateCompanyId
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public CompanyId updateCompanyId(String companyId, String languageId, String loginUserID,
                                     UpdateCompanyId updateCompanyId)
            throws IllegalAccessException, InvocationTargetException {
        CompanyId dbCompanyId = getCompanyId(companyId, languageId);
        BeanUtils.copyProperties(updateCompanyId, dbCompanyId, CommonUtils.getNullPropertyNames(updateCompanyId));
        dbCompanyId.setUpdatedBy(loginUserID);
        dbCompanyId.setUpdatedOn(new Date());

        return companyIdRepository.save(dbCompanyId);
    }

    /**
     * @param companyId
     * @param languageId
     * @param loginUserID
     */
    public void deleteCompanyId(String companyId, String languageId, String loginUserID) {
        CompanyId dbCompanyId = getCompanyId(companyId, languageId);
        if (dbCompanyId != null) {
            dbCompanyId.setDeletionIndicator(1L);
            dbCompanyId.setUpdatedBy(loginUserID);
            companyIdRepository.save(dbCompanyId);
        } else {
            throw new EntityNotFoundException("Error in deleting Id: " + companyId);
        }
    }

}

