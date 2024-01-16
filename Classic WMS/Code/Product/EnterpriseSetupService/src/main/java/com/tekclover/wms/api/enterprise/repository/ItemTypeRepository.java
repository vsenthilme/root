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

import com.tekclover.wms.api.enterprise.model.itemtype.ItemType;

@Repository
@Transactional
public interface ItemTypeRepository extends JpaRepository<ItemType,Long>, JpaSpecificationExecutor<ItemType> {

	public List<ItemType> findAll();
	
	public Optional<ItemType> findByItemTypeId(Long itemTypeId);
	
	public Optional<ItemType> findByLanguageIdAndCompanyIdAndPlantIdAndWarehouseIdAndItemTypeIdAndDeletionIndicator (
	String languageId, String companyId, String plantId, String warehouseId, Long itemTypeId, Long deletionIndicator);

	@Query(value ="select  tl.itm_type_id AS itemTypeId,tl.itm_typ AS description\n"+
			" from tblitemtypeid tl \n" +
			"WHERE \n"+
			"tl.itm_type_id IN (:itemTypeId)and tl.lang_id IN (:languageId) and tl.c_id IN (:companyCodeId) and tl.plant_Id IN (:plantId) and tl.wh_id IN (:warehouseId) and \n"+
			"tl.is_deleted=0 ",nativeQuery = true)

	public IkeyValuePair getItemTypeIdAndDescription(@Param(value="itemTypeId") Long itemTypeId,
													 @Param(value = "languageId")String languageId,
													 @Param(value = "companyCodeId")String companyCodeId,
													 @Param(value = "plantId")String plantId,
													 @Param(value = "warehouseId")String warehouseId);
}