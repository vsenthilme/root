package com.almailem.ams.api.connector.repository;

import com.almailem.ams.api.connector.model.stockreceipt.StockReceiptHeader;
import com.almailem.ams.api.connector.model.transferin.TransferInHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface StockReceiptHeaderRepository extends JpaRepository<StockReceiptHeader, String> {

    StockReceiptHeader findByReceiptNo(String asnNumber);

    List<StockReceiptHeader> findTopByProcessedStatusIdOrderByOrderReceivedOn(long l);

    StockReceiptHeader findTopByStockReceiptHeaderIdAndCompanyCodeAndBranchCodeAndReceiptNoOrderByOrderReceivedOnDesc(
            Long stockReceiptHeaderId, String companyCode, String branchCode, String receiptNumber);


    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE STOCKRECEIPTHEADER set processedStatusId = :processedStatusId, orderProcessedOn = getdate()  \r\n"
            + " WHERE StockReceiptHeaderId = :stockReceiptHeaderId ", nativeQuery = true)
    public void updateProcessStatusId (
            @Param(value = "stockReceiptHeaderId") Long stockReceiptHeaderId,
            @Param(value = "processedStatusId") Long processedStatusId );

    @Query(value = "select * \n" +
            "from STOCKRECEIPTHEADER where Stockreceiptheaderid = :stockReceiptHeaderId ",nativeQuery = true)
    public StockReceiptHeader getStockReceiptHeader(@Param(value = "stockReceiptHeaderId") Long stockReceiptHeaderId);
}
