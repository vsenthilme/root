package com.mnrclara.api.cg.transaction.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mnrclara.api.cg.transaction.model.IkeyValuePair;
import com.mnrclara.api.cg.transaction.model.storepartnerlisting.StorePartnerListing;
import com.mnrclara.api.cg.transaction.model.storepartnerlisting.StorePartnerListingImpl;

@Repository
@Transactional
public interface StorePartnerListingRepository extends JpaRepository<StorePartnerListing, Long>,
        JpaSpecificationExecutor<StorePartnerListing>, DynamicNativeQuery {


    Optional<StorePartnerListing> findByCompanyIdAndLanguageIdAndVersionNumberAndStoreIdAndDeletionIndicator(
            String companyId, String languageId, Long versionNumber, String storeId, Long deletionIndicator);


    StorePartnerListing findByCompanyIdAndLanguageIdAndStoreIdAndStatusId2AndDeletionIndicator(
            String companyId, String languageId, String storeId, Long statusId2, Long deletionIndicator);


    @Query(value = "select tl.CO_OWNER_ID_1 AS coOwnerId1 ,tl.CO_OWNER_ID_2 AS coOwnerId2, \n" +
            "tl.CO_OWNER_ID_3 AS coOwnerId3,tl.CO_OWNER_ID_4 AS coOwnerId4,tl.CO_OWNER_ID_5 AS coOwnerId5, \n" +
            "tl.CO_OWNER_ID_6 AS coOwnerId6,tl.CO_OWNER_ID_7 AS coOwnerId7,tl.CO_OWNER_ID_8 AS coOwnerId8, \n" +
            "tl.CO_OWNER_ID_9 AS coOwnerId9,tl.CO_OWNER_ID_10 AS coOwnerId10, \n" +
            "tl.CO_OWNER_NAME_1 AS coOwnerName1,tl.CO_OWNER_NAME_2 AS coOwnerName2,tl.CO_OWNER_NAME_3 AS coOwnerName3, \n" +
            "tl.CO_OWNER_NAME_4 AS coOwnerName4,tl.CO_OWNER_NAME_5 AS coOwnerName5, \n" +
            "tl.CO_OWNER_NAME_6 AS coOwnerName6,tl.CO_OWNER_NAME_7 AS coOwnerName7,tl.CO_OWNER_NAME_8 AS coOwnerName8, \n" +
            "tl.CO_OWNER_NAME_9 AS coOwnerName9,tl.CO_OWNER_NAME_10 AS coOwnerName10,\n " +
            "tl.CO_OWNER_PER_1 AS coOwnerPercentage1,tl.CO_OWNER_PER_2 AS coOwnerPercentage2,tl.CO_OWNER_PER_3 AS coOwnerPercentage3,\n" +
            "tl.CO_OWNER_PER_4 AS coOwnerPercentage4,tl.CO_OWNER_PER_5 AS coOwnerPercentage5,\n" +
            "tl.CO_OWNER_PER_6 AS coOwnerPercentage6,tl.CO_OWNER_PER_7 AS coOwnerPercentage7,tl.CO_OWNER_PER_8 AS coOwnerPercentage8, \n" +
            "tl.CO_OWNER_PER_9 AS coOwnerPercentage9,tl.CO_OWNER_PER_10 AS coOwnerPercentage10,\n" +
            "tl.STORE_ID AS storeId, tl.STORE_NM AS storeName \n" +
            "from tblstorepartnerlisting tl \n " +
            "WHERE \n" +
            "(tl.CO_OWNER_ID_1 IN (:coOwnerId1) OR \n" +
            "tl.CO_OWNER_ID_2 IN (:coOwnerId2) OR \n" +
            "tl.CO_OWNER_ID_3 IN (:coOwnerId3) OR \n" +
            "tl.CO_OWNER_ID_4 IN (:coOwnerId4) OR \n" +
            "tl.CO_OWNER_ID_5 IN (:coOwnerId5) OR \n" +
            "tl.CO_OWNER_ID_6 IN (:coOwnerId6) OR \n" +
            "tl.CO_OWNER_ID_7 IN (:coOwnerId7) OR \n" +
            "tl.CO_OWNER_ID_8 IN (:coOwnerId8) OR \n" +
            "tl.CO_OWNER_ID_9 IN (:coOwnerId9) OR \n" +
            "tl.CO_OWNER_ID_10 IN (:coOwnerId10)) AND \n" +
            "(tl.IS_DELETED = 0 AND tl.STATUS_ID_2 = 0)", nativeQuery = true)
    public List<IkeyValuePair> getLikeMatchResult(@Param("coOwnerId1") Long coOwnerId1,
                                                  @Param("coOwnerId2") Long coOwnerId2,
                                                  @Param("coOwnerId3") Long coOwnerId3,
                                                  @Param("coOwnerId4") Long coOwnerId4,
                                                  @Param("coOwnerId5") Long coOwnerId5,
                                                  @Param("coOwnerId6") Long coOwnerId6,
                                                  @Param("coOwnerId7") Long coOwnerId7,
                                                  @Param("coOwnerId8") Long coOwnerId8,
                                                  @Param("coOwnerId9") Long coOwnerId9,
                                                  @Param("coOwnerId10") Long coOwnerId10);

    @Query(value = "SELECT " +
            "tl.CO_OWNER_ID_1 AS coOwnerId1, tl.CO_OWNER_ID_2 AS coOwnerId2,tl.CO_OWNER_ID_3 AS coOwnerId3, \n" +
            "tl.CO_OWNER_ID_4 AS coOwnerId4, tl.CO_OWNER_ID_5 AS coOwnerId5, \n" +
            "tl.CO_OWNER_ID_6 AS coOwnerId6, tl.CO_OWNER_ID_7 AS coOwnerId7, tl.CO_OWNER_ID_8 AS coOwnerId8, \n" +
            "tl.CO_OWNER_ID_9 AS coOwnerId9, tl.CO_OWNER_ID_10 AS coOwnerId10, \n" +
            "tl.CO_OWNER_NAME_1 AS coOwnerName1, tl.CO_OWNER_NAME_2 AS coOwnerName2, tl.CO_OWNER_NAME_3 AS coOwnerName3, \n" +
            "tl.CO_OWNER_NAME_4 AS coOwnerName4, tl.CO_OWNER_NAME_5 AS coOwnerName5, \n" +
            "tl.CO_OWNER_NAME_6 AS coOwnerName6, tl.CO_OWNER_NAME_7 AS coOwnerName7, tl.CO_OWNER_NAME_8 AS coOwnerName8, \n" +
            "tl.CO_OWNER_NAME_9 AS coOwnerName9, tl.CO_OWNER_NAME_10 AS coOwnerName10, \n" +
            "tl.CO_OWNER_PER_1 AS coOwnerPercentage1, tl.CO_OWNER_PER_2 AS coOwnerPercentage2, tl.CO_OWNER_PER_3 AS coOwnerPercentage3, \n" +
            "tl.CO_OWNER_PER_4 AS coOwnerPercentage4, tl.CO_OWNER_PER_5 AS coOwnerPercentage5, \n " +
            "tl.CO_OWNER_PER_6 AS coOwnerPercentage6, tl.CO_OWNER_PER_7 AS coOwnerPercentage7, tl.CO_OWNER_PER_8 AS coOwnerPercentage8, \n" +
            "tl.CO_OWNER_PER_9 AS coOwnerPercentage9, tl.CO_OWNER_PER_10 AS coOwnerPercentage10, \n " +
            " tl.STORE_ID AS storeId, tl.STORE_NM AS storeName " +
            "FROM tblstorepartnerlisting tl " +
            "WHERE " +
            "((tl.CO_OWNER_ID_1 IS NULL AND :coOwnerId1 IS NULL) OR (tl.CO_OWNER_ID_1 = :coOwnerId1) OR (tl.CO_OWNER_ID_1 = 0 AND :coOwnerId1 IS NULL)) AND " +
            "((tl.CO_OWNER_ID_2 IS NULL AND :coOwnerId2 IS NULL) OR (tl.CO_OWNER_ID_2 = :coOwnerId2) OR (tl.CO_OWNER_ID_2 = 0 AND :coOwnerId2 IS NULL)) AND " +
            "((tl.CO_OWNER_ID_3 IS NULL AND :coOwnerId3 IS NULL) OR (tl.CO_OWNER_ID_3 = :coOwnerId3) OR (tl.CO_OWNER_ID_3 = 0 AND :coOwnerId3 IS NULL)) AND " +
            "((tl.CO_OWNER_ID_4 IS NULL AND :coOwnerId4 IS NULL) OR (tl.CO_OWNER_ID_4 = :coOwnerId4) OR (tl.CO_OWNER_ID_4 = 0 AND :coOwnerId4 IS NULL)) AND " +
            "((tl.CO_OWNER_ID_5 IS NULL AND :coOwnerId5 IS NULL) OR (tl.CO_OWNER_ID_5 = :coOwnerId5) OR (tl.CO_OWNER_ID_5 = 0 AND :coOwnerId5 IS NULL)) AND " +
            "((tl.CO_OWNER_ID_6 IS NULL AND :coOwnerId6 IS NULL) OR (tl.CO_OWNER_ID_6 = :coOwnerId6) OR (tl.CO_OWNER_ID_6 = 0 AND :coOwnerId6 IS NULL)) AND " +
            "((tl.CO_OWNER_ID_7 IS NULL AND :coOwnerId7 IS NULL) OR (tl.CO_OWNER_ID_7 = :coOwnerId7) OR (tl.CO_OWNER_ID_7 = 0 AND :coOwnerId7 IS NULL)) AND " +
            "((tl.CO_OWNER_ID_8 IS NULL AND :coOwnerId8 IS NULL) OR (tl.CO_OWNER_ID_8 = :coOwnerId8) OR (tl.CO_OWNER_ID_8 = 0 AND :coOwnerId8 IS NULL)) AND " +
            "((tl.CO_OWNER_ID_9 IS NULL AND :coOwnerId9 IS NULL) OR (tl.CO_OWNER_ID_9 = :coOwnerId9) OR (tl.CO_OWNER_ID_9 = 0 AND :coOwnerId9 IS NULL)) AND " +
            "((tl.CO_OWNER_ID_10 IS NULL AND :coOwnerId10 IS NULL) OR (tl.CO_OWNER_ID_10 = :coOwnerId10) OR (tl.CO_OWNER_ID_10 = 0 AND :coOwnerId10 IS NULL)) AND " +
            "(tl.IS_DELETED = 0 AND tl.STATUS_ID_2 = 0)", nativeQuery = true)
    public List<IkeyValuePair> getExcerptMatchResult(
            @Param("coOwnerId1") Long coOwnerId1,
            @Param("coOwnerId2") Long coOwnerId2,
            @Param("coOwnerId3") Long coOwnerId3,
            @Param("coOwnerId4") Long coOwnerId4,
            @Param("coOwnerId5") Long coOwnerId5,
            @Param("coOwnerId6") Long coOwnerId6,
            @Param("coOwnerId7") Long coOwnerId7,
            @Param("coOwnerId8") Long coOwnerId8,
            @Param("coOwnerId9") Long coOwnerId9,
            @Param("coOwnerId10") Long coOwnerId10);


//    @Query(value = "select tl.CO_OWNER_ID_1 AS coOwnerId1 ,tl.CO_OWNER_ID_2 AS coOwnerId2, \n" +
//            "tl.CO_OWNER_ID_3 AS coOwnerId3,tl.CO_OWNER_ID_4 AS coOwnerId4,tl.CO_OWNER_ID_5 AS coOwnerId5, \n" +
//            "tl.CO_OWNER_NAME_1 AS coOwnerName1,tl.CO_OWNER_NAME_2 AS coOwnerName2,tl.CO_OWNER_NAME_3 AS coOwnerName3, \n" +
//            "tl.CO_OWNER_NAME_4 AS coOwnerName4,tl.CO_OWNER_NAME_5 AS coOwnerName5,\n " +
//            "tl.CO_OWNER_PER_1 AS coOwnerPercentage1,tl.CO_OWNER_PER_2 AS coOwnerPercentage2,tl.CO_OWNER_PER_3 AS coOwnerPercentage3,\n" +
//            "tl.CO_OWNER_PER_4 AS coOwnerPercentage4,tl.CO_OWNER_PER_5 AS coOwnerPercentage5, tl.STORE_NM AS storeName, \n" +
//            "tl.GROUP_ID AS groupId,tl.GRP_NM AS groupName,tl.GRP_ID AS groupTypeId,tl.SUB_GRP_ID AS subGroupTypeId,\n" +
//            "tl.STORE_ID AS storeId,tl.GRP_TYP_NM AS groupTypeName,tl.SUB_GRP_NM AS subGroupTypeName \n" +
//            "FROM tblstorepartnerlisting tl " +
//            "WHERE " +
//            "(((tl.CO_OWNER_ID_1 IS NULL AND :coOwnerId1 IS NULL) OR (tl.CO_OWNER_ID_1 = :coOwnerId1)) AND " +
//            "((tl.CO_OWNER_ID_2 IS NULL AND :coOwnerId2 IS NULL) OR (tl.CO_OWNER_ID_2 = :coOwnerId2)) AND " +
//            "((tl.CO_OWNER_ID_3 IS NULL AND :coOwnerId3 IS NULL) OR (tl.CO_OWNER_ID_3 = :coOwnerId3)) AND " +
//            "((tl.CO_OWNER_ID_4 IS NULL AND :coOwnerId4 IS NULL) OR (tl.CO_OWNER_ID_4 = :coOwnerId4)) AND " +
//            "((tl.CO_OWNER_ID_5 IS NULL AND :coOwnerId5 IS NULL) OR (tl.CO_OWNER_ID_5 = :coOwnerId5)) AND " +
//            "((tl.CO_OWNER_ID_6 IS NULL AND :coOwnerId6 IS NULL) OR (tl.CO_OWNER_ID_6 = :coOwnerId6)) AND " +
//            "((tl.CO_OWNER_ID_7 IS NULL AND :coOwnerId7 IS NULL) OR (tl.CO_OWNER_ID_7 = :coOwnerId7)) AND " +
//            "((tl.CO_OWNER_ID_8 IS NULL AND :coOwnerId8 IS NULL) OR (tl.CO_OWNER_ID_8 = :coOwnerId8)) AND " +
//            "((tl.CO_OWNER_ID_9 IS NULL AND :coOwnerId9 IS NULL) OR (tl.CO_OWNER_ID_9 = :coOwnerId9)) AND " +
//            "((tl.CO_OWNER_ID_10 IS NULL AND :coOwnerId10 IS NULL) OR (tl.CO_OWNER_ID_10 = :coOwnerId10)) AND " +
//            "(tl.IS_DELETED = 0)) OR (tl.GROUP_ID = :groupId)", nativeQuery = true)
//    public List<IkeyValuePair> getExactMatchGroup( //new added 18-10-23
//                                                         @Param("coOwnerId1") Long coOwnerId1,
//                                                         @Param("coOwnerId2") Long coOwnerId2,
//                                                         @Param("coOwnerId3") Long coOwnerId3,
//                                                         @Param("coOwnerId4") Long coOwnerId4,
//                                                         @Param("coOwnerId5") Long coOwnerId5,
//                                                         @Param("coOwnerId6") Long coOwnerId6,
//                                                         @Param("coOwnerId7") Long coOwnerId7,
//                                                         @Param("coOwnerId8") Long coOwnerId8,
//                                                         @Param("coOwnerId9") Long coOwnerId9,
//                                                         @Param("coOwnerId10") Long coOwnerId10,
//                                                         @Param("groupId") String groupId);

//    @Query(value = "select tl.CO_OWNER_ID_1 AS coOwnerId1 ,tl.CO_OWNER_ID_2 AS coOwnerId2, \n" +
//            "tl.CO_OWNER_ID_3 AS coOwnerId3,tl.CO_OWNER_ID_4 AS coOwnerId4,tl.CO_OWNER_ID_5 AS coOwnerId5, \n" +
//            "tl.CO_OWNER_NAME_1 AS coOwnerName1,tl.CO_OWNER_NAME_2 AS coOwnerName2,tl.CO_OWNER_NAME_3 AS coOwnerName3, \n" +
//            "tl.CO_OWNER_NAME_4 AS coOwnerName4,tl.CO_OWNER_NAME_5 AS coOwnerName5,\n " +
//            "tl.CO_OWNER_PER_1 AS coOwnerPercentage1,tl.CO_OWNER_PER_2 AS coOwnerPercentage2,tl.CO_OWNER_PER_3 AS coOwnerPercentage3,\n" +
//            "tl.CO_OWNER_PER_4 AS coOwnerPercentage4,tl.CO_OWNER_PER_5 AS coOwnerPercentage5, tl.STORE_NM AS storeName, \n" +
//            "tl.GROUP_ID AS groupId,tl.GRP_NM AS groupName,tl.GRP_ID AS groupTypeId,tl.SUB_GRP_ID AS subGroupTypeId,\n" +
//            "tl.STORE_ID AS storeId,tl.GRP_TYP_NM AS groupTypeName,tl.SUB_GRP_NM AS subGroupTypeName \n" +
//            "from tblstorepartnerlisting tl \n " +
//            "WHERE \n" +
//            "(tl.CO_OWNER_ID_1 IN (:coOwnerId1) OR \n" +
//            "tl.CO_OWNER_ID_2 IN (:coOwnerId2) OR \n" +
//            "tl.CO_OWNER_ID_3 IN (:coOwnerId3) OR \n" +
//            "tl.CO_OWNER_ID_4 IN (:coOwnerId4) OR \n" +
//            "tl.CO_OWNER_ID_5 IN (:coOwnerId5) OR \n" +
//            "tl.CO_OWNER_ID_6 IN (:coOwnerId6) OR \n" +
//            "tl.CO_OWNER_ID_7 IN (:coOwnerId7) OR \n" +
//            "tl.CO_OWNER_ID_8 IN (:coOwnerId8) OR \n" +
//            "tl.CO_OWNER_ID_9 IN (:coOwnerId9) OR \n" +
//            "tl.CO_OWNER_ID_10 IN (:coOwnerId10) OR \n" +
//            "tl.GROUP_ID IN (:groupId)) AND \n" +
//            "tl.IS_DELETED = 0", nativeQuery = true) //new added 18-10-23
//    public List<IkeyValuePair> getLikeGroup(@Param("coOwnerId1") Long coOwnerId1,
//                                                 @Param("coOwnerId2") Long coOwnerId2,
//                                                 @Param("coOwnerId3") Long coOwnerId3,
//                                                 @Param("coOwnerId4") Long coOwnerId4,
//                                                 @Param("coOwnerId5") Long coOwnerId5,
//                                                 @Param("coOwnerId6") Long coOwnerId6,
//                                                 @Param("coOwnerId7") Long coOwnerId7,
//                                                 @Param("coOwnerId8") Long coOwnerId8,
//                                                 @Param("coOwnerId9") Long coOwnerId9,
//                                                 @Param("coOwnerId10") Long coOwnerId10,
//                                                 @Param("groupId") String groupId);

//    @Query(value = "select max(version_no) version_no,group_id,store_id into #spl from tblstorepartnerlisting where is_deleted = 0 and \n" +
//            "(COALESCE(:groupId,null) IS NULL OR (GROUP_ID IN (:groupId))) \n" +
//            "group by group_id,store_id \n" +
//            "select * from (\n" +
//            "select tl.version_no versionNumber, tl.CO_OWNER_ID_1 AS coOwnerId1 ,tl.CO_OWNER_ID_2 AS coOwnerId2, \n" +
//            "tl.CO_OWNER_ID_3 AS coOwnerId3,tl.CO_OWNER_ID_4 AS coOwnerId4,tl.CO_OWNER_ID_5 AS coOwnerId5, \n" +
//            "tl.CO_OWNER_ID_6 AS coOwnerId6,tl.CO_OWNER_ID_7 AS coOwnerId7,tl.CO_OWNER_ID_8 AS coOwnerId8, \n" +
//            "tl.CO_OWNER_ID_9 AS coOwnerId9,tl.CO_OWNER_ID_10 AS coOwnerId10, \n" +
//            "tl.CO_OWNER_NAME_1 AS coOwnerName1,tl.CO_OWNER_NAME_2 AS coOwnerName2,tl.CO_OWNER_NAME_3 AS coOwnerName3, \n" +
//            "tl.CO_OWNER_NAME_4 AS coOwnerName4,tl.CO_OWNER_NAME_5 AS coOwnerName5,\n " +
//            "tl.CO_OWNER_NAME_6 AS coOwnerName6,tl.CO_OWNER_NAME_7 AS coOwnerName7,tl.CO_OWNER_NAME_8 AS coOwnerName8, \n" +
//            "tl.CO_OWNER_NAME_9 AS coOwnerName9,tl.CO_OWNER_NAME_10 AS coOwnerName10,\n " +
//            "tl.CO_OWNER_PER_1 AS coOwnerPercentage1,tl.CO_OWNER_PER_2 AS coOwnerPercentage2,tl.CO_OWNER_PER_3 AS coOwnerPercentage3,\n" +
//            "tl.CO_OWNER_PER_4 AS coOwnerPercentage4,tl.CO_OWNER_PER_5 AS coOwnerPercentage5, \n" +
//            "tl.CO_OWNER_PER_6 AS coOwnerPercentage6,tl.CO_OWNER_PER_7 AS coOwnerPercentage7,tl.CO_OWNER_PER_8 AS coOwnerPercentage8,\n" +
//            "tl.CO_OWNER_PER_9 AS coOwnerPercentage9,tl.CO_OWNER_PER_10 AS coOwnerPercentage10, tl.STORE_NM AS storeName, \n" +
//            "tl.GROUP_ID AS groupId,tl.GRP_NM AS groupName,tl.GRP_ID AS groupTypeId,tl.SUB_GRP_ID AS subGroupTypeId,\n" +
//            "tl.STORE_ID AS storeId,tl.GRP_TYP_NM AS groupTypeName,tl.SUB_GRP_NM AS subGroupTypeName \n" +
//            "FROM tblstorepartnerlisting tl \n" +
//            "WHERE \n" +
//            "(((tl.CO_OWNER_ID_1 IS NULL AND :coOwnerId1 IS NULL) OR (tl.CO_OWNER_ID_1 = :coOwnerId1) OR (tl.CO_OWNER_ID_1 = 0 AND :coOwnerId1 IS NULL)) AND \n" +
//            "((tl.CO_OWNER_ID_2 IS NULL AND :coOwnerId2 IS NULL) OR (tl.CO_OWNER_ID_2 = :coOwnerId2) OR (tl.CO_OWNER_ID_2 = 0 AND :coOwnerId2 IS NULL)) AND \n" +
//            "((tl.CO_OWNER_ID_3 IS NULL AND :coOwnerId3 IS NULL) OR (tl.CO_OWNER_ID_3 = :coOwnerId3) OR (tl.CO_OWNER_ID_3 = 0 AND :coOwnerId3 IS NULL)) AND \n" +
//            "((tl.CO_OWNER_ID_4 IS NULL AND :coOwnerId4 IS NULL) OR (tl.CO_OWNER_ID_4 = :coOwnerId4) OR (tl.CO_OWNER_ID_4 = 0 AND :coOwnerId4 IS NULL)) AND \n" +
//            "((tl.CO_OWNER_ID_5 IS NULL AND :coOwnerId5 IS NULL) OR (tl.CO_OWNER_ID_5 = :coOwnerId5) OR (tl.CO_OWNER_ID_5 = 0 AND :coOwnerId5 IS NULL)) AND \n" +
//            "((tl.CO_OWNER_ID_6 IS NULL AND :coOwnerId6 IS NULL) OR (tl.CO_OWNER_ID_6 = :coOwnerId6) OR (tl.CO_OWNER_ID_6 = 0 AND :coOwnerId6 IS NULL)) AND \n" +
//            "((tl.CO_OWNER_ID_7 IS NULL AND :coOwnerId7 IS NULL) OR (tl.CO_OWNER_ID_7 = :coOwnerId7) OR (tl.CO_OWNER_ID_7 = 0 AND :coOwnerId7 IS NULL)) AND \n" +
//            "((tl.CO_OWNER_ID_8 IS NULL AND :coOwnerId8 IS NULL) OR (tl.CO_OWNER_ID_8 = :coOwnerId8) OR (tl.CO_OWNER_ID_8 = 0 AND :coOwnerId8 IS NULL)) AND \n" +
//            "((tl.CO_OWNER_ID_9 IS NULL AND :coOwnerId9 IS NULL) OR (tl.CO_OWNER_ID_9 = :coOwnerId9) OR (tl.CO_OWNER_ID_9 = 0 AND :coOwnerId9 IS NULL)) AND \n" +
//            "((tl.CO_OWNER_ID_10 IS NULL AND :coOwnerId10 IS NULL) OR (tl.CO_OWNER_ID_10 = :coOwnerId10) OR (tl.CO_OWNER_ID_10 = 0 AND :coOwnerId10 IS NULL)) AND \n" +
//            "(tl.IS_DELETED = 0)AND(tl.STATUS_ID_2=0)) OR (tl.GROUP_ID = :groupId) \n" +
//            ") x where x.versionNumber in (select version_no from #spl)", nativeQuery = true)
//    public List<IkeyValuePair> getExactMatchResultGroup(
//            @Param("coOwnerId1") Long coOwnerId1,
//            @Param("coOwnerId2") Long coOwnerId2,
//            @Param("coOwnerId3") Long coOwnerId3,
//            @Param("coOwnerId4") Long coOwnerId4,
//            @Param("coOwnerId5") Long coOwnerId5,
//            @Param("coOwnerId6") Long coOwnerId6,
//            @Param("coOwnerId7") Long coOwnerId7,
//            @Param("coOwnerId8") Long coOwnerId8,
//            @Param("coOwnerId9") Long coOwnerId9,
//            @Param("coOwnerId10") Long coOwnerId10,
//            @Param("groupId") String groupId);

    @Query(value = "SELECT MAX(version_no) AS version_no, group_id, store_id INTO #spl FROM tblstorepartnerlisting WHERE is_deleted = 0 AND " +
            "(COALESCE(:groupId, NULL) IS NULL OR (GROUP_ID IN (:groupId))) " +
            "GROUP BY group_id, store_id " +
            "SELECT * FROM (" +
            "SELECT tl.version_no AS versionNumber, tl.CO_OWNER_ID_1 AS coOwnerId1, tl.CO_OWNER_ID_2 AS coOwnerId2, " +
            "tl.CO_OWNER_ID_3 AS coOwnerId3, tl.CO_OWNER_ID_4 AS coOwnerId4, tl.CO_OWNER_ID_5 AS coOwnerId5, " +
            "tl.CO_OWNER_ID_6 AS coOwnerId6, tl.CO_OWNER_ID_7 AS coOwnerId7, tl.CO_OWNER_ID_8 AS coOwnerId8, " +
            "tl.CO_OWNER_ID_9 AS coOwnerId9, tl.CO_OWNER_ID_10 AS coOwnerId10, " +
            "tl.CO_OWNER_NAME_1 AS coOwnerName1, tl.CO_OWNER_NAME_2 AS coOwnerName2, tl.CO_OWNER_NAME_3 AS coOwnerName3, " +
            "tl.CO_OWNER_NAME_4 AS coOwnerName4, tl.CO_OWNER_NAME_5 AS coOwnerName5, " +
            "tl.CO_OWNER_NAME_6 AS coOwnerName6, tl.CO_OWNER_NAME_7 AS coOwnerName7, tl.CO_OWNER_NAME_8 AS coOwnerName8, " +
            "tl.CO_OWNER_NAME_9 AS coOwnerName9, tl.CO_OWNER_NAME_10 AS coOwnerName10, " +
            "tl.CO_OWNER_PER_1 AS coOwnerPercentage1, tl.CO_OWNER_PER_2 AS coOwnerPercentage2, tl.CO_OWNER_PER_3 AS coOwnerPercentage3, " +
            "tl.CO_OWNER_PER_4 AS coOwnerPercentage4, tl.CO_OWNER_PER_5 AS coOwnerPercentage5, " +
            "tl.CO_OWNER_PER_6 AS coOwnerPercentage6, tl.CO_OWNER_PER_7 AS coOwnerPercentage7, tl.CO_OWNER_PER_8 AS coOwnerPercentage8, " +
            "tl.CO_OWNER_PER_9 AS coOwnerPercentage9, tl.CO_OWNER_PER_10 AS coOwnerPercentage10, tl.STORE_NM AS storeName, " +
            "tl.GROUP_ID AS groupId, tl.GRP_NM AS groupName, tl.GRP_ID AS groupTypeId, tl.SUB_GRP_ID AS subGroupTypeId, " +
            "tl.STORE_ID AS storeId, tl.GRP_TYP_NM AS groupTypeName, tl.SUB_GRP_NM AS subGroupTypeName " +
            "FROM tblstorepartnerlisting tl " +
            "WHERE " +
            "(:coOwnerId1 IS NULL OR :coOwnerId1 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10)) AND " +
            "(:coOwnerId2 IS NULL OR :coOwnerId2 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10)) AND " +
            "(:coOwnerId3 IS NULL OR :coOwnerId3 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10)) AND " +
            "(:coOwnerId4 IS NULL OR :coOwnerId4 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10)) AND " +
            "(:coOwnerId5 IS NULL OR :coOwnerId5 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10)) AND " +
            "(:coOwnerId6 IS NULL OR :coOwnerId6 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10)) AND " +
            "(:coOwnerId7 IS NULL OR :coOwnerId7 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10)) AND " +
            "(:coOwnerId8 IS NULL OR :coOwnerId8 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10)) AND " +
            "(:coOwnerId9 IS NULL OR :coOwnerId9 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10)) AND " +
            "(:coOwnerId10 IS NULL OR :coOwnerId10 IN (tl.CO_OWNER_ID_1, tl.CO_OWNER_ID_2, tl.CO_OWNER_ID_3, tl.CO_OWNER_ID_4, tl.CO_OWNER_ID_5, tl.CO_OWNER_ID_6, tl.CO_OWNER_ID_7, tl.CO_OWNER_ID_8, tl.CO_OWNER_ID_9, tl.CO_OWNER_ID_10)) AND " +
            "(tl.IS_DELETED = 0) AND (tl.STATUS_ID_2 = 0) " +
            ") x WHERE x.versionNumber IN (SELECT version_no FROM #spl)", nativeQuery = true)
    List<IkeyValuePair> getExactMatchResultGroup(
            @Param("coOwnerId1") Long coOwnerId1,
            @Param("coOwnerId2") Long coOwnerId2,
            @Param("coOwnerId3") Long coOwnerId3,
            @Param("coOwnerId4") Long coOwnerId4,
            @Param("coOwnerId5") Long coOwnerId5,
            @Param("coOwnerId6") Long coOwnerId6,
            @Param("coOwnerId7") Long coOwnerId7,
            @Param("coOwnerId8") Long coOwnerId8,
            @Param("coOwnerId9") Long coOwnerId9,
            @Param("coOwnerId10") Long coOwnerId10,
            @Param("groupId") String groupId);

    @Query(value = "select max(version_no) version_no,group_id,store_id into #spl from tblstorepartnerlisting where is_deleted = 0 and \n" +
            "(COALESCE(:groupId,null) IS NULL OR (GROUP_ID IN (:groupId))) \n" +
            "group by group_id,store_id \n" +
            "select * from (\n" +
            "select tl.version_no versionNumber, tl.CO_OWNER_ID_1 AS coOwnerId1 ,tl.CO_OWNER_ID_2 AS coOwnerId2, \n" +
            "tl.CO_OWNER_ID_3 AS coOwnerId3,tl.CO_OWNER_ID_4 AS coOwnerId4,tl.CO_OWNER_ID_5 AS coOwnerId5, \n" +
            "tl.CO_OWNER_ID_6 AS coOwnerId6,tl.CO_OWNER_ID_7 AS coOwnerId7,tl.CO_OWNER_ID_8 AS coOwnerId8, \n" +
            "tl.CO_OWNER_ID_9 AS coOwnerId9,tl.CO_OWNER_ID_10 AS coOwnerId10, \n" +
            "tl.CO_OWNER_NAME_1 AS coOwnerName1,tl.CO_OWNER_NAME_2 AS coOwnerName2,tl.CO_OWNER_NAME_3 AS coOwnerName3, \n" +
            "tl.CO_OWNER_NAME_4 AS coOwnerName4,tl.CO_OWNER_NAME_5 AS coOwnerName5,\n " +
            "tl.CO_OWNER_NAME_6 AS coOwnerName6,tl.CO_OWNER_NAME_7 AS coOwnerName7,tl.CO_OWNER_NAME_8 AS coOwnerName8, \n" +
            "tl.CO_OWNER_NAME_9 AS coOwnerName9,tl.CO_OWNER_NAME_10 AS coOwnerName10,\n " +
            "tl.CO_OWNER_PER_1 AS coOwnerPercentage1,tl.CO_OWNER_PER_2 AS coOwnerPercentage2,tl.CO_OWNER_PER_3 AS coOwnerPercentage3,\n" +
            "tl.CO_OWNER_PER_4 AS coOwnerPercentage4,tl.CO_OWNER_PER_5 AS coOwnerPercentage5,  \n" +
            "tl.CO_OWNER_PER_6 AS coOwnerPercentage6,tl.CO_OWNER_PER_7 AS coOwnerPercentage7,tl.CO_OWNER_PER_8 AS coOwnerPercentage8,\n" +
            "tl.CO_OWNER_PER_9 AS coOwnerPercentage9,tl.CO_OWNER_PER_10 AS coOwnerPercentage10, tl.STORE_NM AS storeName, \n" +
            "tl.GROUP_ID AS groupId,tl.GRP_NM AS groupName,tl.GRP_ID AS groupTypeId,tl.SUB_GRP_ID AS subGroupTypeId,\n" +
            "tl.STORE_ID AS storeId,tl.GRP_TYP_NM AS groupTypeName,tl.SUB_GRP_NM AS subGroupTypeName \n" +
            "from tblstorepartnerlisting tl \n " +
            "WHERE \n" +
            "(tl.CO_OWNER_ID_1 IN (:coOwnerId1) OR \n" +
            "tl.CO_OWNER_ID_2 IN (:coOwnerId2) OR \n" +
            "tl.CO_OWNER_ID_3 IN (:coOwnerId3) OR \n" +
            "tl.CO_OWNER_ID_4 IN (:coOwnerId4) OR \n" +
            "tl.CO_OWNER_ID_5 IN (:coOwnerId5) OR \n" +
            "tl.CO_OWNER_ID_6 IN (:coOwnerId6) OR \n" +
            "tl.CO_OWNER_ID_7 IN (:coOwnerId7) OR \n" +
            "tl.CO_OWNER_ID_8 IN (:coOwnerId8) OR \n" +
            "tl.CO_OWNER_ID_9 IN (:coOwnerId9) OR \n" +
            "tl.CO_OWNER_ID_10 IN (:coOwnerId10) OR \n" +
            "tl.GROUP_ID IN (:groupId)) AND \n" +
            "tl.IS_DELETED = 0 AND tl.STATUS_ID_2=0 \n" +
            ") x where x.versionNumber in (select version_no from #spl)", nativeQuery = true)
    public List<IkeyValuePair> getLikeMatchGroup(@Param("coOwnerId1") Long coOwnerId1,
                                                 @Param("coOwnerId2") Long coOwnerId2,
                                                 @Param("coOwnerId3") Long coOwnerId3,
                                                 @Param("coOwnerId4") Long coOwnerId4,
                                                 @Param("coOwnerId5") Long coOwnerId5,
                                                 @Param("coOwnerId6") Long coOwnerId6,
                                                 @Param("coOwnerId7") Long coOwnerId7,
                                                 @Param("coOwnerId8") Long coOwnerId8,
                                                 @Param("coOwnerId9") Long coOwnerId9,
                                                 @Param("coOwnerId10") Long coOwnerId10,
                                                 @Param("groupId") String groupId);

    @Query(value = "select version_no as versionNumber, co_owner_id_1 as coOwnerId1, co_owner_id_2 as coOwnerId2, co_owner_id_3 as coOwnerId3, \n"
            + " co_owner_id_4 as coOwnerId4, co_owner_id_5 as coOwnerId5, co_owner_id_6 as coOwnerId6, co_owner_id_7 as coOwnerId7, co_owner_id_8 as coOwnerId8, \n"
            + " co_owner_id_9 as coOwnerId9, co_owner_id_10 as coOwnerId10, co_owner_name_1 as coOwnerName1, co_owner_name_2 as coOwnerName2, co_owner_name_3 as coOwnerName3, \n"
            + " co_owner_name_4 as coOwnerName4, co_owner_name_5 as coOwnerName5, co_owner_name_6 as coOwnerName6, co_owner_name_7 as coOwnerName7, co_owner_name_8 as coOwnerName8,\n"
            + " co_owner_name_9 as coOwnerName9, co_owner_name_10 as coOwnerName10, co_owner_per_1 as coOwnerPercentage1, co_owner_per_2 as coOwnerPercentage2, \n"
            + " co_owner_per_3 as coOwnerPercentage3, co_owner_per_4 as coOwnerPercentage4, co_owner_per_5 as coOwnerPercentage5, co_owner_per_6 as coOwnerPercentage6, \n"
            + " co_owner_per_7 as coOwnerPercentage7, co_owner_per_8 as coOwnerPercentage8, co_owner_per_9 as coOwnerPercentage9, \n"
            + " co_owner_per_10 as coOwnerPercentage10, store_nm as storeName, group_id as groupId, grp_nm as groupName, grp_id as groupTypeId, sub_grp_id as subGroupTypeId, store_id as storeId, \n "
            + " grp_typ_nm as groupTypeName, sub_grp_nm as subGroupTypeName from tblstorepartnerlisting \n "
            + " where \n"
            + " ((:coOwnerId1 is null or :coOwnerId1 in (co_owner_id_1, co_owner_id_2, co_owner_id_3, co_owner_id_4, co_owner_id_5, co_owner_id_6, co_owner_id_7, co_owner_id_8, co_owner_id_9, co_owner_id_10)) or \n "
            + " (:coOwnerId2 is null or :coOwnerId2 in (co_owner_id_1, co_owner_id_2, co_owner_id_3, co_owner_id_4, co_owner_id_5, co_owner_id_6, co_owner_id_7, co_owner_id_8, co_owner_id_9, co_owner_id_10)) or \n "
            + " (:coOwnerId3 is null or :coOwnerId3 in (co_owner_id_1, co_owner_id_2, co_owner_id_3, co_owner_id_4, co_owner_id_5, co_owner_id_6, co_owner_id_7, co_owner_id_8, co_owner_id_9, co_owner_id_10)) or \n "
            + " (:coOwnerId4 is null or :coOwnerId4 in (co_owner_id_1, co_owner_id_2, co_owner_id_3, co_owner_id_4, co_owner_id_5, co_owner_id_6, co_owner_id_7, co_owner_id_8, co_owner_id_9, co_owner_id_10)) or \n "
            + " (:coOwnerId5 is null or :coOwnerId5 in (co_owner_id_1, co_owner_id_2, co_owner_id_3, co_owner_id_4, co_owner_id_5, co_owner_id_6, co_owner_id_7, co_owner_id_8, co_owner_id_9, co_owner_id_10)) or \n "
            + " (:coOwnerId6 is null or :coOwnerId6 in (co_owner_id_1, co_owner_id_2, co_owner_id_3, co_owner_id_4, co_owner_id_5, co_owner_id_6, co_owner_id_7, co_owner_id_8, co_owner_id_9, co_owner_id_10)) or \n "
            + " (:coOwnerId7 is null or :coOwnerId7 in (co_owner_id_1, co_owner_id_2, co_owner_id_3, co_owner_id_4, co_owner_id_5, co_owner_id_6, co_owner_id_7, co_owner_id_8, co_owner_id_9, co_owner_id_10)) or \n "
            + " (:coOwnerId8 is null or :coOwnerId8 in (co_owner_id_1, co_owner_id_2, co_owner_id_3, co_owner_id_4, co_owner_id_5, co_owner_id_6, co_owner_id_7, co_owner_id_8, co_owner_id_9, co_owner_id_10)) or \n "
            + " (:coOwnerId9 is null or :coOwnerId9 in (co_owner_id_1, co_owner_id_2, co_owner_id_3, co_owner_id_4, co_owner_id_5, co_owner_id_6, co_owner_id_7, co_owner_id_8, co_owner_id_9, co_owner_id_10)) or \n "
            + " (:coOwnerId10 is null or :coOwnerId10 in (co_owner_id_1, co_owner_id_2, co_owner_id_3, co_owner_id_4, co_owner_id_5, co_owner_id_6, co_owner_id_7, co_owner_id_8, co_owner_id_9, co_owner_id_10)) or \n "
            + " (:groupId is null or :groupId in (group_id)) and \n "
            + " (is_deleted = 0) and (status_id_2 = 0))", nativeQuery = true)
    List<IkeyValuePair> getLikeMatchResult(@Param("coOwnerId1") Long coOwnerId1,
                                           @Param("coOwnerId2") Long coOwnerId2,
                                           @Param("coOwnerId3") Long coOwnerId3,
                                           @Param("coOwnerId4") Long coOwnerId4,
                                           @Param("coOwnerId5") Long coOwnerId5,
                                           @Param("coOwnerId6") Long coOwnerId6,
                                           @Param("coOwnerId7") Long coOwnerId7,
                                           @Param("coOwnerId8") Long coOwnerId8,
                                           @Param("coOwnerId9") Long coOwnerId9,
                                           @Param("coOwnerId10") Long coOwnerId10,
                                           @Param("groupId") String groupId);

//    @Query(value = "select * from tblstorepartnerlisting \n" +
//            "where \n" +
//            "is_deleted = 0", nativeQuery = true)
//    public List<StorePartnerListing> getStorePartner();

    @Query(value = "select max(version_no) version_no,group_id,store_id into #spl from tblstorepartnerlisting where is_deleted = 0 group by group_id,store_id \n" +
            "select * from (\n" +
            "select * from tblstorepartnerlisting \n" +
            "where \n" +
            "is_deleted = 0 \n" +
            ") x where x.version_no in (select version_no from #spl) ", nativeQuery = true)
    public List<StorePartnerListing> getStorePartner();


    @Query(value = "select max(VERSION_NO)+1 \n" +
            "from tblstorepartnerlisting", nativeQuery = true)
    public Long getVersionId();

    @Query(value = "select max(version_no) version_no,group_id,store_id into #spl from tblstorepartnerlisting where is_deleted = 0 group by group_id,store_id \n" +
            "select * from (\n" +
            "select LANG_ID languageId,\n" +
            "C_ID companyId,\n" +
            "STORE_ID storeId,\n" +
            "VERSION_NO versionNumber,\n" +
            "STORE_NM storeName,\n" +
            "VALID_DATE_FROM validityDateFrom,\n" +
            "VALID_DATE_TO validityDateTo,\n" +
            "GRP_ID groupTypeId,\n" +
            "GRP_TYP_NM groupTypeName,\n" +
            "SUB_GRP_ID subGroupId,\n" +
            "SUB_GRP_NM subGroupName,\n" +
            "GROUP_ID groupId,\n" +
            "GRP_NM groupName,\n" +
            "CO_OWNER_ID_1 coOwnerId1,\n" +
            "CO_OWNER_ID_2 coOwnerId2,\n" +
            "CO_OWNER_ID_3 coOwnerId3,\n" +
            "CO_OWNER_ID_4 coOwnerId4,\n" +
            "CO_OWNER_ID_5 coOwnerId5,\n" +
            "CO_OWNER_ID_6 coOwnerId6,\n" +
            "CO_OWNER_ID_7 coOwnerId7,\n" +
            "CO_OWNER_ID_8 coOwnerId8,\n" +
            "CO_OWNER_ID_9 coOwnerId9,\n" +
            "CO_OWNER_ID_10 coOwnerId10,\n" +
            "CO_OWNER_NAME_1 coOwnerName1,\n" +
            "CO_OWNER_NAME_2 coOwnerName2,\n" +
            "CO_OWNER_NAME_3 coOwnerName3,\n" +
            "CO_OWNER_NAME_4 coOwnerName4,\n" +
            "CO_OWNER_NAME_5 coOwnerName5,\n" +
            "CO_OWNER_NAME_6 coOwnerName6,\n" +
            "CO_OWNER_NAME_7 coOwnerName7,\n" +
            "CO_OWNER_NAME_8 coOwnerName8,\n" +
            "CO_OWNER_NAME_9 coOwnerName9,\n" +
            "CO_OWNER_NAME_10 coOwnerName10,\n" +
            "CO_OWNER_PER_1 coOwnerPercentage1,\n" +
            "CO_OWNER_PER_2 coOwnerPercentage2,\n" +
            "CO_OWNER_PER_3 coOwnerPercentage3,\n" +
            "CO_OWNER_PER_4 coOwnerPercentage4,\n" +
            "CO_OWNER_PER_5 coOwnerPercentage5,\n" +
            "CO_OWNER_PER_6 coOwnerPercentage6,\n" +
            "CO_OWNER_PER_7 coOwnerPercentage7,\n" +
            "CO_OWNER_PER_8 coOwnerPercentage8,\n" +
            "CO_OWNER_PER_9 coOwnerPercentage9,\n" +
            "CO_OWNER_PER_10 coOwnerPercentage10,\n" +
            "STATUS_ID statusId,\n" +
            "IS_DELETED deletionIndicator,\n" +
            "REF_FIELD_1 referenceField1,\n" +
            "REF_FIELD_2 referenceField2,\n" +
            "REF_FIELD_3 referenceField3,\n" +
            "REF_FIELD_4 referenceField4,\n" +
            "REF_FIELD_5 referenceField5,\n" +
            "REF_FIELD_6 referenceField6,\n" +
            "REF_FIELD_7 referenceField7,\n" +
            "REF_FIELD_8 referenceField8,\n" +
            "REF_FIELD_9 referenceField9,\n" +
            "REF_FIELD_10 referenceField10,\n" +
            "CTD_BY createdBy,\n" +
            "CTD_ON createdOn,\n" +
            "UTD_BY updatedBy,\n" +
            "UTD_ON updatedOn \n" +
            "from tblstorepartnerlisting \n" +
            "where \n" +
            "(COALESCE(:companyId,null) IS NULL OR (C_ID IN (:companyId))) and\n" +
            "(COALESCE(:languageId,null) IS NULL OR (LANG_ID IN (:languageId))) and\n" +
            "(COALESCE(:versionNumber,null) IS NULL OR (VERSION_NO IN (:versionNumber))) and \n" +
            "(COALESCE(:groupTypeId,null) IS NULL OR (GRP_ID IN (:groupTypeId))) and  \n" +
            "(COALESCE(:storeId,null) IS NULL OR (STORE_ID IN (:storeId))) and  \n" +
            "(COALESCE(:groupId,null) IS NULL OR (GROUP_ID IN (:groupId))) and  \n" +
            "(COALESCE(:subGroupId,null) IS NULL OR (SUB_GRP_ID IN (:subGroupId))) and \n" +
            " is_deleted = 0 and status_id_2 = 0  \n" +
            " ) x where x.versionNumber in (select version_no from #spl) ", nativeQuery = true)
    public List<StorePartnerListingImpl> findStorePartnerListing(@Param(value = "companyId") List<String> companyId,
                                                                 @Param(value = "languageId") List<String> languageId,
                                                                 @Param(value = "versionNumber") List<Long> versionNumber,
                                                                 @Param(value = "groupTypeId") List<Long> groupTypeId,
                                                                 @Param(value = "storeId") List<String> storeId,
                                                                 @Param(value = "groupId") List<Long> groupId,
                                                                 @Param(value = "subGroupId") List<Long> subGroupId);


    @Query(value = "select tl.CO_OWNER_ID_1 AS coOwnerId1 ,tl.CO_OWNER_ID_2 AS coOwnerId2, \n" +
            "tl.CO_OWNER_ID_3 AS coOwnerId3,tl.CO_OWNER_ID_4 AS coOwnerId4,tl.CO_OWNER_ID_5 AS coOwnerId5, \n" +
            "tl.CO_OWNER_NAME_1 AS coOwnerName1,tl.CO_OWNER_NAME_2 AS coOwnerName2,tl.CO_OWNER_NAME_3 AS coOwnerName3, \n" +
            "tl.CO_OWNER_NAME_4 AS coOwnerName4,tl.CO_OWNER_NAME_5 AS coOwnerName5,\n " +
            "tl.CO_OWNER_PER_1 AS coOwnerPercentage1,tl.CO_OWNER_PER_2 AS coOwnerPercentage2,tl.CO_OWNER_PER_3 AS coOwnerPercentage3,\n" +
            "tl.CO_OWNER_PER_4 AS coOwnerPercentage4,tl.CO_OWNER_PER_5 AS coOwnerPercentage5, tl.STORE_NM AS storeName, \n" +
            "tl.GROUP_ID AS groupId,tl.GRP_NM AS groupName,tl.GRP_ID AS groupTypeId,tl.SUB_GRP_ID AS subGroupTypeId,\n" +
            "tl.STORE_ID AS storeId,tl.GRP_TYP_NM AS groupTypeName,tl.SUB_GRP_NM AS subGroupTypeName \n" +
            "from tblstorepartnerlisting tl \n " +
            "WHERE \n" +
            "((tl.CO_OWNER_ID_1 = :coOwnerId1) OR (tl.CO_OWNER_ID_1 = :coOwnerId2) OR (tl.CO_OWNER_ID_1 =:coOwnerId3) OR " +
            "(tl.CO_OWNER_ID_1 = :coOwnerId4) OR (tl.CO_OWNER_ID_1 = :coOwnerId5)) AND " +
            "((tl.CO_OWNER_ID_2 = :coOwnerId1) OR (tl.CO_OWNER_ID_2 = :coOwnerId2) OR (tl.CO_OWNER_ID_2 = :coOwnerId3) OR " +
            "(tl.CO_OWNER_ID_2 = :coOwnerId4) OR (tl.CO_OWNER_ID_2 = :coOwnerId5)) AND " +
            "((tl.CO_OWNER_ID_3 = :coOwnerId1) OR (tl.CO_OWNER_ID_3 = :coOwnerId2) OR (tl.CO_OWNER_ID_3 = :coOwnerId3) OR " +
            "(tl.CO_OWNER_ID_3 = :coOwnerId4) OR (tl.CO_OWNER_ID_3 = :coOwnerId5)) AND " +
            "((tl.CO_OWNER_ID_4 = :coOwnerId1) OR (tl.CO_OWNER_ID_4 = :coOwnerId2) OR (tl.CO_OWNER_ID_4 = :coOwnerId3) OR " +
            "(tl.CO_OWNER_ID_4 = :coOwnerId4) OR (tl.CO_OWNER_ID_4 = :coOwnerId5)) AND " +
            "((tl.CO_OWNER_ID_5 = :coOwnerId1) OR (tl.CO_OWNER_ID_5 = :coOwnerId2) OR (tl.CO_OWNER_ID_5 = :coOwnerId3) OR " +
            "(tl.CO_OWNER_ID_5 = :coOwnerId4) OR (tl.CO_OWNER_ID_5 = :coOwnerId5)) AND " +
            "tl.IS_DELETED = 0 AND tl.STATUS_ID_2 = 0 ", nativeQuery = true)
    public List<IkeyValuePair> getExactMatch(@Param("coOwnerId1") Long coOwnerId1,
                                             @Param("coOwnerId2") Long coOwnerId2,
                                             @Param("coOwnerId3") Long coOwnerId3,
                                             @Param("coOwnerId4") Long coOwnerId4,
                                             @Param("coOwnerId5") Long coOwnerId5);

    @Query(value = "select tl.version_no versionNumber, tl.CO_OWNER_ID_1 AS coOwnerId1 ,tl.CO_OWNER_ID_2 AS coOwnerId2, \n" +
            "tl.CO_OWNER_ID_3 AS coOwnerId3,tl.CO_OWNER_ID_4 AS coOwnerId4,tl.CO_OWNER_ID_5 AS coOwnerId5, \n" +
            "tl.CO_OWNER_ID_6 AS coOwnerId6,tl.CO_OWNER_ID_7 AS coOwnerId7,tl.CO_OWNER_ID_8 AS coOwnerId8, \n" +
            "tl.CO_OWNER_ID_9 AS coOwnerId9,tl.CO_OWNER_ID_10 AS coOwnerId10, \n" +
            "tl.CO_OWNER_NAME_1 AS coOwnerName1,tl.CO_OWNER_NAME_2 AS coOwnerName2,tl.CO_OWNER_NAME_3 AS coOwnerName3, \n" +
            "tl.CO_OWNER_NAME_4 AS coOwnerName4,tl.CO_OWNER_NAME_5 AS coOwnerName5,\n " +
            "tl.CO_OWNER_NAME_6 AS coOwnerName6,tl.CO_OWNER_NAME_7 AS coOwnerName7,tl.CO_OWNER_NAME_8 AS coOwnerName8, \n" +
            "tl.CO_OWNER_NAME_9 AS coOwnerName9,tl.CO_OWNER_NAME_10 AS coOwnerName10,\n " +
            "tl.CO_OWNER_PER_1 AS coOwnerPercentage1,tl.CO_OWNER_PER_2 AS coOwnerPercentage2,tl.CO_OWNER_PER_3 AS coOwnerPercentage3,\n" +
            "tl.CO_OWNER_PER_4 AS coOwnerPercentage4,tl.CO_OWNER_PER_5 AS coOwnerPercentage5,  \n" +
            "tl.CO_OWNER_PER_6 AS coOwnerPercentage6,tl.CO_OWNER_PER_7 AS coOwnerPercentage7,tl.CO_OWNER_PER_8 AS coOwnerPercentage8,\n" +
            "tl.CO_OWNER_PER_9 AS coOwnerPercentage9,tl.CO_OWNER_PER_10 AS coOwnerPercentage10, tl.STORE_NM AS storeName, \n" +
            "tl.GROUP_ID AS groupId,tl.GRP_NM AS groupName,tl.GRP_ID AS groupTypeId,tl.SUB_GRP_ID AS subGroupTypeId,\n" +
            "tl.STORE_ID AS storeId,tl.GRP_TYP_NM AS groupTypeName,tl.SUB_GRP_NM AS subGroupTypeName \n" +
            "from tblstorepartnerlisting tl \n " +
            "WHERE \n" +
            "(tl.CO_OWNER_ID_1 IN (:coOwnerId1) OR \n" +
            "tl.CO_OWNER_ID_2 IN (:coOwnerId2) OR \n" +
            "tl.CO_OWNER_ID_3 IN (:coOwnerId3) OR \n" +
            "tl.CO_OWNER_ID_4 IN (:coOwnerId4) OR \n" +
            "tl.CO_OWNER_ID_5 IN (:coOwnerId5) OR \n" +
            "tl.CO_OWNER_ID_6 IN (:coOwnerId6) OR \n" +
            "tl.CO_OWNER_ID_7 IN (:coOwnerId7) OR \n" +
            "tl.CO_OWNER_ID_8 IN (:coOwnerId8) OR \n" +
            "tl.CO_OWNER_ID_9 IN (:coOwnerId9) OR \n" +
            "tl.CO_OWNER_ID_10 IN (:coOwnerId10) OR \n" +
            "tl.GROUP_ID IN (:groupId)) AND \n" +
            "tl.IS_DELETED = 0 AND tl.STATUS_ID_2=0 ", nativeQuery = true)
    public List<IkeyValuePair> getLikeMatchGroupEntity(@Param("coOwnerId1") Long coOwnerId1,
                                                       @Param("coOwnerId2") Long coOwnerId2,
                                                       @Param("coOwnerId3") Long coOwnerId3,
                                                       @Param("coOwnerId4") Long coOwnerId4,
                                                       @Param("coOwnerId5") Long coOwnerId5,
                                                       @Param("coOwnerId6") Long coOwnerId6,
                                                       @Param("coOwnerId7") Long coOwnerId7,
                                                       @Param("coOwnerId8") Long coOwnerId8,
                                                       @Param("coOwnerId9") Long coOwnerId9,
                                                       @Param("coOwnerId10") Long coOwnerId10,
                                                       @Param("groupId") String groupId);

//    @Query(value = "SELECT entity_id AS entityId FROM tblentity WHERE client_id = :coOwnerIds AND is_deleted = 0 ", nativeQuery = true)
//    List<IkeyValuePair> getEntityIds(@Param("coOwnerIds") Long coOwnerIds);

    @Query(value = "SELECT REF_FIELD_1 as refField1 FROM tblentity WHERE client_id = :coOwnerIds AND is_deleted = 0 ", nativeQuery = true)
    List<String> getEntityIds(@Param("coOwnerIds") Long coOwnerIds);



}
