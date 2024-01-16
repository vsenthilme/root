package com.mnrclara.api.cg.setup.service;

import com.mnrclara.api.cg.setup.repository.CityRepository;
import com.mnrclara.api.cg.setup.repository.CompanyIdRepository;
import com.mnrclara.api.cg.setup.repository.CountryRepository;
import com.mnrclara.api.cg.setup.repository.StateRepository;
import com.mnrclara.api.cg.setup.repository.specification.CitySpecification;
import com.mnrclara.api.cg.setup.exception.BadRequestException;
import com.mnrclara.api.cg.setup.model.IKeyValuePair;
import com.mnrclara.api.cg.setup.model.city.AddCity;
import com.mnrclara.api.cg.setup.model.city.City;
import com.mnrclara.api.cg.setup.model.city.FindCity;
import com.mnrclara.api.cg.setup.model.city.UpdateCity;
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
public class CityService {
    @Autowired
    private CompanyIdRepository companyIdRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private StateRepository stateRepository;
    @Autowired
    private StateService stateService;

    /**
     * getAllCity
     *
     * @return
     */
    public List<City> getAllCity() {
        List<City> cityList = cityRepository.findAll();
        cityList = cityList.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        List<City> newCity = new ArrayList<>();
        for (City dbCity : cityList) {
            IKeyValuePair iKeyValuePair =
                    cityRepository.getDescription(dbCity.getCompanyId(), dbCity.getLanguageId(), dbCity.getCountryId(), dbCity.getStateId());

            if (iKeyValuePair != null) {
                dbCity.setCompanyIdAndDescription(iKeyValuePair.getCompanyDescription());
                dbCity.setCountryIdAndDescription(iKeyValuePair.getCountryDescription());
                dbCity.setStateIdAndDescription(iKeyValuePair.getStateDescription());
            }

            newCity.add(dbCity);
        }
        return newCity;
    }

    /**
     * @param cityId
     * @param stateId
     * @param countryId
     * @param languageId
     * @return
     */
    public City getCity(String cityId, String companyId, String stateId, String countryId, String languageId) {
        log.info("city Id: " + cityId);
        Optional<City> dbCity = cityRepository.findByCityIdAndStateIdAndCountryIdAndLanguageIdAndDeletionIndicator(
                cityId, stateId, countryId, languageId, 0L);
        if (dbCity.isEmpty()) {
            throw new BadRequestException("The given ID doesn't exist : " +
                    " City Id " + cityId +
                    " companyId " + companyId +
                    " State Id " + stateId +
                    "Country Id " + countryId);
        }
        City newCity = new City();
        BeanUtils.copyProperties(dbCity.get(), newCity, CommonUtils.getNullPropertyNames(dbCity));
        IKeyValuePair iKeyValuePair =
                cityRepository.getDescription(companyId, languageId, countryId, stateId);

        if (iKeyValuePair != null) {
            newCity.setCompanyIdAndDescription(iKeyValuePair.getCompanyDescription());
            newCity.setCountryIdAndDescription(iKeyValuePair.getCountryDescription());
            newCity.setStateIdAndDescription(iKeyValuePair.getStateDescription());
        }
        return newCity;
    }

