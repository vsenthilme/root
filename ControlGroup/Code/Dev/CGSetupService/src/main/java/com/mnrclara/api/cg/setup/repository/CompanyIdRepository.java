package com.mnrclara.api.cg.setup.repository;

import com.mnrclara.api.cg.setup.model.companyid.CompanyId;
import com.mnrclara.api.cg.setup.model.IKeyValuePair;
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
public interface CompanyIdRepository extends JpaRepository<CompanyId,Long>, JpaSpecificationExecutor<CompanyId> {
	
	public List<CompanyId> findAll();
	public Optional<CompanyId>
	findByCompanyIdAndLanguageIdAndDeletionIndicator(
				String companyId, String languageId, Long deletionIndicator);

	@Query(value ="select  tl.c_id AS companyId,tl.c_text AS description\n"+
			" from tblcompanyid tl \n" +
			"WHERE \n"+
			"tl.c_id IN (:companyId) and tl.lang_id IN (:languageId) and \n"+
			"tl.is_deleted=0 ",nativeQuery = true)

	public IKeyValuePair getCompanyIdAndDescription(@Param(value="companyId") String companyId,
													@Param(value="languageId") String languageId);

}