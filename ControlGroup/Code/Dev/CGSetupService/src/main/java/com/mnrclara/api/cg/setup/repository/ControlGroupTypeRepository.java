package com.mnrclara.api.cg.setup.repository;


import com.mnrclara.api.cg.setup.model.IKeyValuePair;
import com.mnrclara.api.cg.setup.model.controlgrouptype.ControlGroupType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface ControlGroupTypeRepository extends JpaRepository<ControlGroupType, Long>, JpaSpecificationExecutor<ControlGroupType> {

    Optional<ControlGroupType> findByCompanyIdAndLanguageIdAndGroupTypeIdAndVersionNumberAndDeletionIndicator(
            String companyId, String languageId, Long groupTypeId, Long versionNumber, Long deletionIndicator);

//    ControlGroupType findByCompanyIdAndLanguageIdAndGroupTypeIdAndStatusIdAndDeletionIndicator(
//            String companyId, String languageId, String groupTypeId,Long statusId,Long deletionIndicator);

    @Query(value = "select  tl.grp_typ_id AS groupTypeId,tl.grp_typ_nm AS description\n" +
            " from tblcontrolgrouptype tl \n" +
            "WHERE \n" +
            "tl.grp_typ_id IN (:groupTypeId) and tl.c_id IN (:companyId) and tl.lang_id IN (:languageId) and \n" +
            "tl.is_deleted=0 and tl.status_id=0", nativeQuery = true)

    public IKeyValuePair getControlGroupTypeIdAndDescription(@Param(value = "groupTypeId") Long groupTypeId,
                                                             @Param(value = "companyId") String companyId,
                                                             @Param(value = "languageId") String languageId);

    ControlGroupType findByCompanyIdAndLanguageIdAndGroupTypeIdAndStatusIdAndDeletionIndicator(
            String companyId, String languageId, Long groupTypeId, Long statusId, Long deletionIndicator);


    @Query(value = "select MAX(VERSION_NO)+1 \n" +
            "from tblcontrolgrouptype", nativeQuery = true)
    public Long getVersionId();


}
