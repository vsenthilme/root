package com.mnrclara.api.setup.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.setup.model.userprofile.UserProfile;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long>{

	public List<UserProfile> findAll();
	public Optional<UserProfile> findByUserId (String userId);
	public Optional<UserProfile> findByClassId(Long classId);
	public UserProfile findByUserIdAndEmailId(String userId, String emailId);
	public UserProfile findByEmailId(String emailId);
	
	@Query(value = "SELECT CLASS_ID FROM tbluserprofileid WHERE USR_ID = :userId", nativeQuery = true)
	public List<Long> findClassByUserId (@Param(value = "userId") String userId);
	
	// `LANG_ID`, `CLASS_ID`, `USR_ID`, `USR_ROLE_ID`, `PASSWORD`, `USR_TYP_ID`, `IS_DELETED`
	Optional<UserProfile>
		findByLanguageIdAndClassIdAndUserIdAndUserRoleIdAndPasswordAndUserTypeIdAndDeletionIndicator 
		(String languageId, Long classId, String userId, Long userRoleId, String password, Long userTypeId, Long deletionIndicator);
}