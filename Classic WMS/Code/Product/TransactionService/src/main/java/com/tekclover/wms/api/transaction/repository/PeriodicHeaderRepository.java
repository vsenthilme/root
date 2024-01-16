package com.tekclover.wms.api.transaction.repository;

import com.tekclover.wms.api.transaction.repository.fragments.StreamableJpaSpecificationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tekclover.wms.api.transaction.model.cyclecount.periodic.PeriodicHeader;

@Repository
@Transactional
public interface PeriodicHeaderRepository extends JpaRepository<PeriodicHeader,Long>,
		JpaSpecificationExecutor<PeriodicHeader>, StreamableJpaSpecificationRepository<PeriodicHeader> {
	
	public PeriodicHeader findByCompanyCodeAndPlantIdAndLanguageIdAndWarehouseIdAndCycleCountTypeIdAndCycleCountNo(
			String companyCode, String plantId,String languageId, String warehouseId, Long cycleCountTypeId, String cycleCountNo);

	public PeriodicHeader findByCycleCountNo(String cycleCountNo);
}