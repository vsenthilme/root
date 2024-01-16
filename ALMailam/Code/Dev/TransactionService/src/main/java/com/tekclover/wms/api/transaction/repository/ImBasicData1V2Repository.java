package com.tekclover.wms.api.transaction.repository;

import com.tekclover.wms.api.transaction.model.dto.IImbasicData1;
import com.tekclover.wms.api.transaction.model.dto.ImBasicData1V2;
import com.tekclover.wms.api.transaction.repository.fragments.StreamableJpaSpecificationRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ImBasicData1V2Repository extends PagingAndSortingRepository<ImBasicData1V2,Long>,
		JpaSpecificationExecutor<ImBasicData1V2>, StreamableJpaSpecificationRepository<ImBasicData1V2> {

	@Query (value = "SELECT TEXT AS description, MFR_PART AS manufacturePart FROM tblimbasicdata1 \r\n"
			+ " WHERE ITM_CODE = :itemCode", nativeQuery = true)
	public List<IImbasicData1> findByItemCode (@Param(value = "itemCode") String itemCode);

	@Query (value = "SELECT TEXT AS description, MFR_PART AS manufacturePart FROM tblimbasicdata1 \r\n"
			+ " WHERE ITM_CODE = :itemCode and C_ID = :companyCodeId and PLANT_ID = :plantId and LANG_ID = :languageId and IS_DELETED = 0", nativeQuery = true)
	public List<IImbasicData1> findByItemCode (@Param(value = "itemCode") String itemCode,
											   @Param(value = "companyCodeId") String companyCodeId,
											   @Param(value = "plantId") String plantId,
											   @Param(value = "languageId") String languageId);

	public ImBasicData1V2 findByItemCodeAndWarehouseIdInAndDeletionIndicator(String itemCode, List<String> warehouseId, Long deletionIndicator);
	
	public ImBasicData1V2 findByItemCodeAndWarehouseIdAndDeletionIndicator(String itemCode, String warehouseId, Long deletionIndicator);

	public ImBasicData1V2 findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndItemCodeAndUomIdAndDeletionIndicator(
			String languageId, String companyCodeId, String plantId, String warehouseId, String itemCode, String uom,
			Long deletionIndicator);

	Optional<ImBasicData1V2> findByItemCodeAndCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndDeletionIndicator(
			String itemCode, String companyCodeId, String plantId, String languageId, String warehouseId, Long deletionIndicator);

	ImBasicData1V2 findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndItemCodeAndUomIdAndManufacturerPartNoAndDeletionIndicator(
			String languageId, String companyCodeId, String plantId, String warehouseId,
			String itemCode, String uom, String manufacturerName, Long deletionIndicator);


	ImBasicData1V2 findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndItemCodeAndManufacturerPartNoAndDeletionIndicator(
			String languageId, String companyCode, String plantId, String warehouseId,
			String itemCode, String manufacturerName, Long deletionIndicator);

	Optional<ImBasicData1V2> findByItemCodeAndCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndManufacturerPartNoAndDeletionIndicator(
			String itemCode, String companyCodeId, String plantId, String languageId,
			String warehouseId, String manufacturerName, Long deletionIndicator);

	@Query (value = "SELECT cap_chk capacityCheck, ITM_CODE description FROM tblimbasicdata1 \r\n"
			+ " WHERE ITM_CODE = :itemCode and C_ID = :companyCodeId and PLANT_ID = :plantId and LANG_ID = :languageId and WH_ID = :warehouseId AND MFR_PART = :manufactureName AND IS_DELETED = 0", nativeQuery = true)
	public IImbasicData1 findCapacityCheck (@Param(value = "itemCode") String itemCode,
											   @Param(value = "companyCodeId") String companyCodeId,
											   @Param(value = "plantId") String plantId,
											   @Param(value = "languageId") String languageId,
											   @Param(value = "warehouseId") String warehouseId,
											   @Param(value = "manufactureName") String manufactureName);
}