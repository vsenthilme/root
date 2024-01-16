package com.iweb2b.api.master.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iweb2b.api.master.model.userprofiles.UserProfiles;

@Repository
public interface UserProfilesRepository extends JpaRepository<UserProfiles, Long>{

	public List<UserProfiles> findAll();

	public Optional<UserProfiles> findByUserIdAndDeletionIndicator(String userProfilesId, long l);
}