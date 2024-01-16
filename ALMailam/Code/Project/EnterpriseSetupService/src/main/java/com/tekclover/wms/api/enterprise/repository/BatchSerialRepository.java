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

import com.tekclover.wms.api.enterprise.model.batchserial.BatchSerial;

@Repository
@Transactional
public interface BatchSerialRepository extends JpaRepository<BatchSerial,Long>, JpaSpecificationExecutor<BatchSerial> {

	public List<BatchSerial> findAll();
	Optional<BatchSerial> findByStorageMethodAndPlantIdAndCompanyIdAndLanguageIdAndWarehouseIdAndLevelIdAndDeletionIndicator(String storageMethod,String plantId,String companyId,String languageId,String warehouseId,Long levelId,Long deletionIndicator);
	@Query(value ="select  tl.level_id AS levelId,tl.level AS description\n"+
			" from tbllevelid tl \n" +
			"WHERE \n"+
			"tl.level_id IN (:levelId)and tl.lang_id IN (:languageId) and tl.c_id IN (:companyCodeId) and tl.plant_id IN (:plantId) and tl.wh_id IN (:warehouseId) and \n"+
			"tl.is_deleted=0 ",nativeQuery = true)

	public IkeyValuePair getLevelIdAndDescription(@Param(value="levelId") String levelId,
												  @Param(value = "languageId")String languageId,
												  @Param(value = "companyCodeId")String companyCodeId,
												  @Param(value = "plantId")String plantId,
												  @Param(value = "warehouseId")String warehouseId);
}