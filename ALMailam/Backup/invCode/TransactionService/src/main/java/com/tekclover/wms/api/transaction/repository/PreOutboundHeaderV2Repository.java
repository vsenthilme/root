package com.tekclover.wms.api.transaction.repository;

import com.tekclover.wms.api.transaction.model.outbound.pickup.v2.PickupHeaderV2;
import com.tekclover.wms.api.transaction.model.outbound.preoutbound.v2.PreOutboundHeaderV2;
import com.tekclover.wms.api.transaction.repository.fragments.StreamableJpaSpecificationRepository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface PreOutboundHeaderV2Repository extends JpaRepository<PreOutboundHeaderV2, Long>,
        JpaSpecificationExecutor<PreOutboundHeaderV2>, StreamableJpaSpecificationRepository<PreOutboundHeaderV2> {

    Optional<PreOutboundHeaderV2> findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndRefDocNumberAndDeletionIndicator(
            String languageId, String companyCodeId, String plantId, String warehouseId, String refDocNumber, Long deletionIndicator);

    Optional<PreOutboundHeaderV2> findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndRefDocNumberAndPreOutboundNoAndPartnerCodeAndDeletionIndicator(
            String languageId, String companyCodeId, String plantId, String warehouseId,
            String refDocNumber, String preOutboundNo, String partnerCode, Long deletionIndicator);

    PreOutboundHeaderV2 findByPreOutboundNoAndDeletionIndicator(String preOutboundNo, Long deletionIndicator);

    Optional<PreOutboundHeaderV2> findByRefDocNumberAndDeletionIndicator(String refDocumentNo, Long deletionIndicator);

    /**
     * @param warehouseId
     * @param refDocNumber
     * @param statusId
     */
    @Modifying(clearAutomatically = true)
    @Query("UPDATE PreOutboundHeader ib SET ib.statusId = :statusId, REF_FIELD_10 = :refField10, STATUS_TEXT = :refField10 \n" +
            "WHERE ib.languageId = :languageId AND ib.companyCodeId = :companyCodeId AND ib.plantId = :plantId AND ib.warehouseId = :warehouseId AND ib.refDocNumber = :refDocNumber")
    void updatePreOutboundHeaderStatus(@Param("languageId") String languageId,@Param("companyCodeId") String companyCodeId,@Param("plantId") String plantId,@Param("warehouseId") String warehouseId,
                                       @Param("refDocNumber") String refDocNumber, @Param("statusId") Long statusId, @Param("refField10") String refField10);


    @Query(value = "select ht.usr_id from tblhhtuser ht \n" +
            "join tblordertypeid ot on ot.usr_id=ht.usr_id \n" +
            "where ht.is_deleted = 0 and ot.ob_ord_typ_id in (:outboundOrderTypeId) ", nativeQuery = true)
    public List<String> getHHTUserList(
            @Param("outboundOrderTypeId") Long outboundOrderTypeId);

    @Query(value = "select ht.usr_id from tblhhtuser ht \n" +
            "where ht.is_deleted = 0 and ht.level_id in (:levelId) ", nativeQuery = true)
    public List<String> getHHTUserListByLevelId(
            @Param("levelId") Long levelId);


}