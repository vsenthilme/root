package com.mnrclara.api.cg.setup.repository;


import com.mnrclara.api.cg.setup.model.IKeyValuePair;
import com.mnrclara.api.cg.setup.model.controlgroup.ControlGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Repository
@Transactional
public interface ControlGroupRepository extends JpaRepository<ControlGroup,Long>, JpaSpecificationExecutor<ControlGroup> {

    Optional<ControlGroup> findByCompanyIdAndLanguageIdAndGroupIdAndGroupTypeIdAndVersionNumberAndDeletionIndicator(
            String companyId, String languageId, Long groupId, Long groupTypeId,Long versionNumber,Long deletionIndicator);

    ControlGroup findByCompanyIdAndLanguageIdAndGroupIdAndGroupTypeIdAndStatusIdAndDeletionIndicator(
            String companyId, String languageId, Long groupId, Long groupTypeId,Long statusId,Long deletionIndicator);

    @Query(value ="select  tl.group_id AS groupId,tl.grp_nm AS description,tl.group_typ_id AS groupTypeId,tl.grp_typ_nm AS groupTypeName \n"+
            " from tblcontrolgroup tl \n" +
            "WHERE \n"+
            "tl.group_id IN (:groupId) and tl.c_id IN (:companyId) and tl.lang_id IN (:languageId) and " +
            "tl.group_typ_id IN (:groupTypeId) and tl.is_deleted=0 ",nativeQuery = true)

    public IKeyValuePair getGroupIdAndDescription(@Param(value="groupId") Long groupId,
                                                  @Param(value = "companyId")String companyId,
                                                  @Param(value = "languageId")String languageId,
                                                  @Param(value = "groupTypeId")Long groupTypeId);

    @Query(value = "select max(VERSION_NO)+1 \n"+
            "from tblcontrolgroup",nativeQuery = true)
        public Long getVersionId();

}
