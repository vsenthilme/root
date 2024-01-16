package com.tekclover.wms.api.enterprise.repository;

import java.util.Optional;

import com.tekclover.wms.api.enterprise.model.IkeyValuePair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tekclover.wms.api.enterprise.model.company.Company;

@Repository
@Transactional
public interface CompanyRepository extends JpaRepository<Company,Long>, JpaSpecificationExecutor<Company> {

	public Optional<Company> findByCompanyId(String companyId);
	
	public Optional<Company> findByLanguageIdAndCompanyIdAndDeletionIndicator (
			String languageId, String companyId, Long deletionIndicator);

	@Query(value ="select  tl.c_id AS companyCodeId,tl.c_text AS description\n"+
			" from tblcompanyid tl \n" +
			"WHERE \n"+
			"tl.c_id IN (:companyCodeId) and tl.lang_id IN (:languageId) and \n"+
			"tl.is_deleted=0 ",nativeQuery = true)

	public IkeyValuePair getCompanyIdAndDescription(@Param(value="companyCodeId") String companyCodeId,
													@Param(value="languageId") String languageId);

	@Query(value = "select tl.vert_id AS verticalId,tl.vertical AS description\n"+
	"from tblverticalid tl \n"+
	"WHERE \n"+
	"tl.vert_id IN (:verticalId) and tl.lang_id IN (:languageId) and \n"+
	"tl.is_deleted=0",nativeQuery = true)
	public IkeyValuePair getVerticalIdAndDescription(@Param(value = "verticalId")Long verticalId,
													 @Param(value = "languageId")String languageId);
}