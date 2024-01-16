package com.tekclover.wms.api.transaction.repository.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.context.annotation.DeferredImportSelector.Group;
import org.springframework.data.jpa.domain.Specification;

import com.tekclover.wms.api.transaction.model.inbound.staging.SearchStagingLine;
import com.tekclover.wms.api.transaction.model.inbound.staging.StagingLineEntity;

@SuppressWarnings("serial")
public class StagingLineSpecification implements Specification<StagingLineEntity> {

	SearchStagingLine searchStagingLine;

	public StagingLineSpecification(SearchStagingLine inputSearchParams) {
		this.searchStagingLine = inputSearchParams;
	}

	@Override
	public Predicate toPredicate(Root<StagingLineEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (searchStagingLine.getWarehouseId() != null && !searchStagingLine.getWarehouseId().isEmpty()) {
			final Path<Group> group = root.<Group>get("warehouseId");
			predicates.add(group.in(searchStagingLine.getWarehouseId()));
		}

		if (searchStagingLine.getCompanyCode() != null && !searchStagingLine.getCompanyCode().isEmpty()) {
			final Path<Group> group = root.<Group>get("companyCode");
			predicates.add(group.in(searchStagingLine.getCompanyCode()));
		}

		if (searchStagingLine.getPlantId() != null && !searchStagingLine.getPlantId().isEmpty()) {
			final Path<Group> group = root.<Group>get("plantId");
			predicates.add(group.in(searchStagingLine.getPlantId()));
		}

		if (searchStagingLine.getLanguageId() != null && !searchStagingLine.getLanguageId().isEmpty()) {
			final Path<Group> group = root.<Group>get("languageId");
			predicates.add(group.in(searchStagingLine.getLanguageId()));
		}

		if (searchStagingLine.getPreInboundNo() != null && !searchStagingLine.getPreInboundNo().isEmpty()) {
			final Path<Group> group = root.<Group>get("preInboundNo");
			predicates.add(group.in(searchStagingLine.getPreInboundNo()));
		}

		if (searchStagingLine.getRefDocNumber() != null && !searchStagingLine.getRefDocNumber().isEmpty()) {
			final Path<Group> group = root.<Group>get("refDocNumber");
			predicates.add(group.in(searchStagingLine.getRefDocNumber()));
		}

		if (searchStagingLine.getStagingNo() != null && !searchStagingLine.getStagingNo().isEmpty()) {
			final Path<Group> group = root.<Group>get("stagingNo");
			predicates.add(group.in(searchStagingLine.getStagingNo()));
		}

		if (searchStagingLine.getPalletCode() != null && !searchStagingLine.getPalletCode().isEmpty()) {
			final Path<Group> group = root.<Group>get("palletCode");
			predicates.add(group.in(searchStagingLine.getPalletCode()));
		}

		if (searchStagingLine.getCaseCode() != null && !searchStagingLine.getCaseCode().isEmpty()) {
			final Path<Group> group = root.<Group>get("caseCode");
			predicates.add(group.in(searchStagingLine.getCaseCode()));
		}

		if (searchStagingLine.getLineNo() != null && !searchStagingLine.getLineNo().isEmpty()) {
			final Path<Group> group = root.<Group>get("lineNo");
			predicates.add(group.in(searchStagingLine.getLineNo()));
		}

		if (searchStagingLine.getItemCode() != null && !searchStagingLine.getItemCode().isEmpty()) {
			final Path<Group> group = root.<Group>get("itemCode");
			predicates.add(group.in(searchStagingLine.getItemCode()));
		}

		if (searchStagingLine.getStatusId() != null && !searchStagingLine.getStatusId().isEmpty()) {
			final Path<Group> group = root.<Group>get("statusId");
			predicates.add(group.in(searchStagingLine.getStatusId()));
		}

		return cb.and(predicates.toArray(new Predicate[] {}));
	}
}
