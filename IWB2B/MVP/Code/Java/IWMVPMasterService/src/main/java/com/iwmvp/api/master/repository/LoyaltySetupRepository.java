package com.iwmvp.api.master.repository;

import com.iwmvp.api.master.model.loyaltysetup.LoyaltySetup;
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
public interface LoyaltySetupRepository extends JpaRepository<LoyaltySetup,String >, JpaSpecificationExecutor<LoyaltySetup> {
    Optional<LoyaltySetup> findByCompanyIdAndLoyaltyIdAndCategoryIdAndLanguageIdAndDeletionIndicator(String companyId, Long  loyaltyId,String categoryId, String languageId, Long deletionIndicator);

    @Query(value = "Select count(*) \n" +
            "from tblmvployaltysetup \n" +
            "where \n" +
            "((:transactionValueFrom between tv_from and tv_to) or\n" +
            "(:transactionValueTo between tv_from and tv_to)) and\n" +
            "(COALESCE(:categoryId,null) IS NULL OR (category_id IN (:categoryId))) and \n"+
            "IS_DELETED = 0", nativeQuery = true)
    public Integer getExistingCount(
            @Param(value = "categoryId") String categoryId,
            @Param(value = "transactionValueFrom") Double transactionValueFrom,
            @Param(value = "transactionValueTo") Double transactionValueTo );

    List<LoyaltySetup> findByCategoryIdAndDeletionIndicator(String categoryId, long l);
}
