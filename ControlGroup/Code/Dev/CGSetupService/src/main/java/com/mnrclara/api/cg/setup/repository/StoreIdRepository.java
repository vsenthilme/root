package com.mnrclara.api.cg.setup.repository;

import com.mnrclara.api.cg.setup.model.IKeyValuePair;
import com.mnrclara.api.cg.setup.model.store.StoreId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface StoreIdRepository extends JpaRepository<StoreId, Long>, JpaSpecificationExecutor<StoreId> {

    Optional<StoreId> findByCompanyIdAndLanguageIdAndStoreIdAndDeletionIndicator(
            String companyId, String languageId, Long storeId, Long deletionIndicator);

    public StoreId findByCompanyIdAndStoreIdAndLanguageIdAndDeletionIndicator(
            String companyId, Long storeId, String languageId, Long deletionIndicator);

    /**
     * @param storeId
     * @param companyId
     * @param languageId
     * @return
     */
    @Query(value = "select tl.store_id AS storeId, tl.store_name AS description \n" +
            "from tblstoreid tl \n" +
            "WHERE \n" +
            "tl.store_id IN (:storeId) and tl.c_id IN (:companyId) and tl.lang_id IN (:languageId) and \n " +
            "tl.is_deleted=0 and tl.status=0 ", nativeQuery = true)

    public IKeyValuePair getStoreIdAndDescription(@Param(value = "storeId") Long storeId,
                                                  @Param(value = "companyId") String companyId,
                                                  @Param(value = "languageId") String languageId);

    @Query(value = "SELECT tc.c_id AS companyId, tc.c_text AS companyDescription, \n " +
            "ti.country_id AS countryId, ti.country_nm AS countryDescription, \n " +
            "ts.state_id AS stateId, ts.state_nm AS stateDescription, \n " +
            "ta.city_id AS cityId, ta.city_nm AS cityDescription  \n " +
            "FROM tblcompanyid tc, \n " +
            "tblcountryid ti, \n " +
            "tblstateid ts, \n " +
            "tblcityid ta \n " +
            "WHERE tc.c_id = ti.c_id AND tc.lang_id = ti.lang_id \n" +
            "AND tc.c_id = ts.c_id AND tc.lang_id = ts.lang_id AND ti.country_id = ts.country_id \n " +
            "AND tc.c_id = ta.c_id AND tc.lang_id = ta.lang_id AND ti.country_id = ta.country_id AND ts.state_id = ta.state_id \n " +
            "AND tc.c_id IN (:companyId) AND tc.lang_id IN (:languageId) \n " +
            "AND ti.country_id IN (:countryId) AND ts.state_id IN (:stateId) AND ta.city_id IN (:cityId) \n " +
            "AND tc.is_deleted = 0 AND ti.is_deleted = 0 AND ta.is_deleted = 0 AND ts.is_deleted = 0 ", nativeQuery = true)
    public IKeyValuePair getDescription(@Param(value = "companyId") String companyId,
                                        @Param(value = "languageId") String languageId,
                                        @Param(value = "countryId") String countryId,
                                        @Param(value = "stateId") String stateId,
                                        @Param(value = "cityId") String cityId);

    @Query(value = "select store_id as storeId, store_name as description from tblstoreid where is_deleted = 0 ", nativeQuery = true)
    List<IKeyValuePair> getStoreDropDown();
    @Modifying
    @Query(value = "update tblstorepartnerlisting set store_nm = :storeName where store_id = :storeId", nativeQuery = true)
    public void updateStorePartnerList(@Param("storeId") Long storeId,
                                       @Param("storeName") String storeName);

    @Modifying
    @Query(value = "update tblownershiprequest set STORE_NM = :storeName where STORE_ID = :storeId", nativeQuery = true)
    public void updateOwnershipRequest(@Param("storeId") Long storeId,
                                       @Param("storeName") String storeName);
}
