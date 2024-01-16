package com.mnrclara.api.cg.setup.repository;

import com.mnrclara.api.cg.setup.model.IKeyValuePair;
import com.mnrclara.api.cg.setup.model.city.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long>, JpaSpecificationExecutor<City> {

	public List<City> findAll();
	public Optional<City> findByCityIdAndStateIdAndCountryIdAndLanguageIdAndDeletionIndicator(
			String cityId, String stateId, String countryId, String languageId, Long deletionIndicator);

	@Query(value ="select  tl.city_id AS cityId,tl.city_nm AS description\n"+
			" from tblcityid tl \n" +
			"WHERE \n"+
			"tl.city_id IN (:cityId) and tl.c_id IN (:companyId) and tl.lang_id IN (:languageId) and \n " +
			"tl.state_id IN (:stateId) and tl.country_id IN (:countryId) and \n"+
			"tl.is_deleted=0 ",nativeQuery = true)

	public IKeyValuePair getCityIdAndDescription(@Param(value="cityId") String cityId,
													@Param(value = "companyId")String companyId,
													@Param(value = "languageId")String languageId,
													@Param(value = "stateId")String stateId,
													@Param(value = "countryId")String countryId);



	@Query(value = "SELECT tc.c_id AS companyId, tc.c_text AS companyDescription, \n " +
			"ti.country_id AS countryId, ti.country_nm AS countryDescription, \n " +
			"ts.state_id AS stateId, ts.state_nm AS stateDescription \n " +
			"FROM tblcompanyid tc, \n " +
			"tblcountryid ti, \n " +
			"tblstateid ts \n " +
			"WHERE tc.c_id = ti.c_id AND tc.lang_id = ti.lang_id \n" +
			"AND tc.c_id = ts.c_id AND tc.lang_id = ts.lang_id AND ti.country_id = ts.country_id \n " +
			"AND tc.c_id IN (:companyId) AND tc.lang_id IN (:languageId) \n " +
			"AND ti.country_id IN (:countryId) AND ts.state_id IN (:stateId) \n " +
			"AND tc.is_deleted = 0 AND ti.is_deleted = 0 AND ts.is_deleted = 0 ", nativeQuery = true)
	public IKeyValuePair getDescription(@Param(value = "companyId") String companyId,
										@Param(value = "languageId") String languageId,
										@Param(value = "countryId") String countryId,
										@Param(value = "stateId") String stateId);
}