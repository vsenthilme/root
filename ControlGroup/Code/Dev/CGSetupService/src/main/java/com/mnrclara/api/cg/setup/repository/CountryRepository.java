package com.mnrclara.api.cg.setup.repository;

import com.mnrclara.api.cg.setup.model.IKeyValuePair;
import com.mnrclara.api.cg.setup.model.country.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long>, JpaSpecificationExecutor<Country> {

	List<Country> findAll();
	Optional<Country> findByCountryIdAndCompanyIdAndLanguageIdAndDeletionIndicator (
			String countryId, String companyId, String languageId, Long deletionIndicator);

	@Query(value ="select  tl.country_id AS countryId,tl.country_nm AS description\n"+
			" from tblcountryid tl \n" +
			"WHERE \n"+
			"tl.country_id IN (:countryId) and tl.c_id IN (:companyId) and tl.lang_id IN (:languageId) and \n"+
			"tl.is_deleted=0 ",nativeQuery = true)

	public IKeyValuePair getCountryIdAndDescription(@Param(value="countryId") String countryId,
													@Param(value = "companyId")String companyId,
													@Param(value = "languageId")String languageId);
}