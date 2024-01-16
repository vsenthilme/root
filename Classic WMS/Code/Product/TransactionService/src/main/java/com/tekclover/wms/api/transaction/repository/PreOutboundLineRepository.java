package com.tekclover.wms.api.transaction.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tekclover.wms.api.transaction.model.outbound.preoutbound.PreOutboundLine;

@Repository
@Transactional
public interface PreOutboundLineRepository extends JpaRepository<PreOutboundLine, Long>, JpaSpecificationExecutor<PreOutboundLine> {

    public List<PreOutboundLine> findAll();

    public List<PreOutboundLine> findByPreOutboundNo(String preOutboundNo);

    /**
     * @param languageId
     * @param companyCodeId
     * @param plantId
     * @param warehouseId
     * @param refDocNumber
     * @param preOutboundNo
     * @param partnerCode
     * @param lineNumber
     * @param itemCode
     * @param deletionIndicator
     * @return
     */
    public Optional<PreOutboundLine>
    findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndRefDocNumberAndPreOutboundNoAndPartnerCodeAndLineNumberAndItemCodeAndDeletionIndicator(
            String languageId, String companyCodeId, String plantId, String warehouseId, String refDocNumber, String preOutboundNo, String partnerCode,
            Long lineNumber, String itemCode, Long deletionIndicator);

    /**
     * @param languageId
     * @param companyCodeId
     * @param plantId
     * @param warehouseId
     * @param refDocNumber
     * @param preOutboundNo
     * @param partnerCode
     * @param l
     * @return
     */
    public List<PreOutboundLine> findByLanguageIdAndCompanyCodeIdAndPlantIdAndWarehouseIdAndRefDocNumberAndPreOutboundNoAndPartnerCodeAndDeletionIndicator(
            String languageId, String companyCodeId, String plantId, String warehouseId, String refDocNumber,
            String preOutboundNo, String partnerCode, long l);

    public PreOutboundLine findByWarehouseIdAndRefDocNumberAndItemCodeAndLineNumberAndDeletionIndicator(
            String warehouseId, String refDocNumber, String itemCode, Long iineNumber, Long deletionIndicator);

    /**
     * @param warehouseId
     * @param refDocNumber
     * @param statusId
     */
    @Modifying(clearAutomatically = true)
    @Query("UPDATE PreOutboundLine ob SET ob.statusId = :statusId WHERE ob.warehouseId = :warehouseId AND \r\n" +
            "ob.companyCodeId = :companyCodeId AND ob.plantId = :plantId AND ob.languageId = :languageId AND ob.refDocNumber = :refDocNumber")
    void updatePreOutboundLineStatus(@Param("warehouseId") String warehouseId,
                                     @Param("companyCodeId") String companyCodeId,
                                     @Param("plantId") String plantId,
                                     @Param("languageId") String languageId,
                                     @Param("refDocNumber") String refDocNumber,
                                     @Param("statusId") Long statusId);

    @Query(value = " SELECT SUM(ORD_QTY) FROM tblpreoutboundline\r\n"
            + " WHERE REF_DOC_NO = :refDocNumber \r\n"
            + " GROUP BY REF_DOC_NO", nativeQuery = true)
    public Long getSumofOrderedQty(@Param("refDocNumber") String refDocNumber);

    @Query(value = " SELECT COUNT(ORD_QTY) FROM tblpreoutboundline\r\n"
            + " WHERE REF_DOC_NO = :refDocNumber \r\n"
            + " GROUP BY REF_DOC_NO", nativeQuery = true)
    public Long getCountOfOrderedQty(@Param("refDocNumber") String refDocNumber);


}