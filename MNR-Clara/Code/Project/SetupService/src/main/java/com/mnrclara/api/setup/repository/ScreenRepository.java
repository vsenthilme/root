package com.mnrclara.api.setup.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.setup.model.screen.Screen;

@Repository
public interface ScreenRepository extends JpaRepository<Screen, Long>{

	public List<Screen> findAll();
	
	// `LANG_ID`, `SCREEN_ID`, `SUB_SCREEN_ID`, `IS_DELETED`
	Optional<Screen> findByLanguageIdAndScreenIdAndSubScreenIdAndDeletionIndicator 
	(String languageId, Long screenId, Long subScreenId, Long deletionIndicator);

	public Screen findByScreenId(Long screenId);
}