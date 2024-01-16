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

import com.tekclover.wms.api.enterprise.model.itemgroup.ItemGroup;

@Repository
@Transactional
public interface ItemGroupRepository extends JpaRepository<ItemGroup,Long>, JpaSpecificationExecutor<ItemGroup> {
	public List<ItemGroup> findAll();
	
	public Optional<ItemGroup> findByItemGroupId(Long itemGroupId);
	
	public Optional<ItemGroup> findByLanguageIdAndCompanyIdAndPlantIdAndWarehouseIdAndItemTypeIdAndItemGroupIdAndSubItemGroupIdAndDeletionIndicator (
	String languageId, String companyId, String plantId, String warehouseId, Long itemTypeId, 
	Long itemGroupId, Long subItemGroupId, Long deletionIndicator);

	@Query(value ="select  tl.itm_grp_id AS itemGroupId,tl.imt_grp AS description\n"+
			" from tblitemgroupid tl \n" +
			"WHERE \n"+
			"tl.itm_grp_id IN (:itemGroupId)and tl.lang_id IN (:languageId) and tl.c_id IN (:companyCodeId) and tl.plant_id IN (:plantId) and tl.wh_id IN (:warehouseId) and tl.itm_type_id IN (:itemTypeId) and \n"+
			"tl.is_deleted=0 ",nativeQuery = true)

	public IkeyValuePair getItemGroupIdAndDescription(@Param(value="itemGroupId") Long itemGroupId,
													  @Param(value = "languageId")String languageId,
													  @Param(value = "companyCodeId")String companyCodeId,
													  @Param(value = "plantId")String plantId,
													  @Param(value = "warehouseId")String warehouseId,
													  @Param(value = "itemTypeId")Long itemTypeId);

}

