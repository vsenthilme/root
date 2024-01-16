package com.almailem.ams.api.connector.repository;

import com.almailem.ams.api.connector.model.purchasereturn.PurchaseReturnHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface PurchaseReturnHeaderRepository extends JpaRepository<PurchaseReturnHeader, String>,
        JpaSpecificationExecutor<PurchaseReturnHeader> {

    List<PurchaseReturnHeader> findTopByProcessedStatusIdOrderByOrderReceivedOn(long l);

    PurchaseReturnHeader findByReturnOrderNo(String poNumber);

//    @Modifying(clearAutomatically = true)
//    @Query(value = "UPDATE PURCHASERETURNHEADER set processedStatusId = 10, orderProcessedOn = :date  \r\n"
//            + " WHERE ReturnorderNo = :returnOrderNo ", nativeQuery = true)
//    public void updateProcessStatusId(
//            @Param(value = "returnOrderNo") String returnOrderNo,
//            @Param(value = "date") Date date);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE PURCHASERETURNHEADER set processedStatusId = :processedStatusId, orderProcessedOn = getdate()  \r\n"
            + " WHERE PurchaseReturnHeaderId = :purchaseReturnHeaderId ", nativeQuery = true)
    public void updateProcessStatusId (
            @Param(value = "purchaseReturnHeaderId") Long purchaseReturnHeaderId,
            @Param(value = "processedStatusId") Long processedStatusId );

    @Query(value = "select * \n" +
            "from PURCHASERETURNHEADER where Purchasereturnheaderid = :purchaseReturnHeaderId ",nativeQuery = true)
    public PurchaseReturnHeader getPurchaseReturnHeader(@Param(value = "purchaseReturnHeaderId") Long purchaseReturnHeaderId);

    PurchaseReturnHeader findTopByReturnOrderNoOrderByOrderReceivedOnDesc(String returnOrderNo);

    PurchaseReturnHeader findTopByPurchaseReturnHeaderIdAndCompanyCodeAndBranchCodeAndReturnOrderNoOrderByOrderReceivedOnDesc(
            Long purchaseReturnHeaderId, String companyCode, String branchCode, String returnOrderNo);
}
