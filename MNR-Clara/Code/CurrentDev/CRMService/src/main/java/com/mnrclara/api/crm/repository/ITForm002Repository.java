package com.mnrclara.api.crm.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mnrclara.api.crm.model.itform.ITForm002;

@Repository
public interface ITForm002Repository extends MongoRepository<ITForm002, String> {
	
	Optional<ITForm002> findById(String id);
	
}
