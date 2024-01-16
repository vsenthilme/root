package com.iwmvp.api.master.repository;

import com.iwmvp.api.master.model.orderdetails.OrderDetailsLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface OrderDetailsLineRepository extends JpaRepository<OrderDetailsLine,String>, JpaSpecificationExecutor<OrderDetailsLine> {
    List<OrderDetailsLine> findByCompanyIdAndOrderIdAndReferenceNoAndLanguageIdAndDeletionIndicator(String companyId, Long orderId, String referenceNo, String languageId, Long deletionIndicator);

    List<OrderDetailsLine> findByOrderIdAndDeletionIndicator(Long orderId, long l);
}
