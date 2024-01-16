package com.mnrclara.api.cg.setup.repository;

import com.mnrclara.api.cg.setup.model.IKeyValuePair;
import com.mnrclara.api.cg.setup.model.state.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StateRepository extends JpaRepository<State, Long>, JpaSpecificationExecutor<State> {

	List<State> findAll();
	Optional<State> findByStateIdAndCompanyIdAndLanguageIdAndDeletionIndicator(
			String stateId, String companyId, String languageId, Long deletionIndicator);

	State findByCompanyIdAndLanguageIdAndStateIdAndDeletionIndicator(
			String companyId, String languageId, String stateId, Long deletionIndicator);


	@Query(value ="select  tl.state_id AS stateId,tl.state_nm AS description\n"+
			" from tblstateid tl \n" +
			"WHERE \n"+
			"tl.state_id IN (:stateId) and tl.c_id IN (:companyId) and \n" +
			"tl.lang_id IN (:languageId) and tl.country_id IN (:countryId) and \n"+
			"tl.is_deleted=0 ",nativeQuery = true)

	public IKeyValuePair getStateIdAndDescription(@Param(value="stateId") String stateId,
												  @Param(value = "companyId")String companyId,
												  @Param(value = "languageId")String languageId,
												  @Param(value = "countryId")String countryId);




}