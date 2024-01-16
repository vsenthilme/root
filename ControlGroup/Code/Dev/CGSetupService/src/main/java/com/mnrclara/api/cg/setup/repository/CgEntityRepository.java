package com.mnrclara.api.cg.setup.repository;

import com.mnrclara.api.cg.setup.model.IKeyValuePair;
import com.mnrclara.api.cg.setup.model.cgentity.CgEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface CgEntityRepository extends JpaRepository<CgEntity, String>, JpaSpecificationExecutor<CgEntity> {

    Optional<CgEntity> findByEntityIdAndClientIdAndCompanyIdAndLanguageIdAndDeletionIndicator(
            Long entityId, Long clientId, String companyId, String languageId, Long deletionIndicator);

    @Query(value = "select tl.entity_id AS entityId, tl.entity_nm AS description \n" +
            "from tblentity tl \n" +
            "WHERE \n" +
            "tl.entity_id IN (:entityId) and tl.client_id IN (:clientId) and tl.c_id IN (:companyId) and tl.lang_id IN (:languageId) and \n " +
            "tl.is_deleted = 0 ", nativeQuery = true)
    IKeyValuePair getEntityIdAndDescription(@Param(value = "entityId") Long entityId,
                                            @Param(value = "clientId") Long clientId,
                                            @Param(value = "companyId") String companyId,
                                            @Param(value = "languageId") String languageId);
}
