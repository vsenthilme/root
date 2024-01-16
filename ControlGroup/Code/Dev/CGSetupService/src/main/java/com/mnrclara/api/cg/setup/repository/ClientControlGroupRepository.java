package com.mnrclara.api.cg.setup.repository;


import com.mnrclara.api.cg.setup.model.IKeyValuePair;
import com.mnrclara.api.cg.setup.model.clientcontrolgroup.ClientControlGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface ClientControlGroupRepository extends JpaRepository<ClientControlGroup,String>, JpaSpecificationExecutor<ClientControlGroup> {

    Optional<ClientControlGroup> findByCompanyIdAndLanguageIdAndClientIdAndSubGroupTypeIdAndGroupTypeIdAndVersionNumberAndDeletionIndicator(
            String companyId, String languageId, Long clientId, Long subGroupTypeId, Long groupTypeId,Long versionNumber, Long deletionIndicator);

    ClientControlGroup findByCompanyIdAndLanguageIdAndClientIdAndSubGroupTypeIdAndGroupTypeIdAndStatusIdAndDeletionIndicator(
            String companyId, String languageId, Long clientId, Long subGroupTypeId, Long groupTypeId,Long statusId, Long deletionIndicator);


    @Query(value = "SELECT tc.c_id AS companyId, tc.c_text AS companyDescription, \n" +
            "ti.client_id AS clientId, ti.client_nm AS clientName, \n" +
            "tcg.grp_typ_id AS groupTypeId,tcg.grp_typ_nm AS groupTypeName, \n" +
            "ts.sub_grp_typ_id AS subGroupTypeId,ts.sub_grp_typ_nm AS subGroupTypeName \n" +
            "FROM tblcompanyid tc \n" +
            "JOIN tblclient ti ON tc.c_id = ti.c_id AND tc.lang_id = ti.lang_id \n" +
            "JOIN tblcontrolgrouptype tcg ON tc.c_id = tcg.c_id AND tc.lang_id = tcg.lang_id \n" +
            "JOIN tblsubgrouptype ts ON tc.c_id = ts.c_id AND tc.lang_id = ts.lang_id AND tcg.grp_typ_id = ts.grp_typ_id \n" +
            "WHERE tc.c_id IN (:companyId) AND \n" +
            "tc.lang_id IN (:languageId) AND \n" +
            "ti.client_id IN (:clientId) AND \n" +
            "tcg.grp_typ_id IN (:groupTypeId) AND \n" +
            "ts.sub_grp_typ_id IN (:subGroupTypeId) AND \n" +
            "ti.status_id = 0 AND \n" +
            "tcg.status_id = 0 AND \n" +
            "ts.status_id = 0 AND \n" +
            "tc.is_deleted = 0 AND \n" +
            "ti.is_deleted = 0 AND \n" +
            "tcg.is_deleted = 0 AND \n" +
            "ts.is_deleted = 0", nativeQuery = true)
    public IKeyValuePair getDescription(@Param("companyId") String companyId,
                                        @Param("languageId") String languageId,
                                        @Param("clientId") Long clientId,
                                        @Param("groupTypeId") Long groupTypeId,
                                        @Param("subGroupTypeId") Long subGroupTypeId);


    @Query(value = "select MAX(VERSION_NO)+1 \n" +
            "from tblclientcontrolgroup",nativeQuery = true)
    public Long getVersionNo();

}
