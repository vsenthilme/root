package com.iwmvp.api.master.repository;

import com.iwmvp.api.master.model.orderdetails.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface OrderDetailsHeaderRepository extends JpaRepository<OrderDetailsHeader,String>, JpaSpecificationExecutor<OrderDetailsHeader> {
    public OrderDetailsHeader findByCompanyIdAndOrderIdAndReferenceNoAndLanguageIdAndDeletionIndicator(String companyId, Long orderId, String referenceNo, String languageId, Long deletionIndicator);


    @Query(value = "select loyalty_point as loyaltyPoint \n"
            + "from tblmvployaltysetup \n"
            + "where \n"
            + "((:deliveryCharge) between tv_from and tv_to) and \n"
            + "(COALESCE(:categoryId,null) IS NULL OR (category_id IN (:categoryId))) and \n"
            + "IS_DELETED = 0", nativeQuery = true)
    public Long getLoyaltyPoint(
            @Param(value = "deliveryCharge") String deliveryCharge,
            @Param(value = "categoryId") String categoryId);

    OrderDetailsHeader findByOrderIdAndDeletionIndicator(Long orderId, long l);
}
