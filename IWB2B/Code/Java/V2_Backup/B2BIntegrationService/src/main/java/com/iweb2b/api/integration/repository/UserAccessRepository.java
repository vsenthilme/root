package com.iweb2b.api.integration.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.iweb2b.api.integration.model.usermanagement.UserAccess;

@Repository
@Transactional
public interface UserAccessRepository extends JpaRepository<UserAccess, Long>, JpaSpecificationExecutor<UserAccess> {

	public List<UserAccess> findByUserIdAndDeletionIndicator(String userId, Long deletionIndicator);

	public UserAccess findByLanguageIdAndCompanyCodeAndUserIdAndUserRoleIdAndDeletionIndicator(
			String languageId, String companyCode, String userId, Long userRoleId, Long deletionIndicator);

	public UserAccess findByLanguageIdAndCompanyCodeAndUserIdAndDeletionIndicator(
			String languageId, String companyCode, String userId, Long deletionIndicator);
}