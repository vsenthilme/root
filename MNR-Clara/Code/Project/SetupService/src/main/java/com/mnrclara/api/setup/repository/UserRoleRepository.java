package com.mnrclara.api.setup.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.setup.model.userrole.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long>{

	public List<UserRole> findAll();
	public List<UserRole> findByUserRoleIdAndDeletionIndicator(Long userRoleId, Long deletionIndicator);

}