    /**
     * createCity
     *
     * @param newCity
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public City createCity(AddCity newCity, String loginUserID)
            throws IllegalAccessException, InvocationTargetException {

        Optional<City> duplicateCity = cityRepository.findByCityIdAndStateIdAndCountryIdAndLanguageIdAndDeletionIndicator(
                newCity.getCityId(), newCity.getStateId(), newCity.getCountryId(), newCity.getLanguageId(), 0L);

        if (!duplicateCity.isEmpty()) {
            throw new EntityNotFoundException("Record is Getting Duplicated");
        } else {

            IKeyValuePair iKeyValuePair =
                    cityRepository.getDescription(newCity.getCompanyId(), newCity.getLanguageId(),
                            newCity.getCountryId(), newCity.getStateId());

            City dbCity = new City();
            BeanUtils.copyProperties(newCity, dbCity, CommonUtils.getNullPropertyNames(newCity));

            if (iKeyValuePair != null) {
                dbCity.setCompanyIdAndDescription(iKeyValuePair.getCompanyDescription());
                dbCity.setCountryIdAndDescription(iKeyValuePair.getCountryDescription());
                dbCity.setStateIdAndDescription(iKeyValuePair.getStateDescription());
            } else {
                throw new RuntimeException("The given values companyId " + newCity.getCompanyId() +
                        " stateId" + newCity.getStateId() +
                        " countryId " + newCity.getCountryId() +
                        " languageId " + newCity.getLanguageId() + " doesn't exists");
            }

            dbCity.setDeletionIndicator(0L);
            dbCity.setCreatedBy(loginUserID);
            dbCity.setUpdatedBy(loginUserID);
            dbCity.setCreatedOn(new Date());
            dbCity.setUpdatedOn(new Date());
            return cityRepository.save(dbCity);
        }
    }

    /**
     * @param cityId
     * @param stateId
     * @param countryId
     * @param languageId
     * @param loginUserID
     * @param updateCity
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public City updateCity(String cityId, String stateId, String companyId, String countryId, String languageId,
                           String loginUserID, UpdateCity updateCity)
            throws IllegalAccessException, InvocationTargetException {
        City dbCity = getCity(cityId, companyId, stateId, countryId, languageId);
        BeanUtils.copyProperties(updateCity, dbCity, CommonUtils.getNullPropertyNames(updateCity));
        dbCity.setDeletionIndicator(0L);
        dbCity.setUpdatedBy(loginUserID);
        dbCity.setUpdatedOn(new Date());
        return cityRepository.save(dbCity);
    }

    /**
     * @param cityId
     * @param stateId
     * @param countryId
     * @param languageId
     */
    public void deleteCity(String cityId, String stateId, String companyId,
                           String countryId, String languageId, String loginUserID) {

        City dbCity = getCity(cityId, companyId, stateId, countryId, languageId);
        if (dbCity != null) {
            dbCity.setDeletionIndicator(1L);
            dbCity.setUpdatedBy(loginUserID);
            dbCity.setUpdatedOn(new Date());
            cityRepository.save(dbCity);
        } else {
            throw new EntityNotFoundException("Error in deleting Id: " + cityId);
        }
    }

    /**
     * @param findCity
     * @return
     * @throws ParseException
     */
    public List<City> findCity(FindCity findCity) throws ParseException {

        if (findCity.getStartCreatedOn() != null && findCity.getStartCreatedOn() != null) {
            Date date = DateUtils.convertStringToYYYYMMDD(findCity.getStartCreatedOn());
            Date date1 = DateUtils.convertStringToYYYYMMDD(findCity.getEndCreatedOn());

            Date[] dates = DateUtils.addTimeToDatesForSearch(date, date1);
            findCity.setFromDate(dates[0]);
            findCity.setToDate(dates[1]);
        }
        CitySpecification spec = new CitySpecification(findCity);
        List<City> results = cityRepository.findAll(spec);
        results = results.stream().filter(n -> n.getDeletionIndicator() == 0).collect(Collectors.toList());
        log.info("results: " + results);
        List<City> newCity = new ArrayList<>();
        for (City dbCity : results) {

            IKeyValuePair iKeyValuePair =
                    cityRepository.getDescription(dbCity.getCompanyId(), dbCity.getLanguageId(),
                            dbCity.getCountryId(), dbCity.getStateId());

            if (iKeyValuePair != null) {
                dbCity.setCompanyIdAndDescription(iKeyValuePair.getCompanyDescription());
                dbCity.setCountryIdAndDescription(iKeyValuePair.getCountryDescription());
                dbCity.setStateIdAndDescription(iKeyValuePair.getStateDescription());
            }
            newCity.add(dbCity);
        }
        return newCity;
    }
}
