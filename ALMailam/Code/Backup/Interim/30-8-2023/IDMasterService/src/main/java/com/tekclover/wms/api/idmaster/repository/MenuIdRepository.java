package com.tekclover.wms.api.idmaster.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tekclover.wms.api.idmaster.model.menuid.MenuId;


@Repository
@Transactional
public interface MenuIdRepository extends JpaRepository<MenuId,Long>, JpaSpecificationExecutor<MenuId> {
	
	public List<MenuId> findAll();

	/**
	 * 
	 * @param companyCode
	 * @param plantId
	 * @param warehouseId
	 * @param menuId
	 * @param subMenuId
	 * @param authorizationObjectId
	 * @param languageId
	 * @param l
	 * @return
	 */
	public Optional<MenuId> findByCompanyCodeIdAndPlantIdAndWarehouseIdAndMenuIdAndSubMenuIdAndAuthorizationObjectIdAndLanguageIdAndDeletionIndicator(
			String companyCode, String plantId, String warehouseId, Long menuId, Long subMenuId,
			Long authorizationObjectId, String languageId, long l);

	//public MenuId findByMenuId(Long menuId);
	public List<MenuId> findByMenuId(Long menuId);

	public List<MenuId> findBySubMenuId(Long subMenuId);
}