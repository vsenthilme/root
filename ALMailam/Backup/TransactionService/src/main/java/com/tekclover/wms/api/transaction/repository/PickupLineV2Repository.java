package com.tekclover.wms.api.transaction.repository;

import com.tekclover.wms.api.transaction.model.outbound.pickup.v2.PickupLineV2;
import com.tekclover.wms.api.transaction.repository.fragments.StreamableJpaSpecificationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
@Transactional
public interface PickupLineV2Repository extends JpaRepository<PickupLineV2, Long>,
        JpaSpecificationExecutor<PickupLineV2>, StreamableJpaSpecificationRepository<PickupLineV2> {

    @Query(value = "SELECT SUM(PICK_CNF_QTY) FROM tblpickupline WHERE \n"
            + "C_ID = :companyCodeId AND PLANT_ID = :plantId AND LANG_ID = :languageId AND \r\n"
            + "WH_ID = :warehouseId AND REF_DOC_NO = :refDocNumber AND\r\n"
            + "PRE_OB_NO = :preOutboundNo AND OB_LINE_NO = :obLineNumber AND ITM_CODE = :itemCode AND IS_DELETED = 0 \r\n"
            + "GROUP BY REF_DOC_NO", nativeQuery = true)
    public Double getPickupLineCountV2(@Param("companyCodeId") String companyCodeId,
                                       @Param("plantId") String plantId,
                                       @Param("languageId") String languageId,
                                       @Param("warehouseId") String warehouseId,
                                       @Param("refDocNumber") String refDocNumber,
                                       @Param("preOutboundNo") String preOutboundNo,
                                       @Param("obLineNumber") Long obLineNumber,
                                       @Param("itemCode") String itemCode);

    @Query(value = "SELECT COUNT(OB_LINE_NO) AS lineCount FROM tblpickupline WHERE \r\n"
            + " C_ID = :companyCodeId AND PLANT_ID = :plantId AND LANG_ID = :languageId AND \r\n"
            + "	WH_ID = :warehouseId AND REF_DOC_NO IN :refDocNumber AND PRE_OB_NO = :preOutboundNo\r\n"
            + "	AND STATUS_ID=50 AND IS_DELETED = 0 GROUP BY REF_DOC_NO", nativeQuery = true)
    public Double getCountByWarehouseIdAndPreOutboundNoAndRefDocNumberAndDeletionIndicatorV2(@Param("companyCodeId") String companyCodeId,
                                                                                             @Param("plantId") String plantId,
                                                                                             @Param("languageId") String languageId,
                                                                                             @Param("warehouseId") String warehouseId,
                                                                                             @Param("preOutboundNo") String preOutboundNo,
                                                                                             @Param("refDocNumber") List<String> refDocNumber);


    PickupLineV2 findByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndPreOutboundNoAndRefDocNumberAndPartnerCodeAndLineNumberAndItemCodeAndDeletionIndicator(
            String companyCodeId, String plantId, String languageId, String warehouseId, String preOutboundNo,
            String refDocNumber, String partnerCode, Long lineNumber, String itemCode, Long deletionIndicator);

    List<PickupLineV2> findAllByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndPreOutboundNoAndRefDocNumberAndPartnerCodeAndLineNumberAndItemCodeAndDeletionIndicator(
            String companyCodeId, String plantId, String languageId, String warehouseId, String preOutboundNo,
            String refDocNumber, String partnerCode, Long lineNumber, String itemCode, Long deletionIndicator);

    PickupLineV2 findByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndPreOutboundNoAndRefDocNumberAndPartnerCodeAndLineNumberAndPickupNumberAndItemCodeAndPickedStorageBinAndPickedPackCodeAndActualHeNoAndDeletionIndicator(
            String companyCodeId, String plantId, String languageId, String warehouseId, String preOutboundNo,
            String refDocNumber, String partnerCode, Long lineNumber, String pickupNumber, String itemCode,
            String pickedStorageBin, String pickedPackCode, String actualHeNo, Long deletionIndicator);

    List<PickupLineV2> findAllByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndPreOutboundNoAndRefDocNumberAndPartnerCodeAndLineNumberInAndItemCodeInAndDeletionIndicator(
            String companyCodeId, String plantId, String languageId, String warehouseId, String preOutboundNo,
            String refDocNumber, String partnerCode, List<Long> lineNumbers, List<String> itemCodes, Long deletionIndicator);

    List<PickupLineV2> findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndPreOutboundNoInAndRefDocNumberInAndPartnerCodeAndStatusIdAndDeletionIndicator(
            String languageId, String companyCode, String plantId, String warehouseId, List<String> preOutboundNo,
            List<String> refDocNumber, String partnerCode, Long statusId, Long deletionIndicator);

    PickupLineV2 findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndPreOutboundNoAndRefDocNumberAndPartnerCodeAndLineNumberAndPickupNumberAndItemCodeAndActualHeNoAndPickedStorageBinAndPickedPackCodeAndDeletionIndicator(
            String languageId, String companyCodeId, String plantId, String warehouseId, String preOutboundNo,
            String refDocNumber, String partnerCode, Long lineNumber, String pickupNumber, String itemCode,
            String actualHeNo, String pickedStorageBin, String pickedPackCode, Long deletionIndicator);

    PickupLineV2 findByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndActualHeNoAndPickupNumberAndPreOutboundNoAndRefDocNumberAndPartnerCodeAndLineNumberAndItemCodeAndDeletionIndicator(
            String companyCodeId, String plantId, String languageId, String warehouseId, String actualHeNo,
            String pickupNumber, String preOutboundNo, String refDocNumber, String partnerCode, Long lineNumber,
            String itemCode, Long deletionIndicator);

    List<PickupLineV2> findAllByCompanyCodeIdAndPlantIdAndLanguageIdAndWarehouseIdAndStatusIdAndAssignedPickerIdInAndDeletionIndicatorOrderByPickupCreatedOn(
            String companyCodeId, String plantId, String languageId, String warehouseId, Long StatusId, List<String> assignedPickerId, Long deletionIndicator );

    List<PickupLineV2> findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndItemCodeAndManufacturerNameAndPickedStorageBinAndStatusIdAndPickupCreatedOnBetweenAndDeletionIndicator(
            String languageId, String companyCodeId, String plantId, String warehouseId, String itemCode,
            String manufacturerName, String storageBin, Long statusId, Date stockCountDate, Date date, Long deletionIndicator);
}