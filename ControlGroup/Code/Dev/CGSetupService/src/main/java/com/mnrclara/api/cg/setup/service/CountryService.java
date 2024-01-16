package com.mnrclara.api.cg.setup.service;

import com.mnrclara.api.cg.setup.model.IKeyValuePair;
import com.mnrclara.api.cg.setup.model.country.AddCountry;
import com.mnrclara.api.cg.setup.model.country.Country;
import com.mnrclara.api.cg.setup.model.country.FindCountry;
import com.mnrclara.api.cg.setup.model.country.UpdateCountry;
import com.mnrclara.api.cg.setup.repository.CompanyIdRepository;
import com.mnrclara.api.cg.setup.repository.CountryRepository;
import com.mnrclara.api.cg.setup.repository.specification.CountrySpecification;
import com.mnrclara.api.cg.setup.util.CommonUtils;
import com.mnrclara.api.cg.setup.exception.BadRequestException;
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
public class CountryService {
    @Autowired
    private CompanyIdRepository companyIdRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private LanguageIdService languageIdService;

    /**
     * getCompanies
     *
     * @return
     */
    public List<Country> getCountrys() {
        List<Country> countryList = countryRepository.findAll();
        countryList = countryList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        List<Country> newCountry = new ArrayList<>();
        for (Country country : countryList) {
            IKeyValuePair iKeyValuePair = companyIdRepository.getCompanyIdAndDescription(
                    country.getCompanyId(), country.getLanguageId());

            if (iKeyValuePair != null) {
                country.setCompanyIdAndDescription(iKeyValuePair.getDescription());
            }
            newCountry.add(country);
        }
        return newCountry;
    }

    /**
     * @param countryId
     * @param companyId
     * @param languageId
     * @return
     */
    public Country getCountry(String countryId, String companyId, String languageId) {
        log.info("country Id: " + countryId);

        Optional<Country> country =
                countryRepository.findByCountryIdAndCompanyIdAndLanguageIdAndDeletionIndicator(
                        countryId, companyId, languageId, 0L);

        if (country.isEmpty()) {
            throw new BadRequestException("The given values of companyId: " + companyId +
                    " countryId " + countryId +
                    " languageId " + languageId + "doesn't exists");
        }
        Country newCountry = new Country();
        BeanUtils.copyProperties(country.get(), newCountry, CommonUtils.getNullPropertyNames(country));
        IKeyValuePair iKeyValuePair =
                companyIdRepository.getCompanyIdAndDescription(companyId, languageId);

        if (iKeyValuePair != null) {
            newCountry.setCompanyIdAndDescription(iKeyValuePair.getDescription());
        }
        return newCountry;
    }

    /**
     * createCountry
     *
     * @param newCountry
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public Country createCountry(AddCountry newCountry, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {

        Optional<Country> duplicateCountry =
                countryRepository.findByCountryIdAndCompanyIdAndLanguageIdAndDeletionIndicator(newCountry.getCountryId(),
                        newCountry.getCompanyId(), newCountry.getLanguageId(), 0L);

        if (!duplicateCountry.isEmpty()) {
            throw new EntityNotFoundException("Record is Getting Duplicated");
        } else {
            IKeyValuePair iKeyValuePair =
                    companyIdRepository.getCompanyIdAndDescription(newCountry.getCompanyId(), newCountry.getLanguageId());

            if (iKeyValuePair == null) {
                throw new RuntimeException("The given values of companyId " + newCountry.getCompanyId() +
                        "languageId" + newCountry.getLanguageId() + "doesn't exists");
            }
            Country dbCountry = new Country();
            BeanUtils.copyProperties(newCountry, dbCountry, CommonUtils.getNullPropertyNames(newCountry));
            dbCountry.setCompanyIdAndDescription(iKeyValuePair.getDescription());
            dbCountry.setDeletionIndicator(0L);
            dbCountry.setCreatedBy(loginUserID);
            dbCountry.setUpdatedBy(loginUserID);
            dbCountry.setCreatedOn(new Date());
            dbCountry.setUpdatedOn(new Date());
            return countryRepository.save(dbCountry);
        }
    }

    /**
     * updateCountry
     *
     * @param countryId
     * @param updateCountry
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public Country updateCountry(String countryId, String languageId, String companyId,
                                 String loginUserID, UpdateCountry updateCountry)
            throws IllegalAccessException, InvocationTargetException {
        Country dbCountry = getCountry(countryId, companyId, languageId);
        BeanUtils.copyProperties(updateCountry, dbCountry, CommonUtils.getNullPropertyNames(updateCountry));
        dbCountry.setUpdatedBy(loginUserID);
        dbCountry.setDeletionIndicator(0L);
        dbCountry.setUpdatedOn(new Date());
        return countryRepository.save(dbCountry);
    }

    /**
     * @param countryId
     * @param companyId
     * @param languageId
     * @param loginUserID
     */
    public void deleteCountry(String countryId, String companyId, String languageId, String loginUserID) {
        Country dbCountry = getCountry(countryId, companyId, languageId);
        if (dbCountry != null) {
            dbCountry.setDeletionIndicator(1L);
            dbCountry.setUpdatedBy(loginUserID);
            dbCountry.setUpdatedOn(new Date());
            countryRepository.save(dbCountry);
        } else {
            throw new EntityNotFoundException("Error in deleting Id: " + countryId);
        }
    }

    //Find Country
    public List<Country> findCountry(FindCountry findCountry) throws ParseException {
        if (findCountry.getStartCreatedOn() != null && findCountry.getStartCreatedOn() != null) {
            Date date = DateUtils.convertStringToYYYYMMDD(findCountry.getStartCreatedOn());
            Date date1 = DateUtils.convertStringToYYYYMMDD(findCountry.getEndCreatedOn());

            Date[] dates = DateUtils.addTimeToDatesForSearch(date, date1);
            findCountry.setFromDate(dates[0]);
            findCountry.setToDate(dates[1]);
        }

        CountrySpecification spec = new CountrySpecification(findCountry);
        List<Country> results = countryRepository.findAll(spec);
        results = results.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        log.info("results: " + results);
        List<Country> newCountry = new ArrayList<>();
        for (Country dbCountry : results) {
            IKeyValuePair iKeyValuePair =
                    companyIdRepository.getCompanyIdAndDescription(dbCountry.getCompanyId(), dbCountry.getLanguageId());

            if (iKeyValuePair != null) {
                dbCountry.setCompanyIdAndDescription(iKeyValuePair.getDescription());
            }
            newCountry.add(dbCountry);
        }
        return newCountry;
    }
}
