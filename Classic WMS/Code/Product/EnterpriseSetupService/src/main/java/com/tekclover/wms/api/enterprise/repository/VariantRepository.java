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

import com.tekclover.wms.api.enterprise.model.variant.Variant;

@Repository
@Transactional
public interface VariantRepository extends JpaRepository<Variant,Long>, JpaSpecificationExecutor<Variant> {

	public List<Variant> findAll();
	public Optional<Variant> findByVariantIdAndLanguageIdAndCompanyIdAndPlantIdAndWarehouseIdAndLevelIdAndDeletionIndicator(String variantId,String languageId,String companyId,String plantId,String warehouseId,Long levelId,Long deletionIndicator);
	@Query(value = "select tl.var_id AS variantId,tl.var_id_text AS description \n"+
			"from tblvariantid tl \n"+
			"WHERE \n"+
			"tl.var_id IN (:variantId) and tl.lang_id IN (:languageId) and tl.c_id IN (:companyCodeId) and tl.plant_id IN (:plantId) and tl.wh_id IN (:warehouseId) and \n"+
			"tl.is_deleted=0",nativeQuery = true)
	public IkeyValuePair getVariantIdAndDescription(@Param(value = "variantId")String variantId,
													 @Param(value = "languageId")String languageId,
													 @Param(value = "companyCodeId")String companyCodeId,
													 @Param(value = "plantId")String plantId,
													 @Param(value = "warehouseId")String warehouseId);


}