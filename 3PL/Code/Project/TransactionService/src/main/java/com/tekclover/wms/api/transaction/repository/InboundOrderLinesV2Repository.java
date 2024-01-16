package com.tekclover.wms.api.transaction.repository;

import com.tekclover.wms.api.transaction.model.warehouse.inbound.v2.InboundOrderLinesV2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface InboundOrderLinesV2Repository extends JpaRepository<InboundOrderLinesV2,Long> {


    List<InboundOrderLinesV2> findByOrderId(String orderId);

    @Query(value = "SELECT * FROM tbliborderlines WHERE order_id = :orderId ", nativeQuery = true)
    public List<InboundOrderLinesV2> getOrderLines(@Param("orderId") String orderId);

    InboundOrderLinesV2 findTopByOrderIdAndItemCode(String refDocNumber, String itemCode);
}