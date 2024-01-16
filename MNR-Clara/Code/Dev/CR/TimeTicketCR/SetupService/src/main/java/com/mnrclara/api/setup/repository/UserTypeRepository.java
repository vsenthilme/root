package com.mnrclara.api.setup.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.setup.model.usertype.UserType;

@Repository
public interface UserTypeRepository extends JpaRepository<UserType, Long>{

	public List<UserType> findAll();
	Optional<UserType> findByUserTypeId (Long userTypeId);
	
	// `LANG_ID`, `CLASS_ID`, `USR_TYP_ID`, `IS_DELETED`
	Optional<UserType>
		findByLanguageIdAndClassIdAndUserTypeIdAndDeletionIndicator
		(String languageId, Long classId, Long userTypeId, Long deletionIndicator);
	
	public Optional<UserType> findByUserTypeIdAndDeletionIndicator(Long userTypeId, Long deletionIndicator);
}