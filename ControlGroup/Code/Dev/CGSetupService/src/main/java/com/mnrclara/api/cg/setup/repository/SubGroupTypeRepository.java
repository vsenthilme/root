package com.mnrclara.api.cg.setup.repository;


import com.mnrclara.api.cg.setup.model.subgrouptype.SubGroupType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Repository
@Transactional
public interface SubGroupTypeRepository extends JpaRepository<SubGroupType,Long>, JpaSpecificationExecutor<SubGroupType> {

   Optional<SubGroupType> findByCompanyIdAndLanguageIdAndGroupTypeIdAndSubGroupTypeIdAndVersionNumberAndDeletionIndicator(
            String companyId, String languageId, Long groupTypeId, Long subGroupTypeId,Long versionNumber,Long deletionIndicator);

   SubGroupType findByCompanyIdAndLanguageIdAndGroupTypeIdAndSubGroupTypeIdAndStatusIdAndDeletionIndicator(
           String companyId, String languageId, Long groupTypeId, Long subGroupTypeId,Long statusId,Long deletionIndicator);


   @Query(value = "select MAX(VERSION_NO)+1 \n" +
            "from tblsubgrouptype",nativeQuery = true)
   public Long getVersionNo();

}
