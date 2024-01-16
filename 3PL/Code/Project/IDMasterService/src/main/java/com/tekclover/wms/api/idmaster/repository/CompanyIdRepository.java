package com.tekclover.wms.api.idmaster.repository;

import java.util.List;
import java.util.Optional;

import com.tekclover.wms.api.idmaster.model.IKeyValuePair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tekclover.wms.api.idmaster.model.companyid.CompanyId;


@Repository
@Transactional
public interface CompanyIdRepository extends JpaRepository<CompanyId,Long>, JpaSpecificationExecutor<CompanyId> {
	
	public List<CompanyId> findAll();
	public Optional<CompanyId> 
		findByCompanyCodeIdAndLanguageIdAndDeletionIndicator(
				String companyCodeId, String languageId, Long deletionIndicator);

	@Query(value ="select  tl.c_id AS companyCodeId,tl.c_text AS description\n"+
			" from tblcompanyid tl \n" +
			"WHERE \n"+
			"tl.c_id IN (:companyCodeId) and tl.lang_id IN (:languageId) and \n"+
			"tl.is_deleted=0 ",nativeQuery = true)

	public IKeyValuePair getCompanyIdAndDescription(@Param(value="companyCodeId") String companyCodeId,
													@Param(value="languageId") String languageId);

}