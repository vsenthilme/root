package com.mnrclara.api.setup.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.setup.model.clientcategory.ClientCategory;

@Repository
public interface ClientCategoryRepository extends JpaRepository<ClientCategory, Long>{

	public List<ClientCategory> findAll();
	Optional<ClientCategory> findByClientCategoryId (Long clientCategoryId);
	
	// `LANG_ID`, `CLASS_ID`, `CLIENT_CAT_ID`, `IS_DELETED`
	Optional<ClientCategory>
		findByLanguageIdAndClassIdAndClientCategoryIdAndDeletionIndicator
		(String languageId, Long classId, Long clientCategoryId, Long deletionIndicator);
}