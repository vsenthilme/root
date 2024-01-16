package com.mnrclara.api.setup.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.setup.model.clienttype.ClientType;

@Repository
public interface ClientTypeRepository extends JpaRepository<ClientType, Long>{

	public List<ClientType> findAll();
	
	// `LANG_ID`, `CLASS_ID`, `CLIENT_TYP_ID`, `IS_DELETED`
	Optional<ClientType> 
		findByLanguageIdAndClassIdAndClientTypeIdAndDeletionIndicator 
		(String languageId, Long classId, Long clientTypeId, Long deletionIndicator);

	public ClientType findByClientTypeId(Long clientTypeId);
}