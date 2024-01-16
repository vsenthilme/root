package com.mnrclara.api.cg.setup.repository;

import com.mnrclara.api.cg.setup.model.IKeyValuePair;
import com.mnrclara.api.cg.setup.model.clientstore.ClientStore;
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
public interface ClientStoreRepository extends JpaRepository<ClientStore, String>, JpaSpecificationExecutor<ClientStore> {

    List<ClientStore> findAll();

    Optional<ClientStore> findByClientIdAndStoreIdAndCompanyIdAndLanguageIdAndVersionNumberAndDeletionIndicator(
            Long clientId, Long storeId, String companyId, String languageId, Long versionNumber, Long deletionIndicator);

    ClientStore findByClientIdAndStoreIdAndCompanyIdAndLanguageIdAndStatusIdAndDeletionIndicator(
            Long clientId, Long storeId, String companyId, String languageId, Long statusId, Long deletionIndicator);


//    @Query(value = "select tc.c_id AS companyId, tc.c_text AS companyDescription, \n " +
//            "ti.store_id AS storeId,ti.store_name AS storeDescription, \n " +
//            "tl.client_id AS clientId, tl,client_name AS description \n " +
//            "FROM tblcompanyid tc, \n " +
//            "tblclient tl, \n " +
//            "tblstoreid tl \n " +
//            "WHERE tc.c_id = ti.c_id AND tc.lang_id = ti.lang_id \n" +
//            "AND tc.c_id = tl.c_id AND tc.lang_id = tl.lang_id \n " +
//            "AND tc.c_id IN (:companyId) AND tc.lang_id IN (:languageId) \n " +
//            "AND ti.store_id IN (:storeId) AND tl.client_id IN (clientId) \n " +
//            "AND tc.is_deleted = 0 AND ti.is_deleted = 0 AND tl.is_deleted = 0 \n",nativeQuery = true)
//    public IKeyValuePair getDescription( @Param(value = "companyId")String companyId,
//                                         @Param(value = "languageId")String languageId,
//                                         @Param(value = "storeId")Long storeId,
//                                         @Param(value = "clientId")Long clientId);

    @Query(value = "select MAX(VERSION_NO)+1 \n" +
                "from tblclientstore",nativeQuery = true)
    public Long versionId();
}