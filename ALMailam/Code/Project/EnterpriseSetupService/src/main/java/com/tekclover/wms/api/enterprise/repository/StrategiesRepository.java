package com.tekclover.wms.api.enterprise.repository;

import java.util.List;
import java.util.Optional;

import com.tekclover.wms.api.enterprise.model.IkeyValuePair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tekclover.wms.api.enterprise.model.strategies.Strategies;

@Repository
@Transactional
public interface StrategiesRepository extends JpaRepository<Strategies,Long>, JpaSpecificationExecutor<Strategies> {

	public List<Strategies> findAll();
	public Optional<Strategies> findByStrategyTypeId(Long strategyTypeId);
	
	public List<Strategies> findByLanguageIdAndCompanyIdAndPlantIdAndWarehouseIdAndStrategyTypeIdAndSequenceIndicatorAndStrategyNoAndPriorityAndDeletionIndicator(
			String languageId, String companyCode, String plantId, String warehouseId, Long strategyTypeId,
			Long sequenceIndicator, String strategyNo, Long priority, Long deletionIndicator);
	@Query(value ="select  tl.st_no AS strategyNo,tl.str_typ_text AS description,tl.str_typ_id AS strategyTypeId,tl.st_no_text AS strategyNoText  \n"+
			" from tblstrategyid tl \n" +
			"WHERE \n"+
			"tl.st_no IN (:strategyNo) and tl.str_typ_id IN (:strategyTypeId) and tl.lang_id IN (:languageId) and tl.c_id IN (:companyCodeId) and tl.plant_id IN (:plantId) and tl.wh_id IN (:warehouseId) and \n"+
			"tl.is_deleted=0 ",nativeQuery = true)

	public IkeyValuePair getStrategyIdAndDescription(@Param(value="strategyNo") String strategyNo,
														 @Param(value = "strategyTypeId")Long strategyTypeId,
														 @Param(value = "languageId")String languageId,
														 @Param(value = "companyCodeId")String companyCodeId,
														 @Param(value = "plantId")String plantId,
														 @Param(value = "warehouseId")String warehouseId);

}