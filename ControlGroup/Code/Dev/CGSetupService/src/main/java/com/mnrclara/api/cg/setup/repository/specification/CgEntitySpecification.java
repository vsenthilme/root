package com.mnrclara.api.cg.setup.repository.specification;

import com.mnrclara.api.cg.setup.model.cgentity.CgEntity;
import com.mnrclara.api.cg.setup.model.cgentity.FindCgEntity;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class CgEntitySpecification implements Specification<CgEntity> {

    FindCgEntity findCgEntity;

    public CgEntitySpecification(FindCgEntity inputSearchParams) {
        this.findCgEntity = inputSearchParams;
    }

    @Override
    public Predicate toPredicate(Root<CgEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        List<Predicate> predicate = new ArrayList<Predicate>();

        if (findCgEntity.getEntityId() != null && !findCgEntity.getEntityId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("entityId");
            predicate.add(group.in(findCgEntity.getEntityId()));
        }
        if (findCgEntity.getClientId() != null && !findCgEntity.getClientId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("clientId");
            predicate.add(group.in(findCgEntity.getClientId()));
        }
        if (findCgEntity.getCompanyId() != null && !findCgEntity.getCompanyId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("companyId");
            predicate.add(group.in(findCgEntity.getCompanyId()));
        }
        if (findCgEntity.getLanguageId() != null && !findCgEntity.getLanguageId().isEmpty()) {
            final Path<DeferredImportSelector.Group> group = root.<DeferredImportSelector.Group>get("languageId");
            predicate.add(group.in(findCgEntity.getLanguageId()));
        }
        if (findCgEntity.getFromDate() != null && findCgEntity.getToDate() != null) {
            predicate.add(cb.between(root.get("createdOn"), findCgEntity.getFromDate(), findCgEntity.getToDate()));
        }

        predicate.add(cb.equal(root.get("deletionIndicator"), 0L));
        return cb.and(predicate.toArray(new Predicate[]{}));
    }
}
