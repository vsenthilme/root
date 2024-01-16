package com.mnrclara.api.cg.setup.repository.specification;

import com.mnrclara.api.cg.setup.model.city.City;
import com.mnrclara.api.cg.setup.model.city.FindCity;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class CitySpecification implements Specification<City> {
    FindCity findCity;

    public CitySpecification(FindCity inputSearchParams) {
        this.findCity = inputSearchParams;
    }

    @Override
    public Predicate toPredicate(Root<City> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        List<Predicate> predicates = new ArrayList<Predicate>();

        if (findCity.getCityId() != null && !findCity.getCityId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("cityId");
            predicates.add(group.in(findCity.getCityId()));
        }
        if (findCity.getCityName() != null && !findCity.getCityName().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("cityName");
            predicates.add(group.in(findCity.getCityName()));
        }
        if (findCity.getCountryId() != null && !findCity.getCountryId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("countryId");
            predicates.add(group.in(findCity.getCountryId()));
        }
        if (findCity.getStateId() != null && !findCity.getStateId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("stateId");
            predicates.add(group.in(findCity.getStateId()));
        }
        if (findCity.getLanguageId() != null && !findCity.getLanguageId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("languageId");
            predicates.add(group.in(findCity.getLanguageId()));
        }
        if (findCity.getFromDate() != null && findCity.getToDate() != null) {
            predicates.add(cb.between(root.get("createdOn"), findCity.getFromDate(), findCity.getToDate()));
        }
        if (findCity.getCompanyId() != null && !findCity.getCompanyId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("companyId");
            predicates.add(group.in(findCity.getCompanyId()));
        }

        predicates.add(cb.equal(root.get("deletionIndicator"), 0L));
        return cb.and(predicates.toArray(new Predicate[]{}));
    }

}
