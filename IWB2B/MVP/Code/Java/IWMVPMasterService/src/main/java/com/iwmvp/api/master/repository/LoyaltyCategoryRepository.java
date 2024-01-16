package com.iwmvp.api.master.repository;

import com.iwmvp.api.master.model.loyaltycategory.LoyaltyCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface LoyaltyCategoryRepository extends JpaRepository<LoyaltyCategory,String>, JpaSpecificationExecutor<LoyaltyCategory> {
    Optional<LoyaltyCategory> findByCompanyIdAndCategoryIdAndLanguageIdAndDeletionIndicator(String companyId, String categoryId, String languageId, Long deletionIndicator);

    @Query(value = "Select count(*) \n" +
            "from tblmvployaltycategory \n" +
            "where \n" +
            "((:pointsFrom between points_from and points_to) or\n" +
            "(:pointsTo between points_from and points_to)) and\n" +
            "(COALESCE(:categoryId,null) IS NULL OR (category_id IN (:categoryId))) and \n"+
            "IS_DELETED = 0", nativeQuery = true)
    public Integer getExistingCount(
            @Param(value = "categoryId") String categoryId,
            @Param(value = "pointsFrom") Double pointsFrom,
            @Param(value = "pointsTo") Double pointsTo );

    @Query(value = "select credit_value_pt \n" +
            "from tblmvployaltycategory \n" +
            "where \n" +
            "(:loyaltyPoint between points_from and points_to) and \n" +
            "(COALESCE(:categoryId,null) IS NULL OR (category_id IN (:categoryId))) and \n"+
            "IS_DELETED = 0", nativeQuery = true)
    public Double getCreditValue(
            @Param(value = "categoryId") String categoryId,
            @Param(value = "loyaltyPoint") Double loyaltyPoint);

    Optional<LoyaltyCategory> findByCompanyIdAndLanguageIdAndCategoryIdAndPointsFromAndPointsToAndDeletionIndicator(String companyId, String languageId, String categoryId, Double pointsFrom, Double pointsTo, Long deletionIndicator);

    Optional<LoyaltyCategory> findByRangeIdAndDeletionIndicator(Long rangeId, long l);
}
